package com.ssmLogin.constant;

public interface I_ModuleServlet {
	// 微信服务器授权
	public static final String WX_SV_AUTHORITY = "/authority";
	// 微信初始化验证
	public static final String WX_INIT_CONF = "/wx/conf";
	// 玩家登录
	public static final String LONGIN_GAME = "/login"; // 先登录后游戏
	// 玩家登录
	public static final String GAME_LOGIN = "/glogin"; // 先游戏后登录
	// 平台token或取用户
	public static final String TOKEN_LOGIN = "/ptoken"; // 先游戏后登录
	// 玩家支付
	public static final String PAY_USER = "/pay";
	// 订单创建
	public static final String CREATE_ORDER = "/order";

	public final static String LOGIN_INFO = "/info/";
	/** 统计数据 */
	public final static String STATS_LOG = "/stats";
	/** 错误信息 */
	public final static String ERROR_LOG = "/error/log";
	/** 分享 */
	public  static final String SHARE = "/share";

	/**
	 * 获取用户信息和服务器信息（客户端访问）
	 */
	public final static String GET_INFO_SERVERLIST = LOGIN_INFO + "svlist";
	/**
	 * 获取用户信息(游戏服务器访问)
	 */
	public final static String GET_USER_INFO = LOGIN_INFO + "user";
	/**
	 * 修改用户头像和昵称
	 */
	public final static String MODFIY_USER_INFO = LOGIN_INFO + "modfy";

	/**
	 * 聊天服务器获取用户信息
	 */
	public final static String CHAT_USER_INFO = LOGIN_INFO + "chat";

	/**
	 * 聊天服务器获取用户信息
	 */
	public final static String CODE_AUTHORITY = LOGIN_INFO + "cdkey";

}
