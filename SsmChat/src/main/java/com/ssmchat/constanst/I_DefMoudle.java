package com.ssmchat.constanst;

public class I_DefMoudle {

	//websocket连接
	public static final String WEBSOCKET_CLIENT = "/client";
	
	//向所有人发送消息
	public static final String MSG_SEND_RESERVICE = "/";
	
	//向所有人发送消息
	public static final String MSG_PUSH = "/push";
	
	//支付成功消息通知
	public static final String PAY_SUCCESS = "/paysuccess";

	/**用户从其他地方登录**/
	public static final String PLAYER_LOGIN = "用户将退出游戏，已从其他地方登录！";
	
	/**用户从其他地方登录**/
	public static final String MERGE_GAME = "游戏合服，请退出游戏！";
	
	/**用户从其他地方登录**/
	public static final String GAME_STOP = "游戏停服维护，请退出登录!";
	
}
