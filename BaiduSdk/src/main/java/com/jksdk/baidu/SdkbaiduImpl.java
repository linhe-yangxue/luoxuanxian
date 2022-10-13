package com.jksdk.baidu;

import java.util.HashMap;
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

@Service("SdkbdImpl")
@Scope("prototype")
public class SdkbaiduImpl implements I_Platform {
	private static final Logger log = LoggerFactory.getLogger(SdkbaiduImpl.class);
	public static final String URL_callBack = "http%3a%2f%2fgame.wannaplay.cn%2fbd%2fyqdq%2flogin";

	public static final String login_clientid = "9715751";
	public static final String API_Key = "6eGdTEDQZytyWCUS5pW7ExF5";
	public static final String Secret_Key = "bksK6BQ1M0OyuGoZyPkWan2Wbz7tPN9D";

	public static final String pay_clientid = "5024";
	public static final String app_key = "AK24FC83E31D9B1D1724F327E105C3AC";
	public static final String pay_key = "PKD2A153845D5A21B681A1F419BDEDA8";

	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		String pid = (String) param.get("pid");
		String code = (String) param.get("code");

		String accessTokenUrl = "https://openapi.baidu.com/oauth/2.0/token?grant_type=authorization_code&code=" + code + "&client_id=" + API_Key + "&client_secret=" + Secret_Key + "&redirect_uri=" + URL_callBack;
		String accessRes = HttpRequest.GetFunction(accessTokenUrl);
		AccessTokenEntity accessTokenEntity = JsonTransfer._In(accessRes, AccessTokenEntity.class);
		if (accessTokenEntity == null || accessTokenEntity.error != null) {
			log.error(this.getClass() + "AccessToken获取失败！---" + accessRes);
			return null;
		}
		String userInfoRes = HttpRequest.GetFunction("https://openapi.baidu.com/rest/2.0/passport/users/getLoggedInUser?access_token=" + accessTokenEntity.access_token);
		UserInfoEntity userInfoEntity = JsonTransfer._In(userInfoRes, UserInfoEntity.class);
		if (userInfoEntity.error_code != null && userInfoEntity.error_code != 0) {
			log.error(this.getClass() + "用户信息获取失败！---" + userInfoRes);
			return null;
		}
		UserBase ubase = new UserBase();
		ubase.pid = pid;
//		ubase.setNickname(userInfoEntity.uname);
//		ubase.setuImg(HttpRequest.encodeImgageToBase64(new URL("http://tb.himg.baidu.com/sys/portraitn/item/{" + userInfoEntity.portrait + "}")));

		dSource.rtn.getInfo().setAccount(userInfoEntity.uid);
		dSource.rtn.getInfo().setPid(pid);
		dSource.rtn.setUsebase(ubase);
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
		String data = (String) param.get("data");// json
		String sign = (String) param.get("sign");

