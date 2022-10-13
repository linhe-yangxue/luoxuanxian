package com.jksdk.xq;

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

@Service("SdkxqImpl")
@Scope("prototype")
public class SdkxqImpl implements I_Platform {
	private static final Logger log = LoggerFactory.getLogger(SdkxqImpl.class);

	public static final String URL_USERINFO = "https://hgame.x7sy.com/member/get_user_info";
	public static final String URL_TEST_USERINFO = "https://hgame.x7sy.com/member/test_get_user_info";
	private static final String game_key = "9719c6f0c01aabf6e6d0418a730cfa08";
	private static final String game_secret = "FA023251641CD4901255DFFFE97636A1";

	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		String pid = (String) param.get("pid");
		String game_key = (String) param.get("game_key");
		String stime = (String) param.get("stime");
		String ticket = (String) param.get("ticket");
		String loginsign = (String) param.get("sign");

		TreeMap<String, String> lp = new TreeMap<>();
		lp.put("game_key", game_key);
		lp.put("stime", stime);
		lp.put("ticket", ticket);
		String test_loginSign = createSign(lp);
		if (!test_loginSign.equals(loginsign)) {
			log.error(this.getClass() + "登陆sign计算错误！---" + param.toString());
			return null;
		}
		//////////////////////// 获取用户信息///////////////////////////////////
		TreeMap<String, String> p = new TreeMap<>();
		p.put("game_key", game_key);
		p.put("login_stime", stime);
		p.put("login_ticket", ticket);
		p.put("sign", createSign(p));
		String reqresult = HttpRequest.PostFunction(URL_USERINFO, p);
		User_Entity user = JsonTransfer._In(reqresult, User_Entity.class);
		if (user == null || user.errorno != 0) {
			log.error(this.getClass() + "用户信息 获取失败！---" + reqresult);
			return null;
		}

		UserBase ubase = new UserBase();
		ubase.pid = pid;
		dSource.rtn.getInfo().setAccount(user.userData.user_id);
		dSource.rtn.getInfo().setPid(pid);
		dSource.rtn.setUsebase(ubase);
		param.put("extend", user.userData.user_id);
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
		String game_price = (String) param.get("game_price");
		// String xiao7_goid = (String) param.get("xiao7_goid");
		// String subject = (String) param.get("subject");
		String extend = (String) param.get("extends_data");
		String game_orderid = (String) param.get("game_orderid");
		String encryp_data = (String) param.get("encryp_data");

		// String sign = (String) param.get("sign_data");
		// TreeMap<String, String> p1 = new TreeMap<>();
		// p1.put("game_price", game_price);
		// p1.put("xiao7_goid", xiao7_goid);
		// p1.put("subject", subject);
		// p1.put("game_orderid", game_orderid);
		// p1.put("extends_data", extend);
		// p1.put("encryp_data", encryp_data);
		// String test_sign = createSign(p1);
		//
		// System.out.println("sign:" + sign);
		// System.out.println("test_sign:" + test_sign);

		TreeMap<String, String> p2 = new TreeMap<>();
		p2.put("game_orderid", game_orderid);
		p2.put("game_price", game_price);
		p2.put("game_key", game_key);
		p2.put("user_id", extend);
		String test_encrypsign = createSign(p2);

		if (encryp_data.equals(test_encrypsign)) {
			param.put("goodsOrder", game_orderid);
			param.put("Amount", game_price);
			param.put("ownOrder", game_orderid);
			param.put("status", 1);
			param.put("success", "success");
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
		otc.game_key = game_key;
		otc.game_orderid = String.valueOf(dSource.order.get("ownOrder"));
		otc.subject = "Diamond" + String.valueOf(dSource.order.get("goodsNum"));
		Float money = ((Float) dSource.order.get("amount"));
		if (money % 1 == 0) {
			otc.game_price = money.intValue();
		} else {
			otc.game_price = money;
		}

		otc.stime = (int) (System.currentTimeMillis() / 1000);
		otc.user_id = Integer.parseInt((String) dSource.order.get("account"));
		TreeMap<String, String> param = new TreeMap<>();
		param.put("game_key", otc.game_key);
		param.put("game_orderid", otc.game_orderid);
		param.put("subject", otc.subject);
		if (money % 1 == 0) {
			param.put("game_price", String.valueOf(money.intValue()));
		} else {
			param.put("game_price", String.valueOf(money));
		}
		param.put("stime", String.valueOf(otc.stime));
		param.put("user_id", String.valueOf(otc.user_id));
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
		sign += game_secret;
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
	public String description;// 游戏道具描述
	public String game_area;// 用户所在的游戏区
	public String game_group;// 用户所在的游戏服
	public String game_key;// * 小 7 游戏中心提供的 game_key
	public String game_level;// 用户在游戏中的等级
	public String game_orderid;// * 游戏的唯一订单号
	public float game_price;// * 道具支付金额（单位元）0.01
	public String game_role_id;// 用户的角色 Id
	public int stime;// * 当前时间戳，1970-01-01 至今的秒数，注意这里是秒数不是毫秒数。
	public String subject;// * 游戏道具名称，请游戏厂商使用下面这里的格式填写道具名称（在小 7 中并没有强制做验证）
	public int user_id;// * 小 7 游戏中心提供的用户的唯一标识
	public String sign;// * 签名，
}

class User_Entity {
	public int errorno;
	public User userData;
	public String errormsg;
}

class User {
	public String user_id;
	public String user_name;// 性别【1：男 2: 女 0：未知】
	public String user_avatar;
}
