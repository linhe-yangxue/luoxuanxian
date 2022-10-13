package com.jksdk.g9;

import java.util.Map;

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

@Service("Sdki9gImpl")
@Scope("prototype")
public class Sdk9GImpl implements I_Platform {
	private static final Logger log = LoggerFactory.getLogger(Sdk9GImpl.class);

	public static final String URL_USERINFO = "https://wx.9g.com/open/userinfo?";
	private static final String SPID = "yqdq";
	private static final String SP_KEY = "95yQTpVdqdeikABUXTpIXMKKNp9nqqat";

	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		String pid = (String) param.get("pid");
		G9Parm recparam = new G9Parm();
		recparam.setGameid((String) param.get("gameid"));
		if (param.get("channel") != null) {
			recparam.setChannel((String) param.get("channel"));
		} else {
			recparam.setChannel("9g");
		}
		recparam.setToken((String) param.get("token"));
		String reqresult = HttpRequest.GetFunction(URL_USERINFO + "token=" + recparam.getToken() + "&gameid=" + recparam.getGameid());
		User9g user9g = JsonTransfer._In(reqresult, User9g.class);
		if (user9g == null) {
			log.error(this.getClass() + "用户信息 获取失败！---" + reqresult);
			return null;
		}

		UserBase ubase = new UserBase();
		ubase.pid = pid;
//		if (user9g.getHeadimgurl() != null) {
//			String head = HttpRequest.encodeImgageToBase64(new URL(user9g.getHeadimgurl()));
//			if (head != null) {
//				ubase.setNickname(user9g.getNickname());
//				ubase.setuImg(head);
//			}
//		}

		dSource.rtn.getInfo().setAccount(user9g.getUid());
		dSource.rtn.getInfo().setPid(pid);
		dSource.rtn.setUsebase(ubase);
		// url附加参数
		param.put("extend", JsonTransfer.getJson(recparam));
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
		String orderid = (String) param.get("orderid");
		Integer status = Integer.parseInt((String) param.get("status")); //
		String money = (String) param.get("money");
		String sign = (String) param.get("sign");

		String test_sign = Encryption.Encode(orderid + money + status + SP_KEY, Encryption.MD5);
		if (test_sign.equals(sign)) {
			param.put("goodsOrder", orderid);
			param.put("Amount", money);
			param.put("ownOrder", orderid);
			param.put("status", status);
			param.put("success", "OK");
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
		otc.orderid = String.valueOf(dSource.order.get("ownOrder"));
		otc.spid = SPID;
		otc.money = ((Float) dSource.order.get("amount")).intValue();
		otc.product = "Diamond" + dSource.order.get("goodsNum").toString();
		otc.sign = Encryption.Encode(otc.orderid + otc.money + otc.spid + SP_KEY, Encryption.MD5);
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

class G9Parm {
	private String gameid;
	private String channel;
	private String token;

	public String getGameid() {
		return gameid;
	}

	public void setGameid(String gameid) {
		this.gameid = gameid;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}

class OrderToClient {
	public String orderid; // 商户订单号，string，必要
	public Integer money; // 充值金额（单位元，整数）
	public String product; // 充值商品名称（例：300元宝）
	public String spid; // 商户ID
	public String sign; // 验证签名
	public String attach; // 附加参数，在支付回调原样返回，string，可选
}

class User9g {
	private String uid;
	private String nickname;
	private Integer sex;
	private String headimgurl;
	/** 0:否; 1:是 */
	private Integer hasphone;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public Integer getHasphone() {
		return hasphone;
	}

	public void setHasphone(Integer hasphone) {
		this.hasphone = hasphone;
	}
}
