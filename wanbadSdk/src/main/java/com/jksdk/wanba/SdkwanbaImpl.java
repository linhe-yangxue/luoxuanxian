package com.jksdk.wanba;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.constants.I_constants;
import com.ssmCore.constants.ReInfo;
import com.ssmCore.tool.colligate.Encryption;
import com.ssmCore.tool.colligate.HttpRequest;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmShare.entity.UserBase;
import com.ssmShare.facde.I_Platform;
import com.ssmShare.platform.DataConf;

@Service("SdkwanbaImpl")
@Scope("prototype")
public class SdkwanbaImpl implements I_Platform {
	public static final Logger log = LoggerFactory.getLogger(SdkwanbaImpl.class);
	public static final String http_api = "https://api.urlshare.cn";
	public static final String api_user_info = "/v3/user/get_info";
	public static final String api_pay = "/v3/user/buy_playzone_item";

	public static final String appid = "1106062123";
	private static final String appkey = "B1ITOupFVonKLHKw";

	public static final Map<Integer, Integer> item_android = new HashMap<Integer, Integer>() {
		private static final long serialVersionUID = 1L;
		{
			put(10, 11579);// 90 立刻激活成长基金
			put(11, 11551);// 10 获得100钻石，首冲双倍可得200钻石
			put(21, 11552);// 1 获得10钻石，首冲双倍可得20钻石
			put(12, 11553);// 30 获得300钻石，赠送30钻石，首冲双倍可得660钻石
			put(13, 11554);// 60 获得600钻石，赠送60钻石，首冲双倍可得1320钻石
			put(14, 11555);// 100 获得1000钻石，赠送120钻石，首冲双倍可得2240钻石
			put(15, 11556);// 200 获得2000钻石，赠送300钻石，首冲双倍可得4600钻石
			put(16, 11557);// 500 获得5000钻石，赠送900钻石，首冲双倍可得11800钻石
			put(17, 11558);// 1000 获得10000钻石，赠送2000钻石，首冲双倍可得24000钻石
			put(18, 11559);// 2000 获得20000钻石，赠送4800钻石，首冲双倍可得49600钻石
			put(1, 11560);// 30 月卡
			put(2, 11561);// 80终身卡
			put(19, 11562);// 18 吕蒙超值礼包
			put(20, 11563);// 88 典韦限时礼包
		}
	};

	public static final Map<Integer, Integer> item_ios = new HashMap<Integer, Integer>() {
		private static final long serialVersionUID = 1L;
		{
			put(10, 11580);// 90 立刻激活成长基金
			put(11, 11565);// 10 获得100钻石，首冲双倍可得200钻石
			put(21, 11564);// 1 获得10钻石，首冲双倍可得20钻石
			put(12, 11566);// 30 获得300钻石，赠送30钻石，首冲双倍可得660钻石
			put(13, 11567);// 60 获得600钻石，赠送60钻石，首冲双倍可得1320钻石
			put(14, 11568);// 100 获得1000钻石，赠送120钻石，首冲双倍可得2240钻石
			put(15, 11569);// 200 获得2000钻石，赠送300钻石，首冲双倍可得4600钻石
			put(16, 11570);// 500 获得5000钻石，赠送900钻石，首冲双倍可得11800钻石
			put(17, 11571);// 1000 获得10000钻石，赠送2000钻石，首冲双倍可得24000钻石
			put(18, 11572);// 2000 获得20000钻石，赠送4800钻石，首冲双倍可得49600钻石
			put(1, 11573);// 30 月卡
			put(2, 11574);// 80终身卡
			put(19, 11575);// 18 吕蒙超值礼包
			put(20, 11576);// 88 典韦限时礼包
		}
	};

	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		String pid = (String) param.get("pid");
		String ip = (String) param.get("ip");
		String c_paramStr = (String) param.get("extend");
		Object fuid = param.get("fuid");

		ClientParam c_param = JsonTransfer._In(c_paramStr, ClientParam.class);

		SortedMap<String, String> pl = new TreeMap<>();
		pl.put("openid", c_param.openid);
		pl.put("openkey", c_param.openkey);
		pl.put("appid", c_param.appid);
		pl.put("pf", c_param.pf);
		pl.put("format", "json");
		pl.put("userip", ip);
		pl.put("sig", encryptCode(api_user_info, pl));

