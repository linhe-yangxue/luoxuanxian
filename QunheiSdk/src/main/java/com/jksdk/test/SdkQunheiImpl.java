package com.jksdk.test;

import java.util.Map;

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

@Service("SdkqhImpl")
@Scope("prototype")
public class SdkQunheiImpl implements I_Platform {
	private static final Logger log = LoggerFactory.getLogger(SdkQunheiImpl.class);

	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		String pid = String.valueOf(param.get("pid"));// 加密参数
		String userName = String.valueOf(param.get("username"));
		String serverid = String.valueOf(param.get("serverid"));
		String time = String.valueOf(param.get("time"));// 登录时间(UNIX时间戳)
		String isadult = String.valueOf(param.get("isadult"));// 防沉迷标识(1是成年，0是未成年)
		// String uimg = String.valueOf(param.get("uimg"));// 头像
		// String nname = String.valueOf(param.get("nname"));// 昵称

		String flag = String.valueOf(param.get("flag"));// 加密参数

		String test_sign = Encryption.Encode(userName + serverid + isadult + time + dSource.doc.getLoginKey(), Encryption.MD5);
		if (!test_sign.equals(flag)) {
			log.error(this.getClass() + "登陆sign计算异常！---请求参数" + param.toString());
			log.error(this.getClass() + "test_sign" + test_sign);
			param.put(I_constants.PTOKEN_ERRPR, "登陆异常！");
			return null;
		}

		UserBase ubase = new UserBase();
		// ubase.setNickname(nname);
		// ubase.setuImg(uimg);

		dSource.rtn.getInfo().setAccount(userName);
		dSource.rtn.getInfo().setPid(pid);
		dSource.rtn.setUsebase(ubase);
		ubase.pid = pid;

		// 玩家来源(邀请)
		if (param.get("unid") != null) {
			String unid = String.valueOf(param.get("unid"));
			ubase.setInvitID(unid);
		}

		// url附加参数
		param.put("extend", "{'username':" + userName + "}");
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
		if (param.get("serverid") == null) {
			log.info("pay call back error json {}", JsonTransfer.getJson(param));
			param.put("fail", -4);
			return;
		}

		Integer serverid = Integer.parseInt((String) param.get("serverid")); // 游戏服务器，如果游戏有内置服务器选择，该参数则为默认值1
		String orderno = (String) param.get("orderno");// 订单号,不允许重复
		String username = (String) param.get("username"); // 充值账号
		Integer addgold = Integer.parseInt((String) param.get("addgold")); // 充值金币数量
		String rmb = (String) param.get("rmb"); // 充值人民币，单位元
		String paytime = (String) param.get("paytime"); // 订单时间戳
		String ext = (String) param.get("ext"); // 透传参数
		String sign = (String) param.get("sign"); // 验证签名，签名方式为：MD5(orderno+username
													// + serverid+addgold +
													// rmb+paytime+ext+key)这里的key是提交到后台的充值key,加号为连接符
		String test_sign = Encryption.Encode(orderno + username + serverid + addgold + rmb + paytime + ext + dSource.doc.getPayKey(), Encryption.MD5);
		// System.out.println("test sign " + test_sign);
		// System.out.println("their sign " + sign);
		if (test_sign.equals(sign)) {
			param.put("goodsOrder", orderno);
			param.put("account", username);
			param.put("Amount", rmb);
			param.put("ownOrder", ext);
			param.put("status", 1);
			param.put("success", 1); // 1成功
		} else {
			param.put("fail", this.getClass() + "充值回调sign验证失败" + param.toString());
		}
	}

	@Override
	public Object payVerification(DataConf dSource) throws Exception {
		OrderToClient otc = new OrderToClient();
		log.info(JsonTransfer.getJson(dSource.order));
		otc.money = ((Float) dSource.order.get("amount")).intValue();
		/*
		 * if(dSource.doc.getRate()>0){ //金额乘以倍率 otc.money *=
		 * dSource.doc.getRate(); }else{ otc.money /=
		 * (Math.abs(dSource.doc.getRate())); }
		 */
		otc.goodsName = "Diamond" + (Integer) dSource.order.get("goodsNum");
		otc.goodsId = ((Integer) dSource.order.get("shopId")).toString();
		otc.gid = "3441";
		otc.userId = (String) dSource.order.get("account");
		otc.ext = (String) dSource.order.get("ownOrder"); // 我们自己的订单号，用来给支付回调的时候做比对验证
		otc.roleName = (String) dSource.order.get("nickname");
		otc.sign = Encryption.Encode(otc.money.toString() + otc.userId + otc.ext + dSource.doc.getPayKey(), Encryption.MD5);
		// System.out.println("money " + otc.money.toString() + " userId " +
		// otc.userId + " ext " + otc.ext + " payKey " +
		// dSource.doc.getPayKey());
		// System.out.println("MD5 " + otc.sign);
		return otc;
	}

	class OrderToClient {
		public Integer money; // 充值金额（单位元，整数）
		public String goodsName; // 充值商品名称（例：300元宝）
		public String goodsId; // 充值商品id
		public String gid; // 群黑平台游戏id(群黑平台开放后台游戏列表查询)
		public String userId; // 充值账号(该参数是登录里面的username)
		public String ext; // 充值透传参数(主要用于透传玩家服务器，可以是订单号，服务器编号之类的标识，在充值回调接口里面会原样返回,如果该参数是唯一值，需要在后台游戏里面配置)
		public String roleName; // 充值角色名称
		public String sign; // 验证签名，签名方式为：MD5(money + userId + ext +
							// key)这里的key是提交到后台的充值key,加号为连接符
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
