package com.jksdk.test;

import java.util.Map;

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

@Service("SdkhwwImpl")
@Scope("prototype")
public class SdkHwwImpl implements I_Platform {
	private static final Logger log = LoggerFactory.getLogger(SdkHwwImpl.class);

	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		if (param.get("userAccount") == null) {
			log.error("no userAccount {}", JsonTransfer.getJson(param));
			return null;
		}
		String userAccount = param.get("userAccount").toString();
		String loginTime = param.get("loginTime").toString();
		StringBuffer sb = new StringBuffer();
		sb.append(userAccount).append("&").append(loginTime).append("&").append(dSource.doc.getLoginKey());
		String md5 = Encryption.Encode(sb.toString(), Encryption.MD5);
		if (!md5.equals(param.get("sign").toString())) {
			log.info("log md5 test fail, calc {} sign {} all_msg {}", md5, param.get("sign").toString(), JsonTransfer.getJson(param));
			return null;
		}

		UserBase ubase = new UserBase();
		ubase.pid = (String) param.get("pid");
		dSource.rtn.getInfo().setAccount(userAccount);
		dSource.rtn.getInfo().setPid((String) param.get("pid"));
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
		if (param.get("orderId") == null) {
			log.info("pay call back error json {}", JsonTransfer.getJson(param));
			param.put("fail", -4);
			return;
		}
		String orderId = (String) param.get("orderId");
		String orderStatus = (String) param.get("orderStatus");
		if (!orderStatus.equals("1")) {
			log.info("pay call back error orderStatus not 1 {}", JsonTransfer.getJson(param));
			param.put("fail", -4);
			return;
		}
		String amount = (String) param.get("amount");
		String callBackInfo = (String) param.get("callBackInfo");
		String key = dSource.doc.getLoginKey();
		StringBuilder sb = new StringBuilder();
		sb.append("orderId=").append(orderId).append("&").append("orderStatus=").append(orderStatus).append("&").append("amount=").append(amount).append("&").append("callBackInfo=").append(callBackInfo).append("&").append("appkey=").append(key);
		String test_sign = Encryption.Encode(sb.toString(), Encryption.MD5);
		String sign = (String) param.get("sign");

		if (test_sign.equals(sign)) {
			param.put("goodsOrder", orderId);
			param.put("Amount", amount);
			param.put("ownOrder", callBackInfo);
			param.put("status", 1);
			param.put("success", "success"); // 1成功
		} else {
			log.error("calc {} sb {} parm {}", test_sign, sb.toString(), JsonTransfer.getJson(param));
			param.put("fail", -4);
		}
	}

	@Override
	public Object payVerification(DataConf dSource) throws Exception {
		// log.info("payit {}", JsonTransfer.getJson(dSource));
		String goodsName = ((Integer) dSource.order.get("shopId")).toString();
		Integer mi = ((Float) dSource.order.get("amount")).intValue();
		String amount = mi.toString();// 单位分
		String roleName = "1";// (String)dSource.order.get("nickname");
		String callBackInfo = (String) dSource.order.get("ownOrder");
		;
		String key = dSource.doc.getLoginKey();
		StringBuilder sb = new StringBuilder();
		sb.append(goodsName).append(amount).append(roleName).append(callBackInfo).append(key);
		String sign = Encryption.Encode(sb.toString(), Encryption.MD5);
		OrderToClient otc = new OrderToClient();
		otc.goodsName = goodsName;
		otc.amount = amount;
		otc.roleName = roleName;
		otc.callBackInfo = callBackInfo;
		otc.sign = sign;
		// log.info("Create order, to client {}", JsonTransfer.getJson(otc));
		return otc;
	}

	// 客户端拿到这个对象去向平台付钱
	class OrderToClient {
		public String goodsName;
		public String amount;
		public String roleName;
		public String callBackInfo;
		public String sign;
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
