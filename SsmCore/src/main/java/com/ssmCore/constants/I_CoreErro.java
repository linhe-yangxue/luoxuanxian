package com.ssmCore.constants;

/**
 * 核心服务错误信息表（msg）
 */
public interface I_CoreErro {

	public final static int ERRO_HTTP_INVOKE = 4001 ; //http调用错误
	
	public static final int ERRO_WEBSOCKET_INVOKE  = 4002; //websocket调用错误

	public static final int ERRO_WEBSOCKET_SEND = 4003; //websocket发送信息错误

	public static final int ERRO_CALLMETHOD_EXITS = 4004; //http调用方法不存在

	public static final int ERRO_HTTP_PARAM = 4005; //http参数错误

}
