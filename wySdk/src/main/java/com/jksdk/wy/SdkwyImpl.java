package com.jksdk.wy;

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

@Service("SdkwyImpl")
@Scope("prototype")
public class SdkwyImpl implements I_Platform {
	private static final Logger log = LoggerFactory.getLogger(SdkwyImpl.class);

	public static final String URL_USERINFO = "http://www.5you.cc/index.php/Home/Pay/saft?";
	private static final String app_id = "FqJApKmzIoaqFJ6d";
	private static final String accesskey = "402882f04eaf627c014eaf627c350001";

	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		String token = (String) param.get("token");
		String uid = (String) param.get("uid");
		String pid = (String) param.get("pid");
		String reqresult = HttpRequest.GetFunction(URL_USERINFO + "token=" + token + "&uid=" + uid);
		User_wy[] user = JsonTransfer._In(reqresult, User_wy[].class);
		if (user == null || !user[0].uid.equals(uid)) {
			log.error(this.getClass() + "用户信息 获取失败！---" + reqresult);
			return null;

		}
		UserBase ubase = new UserBase();
		ubase.pid = pid;

		dSource.rtn.getInfo().setAccount(uid);
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
		String code = String.valueOf(param.get("code"));
		String appid = String.valueOf(param.get("appid"));
		String reqid = String.valueOf(param.get("reqid"));
		String fee = String.valueOf(param.get("fee"));
		String uid = String.valueOf(param.get("uid"));
		// String timestamp = (String) param.get("timestamp");
		String sign = String.valueOf(param.get("sign"));

		LinkedHashMap<String, String> p = new LinkedHashMap<>();
		p.put("appid", appid);
		p.put("fee", fee);
		p.put("reqid", reqid);
		p.put("uid", uid);
		p.put("accesskey", accesskey);

		String test_sign = createSign(p);
		System.out.println(test_sign);

		if (test_sign.equals(sign)) {
			param.put("goodsOrder", reqid);
			param.put("Amount", String.valueOf(Integer.parseInt(fee) * 100));//特殊处理
			param.put("ownOrder", reqid);
			param.put("status", 1);
			param.put("success", code);
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
		otc.reqid = String.valueOf(dSource.order.get("ownOrder"));
		otc.appid = app_id;
		otc.fee = ((Float) dSource.order.get("amount")).intValue();
		otc.productname = "Diamond" + String.valueOf(dSource.order.get("goodsNum"));
		otc.uid = String.valueOf(dSource.order.get("account"));
		otc.url = "http://game.wannaplay.cn/" + dSource.doc.getPid() + "/" + dSource.gid + "/pay";

		LinkedHashMap<String, String> p = new LinkedHashMap<>();
		p.put("appid", otc.appid);
		p.put("fee", String.valueOf(otc.fee));
		p.put("productname", otc.productname);
		p.put("reqid", otc.reqid);
		p.put("uid", otc.uid);
		p.put("url", otc.url);
		p.put("accesskey", accesskey);
		otc.sign = createSign(p);
		return otc;
	}

	/**
	 * 生成签名
	 * 
	 * @param param
	 *            参数
	 * @return
	 */
	private String createSign(LinkedHashMap<String, String> param) {
		String sign = "";
		if (param != null && param.size() > 0) {
			for (Entry<String, String> entry : param.entrySet()) {
				String name = entry.getKey() == null ? "" : entry.getKey();
				String value = entry.getValue() == null ? "" : entry.getValue();
				sign += name + "=" + value + "&";
			}
		}
		sign = sign.substring(0, sign.length() - 1);
		return Encryption.Encode(sign, Encryption.MD5).toUpperCase();
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

class User_wy {
	public String uid;
}

class OrderToClient {
	public String appid;// 游戏id
	public int fee;// 单位人民币分 1 代表1分钱
	public String productname;// 购买的商品名称,
	public String reqid;// 唯一id字符，要求每次支付都不一样，否则将视为重复订单，建议使用
	public String uid;// 通过认证后返回的用户 id
	public String url;// 支付成功后的回调地址
	public String attach;// 附加参数，用于透传
	public String sign;// 签名字段
}
