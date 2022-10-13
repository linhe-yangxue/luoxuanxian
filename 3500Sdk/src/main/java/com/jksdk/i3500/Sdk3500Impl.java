package com.jksdk.i3500;

import java.util.Map;
import java.util.Map.Entry;
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

@Service("Sdk3500Impl")
@Scope("prototype")
public class Sdk3500Impl implements I_Platform {
	private static final Logger log = LoggerFactory.getLogger(Sdk3500Impl.class);
	public static final String URL_USERINFO = "http://www.3500.com/Wap/Api/getUser.html?";
	private static final String app_id = "a24012d05046f30d";
	private static final String appkey = "t4iMMqi2aRRZcTDqiXyxwa8Kk6rXqhrs";

	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		String pid = (String) param.get("pid");
		String uid = (String) param.get("uid");
		String token = (String) param.get("token");

		TreeMap<String, String> p = new TreeMap<>();
		p.put("appid", app_id);
		p.put("token", token);
		p.put("uid", uid);

		String reqresult = HttpRequest.GetFunction(URL_USERINFO + "uid=" + uid + "&token=" + token + "&sign=" + createSign(p) + "&appid=" + app_id);
		User_Entity user = JsonTransfer._In(reqresult, User_Entity.class);
		if (user.code != 200) {
			log.error(this.getClass() + "获取用户信息失败！" + reqresult);
			return null;
		}

		UserBase ubase = new UserBase();
		// ubase.setNickname(user.data.nickname);
		// ubase.setuImg(HttpRequest.encodeImgageToBase64(new URL(user.data.headimgurl)));
		ubase.pid = pid;

		dSource.rtn.getInfo().setAccount(uid);
		dSource.rtn.getInfo().setPid(pid);
		dSource.rtn.setUsebase(ubase);

		param.put("extend", "{\"uid\":\"" + uid + "\",\"token\":\"" + token + "\"}");
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
		String orderid = (String) param.get("orderid");
		String money = (String) param.get("money");
		String status = (String) param.get("status");
		String uid = (String) param.get("uid");
		String appid = (String) param.get("appid"); //
		String sign = (String) param.get("sign");

		TreeMap<String, String> p = new TreeMap<>();
		p.put("appid", appid);
		p.put("money", money);
		p.put("orderid", orderid);
		p.put("status", status);
		p.put("uid", uid);

		String test_sign = createSign(p);
		if (test_sign.equals(sign)) {
			param.put("goodsOrder", orderid);
			param.put("Amount", String.valueOf(money));
			param.put("ownOrder", orderid);
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
		OrderToClient otc = new OrderToClient();
		otc.appid = app_id;
		otc.orderid = String.valueOf(dSource.order.get("ownOrder"));
		otc.product = "Diamond" + String.valueOf(dSource.order.get("goodsNum"));
		otc.money = ((Float) dSource.order.get("amount")).intValue();

		TreeMap<String, String> param = new TreeMap<>();
		param.put("appid", otc.appid);
		param.put("money", String.valueOf(otc.money));
		param.put("orderid", otc.orderid);
		param.put("product", otc.product);

		otc.sign = createSign(param);
		return otc;
	}

	/**
	 * 生成签名
	 * 
	 * @param param
	 *            参数
	 * @return
	 */
	private String createSign(TreeMap<String, String> param) {
		String sign = "";
		if (param != null && param.size() > 0) {
			for (Entry<String, String> entry : param.entrySet()) {
				String name = entry.getKey() == null ? "" : entry.getKey();
				String value = entry.getValue() == null ? "" : entry.getValue();
				sign += name + "=" + value + "&";
			}
		}
		sign = sign.substring(0, sign.length() - 1);
		sign += appkey;
		return Encryption.Encode(sign, Encryption.MD5);
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
	public int code;
	public User data;
	public String msg;
}

class User {
	public String nickname;
	public String phone;// 性别【1：男 2: 女 0：未知】
	public String headimgurl;
}