		Order order = JsonTransfer._In(data, Order.class);
		SortedMap<String, String> p = new TreeMap<>();
		p.put("fs_bill_no", order.fs_bill_no);
		p.put("app_id", order.app_id);
		p.put("account_name", order.account_name);
		p.put("server_id", order.server_id);
		p.put("role_id", order.role_id);
		p.put("pay_money", order.pay_money);
		p.put("pay_type", order.pay_type);
		p.put("pay_time", order.pay_time);
		p.put("cp_order_id", order.cp_order_id);
		p.put("cp_ext", order.cp_ext);
		String test_sign = createSign(p, pay_key);
		if (test_sign.equals(sign)) {
			param.put("goodsOrder", order.fs_bill_no);
			param.put("Amount", String.valueOf(order.pay_money));
			param.put("ownOrder", order.cp_order_id);
			param.put("status", 1);
			param.put("success", "SUCCESS");
		} else {
			param.put("fail", this.getClass() + "充值回调sign验证失败" + param.toString());
		}
	}

	/**
	 * 订单创建
	 */
	@Override
	public Object payVerification(DataConf dSource) throws Exception {
		Order otc = new Order();
		otc.app_id = pay_clientid;
		otc.account_name = String.valueOf(dSource.order.get("account"));
		otc.server_id = String.valueOf(dSource.order.get("zid"));
		otc.role_id = String.valueOf(dSource.order.get("uuid"));
		otc.goods_name = "Diamond" + String.valueOf(dSource.order.get("goodsNum"));
		otc.pay_money = String.valueOf((Float) dSource.order.get("amount"));
		otc.pay_type = String.valueOf(dSource.params.get("extend"));
		otc.pay_time = String.valueOf((int) (System.currentTimeMillis() / 1000));
		otc.cp_order_id = String.valueOf(dSource.order.get("ownOrder"));
		otc.cp_notify_url = "http://game.wannaplay.cn/" + dSource.doc.getPid() + "/" + dSource.gid + "/pay";
		otc.cp_finish_url = "http://cdn.wannaplay.cn/html/public/baidu_pay_res.html";
		otc.cp_ext = "";

		SortedMap<String, String> param = new TreeMap<>();
		param.put("app_id", otc.app_id);
		param.put("account_name", otc.account_name);
		param.put("server_id", otc.server_id);
		param.put("role_id", otc.role_id);
		param.put("goods_name", otc.goods_name);
		param.put("pay_money", otc.pay_money);
		param.put("pay_type", otc.pay_type);
		param.put("pay_time", otc.pay_time);
		param.put("cp_order_id", otc.cp_order_id);
		param.put("cp_notify_url", otc.cp_notify_url);
		param.put("cp_finish_url", otc.cp_finish_url);
		param.put("cp_ext", otc.cp_ext);

		OrderToClient orderToClient = new OrderToClient();
		orderToClient.ver = "1";
		orderToClient.data = JsonTransfer.getJson(otc);
		orderToClient.sign = createSign(param, app_key);

		Map<String, String> p = new HashMap<>();
		p.put("ver", orderToClient.ver);
		p.put("data", orderToClient.data);
		p.put("sign", orderToClient.sign);
		String payres = HttpRequest.PostFunction("http://h5pay.ddyouxi.com/trade/", p);
		PayResEntity entity = JsonTransfer._In(payres, PayResEntity.class);
		entity.url = entity.url.replaceAll("\\\\", "");
		return entity;
	}

	/**
	 * 生成签名
	 * 
	 * @param param
	 *            参数
	 * @return
	 */
	private String createSign(SortedMap<String, String> param, String key) {
		String sign = "";
		if (param != null && param.size() > 0) {
			for (Entry<String, String> entry : param.entrySet()) {
				String name = entry.getKey() == null ? "" : entry.getKey();
				String value = entry.getValue() == null ? "" : entry.getValue();
				sign += name + "=" + value;
			}
		}
		sign += key;
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

class PayResEntity {
	public int code;// 版本
	public String msg;// JSON
	public String url;// 按照签名算法计算出来的签名 必须
}

class OrderToClient {
	public String ver;// 版本
	public String data;// JSON
	public String sign;// 按照签名算法计算出来的签名 必须

}

class Order {
	public String fs_bill_no;
	public String app_id;// 游戏应用ID int
	public String account_name;// 账号名 string
	public String server_id;// 服务器ID int
	public String role_id;// 玩家角色ID string
	public String goods_name;// 物品名称 string
	public String pay_money;// 充值的人民币 number
	public String pay_type;// 充值方式 int
	public String pay_time;// 充值时间戳 int
	public String cp_order_id;// 游戏开发商提供的订单ID string
	public String cp_notify_url;// 充值成功后异步回调的URL string
	public String cp_finish_url;// 充值完毕后同步返回的URL string
	public String cp_ext;// 扩展参数 string

}

class AccessTokenEntity {
	public String expires_in;
	public String refresh_token;
	public String access_token;
	public String session_secret;
	public String session_key;
	public String scope;
	public String error;
	public String error_description;
}

class UserInfoEntity {
	public String uid;
	public String uname;
	public String portrait;// 头像key
	public Integer error_code;
}
