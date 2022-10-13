package com.ssmShare.entity.role;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document // 微信实体角色
public class WxPublic{
	@Id 
	private String id; //微信简称
	private String text;   //微信公众名称
	private String shopId;  //商户号
	private String appid;   //微信appid
	private String secret;  //微信secret
	private String token;   //微信token
	private String eskey;   //微信key
	private String paykey;  //微信支付密钥
	private String callPay; //微信支付连接
	private int disFollow=0; // 是否开启关注 0不开启 1开 启
	private int disShare=0; // 是否开启分享 0不开启 1开 启

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getEskey() {
		return eskey;
	}
	public void setEskey(String eskey) {
		this.eskey = eskey;
	}
	public String getPaykey() {
		return paykey;
	}
	public void setPaykey(String paykey) {
		this.paykey = paykey;
	}
	public String getCallPay() {
		return callPay;
	}
	public void setCallPay(String callPay) {
		this.callPay = callPay;
	}
	public Integer getDisFollow() {
		return disFollow;
	}
	public void setDisFollow(Integer disFollow) {
		this.disFollow = disFollow;
	}
	public Integer getDisShare() {
		return disShare;
	}
	public void setDisShare(Integer disShare) {
		this.disShare = disShare;
	}
	
}
