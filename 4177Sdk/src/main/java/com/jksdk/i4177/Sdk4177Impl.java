package com.jksdk.i4177;

import java.util.Map;
import java.util.Map.Entry;
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

@Service("Sdk4177Impl")
@Scope("prototype")
public class Sdk4177Impl implements I_Platform {
	private static final Logger log = LoggerFactory.getLogger(Sdk4177Impl.class);

	public static final String URL_USERINFO = "http://passport.4177.com/game/user/info?";
	private static final String app_id = "10389";
	private static final String secret_key = "5ad146d546b86e73094eb36da8ba05f9";

	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		String pid = (String) param.get("pid");
		String open_id = (String) param.get("open_id");// 用户id
		String access_token = (String) param.get("access_token");// 用户登录口令
		String channel = (String) param.get("channel");// 用户渠道
		// Integer is_favorite =
		// Integer.parseInt(String.valueOf(param.get("is_favorite")));//
		// 是否收藏（0：未收藏，1：已收藏）
		String userSign = Encryption.Encode("access_token=" + access_token + "&open_id=" + open_id + "&secret_key=" + secret_key, Encryption.MD5);
		String reqresult = HttpRequest.GetFunction(URL_USERINFO + "open_id=" + open_id + "&access_token=" + access_token + "&sign=" + userSign);
		User_Entity user = JsonTransfer._In(reqresult, User_Entity.class);
		if (user == null || user.code != 200) {
			log.error(this.getClass() + "用户信息获取失败！---" + reqresult);
			return null;
		}

		UserBase ubase = new UserBase();
		ubase.pid = pid;
		// ubase.setNickname(user.data.nickname);
		// ubase.setuImg(user.data.avatar);
		dSource.rtn.getInfo().setAccount(open_id);
		dSource.rtn.getInfo().setPid(pid);
		dSource.rtn.setUsebase(ubase);

		// url附加参数
		ClientInit clientInit = new ClientInit();
		clientInit.app_id = app_id;
		clientInit.channel = channel;
		clientInit.open_id = open_id;
		clientInit.access_token = access_token;
		param.put("extend", JsonTransfer.getJson(clientInit));
		param.put("isSendAllparam", "1");
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
		String app_id = (String) param.get("app_id");
		String open_id = (String) param.get("open_id");
		String bill_no = (String) param.get("bill_no");
		String ext = (String) param.get("ext");
		String price = (String) param.get("price");
		String status = (String) param.get("status"); //
		String sign = (String) param.get("sign");

		TreeMap<String, String> p = new TreeMap<>();
		p.put("app_id", app_id);
		p.put("bill_no", bill_no);
		p.put("ext", ext);
		p.put("open_id", open_id);
		p.put("price", price);
		p.put("secret_key", secret_key);
		p.put("status", status);

		String test_sign = createSign(p);
		if (test_sign.equals(sign)) {
			param.put("goodsOrder", bill_no);
			param.put("Amount", String.valueOf(price));
			param.put("ownOrder", bill_no);
			param.put("status", 1);
			param.put("success", status);
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
		otc.open_id = String.valueOf(dSource.order.get("account"));
		otc.access_token = String.valueOf(dSource.params.get("extend"));
		otc.bill_no = String.valueOf(dSource.order.get("ownOrder"));
		otc.goods_name = "Diamond" + String.valueOf(dSource.order.get("goodsNum"));
		otc.total_fee = ((Float) dSource.order.get("amount")).intValue();
		otc.ext = "abs";

		TreeMap<String, String> param = new TreeMap<>();
		param.put("access_token", otc.access_token);
		param.put("bill_no", otc.bill_no);
		param.put("ext", otc.ext);
		param.put("goods_name", otc.goods_name);
		param.put("open_id", otc.open_id);
		param.put("secret_key", secret_key);
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
	private String createSign(TreeMap<String, String> param) {
		String sign = "";
		if (param != null && param.size() > 0) {
			for (Entry<String, String> entry : param.entrySet()) {
				String name = entry.getKey() == null ? "" : entry.getKey();
				String value = entry.getValue() == null ? "" : entry.getValue();
				sign += name + "=" + value + "&";
			}
		}
		sign = sign.substring(0, sign.length() - 1);
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
	public String open_id;
	public String access_token;
	public String bill_no;
	public String goods_name;
	public int total_fee;
	public String ext;
	public String sign;
}

class User_Entity {
	public int code;
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