		String lg_res = HttpRequest.PostFunction(http_api + api_user_info, pl);
		User_Entity user_Entity = JsonTransfer._In(lg_res, User_Entity.class);
		if (user_Entity.ret != 0) {
			log.error(this.getClass() + "登陆获取用户信息失败！");
			param.put(I_constants.PTOKEN_ERRPR, user_Entity.msg);
			return null;
		}
		UserBase ubase = new UserBase();
		ubase.setNickname(user_Entity.nickname);
		ubase.setuImg(user_Entity.figureurl.replaceAll("\\\\", ""));

		dSource.rtn.getInfo().setAccount(c_param.openid);
		dSource.rtn.getInfo().setPid(pid);
		dSource.rtn.setUsebase(ubase);
		ubase.pid = pid;

		// 分享设置
		if (fuid != null) {
			ubase.setInvitID(String.valueOf(fuid));
		}
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
		return null;
	}

	@Override
	public void callPay(Map<String, Object> param, DataConf dSource) throws Exception {
		String order = String.valueOf(param.get("orderId"));
		String order_no = String.valueOf(param.get("order_no"));
		String money = String.valueOf(param.get("money"));

		param.put("goodsOrder", order_no);
		param.put("Amount", money);
		param.put("ownOrder", order);
		param.put("status", 1);
		param.put("success", "OK");
	}

	/**
	 * 订单创建
	 */
	@Override
	public Object payVerification(DataConf dSource) throws Exception {
		String orderId = String.valueOf(dSource.order.get("ownOrder"));
		int itemid = Integer.parseInt(String.valueOf(dSource.params.get("itemId")));

		String extend = String.valueOf(dSource.params.get("extend"));
		ClientParam c_param = JsonTransfer._In(extend, ClientParam.class);

		SortedMap<String, String> param = new TreeMap<>();
		param.put("openid", c_param.openid);
		param.put("openkey", c_param.openkey);
		param.put("zoneid", String.valueOf(c_param.zoneid));
		param.put("billno", orderId);
		param.put("appid", appid);

		Integer wbItemid = 0;
		if (c_param.zoneid == 1) {
			wbItemid = item_android.get(itemid);
		} else {
			wbItemid = item_ios.get(itemid);
		}

		if (wbItemid == null) {
			log.error(this.getClass() + "未找到物品对应ID!");
			return null;
		}

		param.put("itemid", String.valueOf(wbItemid));
		param.put("pf", c_param.pf);
		param.put("format", "json");
		param.put("ip", dSource.ipdress);
		param.put("sig", encryptCode(api_pay, param));

		String lg_res = HttpRequest.PostFunction(http_api + api_pay, param);
		PayResult result = JsonTransfer._In(lg_res, PayResult.class);
		if (result.code == 0) {
			if (result.data != null && result.data.length > 0) {
				int len = result.data.length;
				for (int i = 0; i < len; i++) {
					PayData payData = result.data[i];
					String _order = payData.billno.split("_")[1];
					// 充值成功！
					HttpRequest.GetFunction("http://game.wannaplay.cn/" + dSource.doc.getPid() + "/" + dSource.gid + "/pay?" + "order_no=" + payData.billno + "&orderId=" + _order + "&money=" + payData.cost);
				}
			}
		}
		return lg_res;
	}

	private String encryptCode(String api, SortedMap<String, String> pl) throws UnsupportedEncodingException {
		String sigStr = "POST&" + URLEncoder.encode(api, "utf-8") + "&" + URLEncoder.encode(createParams(pl), "utf-8");
		return Encryption.MAC_SHA1(sigStr, appkey + "&", Encryption.HMAC_SHA1);
	}

	private String createParams(SortedMap<String, String> param) {
		String sign = "";
		if (param != null && param.size() > 0) {
			for (Entry<String, String> entry : param.entrySet()) {
				String name = entry.getKey() == null ? "" : entry.getKey();
				String value = entry.getValue() == null ? "" : entry.getValue();
				sign += name + "=" + value + "&";
			}
		}
		if (sign.length() > 0)
			sign = sign.substring(0, sign.length() - 1);
		return sign;
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
