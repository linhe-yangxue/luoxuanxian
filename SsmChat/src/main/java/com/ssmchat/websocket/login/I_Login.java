package com.ssmchat.websocket.login;

import com.ssmCore.jetty.EventSocket;
import com.ssmShare.entity.ChatEntity;

public interface I_Login {

	/**
	 * 客户端连接信息处理
	 * @param chat
	 * @param event
	 */
	public void client(EventSocket event);
	
	/**
	 * 个人游戏信息发布
	 * @param chat
	 */
	public void playerPush(ChatEntity chat);
}
