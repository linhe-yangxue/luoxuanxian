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
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmShare.entity.UserBase;
import com.ssmShare.facde.I_Platform;
import com.ssmShare.platform.DataConf;

@Service("SdkjywImpl")
@Scope("prototype")
public class SdkJywImpl implements I_Platform {
	private static final Logger log = LoggerFactory.getLogger(SdkJywImpl.class);
	private static final String PAY_URL = "http://www.jywgame.com/media.php/Game/game_pay/?";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ssmShare.facde.I_Platform#logVerification(java.util.Map,
	 * com.ssmShare.platform.DataConf) user_id 用户唯一ID game_appid
	 * 游戏编号----运营方为游戏分配的唯一编号 存在 dock的 getPayKey email 运营方账号 //这玩意儿没有，不要信文档
	 * new_time 当前时间戳 channelExt 透传信息，在支付跳转时原样返回 sign
	 * 签名----将以上参数加key（key由运营方提供）存在 dock的 getLoginKey
	 * 
	 * sign
	 * =MD5(user_id=user_id&game_appid=game_appid&email=email&new_time=new_time&
	 * channelExt=channelExt&key=key)
	 */
	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		// log.error(JsonTransfer.getJson(param));
		if (param.get("user_id") == null) {
			log.error("no user_id");
			return null;
		}
		Map<String, String> temp_param = new LinkedHashMap<String, String>();
		temp_param.put("user_id", param.get("user_id").toString());
		temp_param.put("game_appid", dSource.doc.getPayKey()); // 存在 dock的
																// getPayKey
		temp_param.put("email", ""); // 这玩意儿没有，不要信文档
		temp_param.put("new_time", param.get("new_time").toString());
		temp_param.put("channelExt", param.get("channelExt").toString());
		temp_param.put("key", dSource.doc.getLoginKey());
		String md5 = getMd5(temp_param);
		if (!md5.equals(param.get("sign").toString())) {
			log.error("log md5 test fail, calc {} sign {} all_msg {}", md5, param.get("sign").toString(), JsonTransfer.getJson(param));
			return null;
		}

		UserBase ubase = new UserBase();
		ubase.pid = (String) param.get("pid");

		// ubase.setNickname((String)param.get("img_nickname"));
		// String imgurl = StringUtils.replace((String) param.get("img_pic"),
		// "/0", "/64");
		// ubase.setuImg(imgurl);
		dSource.rtn.getInfo().setAccount(temp_param.get("user_id"));
		dSource.rtn.getInfo().setPid((String) param.get("pid"));
		dSource.rtn.setUsebase(ubase);

		StringBuffer sb = new StringBuffer();
		sb.append(temp_param.get("channelExt")).append("|").append(temp_param.get("user_id")); // 透传，需要将来用
		param.put("extend", sb.toString());
		// log.error("loging success {}", temp_param.get("user_id"));
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
	 * com.ssmShare.platform.DataConf) source 数据来源 trade_no 订单编号 out_trade_no
	 * 联运平台订单号 amount 金额，单位为分 game_appid 游戏编号----运营方为游戏分配的唯一编号 sign
	 * 签名----将以上参数加key(同上) sign =
	 * MD5(source=vlcms&trade_no=1903586205&out_trade_no=out_trade_no&amount=
	 * 1000&game_appid=100001&key=key)
	 */
	@Override
	public void callPay(Map<String, Object> param, DataConf dSource) throws Exception {
		if (param.get("source") == null) {
			log.error("no source");
			param.put("fail", -8);
			return;
		}
		Map<String, String> temp_param = new LinkedHashMap<String, String>();
		temp_param.put("source", param.get("source").toString());
		temp_param.put("trade_no", param.get("trade_no").toString());
		temp_param.put("out_trade_no", param.get("out_trade_no").toString());
		temp_param.put("amount", param.get("amount").toString());
		temp_param.put("game_appid", dSource.doc.getPayKey());
		temp_param.put("key", dSource.doc.getLoginKey());
		String md5 = getMd5(temp_param);
		if (!md5.equals(param.get("sign").toString())) {
			log.info("pay md5 test fail, calc {} sign {} all_msg {}", md5, param.get("sign").toString(), JsonTransfer.getJson(param));
			param.put("fail", -8);// -2 账号不存在 //-4验证码错误 //-5订单号重复
		} else {
			param.put("goodsOrder", temp_param.get("out_trade_no"));
			param.put("Amount", temp_param.get("amount"));
			param.put("ownOrder", temp_param.get("trade_no"));
			param.put("status", 1);
			Status s = new Status();
			s.status = "success";
			param.put("success", s); // 1成功
		}
	}

	class Status {
		public String status;
	}

	/*
	 * game_appid 游戏编号----运营方为游戏分配的唯一编号 trade_no 订单编号（CP方订单号）没有则为空 props_name
	 * 道具名称 amount 金额，单位为分，没有金额传0 user_id 运营方用户ID channelExt 原样返回登陆时透传的信息 sign
	 * 签名----将以上参数加key(同上)后得到的签名
	 * sign=MD5(game_appid=game_appid&trade_no=trade_no&props_name=props_name&
	 * amount=amount&user_id=user_id&channelExt=channelExt&key=key) 完整的HTTP
	 * GET请求示例（参数请按照正确的填写，这个只是实例）：
	 * http://pay_url/media.php/Game/game_pay/?game_appid=200160300&trade_no=&
	 * props_name=&amount=0&user_id=74624&channelExt=channelExt&sign=
	 * 3fc8808c08ccc755f87bbb7da9effeab
	 */
	@Override
	public Object payVerification(DataConf dSource) throws Exception {
		// log.error("payit {}", JsonTransfer.getJson(dSource));
		String extend = (String) dSource.params.get("extend");
		String user_id = extend.substring(extend.indexOf("|") + 1);
		String channelExt = extend.substring(0, extend.indexOf("|"));
		// log.error("{} {} {}", extend, user_id, channelExt);
		Map<String, String> temp_param = new LinkedHashMap<String, String>();
		temp_param.put("game_appid", dSource.doc.getPayKey());
		temp_param.put("trade_no", (String) dSource.order.get("ownOrder")); // 我们自己的订单号，用来给支付回调的时候做比对验证
		temp_param.put("props_name", ((Integer) dSource.order.get("shopId")).toString());
		Integer mi = ((Float) dSource.order.get("amount")).intValue();
		temp_param.put("amount", mi.toString()); // 单位分
		temp_param.put("user_id", user_id);
		temp_param.put("channelExt", channelExt);
		temp_param.put("key", dSource.doc.getLoginKey());
		String sign = getMd5(temp_param);
		OrderToClient otc = new OrderToClient();
		otc.amount = temp_param.get("amount");
		otc.channelExt = temp_param.get("channelExt");
		otc.game_appid = temp_param.get("game_appid");
		otc.props_name = temp_param.get("props_name");
		otc.sign = sign;
		otc.trade_no = temp_param.get("trade_no");
		otc.user_id = temp_param.get("user_id");
		otc.pay_url = PAY_URL;
		// log.error("Create order, to client {}", JsonTransfer.getJson(otc));
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
		return result;
	}

	// 客户端拿到这个对象去向平台付钱
	class OrderToClient {
		public String game_appid;
		public String trade_no;
		public String props_name;
		public String amount;
		public String user_id;
		public String channelExt;
		public String sign;
		public String pay_url; // 客户端付款url 直接拼接好发下去
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
