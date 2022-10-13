package com.jksdk.xjh5;

import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.constants.I_constants;
import com.ssmCore.constants.ReInfo;
import com.ssmCore.tool.colligate.Encryption;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmShare.entity.UserBase;
import com.ssmShare.facde.I_Platform;
import com.ssmShare.platform.DataConf;

@Service("Sdkxjh5Impl")
@Scope("prototype")
public class SdkXjh5Impl implements I_Platform {
	private static final Logger log = LoggerFactory.getLogger(SdkXjh5Impl.class);
	public static final String appkey = "tt*l2^CX$vS6Vh9p";
	public static final String payAppid = "6A6A947FD17415516";

	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		String pid = (String) param.get("pid");
		String new_time = String.valueOf(param.get("new_time"));
		String sdkloginmodel = String.valueOf(param.get("sdkloginmodel"));
		String channelExt = String.valueOf(param.get("channelExt"));
		String game_appid = String.valueOf(param.get("game_appid"));
		String loginplatform2cp = String.valueOf(param.get("loginplatform2cp"));
		String sdklogindomain = String.valueOf(param.get("sdklogindomain"));
		String user_id = String.valueOf(param.get("user_id"));
		Object emailObj = param.get("email");
		String email = (emailObj == null ? "" : String.valueOf(emailObj));
		String sign = String.valueOf(param.get("sign"));

		SortedMap<String, String> lp = new TreeMap<>();
		lp.put("new_time", new_time);
		lp.put("sdkloginmodel", sdkloginmodel);
		lp.put("channelExt", channelExt);
		lp.put("game_appid", game_appid);
		lp.put("loginplatform2cp", loginplatform2cp);
		lp.put("sdklogindomain", sdklogindomain);
		lp.put("email", email);
		lp.put("user_id", user_id);
		String tempSign = createSign(lp, appkey);
		if (!tempSign.equals(sign)) {
			log.error(this.getClass() + "登陆验证Sign失败！");
			param.put(I_constants.PTOKEN_ERRPR, "登陆验失败！");
			return null;
		}
		UserBase ubase = new UserBase();
		ubase.pid = pid;
		dSource.rtn.getInfo().setAccount(user_id);
		dSource.rtn.getInfo().setPid(pid);
		dSource.rtn.setUsebase(ubase);

		CSData clentData = new CSData();
		clentData.sdklogindomain = sdklogindomain;
		clentData.sdkloginmodel = sdkloginmodel;
		clentData.game_appid = game_appid;
		clentData.channelExt = channelExt;
		param.put("extend", JsonTransfer.getJson(clentData));
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

		return null;
	}

	@Override
	public void callPay(Map<String, Object> param, DataConf dSource) throws Exception {
		String amount = String.valueOf(param.get("amount"));
		String channel_source = String.valueOf(param.get("channel_source"));
		String game_appid = String.valueOf(param.get("game_appid"));
		String out_trade_no = String.valueOf(param.get("out_trade_no"));
		String payplatform2cp = String.valueOf(param.get("payplatform2cp"));
		String trade_no = String.valueOf(param.get("trade_no"));
		String sign = String.valueOf(param.get("sign"));

		SortedMap<String, String> p = new TreeMap<>();
		p.put("amount", amount);
		p.put("channel_source", channel_source);
		p.put("game_appid", game_appid);
		p.put("out_trade_no", out_trade_no);
		p.put("trade_no", trade_no);
		p.put("payplatform2cp", payplatform2cp);

		String test_sign = createSign(p, appkey);
		if (test_sign.equals(sign)) {
			param.put("goodsOrder", out_trade_no);
			param.put("Amount", amount);
			param.put("ownOrder", trade_no);
			param.put("status", 1);
			param.put("success", "{\"status\":\"success\"}");
			////////////////////////
		} else {
			param.put("fail", this.getClass() + "充值回调sign验证失败" + param.toString());
		}
	}

	@Override
	public Object payVerification(DataConf dSource) throws Exception {
		String extend = (String) dSource.params.get("extend");
		CSData csData = JsonTransfer._In(extend, CSData.class);

		OrderToClient client = new OrderToClient();
		client.amount = (int) Float.parseFloat(String.valueOf(dSource.order.get("amount")));
		client.channelExt = csData.channelExt;
		client.game_appid = csData.game_appid;
		client.props_name = "Diamond" + dSource.order.get("goodsNum");
		client.trade_no = String.valueOf(dSource.order.get("ownOrder"));
		client.user_id = String.valueOf(dSource.order.get("account"));
		client.sdklogindomain = csData.sdklogindomain;
		client.sdkloginmodel = csData.sdkloginmodel;

		SortedMap<String, String> param = new TreeMap<String, String>();
		param.put("amount", String.valueOf(client.amount));
		param.put("channelExt", client.channelExt);
		param.put("game_appid", client.game_appid);
		param.put("props_name", client.props_name);
		param.put("trade_no", client.trade_no);
		param.put("user_id", client.user_id);

		client.sign = createSign(param, appkey);
		return client;
	}

	private String createSign(SortedMap<String, String> param, String key) {
		String sign = "";
		if (param != null && param.size() > 0) {
			for (Entry<String, String> entry : param.entrySet()) {
				String name = entry.getKey() == null ? "" : entry.getKey();
				String value = entry.getValue() == null ? "" : entry.getValue();
				sign += name + "=" + value + "&";
			}
		}
		if (sign.length() > 0)
			sign = sign.substring(0, sign.length() - 1);
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

class CSData {
	public String sdklogindomain;
	public String sdkloginmodel;
	public String game_appid;
	public String channelExt;
}

class OrderToClient {
	public String trade_no;
	public int amount;
	public String channelExt;
	public String game_appid;
	public String props_name;
	public String sdklogindomain;
	public String sdkloginmodel;
	public String user_id;
	public String sign;
	public String url;
}
