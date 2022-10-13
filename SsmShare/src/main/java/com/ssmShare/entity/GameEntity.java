package com.ssmShare.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ssmCore.constants.Increment;

@Document
public class GameEntity {
	@Id
	@Increment
	private long  _id;
	private String name;   			//游戏名称
	private String gid;				//游戏简称
	private String gameUrl; 		//游戏地址
	private Date   regTime;			//游戏添加时间
	
	public long get_id() {
		return _id;
	}
	public void set_id(long _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public Date getRegTime() {
		return regTime;
	}
	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}
	public String getGameUrl() {
		return gameUrl;
	}
	public void setGameUrl(String gameUrl) {
		this.gameUrl = gameUrl;
	}
}

