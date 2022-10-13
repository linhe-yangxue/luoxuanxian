package com.jksdk.test;

import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

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

/*
 * http://doc.i1758.cn/guide/sign.html
 * 第一步：对参数按照key=value的格式，并按照参数名ASCII字典序排序如下 stringA = "appKey=1&gid=2&nonce=4&timestamp=3"
 * 第二步：拼接API密钥并取md5值,密钥从平台方获取：stringSignTemp= stringA + "55555"
 * 第3步：Sign = MD5(stringSignTemp)
 * 代码见下方
 */

@Service("SdkhzxhImpl")
@Scope("prototype")
public class SdkHzxhImpl implements I_Platform {
	private static final Logger log = LoggerFactory.getLogger(SdkHzxhImpl.class);

	private static final String REDIRECT_URL = "http://play.vmplay.com:3700/xuhe/api/user?redirectUri=http://game.wannaplay.cn/hzxh/yqdq/login";

	private static final String RSA_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDnPsKTZSsMxCAqtTALGIypTTU8sXE30DVE0QruHUBfJVvJxi/eJHkGBJRkBcgBqNv/93h6hXPDXDdywqLjiXwggBrwAb+EC0OTUwQTbBoTCgsvnC7caG5N56BW8vE9M9q0XE3SX/ang8e0PG3bwG7hXKBJcQbCobgEZi8RV13BywIDAQAB";

	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		if (param.get("game_id") == null) {
			// log.info("log 1");
			dSource.doc.setGameUrl_login(REDIRECT_URL);
			return null;
		}

		UserBase ubase = new UserBase();
		ubase = new UserBase();
		ubase.pid = (String) param.get("pid");
		dSource.rtn.getInfo().setAccount((String) param.get("user_id"));
		dSource.rtn.getInfo().setPid((String) param.get("pid"));
		dSource.rtn.setUsebase(ubase);

		StringBuffer sb = new StringBuffer();
		sb.append((String) param.get("user_id")).append("|").append((String) param.get("game_id"));
		param.put("extend", sb.toString());
		// log.info("log 3");
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
		// log.info("call pay {}", JsonTransfer.getJson(param));
		String code = param.get("code").toString();
		if (!code.equals("4000")) {
			log.info("pay error {}", JsonTransfer.getJson(param));
			param.put("fail", "FAIL");// -2 账号不存在 //-4验证码错误 //-5订单号重复
		} else {
			param.put("goodsOrder", param.get("order_id").toString());
			param.put("ownOrder", param.get("order_id").toString());
			param.put("Amount", (String) param.get("pay_fee"));
			param.put("status", 1);
			param.put("success", "SUCCESS"); // CP收到支付通知后，返回 success 字符串
			log.info("success");
		}
	}

	@Override
	public Object payVerification(DataConf dSource) throws Exception {
		// log.info("payVerification {}", JsonTransfer.getJson(dSource));
		String extend = (String) dSource.params.get("extend");
		String user_id = extend.substring(0, extend.indexOf("|"));
		String game_id = extend.substring(extend.indexOf("|") + 1);

		Map<String, String> my_param = new LinkedHashMap<String, String>();
		my_param.put("game_id", game_id);
		my_param.put("notifyurl", "http://game.wannaplay.cn/hzxh/yqdq/pay");
		my_param.put("order_id", (String) dSource.order.get("ownOrder")); // 我们自己的订单号，用来给支付回调的时候做比对验证)
		my_param.put("returnurl", "http://game.wannaplay.cn/hzxh/yqdq/login");
		my_param.put("user_id", user_id);
		String money = String.format("%.2f", (Float) (dSource.order.get("amount")));
		my_param.put("wares_fee", money);
		my_param.put("wares_name", ((Integer) dSource.order.get("shopId")).toString());
		String result = HttpRequest.PostFunction("http://play.vmplay.com:3700/xuhe/wares/order", my_param);
		// log.info("rr {}", result);
		VertifyResult result_obj = JsonTransfer._In(result, VertifyResult.class);
		log.info("dco {}", JsonTransfer.getJson(my_param));
		if (result_obj == null) {
			log.info("result_obj error");
			return null;
		}

		if (result_obj.code != 3000) {
			log.info("Error code {} msg {}", result_obj.code, result_obj.msg);
			return null;
		}
		if (!Encryption.verify(getRSA(my_param), RSA_KEY, URLDecoder.decode(result_obj.info.signs, "utf-8"), Encryption.SIGNATURE_ALGORITHM_MD5)) {
			log.info("sort:", getRSA(my_param));
			log.info("Error sing calc {} get {}", getRSA(my_param), result_obj.info.signs);
			return null;
		}

		OrderToClient otc = new OrderToClient();
		otc.pay_url = result_obj.info.pay_url;
		// log.info("Order create {}", result_obj.info.pay_url);
		return otc;
	}

	private String getRSA(Map<String, String> temp_param) {
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		for (Entry<String, String> t : temp_param.entrySet()) {
			if (!first) {
				sb.append("&");
			}
			sb.append(t.getKey()).append("=").append(t.getValue());
			if (first) {
				first = false;
			}
		}
		// log.info("rsa {}", sb.toString());
		// log.info("rsa result {}", sb.toString());
		return sb.toString();
	}

	class VertifyResult {
		public int code; // 响应结果
		public String msg; // 错误信息
		public Info info; // 数据
		public String pay_status;
		public String order_id;
		public String extradata;
		public Float pay_fee;
	}

	class Info {
		public String pay_url;
		public String signs;
		public String extradata;
	}

	class OrderToClient {
		public String pay_url;
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
