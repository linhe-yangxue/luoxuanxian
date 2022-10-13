package com.ssmShare.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户基本信息
 */
public class UserBase implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nickname;
	private String uImg;
	private Integer uSex;
	private Date uReg;
	private Integer disFollow = 0;
	private Integer isFollow = 0;
	private Integer disShare = 0;
	private Integer invitCount;
	private String invitID;// 邀请ID
	private Integer currDay;
	private String initPid;
	private Integer device; //用户设备访问类型

	public String pid; // 平台pid
	public String gid;
	public Integer zid;
	private String uid; // 游戏使用游戏id
	private Long guid;
	private Integer vip;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getuImg() {
		return uImg;
	}

	public void setuImg(String uImg) {
			this.uImg = uImg;
	}

	public Integer getuSex() {
		return uSex;
	}

	public void setuSex(Integer uSex) {
		this.uSex = uSex;
	}

	public Date getuReg() {
		return uReg;
	}

	public void setuReg(Date uReg) {
		this.uReg = uReg;
	}

	public Integer getIsFollow() {
		return isFollow;
	}

	public void setIsFollow(Integer isFollow) {
		this.isFollow = isFollow;
	}

	public Long getGuid() {
		return guid;
	}

	public void setGuid(Long guid, int index, int zid) {
		this.guid = guid;
		setUid(guid + String.format("%03d", index) + String.format("%03d", zid)); // 区分游戏id
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Integer getVip() {
		return vip;
	}

	public void setVip(Integer vip) {
		this.vip = vip;
	}

	public Integer getDisFollow() {
		return disFollow;
	}

	public void setDisFollow(Integer disFollow) {
		this.disFollow = disFollow;
	}

	public Integer getInvitCount() {
		return invitCount;
	}

	public void setInvitCount(Integer invitCount) {
		this.invitCount = invitCount;
	}

	public String getInvitID() {
		return invitID;
	}

	public void setInvitID(String invitID) {
		this.invitID = invitID;
	}

	public Integer getCurrDay() {
		return currDay;
	}

	public void setCurrDay(Integer currDay) {
		this.currDay = currDay;
	}

	public Integer getDisShare() {
		return disShare;
	}

	public void setDisShare(Integer disShare) {
		this.disShare = disShare;
	}

	public void setGuid(Long guid) {
		this.guid = guid;
	}

	public String getInitPid() {
		return initPid;
	}

	public void setInitPid(String initPid) {
		this.initPid = initPid;
	}

	public Integer getDevice() {
		return device;
	}

	public void setDevice(Integer device) {
		this.device = device;
	}

}
