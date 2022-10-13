package com.jksdk.laya;

import java.util.HashMap;
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

@Service("SdklayaImpl")
@Scope("prototype")
public class SdkLayaImpl implements I_Platform {
	private static final Logger log = LoggerFactory.getLogger(SdkLayaImpl.class);

	private static final int openId = 10987;
	private static final String openKey = "e45c4293903752d70f0b0c693438fab2";
	private static final String secretKey = "82BF5DD700583EACCC50DD698CB5F222";
	private static final String checkLogin_url = "http://ucenter.layabox.com/api/safetyVerify";

	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		String pid = String.valueOf(param.get("pid"));
		String login_s = String.valueOf(param.get("extend"));
		LoginInfo loginInfo = JsonTransfer._In(login_s, LoginInfo.class);
		Map<String, String> lp = new HashMap<>();
		lp.put("sp", loginInfo.spId);
		lp.put("openId", openKey);
		lp.put("time", String.valueOf(System.currentTimeMillis()));
		lp.put("userId", loginInfo.data.unionUserId);
		lp.put("access_token", loginInfo.data.accessToken);
		lp.put("key", Encryption.Encode(loginInfo.data.accessToken + openKey + lp.get("time"), Encryption.MD5).toUpperCase());
		String result = HttpRequest.PostFunction(checkLogin_url, lp);
		LoginCheck loginCheck = JsonTransfer._In(result, LoginCheck.class);
		if (loginCheck == null || loginCheck.ret != 0) {
			log.error(this.getClass() + "登陆sign计算错误！---" + result);
			return null;
		}

		UserBase ubase = new UserBase();
		ubase.pid = pid;
//		if (loginInfo.data != null && loginInfo.data.avatarUrl != null) {
//			ubase.setNickname(loginInfo.data.nickName);
//			ubase.setuImg(HttpRequest.encodeImgageToBase64(new URL(loginInfo.data.avatarUrl)));
//		}

		dSource.rtn.getInfo().setAccount(loginInfo.data.unionUserId);
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
		dSource.params.put("openId", openId);
		dSource.params.put("openKey", openKey);
		return null;
	}

	@Override
	public void callPay(Map<String, Object> param, DataConf dSource) throws Exception {
		String userId = String.valueOf(param.get("userId"));
		String orderId = String.valueOf(param.get("orderId"));
		String amount = String.valueOf(param.get("amount"));
		String payInfo = String.valueOf(param.get("payInfo"));
		String params = String.valueOf(param.get("params"));
		String payTime = String.valueOf(param.get("payTime"));
		String sign = String.valueOf(param.get("sign"));
		String tempSign = Encryption.Encode(userId + orderId + amount + payInfo + params + payTime + secretKey, Encryption.MD5);
		if (tempSign.equals(sign)) {
			param.put("goodsOrder", orderId);
			param.put("Amount", amount);
			param.put("ownOrder", params);
			param.put("status", 1);
			param.put("success", "{\"ret\":1,\"msg\":\"成功\"}");
		} else {
			param.put("fail", this.getClass() + "充值回调sign验证失败" + param.toString());
		}
	}

	@Override
	public Object payVerification(DataConf dSource) throws Exception {
		OrderToClient order = new OrderToClient();
		order.order_id = String.valueOf(dSource.order.get("ownOrder"));
		order.goods_name = "Diamond" + String.valueOf(dSource.order.get("goodsNum"));
		order.amount = ((Float) dSource.order.get("amount")).intValue();
		order.serverId = Integer.parseInt(String.valueOf(dSource.order.get("zid")));
		order.goods_desc = "Diamond";
		order.params = String.valueOf(dSource.order.get("ownOrder"));
		return order;
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
	public String order_id;// CP 方的订单号，如果没有 order_id 的话传空。
	public String goods_name;// 物品名称
	public int amount;// 人民币数量(单位分)
	public String goods_desc;// 物品描述
	public int serverId;// 服务区号12
	public String params;// 透传参数，market 在发货的时候会原样发回给游戏服务器
}

class LoginCheck {
	public int ret;
	public String msg;
}

class LoginInfo {
	public String msg; // 错误信息13
	public int result; // 用户登录的结果编号(0:成功 非 0: 失败),//1000的时候表示是腾讯登录，且没有缓存到数据，需要显示
						// QQ 或者微信的登录按钮
	public String desc;// 用户登录的结果描述
	public String loginType; // 登录类型，
	public String spId; // 渠道 spId，
	public String type; // 登录类型"qq"或者"wx
	public LoginUserInfo data;// 用户信息
}

class LoginUserInfo {
	public String nickName;// 用户在渠道方的昵称,
	public String accessToken;// 用户在 laya 登录的 token,
	public String unionUserId;// 用户在 laya 登录的用户 id,(CP 请使用此值进行帐号登录，否则登录校验将不通过)
	public String avatarUrl;// 用户在 laya 的头像地址,
	public String spFname;// 用户登录的渠道别名,
	public String sptoken;// 渠道原生 token,
	public String spuid;// 渠道原生用户 id
}
