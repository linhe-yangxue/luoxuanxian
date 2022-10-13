package com.ssmShare.entity.statistics;

import java.util.Date;

/**
 * 创建角色日志
 * 
 * @author Only
 *
 */
public class LogCreateStats {

	private String account;// 平台用户ID
	private String pid;
	private String gid;
	private String uid;// 角色ID
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

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
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
