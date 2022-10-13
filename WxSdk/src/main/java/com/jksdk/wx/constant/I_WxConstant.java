package com.jksdk.wx.constant;

public interface I_WxConstant {
	 
	/**
	 * token获取连接"
	 */
	public static final String ACCESSTOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
	
	/**
	 * 获取tiket
	 */
	public static final String JSTICKET="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=";
	
	/**
	 * 网页授权taken
	 */
	public static final String ACCESSTOKEN_URL_WEB="https://api.weixin.qq.com/sns/oauth2/access_token?";
	/**
	 * 获取用户信息连接
	 */
	public static final String WXUSERINFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?";
	
	/**
	 * web方式
	 */
	public static final String WXUSERINFO_URL_WEB="https://api.weixin.qq.com/sns/userinfo?";
	/**
	 * 获取得到的生成码
	 */
	public static final String CODE = "code";
	/**
	 * 类型
	 */
	public static final String GANTTYPE = "&grant_type=authorization_code";
	/**
	 * 语言
	 */
	public static final String LANGUAGE = "&lang=zh_CN";

	/**
	 * 统一下单
	 */
	public static final String  UNIFIEDORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
}
