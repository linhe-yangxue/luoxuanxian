package com.jksdk.aqy;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssmCore.constants.ReInfo;
import com.ssmCore.mongo.PageList;
import com.ssmCore.tool.colligate.DateUtil;
import com.ssmCore.tool.colligate.Encryption;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmShare.entity.UserBase;
import com.ssmShare.facde.I_Platform;
import com.ssmShare.facde.I_StatsPress;
import com.ssmShare.platform.DataConf;

@Service("SdkaqyImpl")
@Scope("prototype")
public class SdkaqyImpl implements I_Platform {
	@Autowired
	I_StatsPress statspress;

	private static final Logger log = LoggerFactory.getLogger(SdkaqyImpl.class);

	public static final String URL_USERINFO = "https://wx.9g.com/open/userinfo?";
	private static final String game_id = "5949";
	private static final String login_key = "d2e67dec70481e8d92324601bc8ed266";
	private static final String pay_key = "23569f2cdacaca2f7d6dfae552a9c039";

	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		String pid = (String) param.get("pid");
		String user_id = (String) param.get("user_id");
		String agent = (String) param.get("agent");
		String time = (String) param.get("time");
		String loginsign = (String) param.get("sign");

		String encryStr = "user_id=" + user_id + "&agent=" + agent + "&time=" + time + "&key=" + login_key;
		String test_loginsign = Encryption.Encode(encryStr, Encryption.MD5).toLowerCase();
		if (!test_loginsign.equals(loginsign)) {
			log.error(this.getClass() + "登陆sign验证失败！---" + param.toString());
			return null;
		}
		UserBase ubase = new UserBase();
		ubase.pid = pid;
		dSource.rtn.getInfo().setAccount(user_id);
		dSource.rtn.getInfo().setPid(pid);
		dSource.rtn.setUsebase(ubase);
		///////////////////////////////////// 统计///////////////////////////////////////////////////
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
		String user_id = (String) param.get("user_id");
		// String agent = (String) param.get("agent");
		String order_id = (String) param.get("order_id");
		String money = (String) param.get("money");
		String server_id = (String) param.get("server_id");
		// String time = (String) param.get("time");
		String userData = (String) param.get("userData");
		String sign = (String) param.get("sign");
		String signstr = "user_id=" + user_id + "&order_id=" + order_id + "&money=" + money + "&server_id=" + server_id + "&key=" + pay_key;
		String test_sign = Encryption.Encode(signstr, Encryption.MD5).toLowerCase();
		if (test_sign.equals(sign)) {
			param.put("goodsOrder", order_id);
			param.put("Amount", money);
			param.put("ownOrder", userData);
			param.put("status", 1);
			param.put("success", "{\"success\":true,\"data\":" + order_id + ",\"message\":\"成功 \"}");
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
		otc.user_id = String.valueOf(dSource.order.get("account"));
		otc.extra_param = String.valueOf(dSource.order.get("ownOrder"));
		otc.game_id = game_id;
		otc.money = ((Float) dSource.order.get("amount")).intValue();
		otc.server_id = String.valueOf(dSource.order.get("zid"));
		return otc;
	}

	@Override
	public Object statslog(Map<String, Object> param, DataConf dSource) throws Exception {
		LinkedHashMap<String, Object> resMap = new LinkedHashMap<>();
		String res = null;
		///////////////////////////////
		// String game_id = String.valueOf(param.get("game_id"));
		// String agent = String.valueOf(param.get("agent"));
		// 获取服务器ID
		Object serverIdObj = param.get("server_id");
		if (serverIdObj == null) {
			resMap.put("success", false);
			resMap.put("data", -2);
			resMap.put("message", "server_id 为空");
			res = JsonTransfer.getJson(resMap);
			log.error(this.getClass() + "server_id为空");
			return res;
		}
		String server_id = String.valueOf(serverIdObj);
		// 获取类型
		Object typeobj = param.get("type");
		if (typeobj == null) {
			resMap.put("success", false);
			resMap.put("data", -2);
			resMap.put("message", "type 为空");
			res = JsonTransfer.getJson(resMap);
			log.error(this.getClass() + "type 为空");
			return res;
		}
		int type = Integer.parseInt(String.valueOf(typeobj));
		/////////////////////
		String sign = String.valueOf(param.get("sign"));
		String signstr = "game_id=" + game_id + "&server_id=" + server_id + "&key=" + login_key;
		String tempSign = Encryption.Encode(signstr, Encryption.MD5).toLowerCase();
		///////////////

		if (tempSign.equals(sign)) {
			int page = 1;
			if (param.get("page") != null) {
				page = Integer.parseInt(String.valueOf(param.get("page")));
			}
			int pagesize = 1000;
			if (param.get("limit") != null) {
				pagesize = Integer.parseInt(String.valueOf(param.get("limit")));
			}
			String date = null;
			if (param.get("date") != null) {
				date = String.valueOf(param.get("date"));
			} else {
				date = DateUtil.getCurrentDate(DateUtil.DEFAULT_DATA_PATTERN);
			}

			PageList pagelist = null;
			if (type == 1) {
				// 激活
				pagelist = statspress.getCreateRoleLog(dSource.gid, dSource.doc.getPid(), -1, date, date, pagesize, page);
			} else if (type == 2) {
				// 活跃
				pagelist = statspress.getLoginLog(dSource.gid, dSource.doc.getPid(), -1, date, date, pagesize, page);
			}

			LogStats[] list = JsonTransfer._In(JsonTransfer.getJson(pagelist.getDatas()), LogStats[].class);
			List<LogStats> logStats = Arrays.asList(list);
			Map<String, LogStats> mapuser = logStats.stream().collect(Collectors.toMap(LogStats::getAccount, account -> account));

			LinkedHashMap<String, Object> dataMap = new LinkedHashMap<>();
			dataMap.put("total", pagelist.getTotalCount());
			dataMap.put("totalPage", pagelist.getTotalPage());
			dataMap.put("userList", mapuser);

			resMap.put("success", true);
			resMap.put("data", dataMap);
			resMap.put("message", "成功");

			res = JsonTransfer.getJson(resMap, new String[] { "account" }, DateUtil.DEFAULT_DATATIME_PATTERN);
		} else {
			resMap.put("success", false);
			resMap.put("data", -1);
			resMap.put("message", "sign计算失败！");
			res = JsonTransfer.getJson(resMap);
			log.error(this.getClass() + "获取活跃数据异常！");
		}
		return res;
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
	public String game_id;
	public String user_id;
	public String server_id;
	public Integer money;
	public String extra_param;
}

@JsonIgnoreProperties(value = { "account" })
class LogStats {
	private String account;// 平台用户ID
	private String roleName;// 角色名称
	private Integer roleLevel;// 角色等级
	private Date createTime;// 创建名称
	private String serverId;// 服务器ID

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(Integer roleLevel) {
		this.roleLevel = roleLevel;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

}
