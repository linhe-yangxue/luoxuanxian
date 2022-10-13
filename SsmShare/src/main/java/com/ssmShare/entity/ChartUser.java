package com.ssmShare.entity;

// 登录
public class ChartUser {
	private String uid;
	private Long guid;
	private String gid;
	private Integer zid;
	private Integer vip;
	private String guildId; // 公会id
	private String nickname; // 玩家昵称
	private String uImg; // 玩家头像
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public Long getGuid() {
		return guid;
	}
	public void setGuid(Long guid) {
		this.guid = guid;
	}
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public Integer getZid() {
		return zid;
	}
	public void setZid(Integer zid) {
		this.zid = zid;
	}
	public Integer getVip() {
		return vip;
	}
	public void setVip(Integer vip) {
		this.vip = vip;
	}
	public String getGuildId() {
		return guildId;
	}
	public void setGuildId(String guildId) {
		this.guildId = guildId;
	}
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
}
