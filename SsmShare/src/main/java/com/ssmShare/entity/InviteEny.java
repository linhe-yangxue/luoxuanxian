package com.ssmShare.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import com.ssmCore.tool.colligate.DateUtil;

@Document /** 邀请实体 **/
public class InviteEny {
	
	private String pid; //所在平台
	private String inviterId; //邀请人id
	private String inviteeId; //被邀请人id
	private String invitee_login; //被邀请人登录时间;
	
	public InviteEny(){}
	
	public InviteEny(String yq,String byq,String pid){
		this.inviterId = yq;
		this.inviteeId = byq;
		this.pid = pid;
		setInvitee_login(System.currentTimeMillis()); //设置登录时间
	}
	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getInviterId() {
		return inviterId;
	}

	public void setInviterId(String inviterId) {
		this.inviterId = inviterId;
	}

	public String getInviteeId() {
		return inviteeId;
	}

	public void setInviteeId(String inviteeId) {
		this.inviteeId = inviteeId;
	}

	public String getInvitee_login() {
		return invitee_login;
	}
	public void setInvitee_login(String invitee_login) {
		this.invitee_login = invitee_login;
	}
	public void setInvitee_login(long time){
		this.invitee_login = DateUtil.getCurrDate("-");
	}
}
