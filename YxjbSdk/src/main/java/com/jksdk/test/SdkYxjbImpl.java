package com.jksdk.test;

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

/*
 * https://dev.99kgames.com/gameApi/index/partnerid/ff2df2841b435ac584a20ade4b6e3c50
 */

@Service("SdkyxjbImpl")
@Scope("prototype")
public class SdkYxjbImpl implements I_Platform {
	private static final Logger log = LoggerFactory.getLogger(SdkYxjbImpl.class);

	private static final String INFO_URL = "http://api.99kgames.com/vendor/api/userinfo";
	// key 是存在后台登陆key
	// partnerid 是存在后台支付key

	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		// log.info("log {}", JsonTransfer.getJson(param));
		String access_token = (String) param.get("access_token");
		if (access_token == null) {
			return null;
		}

		StringBuffer sb_for_sign = new StringBuffer();
		sb_for_sign.append("access_token=").append(access_token).append("&partnerid=").append(dSource.doc.getPayKey()).append(dSource.doc.getLoginKey());
		String sign = Encryption.Encode(sb_for_sign.toString(), Encryption.SHA1);

		StringBuffer sb_for_info = new StringBuffer();
		sb_for_info.append(INFO_URL).append("?partnerid=").append(dSource.doc.getPayKey()).append("&access_token=").append(access_token).append("&sign=").append(sign);
		// log.info("query info {}", sb_for_info.toString());
		String result = HttpRequest.GetFunction(sb_for_info.toString());
		// log.info("result {}", result);
		UserPack result_obj = JsonTransfer._In(result, UserPack.class);
		if (result_obj.code == null) {
			log.error("result error {}", result);
			return null;
		}
		if (result_obj.code != 0) {
			log.error("result error {}", result);
			return null;
		}

		UserBase ubase = new UserBase();
		ubase.pid = (String) param.get("pid");
		// ubase.setNickname(result_obj.nickname);
		// String imgurl = StringUtils.replace(result_obj.avatar, "/0", "/64");
		// ubase.setuImg(imgurl);
		dSource.rtn.getInfo().setAccount(result_obj.userid.toString());
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
		// log.info("call pay {}", JsonTransfer.getJson(param));
		String trade_status = param.get("trade_status").toString();
		if (trade_status == null) {
			log.info("pay error {}", JsonTransfer.getJson(param));
			param.put("fail", "FAIL");
			return;
		}

		StringBuffer sb = new StringBuffer();
		sb.append("attach=").append((String) param.get("attach")).append("&game=").append((String) param.get("game")).append("&out_trade_no=").append((String) param.get("out_trade_no")).append("&partnerid=").append((String) param.get("partnerid")).append("&pay_time=")
				.append((String) param.get("pay_time")).append("&product_id=").append((String) param.get("product_id")).append("&timestamp=").append((String) param.get("timestamp")).append("&total_fee=").append((String) param.get("total_fee")).append("&trade_status=").append(trade_status)
				.append("&transaction_id=").append((String) param.get("transaction_id")).append("&userid=").append((String) param.get("userid")).append(dSource.doc.getLoginKey());
		String sign = (String) param.get("sign");
		String test = Encryption.Encode(sb.toString(), Encryption.SHA1);
		if (!sign.equals(test)) {
			log.info("call error calc {} sign {}", test, sign);
			param.put("fail", "FAIL");
			return;
		}
		param.put("goodsOrder", param.get("out_trade_no").toString());
		param.put("ownOrder", param.get("out_trade_no").toString());
		param.put("Amount", (String) param.get("total_fee"));
		param.put("status", 1);
		param.put("success", "success");
		// log.info("success");
	}

	@Override
	public Object payVerification(DataConf dSource) throws Exception {
		log.info("payVerification {}", JsonTransfer.getJson(dSource));
		// String extend = (String) dSource.params.get("extend");
		// String user_id = extend.substring(0, extend.indexOf("|"));
		// String game_id = extend.substring(extend.indexOf("|") + 1);
		String out_trade_no = (String) dSource.order.get("ownOrder"); // 我们自己的订单号，用来给支付回调的时候做比对验证)
		if (out_trade_no == null) {
			log.info("order error {}", JsonTransfer.getJson(dSource));
			return null;
		}
		OrderToClient otc = new OrderToClient();
		otc.out_trade_no = out_trade_no;
		otc.product_id = ((Integer) dSource.order.get("shopId")).toString();
		Integer mi = ((Float) dSource.order.get("amount")).intValue();
		otc.total_fee = mi.toString();
		otc.body = otc.product_id;
		StringBuffer sb = new StringBuffer();
		sb.append("attach=").append(otc.attach).append("&body=").append(otc.body).append("&detail=").append(otc.detail).append("&out_trade_no=").append(otc.out_trade_no).append("&product_id=").append(otc.product_id).append("&total_fee=").append(otc.total_fee).append(dSource.doc.getLoginKey());
		// log.info("test str {}");
		otc.sign = Encryption.Encode(sb.toString(), Encryption.SHA1);

		// log.info("Order create {}", JsonTransfer.getJson(otc));
		return otc;
	}

	class OrderToClient {
		public String out_trade_no;// : '厂商订单编号',
		public String product_id;// : '商品id',
		public String total_fee;// : '支付总金额，以分为单位，必须大于0',
		public String body;// : '订单或商品的名称',
		public String detail = "1";// : '订单或商品的详情',
		public String attach = "1";// : '附加数据，后台通知时原样返回',
		public String sign;// : '请求参数签名'
	}

	class UserPack {
		public Integer code;// : 请求状态，0表示请求成功。
		public String message;// : 接口错误信息。请求成功时为OK
		public Long userid;// : 用户唯一标识
		public String nickname;// : 用户昵称
		public String avatar;// : 用户头像地址
		public Integer sex;// : 用户的性别。0:未知，1:男性，2:女性
		public String province;// : 用户所在的省份
		public String city;// :用户所在的城市
		public Integer invitor;// : 邀请者。没有邀请者为0，有邀请者，为邀请者的userid
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
