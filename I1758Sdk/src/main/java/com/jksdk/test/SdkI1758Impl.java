package com.jksdk.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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
 * http://doc.i1758.cn/guide/sign.html
 * 第一步：对参数按照key=value的格式，并按照参数名ASCII字典序排序如下 stringA = "appKey=1&gid=2&nonce=4&timestamp=3"
 * 第二步：拼接API密钥并取md5值,密钥从平台方获取：stringSignTemp= stringA + "55555"
 * 第3步：Sign = MD5(stringSignTemp)
 * 代码见下方
 */

@Service("Sdki1758Impl")
@Scope("prototype")
public class SdkI1758Impl implements I_Platform {
	private static final Logger log = LoggerFactory.getLogger(SdkI1758Impl.class);
	// private static final String APP_SECRET = "5555"; //TODO
	private static final String LOGING_VERTIFY_URL = "http://api.1758.com/auth/v4.1/verifyUser.json";
	private static final String ORDER_URL = "http://api.1758.com/pay/v4.1/unifiedorder.json";

	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		if (param.get("appKey") == null) {
			log.error("1758 no app key");
			return null;
		}
		Map<String, String> temp_param = new HashMap<String, String>(param.size());
		temp_param.put("appKey", param.get("appKey").toString());
		temp_param.put("userToken", param.get("userToken").toString());
		temp_param.put("state", "");
		temp_param.put("hlmy_gw", param.get("hlmy_gw").toString());
		temp_param.put("hlmy_gp", param.get("hlmy_gp").toString());
		temp_param.put("nonce", param.get("nonce").toString());
		temp_param.put("timestamp", param.get("timestamp").toString());
		String token_sign = param.get("sign").toString();
		String mine_sign = Util.getSignData(temp_param, dSource.doc.getLoginKey());
		if (!mine_sign.equals(token_sign)) {
			log.error("1758 sdk error token_sign not equals mine {} theirs {}", mine_sign, token_sign);
			return null;
		}

		// 第二步，CP向平台发验证token的请求
		/*
		 * 请求参数 把第一步的 appKey hlmy_gw userToken nonce 加上当前timestamp 同样方法计算出 sign
		 */
		Map<String, String> my_param = new LinkedHashMap<String, String>(param.size());
		my_param.put("appKey", temp_param.get("appKey"));
		my_param.put("hlmy_gw", temp_param.get("hlmy_gw"));
		my_param.put("userToken", temp_param.get("userToken"));
		my_param.put("nonce", temp_param.get("nonce"));
		my_param.put("timestamp", Long.toString(Calendar.getInstance().getTimeInMillis()));
		my_param.put("sign", Util.getSignData(my_param, dSource.doc.getLoginKey()));
		String vertify_result = HttpRequest.PostFunction(LOGING_VERTIFY_URL, my_param);
		VertifyResult v_res = JsonTransfer._In(vertify_result, VertifyResult.class);
		if (v_res.result == 0) {
			log.error("1758 sdk log vertify error ERROR CODE {} msg {}", v_res.errorcode, v_res.message);
			return null;
		}

		UserBase ubase = new UserBase();
		ubase.pid = (String) param.get("pid");
//		ubase.setNickname(v_res.data.userInfo.nickName);
//		String imgurl = StringUtils.replace(v_res.data.userInfo.avatar, "/0", "/64");
//		ubase.setuImg(imgurl);
		dSource.rtn.getInfo().setAccount(v_res.data.userInfo.gid);
		dSource.rtn.getInfo().setPid((String) param.get("pid"));
		dSource.rtn.setUsebase(ubase);
		
		StringBuffer sb = new StringBuffer();
		sb.append(temp_param.get("hlmy_gw")).append("|").append(temp_param.get("appKey")).append("|").append(v_res.data.userInfo.gid);
		param.put("extend", sb.toString());

