package com.jksdk.lbw;

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
import com.ssmShare.entity.UserBase;
import com.ssmShare.facde.I_Platform;
import com.ssmShare.platform.DataConf;

@Service("SdklbwImpl")
@Scope("prototype")
public class SdklbwImpl implements I_Platform {
	private static final Logger log = LoggerFactory.getLogger(SdklbwImpl.class);

	public static final String URL_USERINFO = "http://passport.4177.com/game/user/info?";
	private static final String game_id = "457";
	private static final String secret_key = "I5Zx65TmVp";

	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		String pid = (String) param.get("pid");
		String gid = (String) param.get("g_id");
		String time = String.valueOf(param.get("time"));
		String uid = (String) param.get("uid");// 用户登录口令
		String sign = (String) param.get("sign");// 用户渠道

		SortedMap<String, String> p = new TreeMap<String, String>();
		p.put("gid", gid);
		p.put("time", time);
		p.put("uid", uid);

		String tempSign = createSign(p);
		if (!sign.equals(tempSign)) {
			log.error(this.getClass() + "登陆sign计算失败！---" + param.toString());
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
		String gid = (String) param.get("g_id");
		String orderNum = (String) param.get("orderNum");
		String orderId = (String) param.get("orderId");
		String total_fee = (String) param.get("total_fee");
		String gold = (String) param.get("gold");
		String uid = (String) param.get("uid");
		String time = (String) param.get("time");
		String sign = (String) param.get("sign");

		SortedMap<String, String> p = new TreeMap<String, String>();
		p.put("gid", gid);
		p.put("orderNum", orderNum);
		p.put("orderId", orderId);
		p.put("total_fee", total_fee);
		p.put("gold", gold);
		p.put("uid", uid);
		p.put("time", time);

		String test_sign = createSign(p);
		if (test_sign.equals(sign)) {
			param.put("goodsOrder", orderNum);
			param.put("Amount", total_fee);
			param.put("ownOrder", orderId);
			param.put("status", 1);
			param.put("success", "success");
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
		otc.gid = game_id;
		otc.orderId = String.valueOf(dSource.order.get("ownOrder"));
		otc.total_fee = ((Float) dSource.order.get("amount")).intValue();
		otc.gold = "Diamond" + String.valueOf(dSource.order.get("goodsNum"));
		otc.uid = String.valueOf(dSource.order.get("account"));
		otc.serverNum = Integer.parseInt(String.valueOf(dSource.order.get("zid")));
		otc.playerName = String.valueOf(dSource.params.get("extend"));
		otc.time = Integer.parseInt(String.valueOf(System.currentTimeMillis() / 1000));

		SortedMap<String, String> param = new TreeMap<String, String>();
		param.put("orderId", otc.orderId);
		param.put("gold", otc.gold);
		param.put("time", String.valueOf(otc.time));
		param.put("gid", otc.gid);
		param.put("playerName", otc.playerName);
		param.put("serverNum", String.valueOf(otc.serverNum));
		param.put("uid", otc.uid);
		param.put("total_fee", String.valueOf(otc.total_fee));
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
			Set<Entry<String, String>> entry = param.entrySet();
			Iterator<Entry<String, String>> tor = entry.iterator();
			while (tor.hasNext()) {
				Entry<String, String> eit = tor.next();
				sign += eit.getKey() + "=" + eit.getValue() + "&";
			}
		}
		sign = sign.substring(0, sign.length() - 1);
		sign += secret_key;
		return Encryption.Encode(sign, Encryption.SHA1);
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
	public String gid;
	public String orderId;
	public int total_fee;
	public String gold;
	public String uid;
	public int serverNum;
	public String playerName;
	public int time;
	public String sign;
	public String extend;
}

class User_Entity {
	public String code;
	public User data;
	public String msg;
}

class User {
	public int id;
	public String nickname;
	public int gender;// 性别【1：男 2: 女 0：未知】
	public String avatar;
	public int share_show;// 游戏是否显示分享按钮【0:不显示；1：显示】
	public int favorite_show;// 游戏是否显示收藏按钮【0:不显示；1：显示】
	public int is_favorite;// 是否收藏【0:未收藏；1：已收藏】
}

class ClientInit {
	public String app_id;
	public String open_id;
	public String channel;
	public String access_token;
}
