package com.jksdk.test;

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

@Service("SdkchwImpl")
@Scope("prototype")
public class SdkChwImpl implements I_Platform {
	private static final Logger log = LoggerFactory.getLogger(SdkChwImpl.class);
	private static final String GET_INFO_URL = "http://juhe.newgame.com/user/info?token=";
	private static final String RSA_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDBCthDLXjf8Zit3D2A6wSnm1bP6S+g99XrTjFIyvJkiE5WLd3p4O1l2pEFZrc4/SJccw5NKgkfRugoFu2Y/jMjgjXHdd+gXGZZcXFWmMR41czve/FQ4cR4jAcmLQ2I4h1AURGVjw7dAYrSvGww+LB3iL4EcQqlEi4QVMHq8BlghwIDAQAB";

	/*
	 */
	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		// log.info("log {}", JsonTransfer.getJson(param));
		if (param.get("token") == null) {
			log.error("no token");
			return null;
		}
		String token = param.get("token").toString();
		String channel_id = param.get("channel_id").toString();
		StringBuffer sb = new StringBuffer();
		sb.append(GET_INFO_URL).append(token);
		String get_info = HttpRequest.GetFunction(sb.toString());
		Info info_obj = JsonTransfer._In(get_info, Info.class);
		if (info_obj == null) {
			log.info("get info error url {} result {}", sb.toString(), get_info);
			return null;
		}

		UserBase ubase = new UserBase();
		ubase.pid = (String) param.get("pid");

		// ubase.setNickname(info_obj.data.nick_name);
		// String imgurl = StringUtils.replace(info_obj.data.avatar_url,
		// "/0", "/64");
		// ubase.setuImg(imgurl);
		dSource.rtn.getInfo().setAccount(info_obj.data.open_id);
		dSource.rtn.getInfo().setPid((String) param.get("pid"));
		dSource.rtn.setUsebase(ubase);
		StringBuffer sbe = new StringBuffer();
		sbe.append(info_obj.data.open_id).append("|").append(channel_id);// 透传，需要将来用
		param.put("extend", sbe.toString());
		// log.info("loging success ");
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
		// log.info("call pay {}", JsonTransfer.getJson(param));
		if (param.get("amount") == null) {
			log.error("no amount");
			param.put("fail", "fail");
			return;
		}
		Map<String, String> temp_param = new LinkedHashMap<String, String>();
		temp_param.put("amount", param.get("amount").toString());
		temp_param.put("app_id", param.get("app_id").toString());
		temp_param.put("app_order_id", param.get("app_order_id").toString());
		temp_param.put("ext", param.get("ext").toString());
		temp_param.put("open_id", param.get("open_id").toString());
		temp_param.put("order_id", param.get("order_id").toString());
		temp_param.put("payment_time", param.get("payment_time").toString());
		temp_param.put("status", param.get("status").toString());
		temp_param.put("subject", param.get("subject").toString());
		String rsa_data = getRSA(temp_param);
		String sign = param.get("sign").toString();
		String status = param.get("status").toString();
		Boolean test = Encryption.verify(rsa_data, RSA_KEY, sign, Encryption.SIGNATURE_ALGORITHM_SHA1);
		if (status.equals("STATUS_SUCCESS") && test) {
			param.put("goodsOrder", temp_param.get("order_id"));
			param.put("ownOrder", temp_param.get("app_order_id"));
			param.put("Amount", temp_param.get("amount"));
			param.remove("status");
			param.put("status", 1);
			param.put("success", "success");
			// log.info("success");
		} else {
			log.info("call pay fail {}, {}, {}", JsonTransfer.getJson(param), status, test);
			param.remove("status");
			param.put("status", 0);
			param.put("fail", "fail");
		}
	}

	@Override
	public Object payVerification(DataConf dSource) throws Exception {
		String extend = (String) dSource.params.get("extend");
		String open_id = extend.substring(0, extend.indexOf("|"));
		OrderToClient otc = new OrderToClient();
		otc.app_id = dSource.doc.getPayKey();
		otc.amount = Integer.toString(((Float) dSource.order.get("amount")).intValue());
		otc.subject = ((Integer) dSource.order.get("shopId")).toString();
		otc.app_order_id = (String) dSource.order.get("ownOrder");
		otc.open_id = open_id;
		otc.notify_url = "http://game.wannaplay.cn/" + dSource.doc.getPid() + "/" + dSource.gid + "/pay";
		Map<String, String> temp_param = new LinkedHashMap<String, String>();
		temp_param.put("amount", otc.amount);
		temp_param.put("app_id", otc.app_id);
		temp_param.put("app_order_id", otc.app_order_id);
		temp_param.put("ext", otc.ext);
		temp_param.put("notify_url", otc.notify_url);
		temp_param.put("open_id", otc.open_id);
		temp_param.put("subject", otc.subject);
		otc.sign = getMd5(temp_param, dSource.doc.getLoginKey());
		log.info("Create order, to client {}", JsonTransfer.getJson(otc));
		return otc;
	}

	private String getMd5(Map<String, String> temp_param, String key) {
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
		sb.append(key);
		String result = Encryption.Encode(sb.toString(), Encryption.MD5);
		// log.info("Gen MD5 {} result {} ", sb.toString(), result);
		return result;
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
		// log.info("RAS DATA {}", sb.toString());
		return sb.toString();
	}

	// 客户端拿到这个对象去向平台付钱
	class OrderToClient {
		public String app_id;
		public String amount;
		public String subject;
		public String app_order_id;
		public String ext = "1";
		public String open_id;
		public String notify_url;
		public String sign;
	}

	class Info {
		public Data data;
	}

	class Data {
		public String avatar_url;
		public String channel_id;
		public String nick_name;
		public String open_id;
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
