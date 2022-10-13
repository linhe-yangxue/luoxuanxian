package com.jksdk.lhh;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.constants.ReInfo;
import com.ssmCore.tool.colligate.Encryption;
import com.ssmShare.entity.UserBase;
import com.ssmShare.facde.I_Platform;
import com.ssmShare.platform.DataConf;

@Service("SdklhhImpl")
@Scope("prototype")
public class SdklhhImpl implements I_Platform {
	private static final Logger log = LoggerFactory.getLogger(SdklhhImpl.class);

	public static final String URL_USERINFO = "https://wx.9g.com/open/userinfo?";
	private static final String app_id = "287";
	private static final String KEY = "qvPgupAfWSmmr3xM24pgkjElq0Zl5CPU";

	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		String pid = (String) param.get("pid");
		String appid = (String) param.get("appid");
		String uid = (String) param.get("uid");
		String token = (String) param.get("token");
		String time = (String) param.get("time");
		String sign = (String) param.get("sign");

		String test_sign = Encryption.Encode(appid + uid + token + time + KEY, Encryption.MD5);
		if (test_sign.equals(sign)) {
			UserBase ubase = new UserBase();
			ubase.pid = pid;

			dSource.rtn.getInfo().setAccount(uid);
			dSource.rtn.getInfo().setPid(pid);
			dSource.rtn.setUsebase(ubase);
			// url附加参数
			param.put("extend", token);
			return ubase;
		} else {
			log.error(this.getClass() + "sign计算错误！---" + param.toString());
			return null;
		}
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
		String appid = (String) param.get("appid");
		String order_id = (String) param.get("order_id");
		String uid = (String) param.get("uid");
		String serverid = (String) param.get("serverid");
		String amount = (String) param.get("amount");
		String extra_orderno = (String) param.get("extra_orderno");
		String time = (String) param.get("time");
		String sign = (String) param.get("sign");

		String test_sign = Encryption.Encode(appid + order_id + uid + serverid + amount + extra_orderno + time + KEY, Encryption.MD5).toLowerCase();
		if (test_sign.equals(sign)) {
			param.put("goodsOrder", order_id);
			param.put("Amount", amount);
			param.put("ownOrder", extra_orderno);
			param.put("status", 1);
			param.put("success", "succ");
		} else {
			param.put("fail", this.getClass() + "充值回调sign验证失败" + param.toString());
		}
	}

	/**
	 * 订单创建
	 */
	@Override
	public Object payVerification(DataConf dSource) throws Exception {
		Map<String, String> otc = new HashMap<>();
		otc.put("extra_orderno", String.valueOf(dSource.order.get("ownOrder")));
		otc.put("appid", app_id);
		otc.put("amount", String.valueOf(((Float) dSource.order.get("amount")).intValue()));
		otc.put("serverid", String.valueOf(dSource.order.get("zid")));
		otc.put("token", String.valueOf(dSource.params.get("extend")));
		otc.put("time", String.valueOf((int) (System.currentTimeMillis() / 1000)));
		otc.put("uid", String.valueOf(dSource.order.get("account")));
		otc.put("sign", Encryption.Encode(otc.get("appid") + otc.get("uid") + otc.get("token") + otc.get("amount") + otc.get("serverid") + otc.get("extra_orderno") + otc.get("time") + KEY, Encryption.MD5));
		return createPayParam(otc);
	}

	private String createPayParam(Map<String, String> param) {
		String sign = "";
		if (param != null && param.size() > 0) {
			for (Entry<String, String> entry : param.entrySet()) {
				String name = entry.getKey() == null ? "" : entry.getKey();
				String value = entry.getValue() == null ? "" : entry.getValue();
				sign += name + "=" + value + "&";
			}
		}
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
