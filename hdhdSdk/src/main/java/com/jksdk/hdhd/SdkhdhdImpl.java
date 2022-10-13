package com.jksdk.hdhd;

import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.constants.ReInfo;
import com.ssmCore.tool.colligate.Encryption;
import com.ssmCore.tool.colligate.HttpRequest;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmShare.entity.UserBase;
import com.ssmShare.facde.I_Platform;
import com.ssmShare.platform.DataConf;

@Service("SdkhdhdImpl")
@Scope("prototype")
public class SdkhdhdImpl implements I_Platform {
	private static final Logger log = LoggerFactory.getLogger(SdkhdhdImpl.class);
	public static final String URL_USERINFO = "http://gc.hgame.com/user/getticketuserinfo";
	// private static final int gameId = 100371;
	private static final String gamekey = "e5b586c98bdd1cfd";
	private static final String gamesecret = "7aca1e1f3d0e190d4451c5917067580a";

	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		String pid = (String) param.get("pid");
		String game_key = (String) param.get("game_key");
		String timestamp = (String) param.get("timestamp");
		String nonce = (String) param.get("nonce");
		String login_type = (String) param.get("login_type");
		String ticket = (String) param.get("ticket");
		String game_url = (String) param.get("game_url");
		String signature = (String) param.get("signature");

		SortedMap<String, String> pl = new TreeMap<>();
		pl.put("game_key", game_key);
		pl.put("timestamp", timestamp);
		pl.put("nonce", nonce);
		pl.put("login_type", login_type);
		pl.put("ticket", ticket);
		pl.put("game_url", game_url);

		String temp_signature = createSign(pl);
		if (!temp_signature.equals(signature)) {
			log.error(this.getClass() + "登陆验证失败" + param.toString());
			return null;
		}

		SortedMap<String, String> p = new TreeMap<>();
		p.put("game_key", game_key);
		p.put("timestamp", String.valueOf((int) (System.currentTimeMillis() / 1000)));
		p.put("nonce", Encryption.Encode(String.valueOf(System.currentTimeMillis()), Encryption.MD5));
		p.put("login_type", login_type);
		p.put("login_ticket", ticket);
		p.put("signature", createSign(p));

		String reqresult = HttpRequest.GetFunction(URL_USERINFO + "?" + createParams(p));

		User_Entity user = JsonTransfer._In(reqresult, User_Entity.class);
		if (user.code != 0) {
			log.error("蝴蝶：用户信息 参数！---" + p.toString());
			log.error("蝴蝶：用户信息 获取失败！---" + reqresult);
			return null;
		}

		UserBase ubase = new UserBase();
//		ubase.setNickname(user.data.nickname);
//		ubase.setuImg(HttpRequest.encodeImgageToBase64(new URL(user.data.avatar.replaceAll("\\\\", ""))));
		ubase.pid = pid;

		dSource.rtn.getInfo().setAccount(user.data.open_id);
		dSource.rtn.getInfo().setPid(pid);
		dSource.rtn.setUsebase(ubase);

		param.put("extend", gamekey);
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
		return null;
	}

	@Override
	public void callPay(Map<String, Object> param, DataConf dSource) throws Exception {
		String game_key = (String) param.get("game_key");
		String game_orderno = (String) param.get("game_orderno");
		String orderno = (String) param.get("orderno");
		String subject = (String) param.get("subject");
		String description = (String) param.get("description"); //
		String total_fee = (String) param.get("total_fee");
		String signature = (String) param.get("signature");

		SortedMap<String, String> p = new TreeMap<>();
		p.put("game_key", game_key);
		p.put("game_orderno", game_orderno);
		p.put("orderno", orderno);
		p.put("subject", subject);
		p.put("description", description);
		p.put("total_fee", total_fee);

		String test_sign = createSign(p);
		p.put("signature", test_sign);

		if (test_sign.equals(signature)) {
			param.put("goodsOrder", orderno);
			param.put("Amount", total_fee);
			param.put("ownOrder", game_orderno);
			param.put("status", 1);
			param.put("success", "{code:0, message: \"游戏支付成功\"}");
			////////////////////////
		} else {
			param.put("fail", this.getClass() + "充值回调sign验证失败" + param.toString());
		}
	}

	/**
	 * 订单创建
	 */
	@Override
	public Object payVerification(DataConf dSource) throws Exception {
		OrderToClient otc = new OrderToClient();
		otc.game_key = gamekey;
		otc.open_id = String.valueOf(dSource.order.get("account"));
		otc.total_fee = ((Float) dSource.order.get("amount"));
		otc.game_orderno = String.valueOf(dSource.order.get("ownOrder"));
		otc.subject = "Diamond" + dSource.order.get("goodsNum");
		otc.description = "Diamond";
		otc.notify_url = "http://game.wannaplay.cn/" + dSource.doc.getPid() + "/" + dSource.gid + "/pay";
		otc.timestamp = (int) (System.currentTimeMillis() / 1000);
		otc.nonce = Encryption.Encode(String.valueOf(System.currentTimeMillis()), Encryption.MD5);

		SortedMap<String, String> param = new TreeMap<>();
		param.put("game_key", otc.game_key);
		param.put("open_id", otc.open_id);
		if (otc.total_fee % 1 == 0) {
			param.put("total_fee", String.valueOf(otc.total_fee.intValue()));
		} else {
			param.put("total_fee", String.valueOf(otc.total_fee));
		}
		param.put("game_orderno", otc.game_orderno);
		param.put("subject", otc.subject);
		param.put("description", otc.description);
		param.put("notify_url", otc.notify_url);
		param.put("timestamp", String.valueOf(otc.timestamp));
		param.put("nonce", otc.nonce);

		otc.signature = createSign(param);
		return otc;
	}

	/**
	 * 生成签名
	 * 
	 * @param param
	 *            参数
	 * @return
	 */
	private String createSign(SortedMap<String, String> param) {
		String sign = createParams(param);
		sign += gamesecret;
		return Encryption.Encode(sign, Encryption.SHA1);
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
			sign = sign.substring(0, sign.length() - 1);
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
	public String game_key;// ： 这里是游戏中心提供的game_key（required）
	public String open_id;// ： 小伙伴游戏游戏中心提供的用户id（required）
	public Float total_fee;// ： 道具支付金额（单位元），精确到小数点后两位，（required）
	public String game_orderno;// ： 游戏生成的订单号（required，唯一）
	public String subject;// ： 游戏道具名称（required）
	public String description;// ： 游戏道具描述（option）
	public String notify_url;// ： 支付完成后通知URL（required）, 不要单独对这个参数进行encode
	public Integer timestamp;// ： 时间戳，1970-1-1至今秒数 （required）
	public String nonce;// ： 随机字符串（required）
	public String signature;// : 签名（required）
}

class User_Entity {
	public int code;
	public User data;
	public String message;
}

class User {
	public String open_id;
	public String avatar;
	public String nickname;
}