		// log.info("1758===LOGIN FINISH======" + JsonTransfer.getJson(param));

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
		Map<String, String> temp_param = new HashMap<String, String>(param.size());
		if (param.get("appKey") == null) {
			log.info("1758 pay call back error json {}", JsonTransfer.getJson(param));
			param.put("fail", -4);
			return;
		}
		temp_param.put("appKey", param.get("appKey").toString());
		temp_param.put("gid", param.get("g_id").toString()); ///// 因gid参数与自有参数重名，从外部过来的gid改为用g_id访问
		temp_param.put("orderId", param.get("orderId").toString());
		temp_param.put("itemCode", param.get("itemCode").toString());
		temp_param.put("buyAmount", param.get("buyAmount").toString());
		temp_param.put("status", param.get("status").toString());
		String temp_sign = Util.getSignData(temp_param, dSource.doc.getLoginKey());
		String send_sign = param.get("sign").toString();
		if (temp_sign.equals(send_sign)) {

			String order_json = param.get("orderInfo").toString();
			OrderInfo1758 order_info = JsonTransfer._In(order_json, OrderInfo1758.class);

			param.put("goodsOrder", temp_param.get("orderId"));
			param.put("account", temp_param.get("gid"));
			param.put("Amount", order_info.totalFee.toString());
			param.put("ownOrder", order_info.txId.toString());
			param.put("status", 1);
			param.put("success", "success"); // CP收到支付通知后，返回 success 字符串
		} else {
			log.info("1758 pay call back error json {}", JsonTransfer.getJson(param));
			param.put("fail", -4);// -2 账号不存在 //-4验证码错误 //-5订单号重复
		}
	}

	@Override
	public Object payVerification(DataConf dSource) throws Exception {
		/*
		 * 第一步，向平台下订单 appKey 游戏id gid 1758用户的唯一id hlmy_gw 1758的自定义参数 itemCode
		 * 道具编号 money 价格，单位元 txId cp订单号 , 可为空 state cp订单自定义参数，可为空 nonce
		 * 随机串，不长于32位 timestamp 当前时间戳 sign 参数签名
		 */
		// log.info("1758===ORDER FROM CLIENT======" +
		// JsonTransfer.getJson(dSource));
		Map<String, String> my_param = new LinkedHashMap<String, String>(16);
		my_param.put("appKey", dSource.doc.getPayKey()); // 用我们后台里的配置来读取
		my_param.put("gid", (String) dSource.order.get("account"));
		my_param.put("hlmy_gw", (String) dSource.params.get("hlmy_gw"));// 从登陆时平台发来的消息里看到的
		int item_id = (Integer) dSource.order.get("shopId");
		my_param.put("itemCode", PlatformItemIdMap.MyToPlat.get(item_id).toString());
		Integer money = ((Float) dSource.order.get("amount")).intValue();
		// if(dSource.doc.getRate()>0){ //金额乘以倍率
		// money *= dSource.doc.getRate();
		// }else{
		// money /= (Math.abs(dSource.doc.getRate()));
		// }
		my_param.put("money", money.toString());
		my_param.put("txId", (String) dSource.order.get("ownOrder")); // 我们自己的订单号，用来给支付回调的时候做比对验证)
		my_param.put("state", "");
		my_param.put("nonce", Long.toString(Calendar.getInstance().getTimeInMillis()));
		my_param.put("timestamp", Long.toString(Calendar.getInstance().getTimeInMillis()));
		my_param.put("sign", Util.getSignData(my_param, dSource.doc.getLoginKey()));
		// log.info("1758====MY TO PAY=====" + JsonTransfer.getJson(my_param));
		String vertify_result = HttpRequest.PostFunction(ORDER_URL, my_param);
		VertifyResult v_res = JsonTransfer._In(vertify_result, VertifyResult.class);
		if (v_res.result == 0) {
			log.info("1758 sdk pay order vertify error ERROR CODE {} msg {} ======== dsource json {}", v_res.errorcode, v_res.message, JsonTransfer.getJson(dSource));
			return null;
		}

		/*
		 * 第二步，下发安全码给客户端
		 */
		Data to_client = new Data();
		to_client.paySafecode = v_res.data.paySafecode;
		// log.info("1758===SAFE TO CLIENT======" +
		// JsonTransfer.getJson(to_client));
		return to_client;
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

class Util {
	// 平台文档给的代码 http://doc.i1758.cn/guide/sign.html
	public static String getSignData(Map<String, String> params, String cp_key) {
		// params为要参与签名的参数键值对.
		StringBuffer content = new StringBuffer();

		// 按照key做排序
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			if ("sign".equals(key) || "sign_type".equals(key)) {
				continue;
			}
			String value = (String) params.get(key);
			if (value != null) {
				content.append((i == 0 ? "" : "&") + key + "=" + value);
			} else {
				content.append((i == 0 ? "" : "&") + key + "=");
			}

		}
		content.append(cp_key);// 注意: 这里的CP_SECRETKEY 为分配给cp的appSecret

		return Encryption.Encode(content.toString(), Encryption.MD5);
	}
}

class VertifyResult {
	public int result; // 响应结果，1成功、0失败
	public int errorcode; // 错误码
	public String message; // 错误信息
	public Data data; // 业务数据
}

class Data {
	public UserInfo1758 userInfo; // 用户资料对象

	public String appKey;
	public String paySafecode; // 订单响应安全码，下发给客户端去支付
}

class UserInfo1758 {
	public String gid; // 用户唯一识别id
	public String avatar; // 头像url
	public String nickName; // 昵称
	public int sex; // 性别，1男、2女、0未知
}

class PlatformItemIdMap {
	public static final Map<Integer, Integer> MyToPlat = new HashMap<Integer, Integer>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		{
			put(1, 108786); // 月卡
			put(2, 108787); // 终身卡
			put(10, 108788); // 成长基金
			put(21, 108789); // 充值1元
			put(11, 108790); // 充值10元
			put(12, 108791); // 充值30元
			put(13, 108792); // 充值60元
			put(14, 108793); // 充值100元
			put(15, 108794);// 充值200元
			put(16, 108795); // 充值500元
			put(17, 108796); // 充值1000元
			put(18, 108797); // 充值2000元
			put(19, 108798); // 限时礼包18
			put(20, 108799); // 限时礼包88
		}
	};
}

class OrderInfo1758 {
	public String gid; // 1758的用户id
	public String orderId; // 1758订单号
	public String txId; // CP订单号
	public String title; // 道具名称
	public Float productFee; // 商品金额，单位元
	public String itemCode; // 道具itemCode
	public Float itemFee; // 道具单价，单位元
	public Integer buyAmount; // 购买数量，单位个
	public Float transportFee; // 运费，单位元
	public Float discountFee; // 折扣，单位元
	public Float totalFee; // 累计支付金额，单位元
	public Integer status; // 订单状态
	public String state; // cp自定义参数
	public String createTime; // 订单创建时间
}
