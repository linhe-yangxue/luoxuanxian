package com.ssmLogin.springmvc.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.tool.colligate.CommUtil;
import com.ssmCore.tool.colligate.Encryption;
import com.ssmCore.tool.colligate.HttpRequest;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmLogin.constant.ConstData;
import com.ssmLogin.constant.I_Error_Login;
import com.ssmLogin.defdata.facde.I_GamePress;
import com.ssmLogin.springmvc.facde.I_Game;
import com.ssmShare.entity.ServerList;
import com.ssmShare.order.GiftItem;
import com.ssmShare.platform.UserInfo;

@Controller
@Scope("prototype")
@RequestMapping("player")
public class PlayerController {
	private static final Logger log = LoggerFactory.getLogger(AdminLoginController.class);
	private static final String shopping_Cart = "shoppingCart";
	@Autowired
	I_Game server;
	@Autowired
	I_GamePress gamepress;

	@RequestMapping(value = "seach")
	public void seach(@RequestParam("gameId") String gameId, @RequestParam("zid") int zid, @RequestParam("pageNum") int pageNum, @RequestParam("plat") String plat, @RequestParam("account") String account, @RequestParam("nikeName") String nikeName, HttpServletResponse response) {
		try {
			HttpWrite.getInstance().writeMsg(response, server.getUserInfo(gameId, zid, plat, account, nikeName, pageNum));
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
		} finally {
			server = null;
		}
	}

	/**
	 * ????????????
	 * 
	 * @param userId
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "userAdd")
	public void shoppingCart_add(@RequestParam("userId") int userId, HttpSession session, HttpServletResponse response) {
		try {
			List<Integer> userls = (List<Integer>) session.getAttribute(PlayerController.shopping_Cart);
			if (userls == null)
				userls = new ArrayList<>();

			if (!userls.contains(userId)) {
				userls.add(userId);
				session.setAttribute(PlayerController.shopping_Cart, userls);
			}
			HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.SUCCESS));
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
		} finally {
			server = null;
		}
	}

	/**
	 * ????????????
	 * 
	 * @param userId
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "userSubtract")
	public void shoppingCart_Subtract(@RequestParam("userId") int userId, HttpSession session, HttpServletResponse response) {
		try {
			List<Integer> userls = (List<Integer>) session.getAttribute(PlayerController.shopping_Cart);
			if (userls != null) {
				int index = userls.indexOf(userId);
				if (index >= 0) {
					userls.remove(index);
					session.setAttribute(PlayerController.shopping_Cart, userls);
				}
			}
			HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.SUCCESS));
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
		} finally {
			server = null;
		}
	}

	/**
	 * ?????????????????????
	 * 
	 * @param session
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "userls")
	public void shoppingCart_ls(HttpSession session, HttpServletResponse response) {
		try {
			List<Integer> userls = (List<Integer>) session.getAttribute(PlayerController.shopping_Cart);
			if (userls == null)
				userls = new ArrayList<>();
			HttpWrite.getInstance().writeMsg(response, server.getUserByIdls(userls));
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
		} finally {
			server = null;
		}
	}

	@RequestMapping(value = "sendItems")
	public void shoppingCartForm(@RequestParam("gameId") String gameId, @RequestParam("zid") int zid, @RequestParam("plat") String pid, @RequestParam("users") String users, @ModelAttribute GiftItem gift, HttpSession session, HttpServletResponse response) {
		try {
			// ????????????
			gift.setPid(pid);
			// ????????????
			gift.setCreateDate(new Date());
			// ?????????????????????
			gift.setUid(new ArrayList<String>());

			if (gift.getMailType() == 1) {
				Integer[] user = CommUtil.stringToArray(users);
				List<UserInfo> infos = gamepress.getUserByIdls(Arrays.asList(user));
				List<String> uids = new ArrayList<>();
				int len = infos.size();
				for (int i = 0; i < len; i++) {
					UserInfo info = infos.get(i);
					uids.add(info.get_id() + String.format("%03d", info.getUaction().getGameIndex(gameId)) + String.format("%03d", zid));
				}
				gift.setUid(uids);
			}

			//////// ???????????????/////
			ServerList[] serverLists = gamepress.getServiceByGameId(gameId);
			ArrayList<Integer> errorls = new ArrayList<>();
			ServerList curServer = null;
			if (zid == -1) {
				// ?????????
				int serlen = serverLists.length - 1;
				while (serlen >= 0) {
					curServer = serverLists[serlen];
					if (curServer != null) {
						int res = sendItembyZid(gift, curServer);
						if (res != 0) {
							// ????????????
							errorls.add(curServer.getZid());
						}
					}
					serlen--;
				}
			} else {
				// ??????
				for (ServerList server : serverLists) {
					if (server.getZid() == zid) {
						curServer = server;
						break;
					}
				}
				if (curServer != null) {
					int res = sendItembyZid(gift, curServer);
					if (res != 0) {
						// ????????????
						errorls.add(curServer.getZid());
					}
				} else {
					HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR, "?????????????????????"));
				}

			}
			Collections.sort(errorls);
			HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.SUCCESS, (errorls.size() <= 0 ? "" : "?????????" + errorls.toString() + "???????????????")));
			BaseDaoImpl.getInstance().add(gift);
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
			HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR, "???????????????"));
		} finally {
			server = null;
		}
	}

	private int sendItembyZid(GiftItem gift, ServerList curServer) throws Exception {
		if (curServer == null)
			return I_Error_Login.ERROR;
		String sendstr = Encryption.encrypt1(JsonTransfer.getJson(gift), ConstData.MSG_KEY);
		String result = HttpRequest.PostFunction(curServer.getDress() + "/bill/gift", sendstr);
		if (result != null) {
			// ????????????
			return I_Error_Login.SUCCESS;
		} else {
			return I_Error_Login.ERROR;
		}
	}

}
