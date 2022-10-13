package com.ssmShare.entity.role;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class GameMsg implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	private String id;				//游戏简称
	private String text; 			//游戏名称
	
	private String loginUrl;        //游戏登录服地址
	private String chatUrl; 		//聊天服地址
	private String gameUrl; 		//游戏资源地址
	private String gameInerface;    //游服接口地址   //发送奖励数据
	private Date   regTime;			//游戏添加时间
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLoginUrl() {
		return loginUrl;
	}
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	public String getChatUrl() {
		return chatUrl;
	}
	public void setChatUrl(String chatUrl) {
		this.chatUrl = chatUrl;
	}
	public Date getRegTime() {
		return regTime;
	}
	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}
	public String getGameInerface() {
		return gameInerface;
	}
	public void setGameInerface(String gameInerface) {
		this.gameInerface = gameInerface;
	}
	public String getGameUrl() {
		return gameUrl;
	}
	public void setGameUrl(String gameUrl) {
		this.gameUrl = gameUrl;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
