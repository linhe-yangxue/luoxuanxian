package com.jksdk.test;

import java.net.URLEncoder;
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

@Service("SdkzywlImpl")
@Scope("prototype")
public class SdkZywlImpl implements I_Platform {
	private static final Logger log = LoggerFactory.getLogger(SdkZywlImpl.class);

	/*
	 * token=
	 * MD5(“username=zhangsan&logintime=20130101125612&appkey=123123123213”)
	 * 注：appkey 由 SDK方 提供，登录、充值使用同一个 appkey
	 */
	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		// log.info("log {}", JsonTransfer.getJson(param));
		if (param.get("username") == null) {
			log.error("no username");
			return null;
		}
		Map<String, String> temp_param = new LinkedHashMap<String, String>();
		temp_param.put("username", param.get("username").toString());
		temp_param.put("logintime", param.get("logintime").toString());
		temp_param.put("appkey", dSource.doc.getLoginKey());

		String md5 = getMd5(temp_param);
		if (!md5.equals(param.get("token").toString())) {
			log.info("log md5 test fail, calc {} sign {} all_msg {}", md5, param.get("token").toString(), JsonTransfer.getJson(param));
			return null;
		}
		temp_param.put("userid", param.get("userid").toString());

		UserBase ubase = new UserBase();
		ubase.pid = (String) param.get("pid");

		dSource.rtn.getInfo().setAccount(temp_param.get("userid"));
		dSource.rtn.getInfo().setPid((String) param.get("pid"));
		dSource.rtn.setUsebase(ubase);

		StringBuffer sb = new StringBuffer();
		sb.append(temp_param.get("username"));// 透传，需要将来用
		param.put("extend", sb.toString());
		// log.info("loging success {}", JsonTransfer.getJson(param));
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
	 * com.ssmShare.platform.DataConf) //签名字符串规则：
	 * //$str="orderid=".$orderid."&username=".urlencode($username).
	 * //"&productname=".urlencode($productname)."&amount=".urlencode($amount).
	 * //"&roleid=".urlencode($roleid)."&serverid=".urlencode($serverid).
	 * //"&appid=".urlencode($appid)."&paytime=".urlencode($paytime)."&attach=".
	 * urlencode($attach). //"&appkey=".$game['appkey'];
	 */
	@Override
	public void callPay(Map<String, Object> param, DataConf dSource) throws Exception {
		// log.info("call pay");
		if (param.get("orderid") == null) {
			log.error("no orderid");
			param.put("fail", "fail");
			return;
		}
		Map<String, String> temp_param = new LinkedHashMap<String, String>();
		temp_param.put("orderid", param.get("orderid").toString());
		temp_param.put("username", URLEncoder.encode(param.get("username").toString(), ENC));
		temp_param.put("productname", URLEncoder.encode(param.get("productname").toString(), ENC));
		temp_param.put("amount", URLEncoder.encode(param.get("amount").toString(), ENC));
		temp_param.put("roleid", URLEncoder.encode(param.get("roleid").toString(), ENC));
		temp_param.put("serverid", URLEncoder.encode(param.get("serverid").toString(), ENC));
		temp_param.put("appid", URLEncoder.encode(param.get("appid").toString(), ENC));
		temp_param.put("paytime", URLEncoder.encode(param.get("paytime").toString(), ENC));
		temp_param.put("attach", URLEncoder.encode(param.get("attach").toString(), ENC));
		temp_param.put("appkey", dSource.doc.getLoginKey());
		String md5 = getMd5(temp_param);
		if (!md5.equals(param.get("token").toString())) {
			log.info("pay md5 test fail, calc {} sign {} all_msg {}", md5, param.get("token").toString(), JsonTransfer.getJson(param));
			param.put("fail", "fail");
		} else {
			param.put("goodsOrder", temp_param.get("orderid"));
			param.put("Amount", temp_param.get("amount"));
			param.put("ownOrder", temp_param.get("attach"));
			param.put("status", 1);
			param.put("success", "success");
		}
	}

	private static final String ENC = "UTF-8";

	@Override
	public Object payVerification(DataConf dSource) throws Exception {
		// log.info("pay {}", JsonTransfer.getJson(dSource));
		String username = (String) dSource.params.get("extend");
		OrderToClient otc = new OrderToClient();
		otc.username = URLEncoder.encode(username, ENC);
		otc.productname = URLEncoder.encode(otc.productname, ENC);
		int m = ((Float) dSource.order.get("amount")).intValue();
		otc.amount = URLEncoder.encode(Integer.toString(m), ENC);
		otc.serverid = URLEncoder.encode(otc.roleid, ENC);
		otc.serverid = URLEncoder.encode(otc.serverid, ENC);
		otc.appid = URLEncoder.encode(dSource.doc.getPayKey(), ENC);
		otc.paytime = URLEncoder.encode(Long.toString(Calendar.getInstance().getTimeInMillis()), ENC);
		otc.attach = URLEncoder.encode((String) dSource.order.get("ownOrder"), ENC); // 我们自己的订单号，用来给支付回调的时候做比对验证
		// Token =
		// MD5("username=urlencode('zhangsan')&productname=urlencode('test')&amount=urlencode('1')
		// &roleid=urlencode('10')&serverid=urlencode('1')&appid=urlencode('6')&paytime=urlencode('20130101125612')
		// &attach=urlencode('test')&appkey=123123123213)
		Map<String, String> temp_param = new LinkedHashMap<String, String>();
		temp_param.put("username", otc.username);
		temp_param.put("productname", otc.productname);
		temp_param.put("amount", otc.amount);
		temp_param.put("roleid", otc.roleid);
		temp_param.put("serverid", otc.serverid);
		temp_param.put("appid", otc.appid);
		temp_param.put("paytime", otc.paytime);
		temp_param.put("attach", otc.attach);
		temp_param.put("appkey", dSource.doc.getLoginKey());
		otc.token = getMd5(temp_param);
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
		public String username;
		public String productname = "1";
		public String amount;
		public String roleid = "1";
		public String serverid = "1";
		public String appid;
		public String paytime;
		public String attach;
		public String token;
		public String pay_url = "http://h5i.6533.com/Pay/index";
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
