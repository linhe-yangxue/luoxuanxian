package com.jksdk.ayx;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.constants.ReInfo;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmShare.entity.UserBase;
import com.ssmShare.facde.I_Platform;
import com.ssmShare.platform.DataConf;

@Service("SdkayxImpl")
@Scope("prototype")
public class SdkayxImpl implements I_Platform {
	public static final Logger log = LoggerFactory.getLogger(SdkayxImpl.class);

	/**
	 * token换取h5游戏授权码
	 */
	public static final String URL_token = "https://open.play.cn/oauth/token";
	public static final String URL_UserInfo = "https://open.play.cn/oauth/info";
	public static final String URL_redirect = "http://cdn.wannaplay.cn/html/public/agame.html";

	public static final String game_id = "5133456";
	public static final String game_code = "120356020239";
	public static final String client_id = "43541999";
	public static final String client_secret = "47213777f3d54bbfb45db7846bc553c9";
	public static final String client_AppKey = "3aa7094afa3c1ee6ae5ea5c94c2ee8ff";

	@SuppressWarnings("unchecked")
	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		String code = (String) param.get("code");
		String pid = (String) param.get("pid");
		///////////////////////// 获取access_token////////////////////////////////////////////
		Map<String, String> signMap = new HashedMap();
		signMap.put("client_id", client_id);
		signMap.put("client_secret", client_secret);
		signMap.put("code", code);
		signMap.put("sign_method", "MD5");
		signMap.put("version", "1.0");
		signMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
		signMap.put("grant_type", "authorization_code");

		List<String> signList = AyxUtil.paramConcat(signMap);
		String signSort = signList.get(0);
		String signCotent = signList.get(1);
		String signature = HmacSignature.encodeMD5(signCotent);

		signMap.put("sign_sort", signSort);
		signMap.put("signature", signature);
		String signresult = AyxUtil.PostFunction(URL_token, signMap);
		if (signresult == null) {
			log.error("ayx_1：access_token 获取失败！---" + param.toString());
			return null;
		}
		Code_Entity code_Entity = JsonTransfer._In(signresult, Code_Entity.class);
		if (code_Entity == null || code_Entity.access_token == null) {
			log.error("ayx_2：access_token 获取失败！---" + signresult);
			return null;
		}

		////////////////////// 获取用户信息///////////////////////////////////////////////
		Map<String, String> userMap = new HashedMap();
		userMap.put("client_id", client_id);
		userMap.put("client_secret", client_secret);
		userMap.put("access_token", code_Entity.access_token);
		userMap.put("sign_method", "MD5");
		userMap.put("version", "1.0");
		userMap.put("timestamp", String.valueOf(System.currentTimeMillis()));

		List<String> userList = AyxUtil.paramConcat(userMap);
		String userSort = userList.get(0);
		String userCotent = userList.get(1);
		String usersignature = HmacSignature.encodeMD5(userCotent);
		userMap.put("sign_sort", userSort);
		userMap.put("signature", usersignature);

		String userresult = AyxUtil.PostFunction(URL_UserInfo, userMap);

		if (userresult == null) {
			log.error("ayx_3：获取用户信息失败！---");
			return null;
		}
		User_Entity user_Entity = null;
		user_Entity = JsonTransfer._In(userresult, User_Entity.class);

		if (user_Entity == null || user_Entity.code != 0) {
			log.error("ayx_4：用户信息  获取失败！---" + signresult);
			return null;
		}
		////////////////////// 创建角色///////////////////////////////////////////////
		UserBase ubase = new UserBase();
		ubase.pid = pid;
//		ubase.setNickname(user_Entity.ext.nickname);
//		ubase.setuImg(HttpRequest.encodeImgageToBase64(new URL(user_Entity.ext.head_url)));

		dSource.rtn.getInfo().setAccount(String.valueOf(user_Entity.ext.id));
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
		String correlator = String.valueOf(param.get("correlator"));
		String orderid = String.valueOf(param.get("cp_order_id"));
		Integer fee = Integer.parseInt((String) param.get("fee"));
		String pay_type = String.valueOf(param.get("pay_type"));
		String result_code = String.valueOf(param.get("result_code"));
		String result_msg = String.valueOf(param.get("result_msg"));
		String game_code = String.valueOf(param.get("game_code"));
		String request_time = String.valueOf(param.get("request_time"));
		String sign = String.valueOf(param.get("sign_msg"));

		String test_sign = HmacSignature.encodeMD5(correlator + orderid + fee + result_code + pay_type + game_code + request_time + client_AppKey).toLowerCase();
		if (test_sign.equals(sign)) {
			param.put("goodsOrder", correlator);
			param.put("Amount", String.valueOf(fee / 100));
			param.put("ownOrder", orderid);
			param.put("status", 1);
			param.put("success", "header");
			param.put("header", "correlator:" + correlator);
		} else {
			param.put("fail", this.getClass() + "充值回调sign验证失败--" + result_msg);
		}
	}

	/**
	 * 订单创建
	 */
	@Override
	public Object payVerification(DataConf dSource) throws Exception {
		OrderToClient otc = new OrderToClient();
		otc.cp_order_id = String.valueOf(dSource.order.get("ownOrder"));
		otc.fee = ((Float) dSource.order.get("amount")).intValue();
		otc.subject = "Diamond" + String.valueOf(dSource.order.get("goodsNum"));
		otc.expired = (int) (System.currentTimeMillis() / 1000) + 3000;
		otc.game_id = game_id;
		otc.game_code = game_code;
		otc.redirect = URL_redirect;
		otc.model = "";
		String sign = HmacSignature.encodeMD5(otc.game_id + otc.expired + otc.cp_order_id + otc.redirect + otc.fee + otc.subject + client_AppKey);
		otc.sign = sign.toLowerCase();
		otc.auto = true;
		return otc;
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
	public String cp_order_id; // 订单号（自定义）
	public int expired; // （时间戳————秒）
	public String subject; // 道具名称
	public int fee; // 物品价格（以元为单位，必须为整数，与open后台相应计费点价格一致）
	public String pay_id; // （不必须）(计费点ID);
	public String game_id; // 游戏id
	public String game_code;// 游戏长ID，在open后台获取
	public String redirect; // 游戏地址，必须与OPEN平台提交的相同，即OPEN中的“游戏url”
	public String sign; // 加密
	public String page_back_url; // （不必须）取消订单返回地址
	public String model; // 默认不添加此参数：话费支付 + 第三方计费； model = 1：单独话费支付计费
	public String rebound_url; // （不必须）回跳地址：支付过程中返回地址或支付成功或失败后的返回地址
	public String user_id; // （不必须）爱游戏用户id，根据要求是否添加
	public String channel_code; // （不必须）渠道号
	public boolean auto; // true或false，为true代表系统自动判断计费方式，为false代表系统强制为wap计费
}

class Code_Entity {
	public String access_token;
	public String token_type;
	public String refresh_token;
	public String expires_in;
	public String re_expires_in;
	public String scope;
	public String user_id;
}

class User_Entity {
	public int code;
	public User ext;
	public String text;
}

class User {
	public int id;
	public String nickname;
	public String name;
	public String phone;
	public String head_url;
	public int age;
	public int sex;
	public String province;
	public String city;

}
