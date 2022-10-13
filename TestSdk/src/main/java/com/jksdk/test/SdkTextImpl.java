package com.jksdk.test;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ssmCore.constants.ReInfo;
import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.mongo.PageList;
import com.ssmCore.tool.colligate.DateUtil;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmShare.entity.UserBase;
import com.ssmShare.facde.I_Platform;
import com.ssmShare.facde.I_StatsPress;
import com.ssmShare.platform.DataConf;
import com.ssmShare.platform.UserInfo;

@Service("SdktestImpl")
@Scope("prototype")
public class SdkTextImpl implements I_Platform {
	@Autowired
	I_StatsPress statspress;

	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		BaseDaoImpl db = BaseDaoImpl.getInstance();
		UserInfo userInfo = db.find(new Query(Criteria.where("account").is((String) param.get("nickname")).and("pid").is((String) param.get("pid"))), UserInfo.class);

		UserBase ubase = null;
		if (userInfo != null) {
			dSource.rtn.setInfo(userInfo);
			dSource.rtn.getInfo().setAccount(userInfo.getAccount());// 接收微信帐号
			dSource.rtn.getInfo().setPid((String) param.get("pid"));
			ubase = userInfo.getUserBase();
		} else {
			ubase = new UserBase();
			ubase.setNickname((String) param.get("nickname"));
			dSource.rtn.getInfo().setAccount((String) param.get("nickname"));
			dSource.rtn.getInfo().setPid((String) param.get("pid"));
			dSource.rtn.setUsebase(ubase);
		}
		ubase.pid = (String) param.get("pid");
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

	}

	@Override
	public Object payVerification(DataConf dSource) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object statslog(Map<String, Object> param, DataConf dSource) throws Exception {
		// int gameid = 5949;
		// int serGameId = Integer.parseInt((String) param.get("game_id"));
		// int ser_id = Integer.parseInt((String) param.get("server_id"));
		int type = Integer.parseInt((String) param.get("type"));
		// String sign = String.valueOf(param.get("sign"));
		String date = String.valueOf(param.get("date"));
		int page = 1;
		if (param.get("page") != null) {
			page = Integer.parseInt(String.valueOf(param.get("page")));
		}
		int pagesize = 100000;
		if (param.get("limit") != null) {
			pagesize = Integer.parseInt(String.valueOf(param.get("limit")));
		}
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("success", true);
		resMap.put("message", "成功");
		PageList pagelist = null;
		String res = null;
		if (type == 1) {
			// 激活
			pagelist = statspress.getCreateRoleLog(dSource.gid, dSource.doc.getPid(), -1, date, date, pagesize, page);
			LogStats[] list = JsonTransfer._In(JsonTransfer.getJson(pagelist.getDatas()), LogStats[].class);
			List<LogStats> logStats = Arrays.asList(list);

			Map<String, LogStats> mapuser = logStats.stream().collect(Collectors.toMap(LogStats::getAccount, account -> account));
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("userList", mapuser);
			dataMap.put("total", pagelist.getTotalCount());
			dataMap.put("totalPage", pagelist.getTotalPage());
			resMap.put("data", dataMap);
			res = JsonTransfer.getJson(resMap, DateUtil.DEFAULT_DATATIME_PATTERN);
		} else if (type == 2) {
			// 活跃
			pagelist = statspress.getLoginLog(dSource.gid, dSource.doc.getPid(), -1, date, date, pagesize, page);
			LogStats[] list = JsonTransfer._In(JsonTransfer.getJson(pagelist.getDatas()), LogStats[].class);
			List<LogStats> logStats = Arrays.asList(list);
			Map<String, LogStats> mapuser = logStats.stream().collect(Collectors.toMap(LogStats::getAccount, account -> account));
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("userList", mapuser);
			dataMap.put("total", pagelist.getTotalCount());
			dataMap.put("totalPage", pagelist.getTotalPage());
			resMap.put("data", dataMap);
			res = JsonTransfer.getJson(resMap, DateUtil.DEFAULT_DATA_PATTERN);
		}
		System.out.println(res);
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

class LogStats {
	private String account;// 平台用户ID
	private String roleName;// 角色名称
	private Integer roleLevel;// 角色等级
	private Date createtime;// 创建名称
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

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

}
