package com.jksdk.i7724;

import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.constants.I_constants;
import com.ssmCore.constants.ReInfo;
import com.ssmCore.tool.colligate.Encryption;
import com.ssmCore.tool.colligate.HttpRequest;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmShare.entity.UserBase;
import com.ssmShare.facde.I_Platform;
import com.ssmShare.platform.DataConf;

@Service("Sdk7724Impl")
@Scope("prototype")
public class Sdk7724Impl implements I_Platform {
	private static final Logger log = LoggerFactory.getLogger(Sdk7724Impl.class);
	public static final String URL_USERINFO = "http://www.7724.com/networkgame/getUserInfo";
	public static final Integer gameId = 419;
	public static final String key = "f94790c6bc5d2ed47819a78120662388";
	public static final String paykey = "7d84f502984a89e2e74000066a91e978";
	public static final String payBackkey = "c39b11fe427f279786776dcdd29db718";

	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		String pid = (String) param.get("pid");
		String appkey = (String) param.get("appkey");
		String time = (String) param.get("time");
		String token = (String) param.get("token");
		String uid = (String) param.get("uid");
		// String nickname = (String) param.get("nickname");
		String sign = (String) param.get("sign");
		String channel = (String) param.get("channel");

		InviterData inviterData = null;
		try {
			String ext = (String) param.get("ext");
			inviterData = JsonTransfer._In(ext, InviterData.class);
		} catch (Exception e) {
			inviterData = null;
		}

		SortedMap<String, String> pl = new TreeMap<>();
		pl.put("appkey", appkey);
		pl.put("time", time);
		pl.put("token", token);
		pl.put("uid", uid);
		String tempSign = createSign(pl, key);

		if (!tempSign.equals(sign)) {
			log.error(this.getClass() + "登陆验证Sign失败！");
			param.put(I_constants.PTOKEN_ERRPR, "登陆验失败！");
			return null;
		}

		SortedMap<String, String> puser = new TreeMap<>();
		puser.put("appkey", appkey);
		puser.put("time", String.valueOf((int) (System.currentTimeMillis() / 1000)));
		puser.put("uid", uid);
		puser.put("sign", createSign(puser, "qqes"));

		String reqresult = HttpRequest.PostFunction(URL_USERINFO, puser);
		User_Entity user = JsonTransfer._In(reqresult, User_Entity.class);
		if (user == null || user.success != 1) {
			log.error(this.getClass() + "登陆获取用户信息失败！" + reqresult);
			return null;
		}
		UserBase ubase = new UserBase();
//		ubase.setNickname(user.nickname);
//		ubase.setuImg(user.head_img.replaceAll("\\\\", ""));
		dSource.rtn.getInfo().setAccount(uid);
		dSource.rtn.getInfo().setPid(pid);
		dSource.rtn.setUsebase(ubase);
		ubase.pid = pid;

		CSData csData = new CSData();
		csData.appkey = appkey;
		csData.token = token;
		csData.channel = channel;
		param.put("extend", JsonTransfer.getJson(csData));

		/**
		 * 邀请者ID
		 */
		if (inviterData != null) {
			ubase.setInvitID(inviterData.inviter_uid);
		}

		return ubase;
	}

	@Override
	public ReInfo shareVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReInfo followVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object platInit(DataConf dSource, String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void callPay(Map<String, Object> param, DataConf dSource) throws Exception {
		String appkey = (String) param.get("appkey");
		String fee = (String) param.get("fee");
		String orderno = (String) param.get("orderno");
		String qqes_orderno = (String) param.get("qqes_orderno");
		String success = (String) param.get("success");
		String time = (String) param.get("time"); //
		String token = (String) param.get("token");
		String sign = (String) param.get("sign");
		// String channel = (String) param.get("channel");

		SortedMap<String, String> p = new TreeMap<>();
		p.put("appkey", appkey);
		p.put("fee", fee);
		p.put("orderno", orderno);
		p.put("qqes_orderno", qqes_orderno);
		p.put("success", success);
		p.put("time", time);
		p.put("token", token);

		String test_sign = createSign(p, payBackkey);
		if (test_sign.equals(sign)) {
			param.put("goodsOrder", orderno);
			param.put("Amount", fee);
			param.put("ownOrder", orderno);
			param.put("status", 1);
			param.put("success", "OK");
		} else {
			param.put("fail", this.getClass() + "充值回调sign验证失败" + param.toString());
		}
	}

	/**
	 * 订单创建
	 */
	@Override
	public Object payVerification(DataConf dSource) throws Exception {
		String extend = (String) dSource.params.get("extend");
		CSData csData = JsonTransfer._In(extend, CSData.class);
		SortedMap<String, String> param = new TreeMap<>();
		param.put("appkey", csData.appkey);
		param.put("channel", csData.channel);
		param.put("fee", String.valueOf(dSource.order.get("amount")));
		param.put("orderno", String.valueOf(dSource.order.get("ownOrder")));
		param.put("subject", "Diamond" + String.valueOf(dSource.order.get("goodsNum")));
		param.put("time", String.valueOf((int) (System.currentTimeMillis() / 1000)));
		param.put("token", csData.token);
		param.put("sign", createSign(param, paykey));

		// 中文Encoder
		String subject = URLEncoder.encode(param.get("subject"), "utf-8");
		param.put("subject", subject);

		return "http://i.7724.com/recharge/sdkindex?" + createParams(param);
	}

	/**
	 * 生成签名
	 * 
	 * @param param
	 *            参数
	 * @return
	 */
	private String createSign(SortedMap<String, String> param, String key) {
		return Encryption.Encode(createParams(param) + key, Encryption.MD5);
	}

	private String createParams(SortedMap<String, String> param) {
		String sign = "";
		if (param != null && param.size() > 0) {
			for (Entry<String, String> entry : param.entrySet()) {
				String name = entry.getKey() == null ? "" : entry.getKey();
				String value = entry.getValue() == null ? "" : entry.getValue();
				sign += name + "=" + value + "&";
			}
		}
		if (sign.length() > 0)
			sign.substring(0, sign.length() - 1);
		return sign;
	}

	@Override
	public Object statslog(Map<String, Object> param, DataConf dSource) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getUserinfo(Map<String, Object> param, DataConf dSource) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPayInfo(Map<String, Object> param, DataConf dSource) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object other(Map<String, Object> param, DataConf dSource) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}

class OrderToClient {
	public String orderid;
	public int money;
	public String product;
	public String appid;
	public String sign;
	public String ext;
}

class User_Entity {
	public int success;
	public String nickname;
	public String head_img;
	public String isSubscribe;// 是否关注过公众号， 1是 2否
}

class CSData {
	public String appkey;
	public String token;
	public String channel;
}

class InviterData {
	/** 被邀请者ID */
	public String inviter_uid;
	/** 邀请者（及自己） */
	public String receiver_uid;
	/** 自定义参数 */
	public String param;
}
