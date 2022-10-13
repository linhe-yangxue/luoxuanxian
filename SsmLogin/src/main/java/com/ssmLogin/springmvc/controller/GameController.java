package com.ssmLogin.springmvc.controller;

import javax.management.relation.RoleInfo;
import javax.servlet.http.HttpServletResponse;

import com.ssmCore.tool.colligate.HttpRequest;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmLogin.constant.ConstData;
import com.ssmShare.order.ActiveTemp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssmCore.constants.HttpWrite;
import com.ssmCore.constants.ReInfo;
import com.ssmLogin.constant.I_Error_Login;
import com.ssmLogin.springmvc.facde.I_Game;
import com.ssmShare.entity.ServerList;
import com.ssmShare.order.ShopItem;
import com.ssmShare.platform.PlatformInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Controller
@Scope("prototype")
@RequestMapping("game")
public class GameController {
	private static final Logger log = LoggerFactory.getLogger(GameController.class);
	@Autowired
	I_Game service;

	/**
	 * 获取游戏列表
	 * 
	 * @param response
	 */
	@RequestMapping(value = "gamels")
	public void gamels(HttpServletResponse response) {
		try {
			HttpWrite.getInstance().writeHtml(response, service.getGamels());
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
			HttpWrite.getInstance().writeHtml(response, new ReInfo(I_Error_Login.ERROR));
		} finally {
			service = null;
		}
	}

	/**
	 * 根据Id获取游戏详细信息
	 * 
	 * @param gameid
	 * @param response
	 */
	@RequestMapping(value = "getById")
	public void getById(@RequestParam("gameId") String gameid, HttpServletResponse response) {
		try {
			HttpWrite.getInstance().writeHtml(response, service.getGameById(gameid));
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
			HttpWrite.getInstance().writeHtml(response, new ReInfo(I_Error_Login.ERROR));
		} finally {
			service = null;
		}
	}

	/**
	 * 根据Id获取游戏详细信息
	 * 
	 * @param gameid
	 * @param response
	 */
	@RequestMapping(value = "getServicesByGameId")
	public void getServicesByGameId(@RequestParam("gameId") String gameid, HttpServletResponse response) {
		try {
			HttpWrite.getInstance().writeHtml(response, service.getServiceByGameId(gameid));
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
			HttpWrite.getInstance().writeHtml(response, new ReInfo(I_Error_Login.ERROR));
		} finally {
			service = null;
		}
	}

	/**
	 * 根据Id获取游戏详细信息
	 * 
	 * @param gameid
	 * @param response
	 */
	@RequestMapping(value = "getDockingByGameId")
	public void getDockingByGameId(@RequestParam("gameId") String gameid, HttpServletResponse response) {
		try {
			HttpWrite.getInstance().writeHtml(response, service.getDockingByGameId(gameid));
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
			HttpWrite.getInstance().writeHtml(response, new ReInfo(I_Error_Login.ERROR));
		} finally {
			service = null;
		}
	}

	/**
	 * 根据Id获取游戏详细信息
	 * 
	 * @param gameid
	 * @param response
	 */
	@RequestMapping(value = "modifyDockingByPlatId")
	public void modifyDockingByPlatId(@RequestParam("gameId") String gameId, @RequestParam("pid") String pid, @RequestParam("content") String content, HttpServletResponse response) {
		try {
			HttpWrite.getInstance().writeHtml(response, service.modifyLoginNotice(gameId, pid, content));
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
			HttpWrite.getInstance().writeHtml(response, new ReInfo(I_Error_Login.ERROR));
		} finally {
			service = null;
		}
	}

	/**
	 * 修改服务器信息
	 * 
	 * @param gameid
	 * @param response
	 */
	@RequestMapping(value = "modifySerInfo")
	public void modifySerInfo(@RequestParam("gameId") String gameid, @ModelAttribute ServerList ser, HttpServletResponse response) {
		try {
			if (ser.getStatus().equals(0)) {
				service.repairToRollOff(gameid,ser.getZid());
			}
			HttpWrite.getInstance().writeHtml(response, service.modifySerInfo(gameid, ser));
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
			HttpWrite.getInstance().writeHtml(response, new ReInfo(I_Error_Login.ERROR));
		} finally {
			service = null;
		}
	}

