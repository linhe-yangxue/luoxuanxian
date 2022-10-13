package com.ssmShare.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//微信信息
@Document
public class WxPlatform {
	@Id
	private String wxname;  //微信公众名称
	private String pid;	   //游戏检查
	private String shopId;  //商户号
	private String appid;   //微信appid
	private String secret;  //微信secret
	private String token;   //微信token
	private String eskey;   //微信key
	private String paykey;  //微信支付密钥
	private String callPay; //微信支付连接
	private Integer disFollow; // 是否开启关注 0不开启 1开 启
	private Integer disShare; // 是否开启分享 0不开启 1开 启
	
	private Long guid;  //临时使用
	private String account; //微信账户
	private String shareUrl; //分享地址
	
	public String getWxname() {
		return wxname;
	}
	public void setWxname(String wxname) {
		this.wxname = wxname;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
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
	public Long getGuid() {
		return guid;
	}
	public void setGuid(Long guid) {
		this.guid = guid;
	}
	public String getShareUrl() {
		return shareUrl;
	}
	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public Integer getDisShare() {
		return disShare;
	}
	public void setDisShare(Integer disShare) {
		this.disShare = disShare;
	}
	public Integer getDisFollow() {
		return disFollow;
	}
	public void setDisFollow(Integer disFollow) {
		this.disFollow = disFollow;
	}
}
