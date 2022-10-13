package com.jksdk.test;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.constants.ReInfo;
import com.ssmCore.tool.colligate.Encryption;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmShare.entity.UserBase;
import com.ssmShare.facde.I_Platform;
import com.ssmShare.platform.DataConf;

@Service("SdkxxImpl")
@Scope("prototype")
public class SdkXxImpl implements I_Platform {
	private static final Logger log = LoggerFactory.getLogger(SdkXxImpl.class);

	/*
	 * http://fb.h5.6816.com/index.php?ac=doc&id=login
	 * Md5(gameId=113&time=1475042196&uid=29923&userName=dreamfly_1981&key=
	 * testappykey)
	 */
	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		// log.info("log {}", JsonTransfer.getJson(param));
		if (param.get("gameId") == null) {
			log.error("no gameId");
			return null;
		}
		Map<String, String> temp_param = new LinkedHashMap<String, String>();
		temp_param.put("gameId", param.get("gameId").toString());
		temp_param.put("time", param.get("time").toString());
		temp_param.put("uid", param.get("uid").toString());
		temp_param.put("userName", param.get("userName").toString());
		temp_param.put("key", dSource.doc.getLoginKey());

		String md5 = getMd5(temp_param);
		if (!md5.equals(param.get("sign").toString())) {
			log.info("log md5 test fail, calc {} sign {} all_msg {}", md5, param.get("sign").toString(), JsonTransfer.getJson(param));
			return null;
		}

		UserBase ubase = new UserBase();

//		ubase.setNickname((String) param.get("userName"));
//		String imgurl = StringUtils.replace((String) param.get("avatar"), "/0", "/64");
//		ubase.setuImg(HttpRequest.encodeImgageToBase64(new URL(imgurl)));
		dSource.rtn.getInfo().setAccount(temp_param.get("uid"));
		dSource.rtn.getInfo().setPid((String) param.get("pid"));
		dSource.rtn.setUsebase(ubase);
		ubase.pid = (String) param.get("pid");
		StringBuffer sb = new StringBuffer();
		sb.append(temp_param.get("gameId"));// 透传，需要将来用
		param.put("extend", sb.toString());
		// log.info("loging success {}", temp_param.get("uid"));
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssmShare.facde.I_Platform#callPay(java.util.Map,
	 * com.ssmShare.platform.DataConf)
	 * 
	 */
	@Override
	public void callPay(Map<String, Object> param, DataConf dSource) throws Exception {
		// MD5按照参数名的字母顺序，key在最后
		// log.info("call pay");
		if (param.get("cpOrderId") == null) {
			log.error("no cpOrderId");
			param.put("fail", "fail");
			return;
		}
		Map<String, String> temp_param = new LinkedHashMap<String, String>();
		temp_param.put("cpOrderId", param.get("cpOrderId").toString());
		temp_param.put("gameId", param.get("gameId").toString());
		temp_param.put("goodsId", param.get("goodsId").toString());
		temp_param.put("goodsName", param.get("goodsName").toString());
		temp_param.put("money", param.get("money").toString());
		temp_param.put("orderId", param.get("orderId").toString());
		temp_param.put("role", param.get("role").toString());
		temp_param.put("server", param.get("server").toString());
		temp_param.put("status", param.get("status").toString());
		if (!temp_param.get("status").equals("success")) {
			log.info("call pay status not success {}", JsonTransfer.getJson(param));
			return;
		}
		temp_param.put("time", param.get("time").toString());
		temp_param.put("uid", param.get("uid").toString());
		temp_param.put("userName", param.get("userName").toString());
		temp_param.put("key", dSource.doc.getPayKey());
		String md5 = getMd5(temp_param);
		if (!md5.equals(param.get("sign").toString())) {
			log.info("pay md5 test fail, calc {} sign {} all_msg {}", md5, param.get("sign").toString(), JsonTransfer.getJson(param));
			param.put("fail", "fail");
		} else {
			param.put("goodsOrder", temp_param.get("orderId"));
			param.put("account", temp_param.get("uid"));
			param.put("Amount", temp_param.get("money"));
			param.put("ownOrder", temp_param.get("cpOrderId"));
			param.put("status", 1);
			param.put("success", "success");
		}
	}

	@Override
	public Object payVerification(DataConf dSource) throws Exception {
		String gameId = (String) dSource.params.get("extend");
		OrderToClient otc = new OrderToClient();
		otc.gameId = gameId;
		otc.uid = (String) dSource.order.get("account");
		otc.time = Long.toString(Calendar.getInstance().getTimeInMillis() / 1000);
		otc.goodsId = ((Integer) dSource.order.get("shopId")).toString();
		float m = (Float) dSource.order.get("amount");
		otc.money = Float.toString(m);
		otc.cpOrderId = (String) dSource.order.get("ownOrder"); // 我们自己的订单号，用来给支付回调的时候做比对验证
		// Md5(cpOrderId=1475049097&gameId=113&goodsId=1&goodsName=测试商品&money=1&role=1&server=1&time=1475049097&uid=6298253&key=testpaykey)
		Map<String, String> temp_param = new LinkedHashMap<String, String>();
		temp_param.put("cpOrderId", otc.cpOrderId);
		temp_param.put("gameId", otc.gameId);
		temp_param.put("goodsId", otc.goodsId);
		temp_param.put("goodsName", otc.goodsName);
		temp_param.put("money", otc.money);
		temp_param.put("role", otc.role);
		temp_param.put("server", otc.server);
		temp_param.put("time", otc.time);
		temp_param.put("uid", otc.uid);
		temp_param.put("key", dSource.doc.getPayKey());
		otc.sign = getMd5(temp_param);
		// log.info("Create order, to client {}", JsonTransfer.getJson(otc));
		return otc;
	}

	private String getMd5(Map<String, String> temp_param) {
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
		String result = Encryption.Encode(sb.toString(), Encryption.MD5);
		// log.info("Gen MD5 {} result {} ", sb.toString(), result);
		return result;
	}

	/*
	*/
	// 客户端拿到这个对象去向平台付钱
	class OrderToClient {
		public String gameId;
		public String uid; // 用户id
		public String time;
		public String server = "1";
		public String role = "1";
		public String goodsId;
		public String goodsName = "1";
		public String money;
		public String cpOrderId;
		public String signType = "md5";
		public String sign;
		public String pay_url = "http://fb.h5.6816.com/gapi.php?ac=order";
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
