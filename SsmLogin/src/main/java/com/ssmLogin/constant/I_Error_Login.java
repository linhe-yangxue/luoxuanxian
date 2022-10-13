package com.ssmLogin.constant;

public interface I_Error_Login {

	/**
	 * 成功
	 */
	public static int SUCCESS = 0;
	/** 未知错误 */
	public static int ERROR = -1;
	/**
	 * 登陆失败
	 */
	public static int ERROR_LOGIN = 20001;

	public static int ERRO_SERVERLIST = 20002;
	// 登录获取密钥失败
	public static int ERRO_GET_TOKEN = 3000;
	// 未得到用户信息
	public static int ERRO_USER_GET = 3001;
	// 平台对接sdk不存在未找到
	public static int ERRO_SDK_ISEXIT = 3002;
	// 错误的用户登录
	public static int ERRO_USER_LOGIN = 3003;
	// 用户数据存储错误
	public static int ERRO_DB_RW = 3004;
	// 用户信息修改
	public static int ERRO_USER_MODFIY = 3005;
	// 用户信息修改
	public static int ERRO_ACTIVE_CODE = 3006;

	/** 游戏数据不存在 */
	public static int ERROR_ADMIN_GAME_NOTEXIST = 6000;
	/** 未登录 */
	public static int ERROR_ADMIN_NOT_LOGIN = 6001;
	/** 服务器已经存在 */
	public static int ERROR_ADMIN_SERVER_NOTEXIST = 6002;
	/** 修改密码与新密码相同 */
	public static int ERROR_ADMIN_USER_PW_IS_SAME = 6003;
	/** 旧密码输入错误 */
	public static int ERROR_ADMIN_USER_OLDPW_ERROR = 6004;
	/** 玩家已经添加到列表 */
	public static int ERROR_SHOPPINGCACT_EXIT = 6005;

}
