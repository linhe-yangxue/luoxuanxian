package com.ssmShare.entity;

import java.io.Serializable;

/**
 * 平台对接实体
 */
public class Docking implements Serializable {
	private static final long serialVersionUID = 1L;

	private String pid; // 对接平id
	private String pname; // 对接平台名称
	private Float rate; // 货币比率
	private String loginKey; // 登录key （运营商提供 或协商后统一key）
	private String payKey;     // 支付key （运营商提供 或协商后统一key）
	private String notice;      // 公告
	private String gameUrl_res;  // 游戏资源地址(客户端访问地址)
	private Integer isWx;        // 是否是微信 0--默认不是微信 是微信
	private Integer disFollow;   // 是否开启关注 0不开启 1开 启
	private Integer disShare;     // 是否开启分享 0不开启 1开 启
	private String platID; 			// 第三方平台id
	private String extend; 		  // 第三方扩展参数
	private String wxPlat; 		  // 须微信初始化平台
	private TypeServer svType;    //独有服务器列表

	private String gameUrl_login; // 游戏跳转地址 （客户端访问）；
	private String gameUrl_pay; // 游戏订单创建地址
	private String payUrl; // 第三方地址游戏支付地址；

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getLoginKey() {
		return loginKey;
	}

	public void setLoginKey(String loginKey) {
		this.loginKey = loginKey;
	}

	public String getPayKey() {
		return payKey;
	}

	public void setPayKey(String payKey) {
		this.payKey = payKey;
	}

	public int getIsWx() {
		return isWx;
	}

	public void setIsWx(int isWx) {
		this.isWx = isWx;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public String getGameUrl_pay() {
		return gameUrl_pay;
	}

	public void setGameUrl_pay(String gameUrl_pay) {
		this.gameUrl_pay = gameUrl_pay;
	}

	public String getGameUrl_login() {
		return gameUrl_login;
	}

	public void setGameUrl_login(String gameUrl_login) {
		this.gameUrl_login = gameUrl_login;
	}

	public String getGameUrl_res() {
		return gameUrl_res;
	}

	public void setGameUrl_res(String gameUrl_res) {
		this.gameUrl_res = gameUrl_res;
	}

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public String getWxPlat() {
		return wxPlat;
	}

	public void setWxPlat(String wxPlat) {
		this.wxPlat = wxPlat;
	}

	public Float getRate() {
		return rate;
	}

	public void setRate(Float rate) {
		this.rate = rate;
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

	public String getPlatID() {
		return platID;
	}

	public void setPlatID(String platID) {
		this.platID = platID;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	public TypeServer getSvType() {
		return svType;
	}

	public void setSvType(TypeServer svType) {
		this.svType = svType;
	}
}
