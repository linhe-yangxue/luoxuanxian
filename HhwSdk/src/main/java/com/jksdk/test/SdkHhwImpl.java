package com.jksdk.test;

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

@Service("SdkhhwImpl")
@Scope("prototype")
public class SdkHhwImpl implements I_Platform {
	private static final Logger log = LoggerFactory.getLogger(SdkHhwImpl.class);

	private static final String PAY_URL = "http://m.hehewan.com/pay";

	/*
	 * uid 用户id 无符号整数 gid 游戏id time 当前Unix时间戳(北京时间) 可以作为超时校验 nickname 玩家昵称
	 * 不参与签名，可能为空 avatar 头像图片url 不参与签名，可能为空 sign 签名校验码 采用md5方式
	 * md5(uid=123&gid=1001&time=123456789&key=miyue)
	 */
	///////////////// ！！！！！！！！！！！！！！！规则： gid 同名 用 g_id代替
	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		if (param.get("uid") == null) {
			log.error("no uid");
			return null;
		}
		Map<String, String> temp_param = new LinkedHashMap<String, String>();
		temp_param.put("uid", param.get("uid").toString());
		temp_param.put("gid", param.get("g_id").toString()); ///////////////// ！！！！！！！！！！！！！！！规则：
																///////////////// gid
																///////////////// 同名
																///////////////// 用
																///////////////// g_id代替
		temp_param.put("time", param.get("time").toString());
		temp_param.put("key", dSource.doc.getLoginKey());
		String md5 = getMd5(temp_param);
		if (!md5.equals(param.get("sign").toString())) {
			log.info("log md5 test fail, calc {} sign {} all_msg {}", md5, param.get("sign").toString(), JsonTransfer.getJson(param));
			return null;
		}

		UserBase ubase = new UserBase();
		ubase.pid = (String) param.get("pid");

//		ubase.setNickname((String) param.get("nickname"));
//		String imgurl = StringUtils.replace((String) param.get("avatar"), "/0", "/64");
//		ubase.setuImg(HttpRequest.encodeImgageToBase64(new URL(imgurl)));

		dSource.rtn.getInfo().setAccount(temp_param.get("uid"));
		dSource.rtn.getInfo().setPid((String) param.get("pid"));
		dSource.rtn.setUsebase(ubase);

		// log.info("loging success {}", temp_param.get("uid"));
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
	 * com.ssmShare.platform.DataConf) uid 用户id 无符号整数 gid 游戏id oid 平台订单号 money
	 * 金额 单位是元 一毛钱这里要0.1，而不是0.10否则会验证不通过 千万不要传2,000.00，会被解析成2元，应该直接传整形。 custom
	 * 打开充值链接时传递给呵呵玩游戏的参数 time 当前Unix时间戳(北京时间) 可以作为超时校验 sign 签名校验码 采用md5方式
	 * 
	 * 充值成功:1 订单已存在:2 参数不全:-1 用户不存在:-2 游戏不存在:-3 该游戏未开放充值:-4 充值金额必须大于零:-5
	 * 时间戳必须是数字:-6 自定义字段长度超过50:-7 签名错误:-8
	 */
	@Override
	public void callPay(Map<String, Object> param, DataConf dSource) throws Exception {
		if (param.get("uid") == null) {
			log.error("no uid");
			param.put("fail", -8);
			return;
		}
		Map<String, String> temp_param = new LinkedHashMap<String, String>();
		temp_param.put("uid", param.get("uid").toString());
		temp_param.put("gid", param.get("g_id").toString()); ///////////////// ！！！！！！！！！！！！！！！规则：
																///////////////// gid
																///////////////// 同名
																///////////////// 用
																///////////////// g_id代替
		temp_param.put("oid", param.get("oid").toString());
		temp_param.put("money", param.get("money").toString());
		temp_param.put("custom", param.get("custom").toString());
		temp_param.put("time", param.get("time").toString());
		temp_param.put("key", dSource.doc.getLoginKey());
		String md5 = getMd5(temp_param);
		if (!md5.equals(param.get("sign").toString())) {
			log.info("pay md5 test fail, calc {} sign {} all_msg {}", md5, param.get("sign").toString(), JsonTransfer.getJson(param));
			param.put("fail", -8);// -2 账号不存在 //-4验证码错误 //-5订单号重复
		} else {
			param.put("goodsOrder", temp_param.get("oid"));
			param.put("account", temp_param.get("uid"));
			param.put("Amount", temp_param.get("money"));
			param.put("ownOrder", temp_param.get("custom"));
			param.put("status", 1);
			param.put("success", 1); // 1成功
		}
	}

	@Override
	public Object payVerification(DataConf dSource) throws Exception {
		OrderToClient otc = new OrderToClient();
		otc.uid = (String) dSource.order.get("account");
		otc.gid = dSource.doc.getPayKey(); // 后台里用paykey 支付校验 key 存gid
		otc.good = ((Integer) dSource.order.get("shopId")).toString();
		otc.money = ((Float) dSource.order.get("amount")).intValue();
		/*
		 * if(dSource.doc.getRate()>0){ //金额乘以倍率 otc.money *=
		 * dSource.doc.getRate(); }else{ otc.money /=
		 * (Math.abs(dSource.doc.getRate())); }
		 */
		otc.custom = (String) dSource.order.get("ownOrder"); // 我们自己的订单号，用来给支付回调的时候做比对验证
		otc.time = Calendar.getInstance().getTimeInMillis();

		Map<String, String> temp_param = new LinkedHashMap<String, String>();
		temp_param.put("uid", otc.uid);
		temp_param.put("gid", otc.gid);
		temp_param.put("good", otc.good);
		temp_param.put("money", otc.money.toString());
		temp_param.put("custom", otc.custom);
		temp_param.put("time", otc.time.toString());
		temp_param.put("key", dSource.doc.getLoginKey());
		otc.sign = getMd5(temp_param);
		otc.pay_url = PAY_URL;
		log.info("Create order, to client {}", JsonTransfer.getJson(otc));
		return otc;
	}

	// md5(uid=123&gid=1001&time=123456789&key=miyue)
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
	 * uid 用户id，请按照登录接口传入值回传 无符号整数 gid 游戏id good 商品名称或商品主题 money 订单金额 单位是元
	 * 一毛钱这里要0.10 custom 自定义字段，充值通知会原样回传 可以作为游戏订单号 最多150位字符串 time
	 * 当前Unix时间戳(北京时间) 可以作为超时校验 sign 签名校验码 采用md5方式
	 */
	// 客户端拿到这个对象去向平台付钱
	class OrderToClient {
		public String uid; // 用户id
		public String gid; // 游戏id
		public String good; // 商品id
		public Integer money; // 价格
		public String custom; // 自有订单号
		public Long time; // 时间戳
		public String sign; // 验证签名
		public String pay_url; // 客户端付款url
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
