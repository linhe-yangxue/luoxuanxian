package com.jksdk.i360;

import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.constants.ReInfo;
import com.ssmCore.tool.colligate.Encryption;
import com.ssmShare.entity.UserBase;
import com.ssmShare.facde.I_Platform;
import com.ssmShare.platform.DataConf;

@Service("Sdk360Impl")
@Scope("prototype")
public class Sdk360Impl implements I_Platform {
	private static final Logger log = LoggerFactory.getLogger(Sdk360Impl.class);
	public static final String URL_USERINFO = "http://www.3500.com/Wap/Api/getUser.html?";
	private static final String game_key = "76005c85768a18521468dc47b420fa1e";
	private static final String game_secret = "cf56266d4670ce3c48cbebe1326dd6df";

	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		String pid = (String) param.get("pid");

		String t = (String) param.get("t");
		String nonce = (String) param.get("nonce");
		String plat_user_id = (String) param.get("plat_user_id");
		String nickname = (String) param.get("nickname");
		String avatar = (String) param.get("avatar");
		String is_tourist = (String) param.get("is_tourist");
		String sign = (String) param.get("sign");

		SortedMap<String, String> p = new TreeMap<>();
		p.put("t", t);
		p.put("nonce", nonce);
		p.put("plat_user_id", plat_user_id);
		p.put("nickname", nickname);
		p.put("avatar", avatar);
		p.put("is_tourist", is_tourist);
		String tempSign = createSign(p);

		if (!tempSign.equals(sign)) {
			log.error(this.getClass() + "登陆sign验证失败！---" + p.toString());
			return null;
		}

		UserBase ubase = new UserBase();
		ubase.pid = pid;
//		ubase.setNickname(nickname);
//		ubase.setuImg(avatar);
		dSource.rtn.getInfo().setAccount(plat_user_id);
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
		String game_key = (String) param.get("game_key");
		String plat_user_id = (String) param.get("plat_user_id");
		String order_id = (String) param.get("order_id");
		String amount = (String) param.get("amount");
		String plat_order_id = (String) param.get("plat_order_id"); //
		String sign = (String) param.get("sign");

		SortedMap<String, String> p = new TreeMap<>();
		p.put("game_key", game_key);
		p.put("plat_user_id", plat_user_id);
		p.put("order_id", order_id);
		p.put("amount", amount);
		p.put("plat_order_id", plat_order_id);

		String test_sign = createSign(p);
		if (test_sign.equals(sign)) {
			param.put("goodsOrder", order_id);
			param.put("Amount", String.valueOf(amount));
			param.put("ownOrder", order_id);
			param.put("status", 1);
			param.put("success", "ok");
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
		otc.game_key = game_key;
		otc.game_name = "一骑当千";
		otc.plat_user_id = String.valueOf(dSource.order.get("account"));
		otc.amount = ((Float) dSource.order.get("amount")).intValue();
		otc.product_name = "Diamond" + String.valueOf(dSource.order.get("goodsNum"));
		otc.notify_url = "http://game.wannaplay.cn/" + dSource.doc.getPid() + "/" + dSource.gid + "/pay";
		otc.order_id = String.valueOf(dSource.order.get("ownOrder"));
		otc.timestamp = String.valueOf((int) (System.currentTimeMillis() / 1000));

		SortedMap<String, String> param = new TreeMap<>();
		param.put("game_key", otc.game_key);
		param.put("game_name", otc.game_name);
		param.put("plat_user_id", otc.plat_user_id);
		param.put("amount", String.valueOf(otc.amount));
		param.put("product_name", otc.product_name);
		param.put("notify_url", otc.notify_url);
		param.put("order_id", otc.order_id);
		param.put("timestamp", otc.timestamp);

		otc.sign = createSign(param);
		return otc;
	}

	/**
	 * 生成签名
	 * 
	 * @param param
	 *            参数
	 * @return
	 */
	private String createSign(SortedMap<String, String> param) {
		String sign = "";
		if (param != null && param.size() > 0) {
			for (Entry<String, String> entry : param.entrySet()) {
				String name = entry.getKey() == null ? "" : entry.getKey();
				String value = entry.getValue() == null ? "" : entry.getValue();
				sign += name + value;
			}
		}
		sign += game_secret;
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

class OrderToClient {
	public String game_key;// 应用key 必须
	public String game_name;// 游戏名称 必须
	public String plat_user_id;// 平台颁发给CP的用户账号 必须
	public int amount;// 金额单元为元 必须
	public String product_name;// 商品名称 必须
	public String product_desc;// 商品描述描述 可选
	public String notify_url;// 回调通知cp服务端后端回调地址 必须
	public String order_id;// cp服务端订单id,不允许重复使用 必须
	public String sign;// 按照签名算法计算出来的签名 必须
	public String timestamp;// 时间戳,超过30分钟的订单无法支付 必须
	public String ext;// 游戏方尝试回传的参数,如果不为空将在支付成功后原样回传 可选

}