	/**
	 * 添加服务器信息
	 * 
	 * @param gameid
	 * @param response
	 */
	@RequestMapping(value = "addSerInfo")
	public void addSerInfo(@RequestParam("gameId") String gameid, @ModelAttribute ServerList ser, HttpServletResponse response) {
		try {
			HttpWrite.getInstance().writeHtml(response, service.addSerInfo(gameid, ser));
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
			HttpWrite.getInstance().writeHtml(response, new ReInfo(I_Error_Login.ERROR));
		} finally {
			service = null;
		}
	}

	/**
	 * 删除服务器信息
	 * 
	 * @param response
	 */
	@RequestMapping(value = "delSerInfo")
	public void delSerInfo(@RequestParam("gameId") String gameId, @RequestParam("serId") int serId, HttpServletResponse response) {
		try {
			HttpWrite.getInstance().writeHtml(response, service.delSerInfo(gameId, serId));
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
			HttpWrite.getInstance().writeHtml(response, new ReInfo(I_Error_Login.ERROR));
		} finally {
			service = null;
		}
	}

	/**
	 * 更新聊天服务器地址
	 * 
	 * @param chatUrl
	 * @param response
	 */
	@RequestMapping(value = "udpateChat")
	public void updateGameChatInfo(@ModelAttribute PlatformInfo plat, HttpServletResponse response) {
		try {
			HttpWrite.getInstance().writeMsg(response, service.updateChatUrl(plat.getGid(), plat.getChatUrl()));
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
			HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR));
		} finally {
			service = null;
		}
	}

	/**
	 * 获取商品列表
	 * 
	 * @param gameId
	 * @param response
	 */
	@RequestMapping(value = "getShopItemls")
	public void getShopItemls(@RequestParam("gameId") String gameId, HttpServletResponse response) {
		try {
			HttpWrite.getInstance().writeMsg(response, service.getShopItemls(gameId));
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
			HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR));
		} finally {
			service = null;
		}
	}

	/**
	 * 修改商品信息
	 * 
	 * @param gameid
	 * @param ser
	 * @param response
	 */
	@RequestMapping(value = "modifyShopItemInfo")
	public void modifyShopItemInfo(@RequestParam("gameId") String gameId, @ModelAttribute ShopItem item, HttpServletResponse response) {
		try {
			HttpWrite.getInstance().writeMsg(response, service.modifyShopItem(gameId, item));
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
			HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR));
		} finally {
			service = null;
		}
	}

	/**
	 * 添加商品
	 * @param gameId
	 * @param item
	 * @param response
	 */
	@RequestMapping(value = "addShopItemInfo")
	public void addShopItemInfo(@RequestParam("gameId") String gameId, @ModelAttribute ShopItem item, HttpServletResponse response) {
		try {
			HttpWrite.getInstance().writeMsg(response, service.addShopItem(gameId, item));
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
			HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR));
		} finally {
			service = null;
		}
	}

	/**
	 * 删除商品
	 * 
	 * @param gameId
	 * @param item
	 * @param response
	 */
	@RequestMapping(value = "delShopItemInfo")
	public void delShopItemInfo(@RequestParam("gameId") String gameId, @RequestParam("itemId") int itemId, HttpServletResponse response) {
		try {
			HttpWrite.getInstance().writeMsg(response, service.delShopItem(gameId, itemId));
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
			HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR));
		} finally {
			service = null;
		}
	}

	@RequestMapping(value = "updateJson")
	public String addTemplate(@ModelAttribute ActiveTemp act, HttpServletResponse response) {
		try {
			String cmd="/usr/share/nginx/html/h5games2/publish/SsmGame/bat/jsonUpdate.sh";
			//执行命令
			Process p = Runtime.getRuntime().exec(cmd);
			//取得命令结果的输出流
			InputStream fis=p.getInputStream();
			//用一个读输出流类去读
			InputStreamReader isr=new InputStreamReader(fis);
			//用缓冲器读行
			BufferedReader br=new BufferedReader(isr);
			String line=null;
			//直到读完为止
			while((line=br.readLine())!=null)
			{
				System.out.println(line);
			}
//			Runtime.getRuntime().exec("svn update /usr/share/nginx/html/h5games2/publish/SsmGame/lx_h5_json/;" +
//					"");
		} catch (IOException e) {
			e.printStackTrace();
			return "错误";
		}
		return "成功";
		// 执行路径写死
	}
}
