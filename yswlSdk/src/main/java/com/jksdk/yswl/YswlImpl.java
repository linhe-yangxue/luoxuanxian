package com.jksdk.yswl;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

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

@Service("SdkyswlImpl")
@Scope("prototype")
public class YswlImpl implements I_Platform {
	private static final Logger log = LoggerFactory.getLogger(YswlImpl.class);
	private static final String HTTP_API = "http://ys.jygame.net/";
	private static final String appid = "91498";
	private static final String KEY = "K1rZyLCLnkNN4mdtqHha6";

	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		String token = (String) param.get("token");
		String pid = (String) param.get("pid");

		SortedMap<String, String> p = new TreeMap<String, String>();
		p.put("token", token);
		p.put("time", String.valueOf(System.currentTimeMillis()));
		p.put("appId", appid);
		p.put("sign", createSign(p));
		String result = HttpRequest.PostFunction(HTTP_API + "ysgame/ysapi/getUserInfo.php", p);
		User_Entity user_Entity = null;
		user_Entity = JsonTransfer._In(result, User_Entity.class);
		if (user_Entity == null || user_Entity.code != 0) {
			log.error(this.getClass() + "用户信息 获取失败！---" + result);
			return null;
		}
		////////////////////// 创建角色///////////////////////////////////////////////
		UserBase ubase = new UserBase();
		ubase.pid = pid;
//		ubase.setNickname(user_Entity.data.name);
//		ubase.setuImg(URLDecoder.decode(user_Entity.data.pic, "utf-8"));
		dSource.rtn.getInfo().setAccount(user_Entity.data.id);
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
		String orderId = String.valueOf(param.get("orderId"));
		String id = String.valueOf(param.get("id"));
		String money = String.valueOf(param.get("money"));
		String time = String.valueOf(param.get("time"));
		String serverId = String.valueOf(param.get("serverId"));
		String goodsId = String.valueOf(param.get("goodsId"));
		String goodsNumber = String.valueOf(param.get("goodsNumber"));
		String ext = String.valueOf(param.get("ext"));
		String sign = String.valueOf(param.get("sign"));

		SortedMap<String, String> p = new TreeMap<String, String>();
		p.put("orderId", orderId);
		p.put("id", id);
		p.put("money", money);
		p.put("time", time);
		p.put("serverId", serverId);
		p.put("goodsId", goodsId);
		p.put("goodsNumber", goodsNumber);
		p.put("ext", ext);
		String test_sign = createSign(p);

		if (test_sign.equals(sign)) {
			param.put("goodsOrder", orderId);
			param.put("Amount", money);
			param.put("ownOrder", ext);
			param.put("status", 1);
			param.put("success", "{code:0}");
		} else {
			param.put("fail", this.getClass() + "充值回调sign验证失败" + param.toString());
		}
	}

	/**
	 * 订单创建
	 */
	@Override
	public Object payVerification(DataConf dSource) throws Exception {
		String item_id = String.valueOf(dSource.order.get("shopId"));
		String orderid = String.valueOf(dSource.order.get("ownOrder"));
		OrderToClient order = new OrderToClient();
		order.goodsId = item_id;
		order.goodsNumber = "1";
		order.serverId = "1";
		order.ext = orderid;
		return order;
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
			Set<Entry<String, String>> entry = param.entrySet();
			Iterator<Entry<String, String>> tor = entry.iterator();
			while (tor.hasNext()) {
				Entry<String, String> eit = tor.next();
				sign += eit.getKey() + "=" + eit.getValue();
			}
		}
		sign += KEY;
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
	public String goodsId; // 物品id
	public String goodsNumber; // 物品数量 目前固定为1
	public String serverId; // 服务器Id
	public String ext; // 透传参数
}

class User_Entity {
	public int code;
	public User data;
	public String msg;
}

class User {
	public String id;
	public String name;
	public String pic;
}
