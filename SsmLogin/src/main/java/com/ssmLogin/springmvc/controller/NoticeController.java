package com.ssmLogin.springmvc.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssmCore.constants.HttpWrite;
import com.ssmCore.constants.ReInfo;
import com.ssmCore.tool.colligate.HttpRequest;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmLogin.constant.I_Error_Login;
import com.ssmLogin.defdata.facde.I_GamePress;
import com.ssmShare.constants.E_ChatChannel;
import com.ssmShare.constants.E_TYPE;
import com.ssmShare.entity.ChatEntity;
import com.ssmShare.platform.PlatformInfo;

@Controller
@Scope("prototype")
@RequestMapping("notice")
public class NoticeController {
	private static final Logger log = LoggerFactory.getLogger(NoticeController.class);
	@Autowired
	I_GamePress service;

	/**
	 * 发布公告！
	 * 
	 * @param content
	 * @param response
	 */
	@RequestMapping(value = "send")
	public void send(@RequestParam("gameId") String gameId, @RequestParam("gameType") int gameType, @RequestParam("content") String content, HttpServletResponse response) {
		try {
			PlatformInfo info = service.getGameById(gameId);
			if (info == null) {
				// 游戏不存在
				HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR_ADMIN_GAME_NOTEXIST, "未找到游戏数据"));
				return;
			}
			ChatEntity entity = new ChatEntity();
			entity.setContext(content);
			entity.setMsgType(E_TYPE.NOTICE.getCode());
			entity.setGid(gameId);
			if (gameType == 0) {
				// 全区
				entity.setChannel(E_ChatChannel.ALL.getCode());
			} else {
				entity.setChannel(E_ChatChannel.AREEA.getCode());
				entity.setZid(gameType);
			}
			String result = HttpRequest.PostFunction("https://" + info.getChatUrl() + "/push", JsonTransfer.getJson(entity));
			if (result == null) {
				HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR, "发送失败！"));
			} else {
				HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.SUCCESS));
			}
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
		} finally {
		}
	}
}
