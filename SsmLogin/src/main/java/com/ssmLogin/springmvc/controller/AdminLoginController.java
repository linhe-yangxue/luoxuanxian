package com.ssmLogin.springmvc.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssmCore.constants.HttpWrite;
import com.ssmCore.constants.ReInfo;
import com.ssmLogin.defdata.AdminUser;
import com.ssmLogin.springmvc.facde.I_AdminLogin;

@Controller
@Scope("prototype")
@RequestMapping("user")
public class AdminLoginController {
	private static final Logger log = LoggerFactory.getLogger(AdminLoginController.class);
	@Autowired
	I_AdminLogin service;

	/**
	 * 获取游戏列表
	 * 
	 * @param type
	 * @param page
	 * @param response
	 * @param session
	 */
	@RequestMapping(value = "login")
	public void login(HttpSession session, @RequestParam("username") String username, @RequestParam("password") String password, HttpServletResponse response) {
		try {
			ReInfo user = service.adminLogin(username, password);
			if (user.rt == 0) {
				session.setAttribute("adminuser", user.msg);
			}
			HttpWrite.getInstance().writeHtml(response, user);
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
		} finally {
			service = null;
		}
	}

	@RequestMapping(value = "checkLogin")
	public void checkLogin(HttpSession session, HttpServletResponse response) {

	}

	@RequestMapping(value = "updatePw")
	public void updatePw(HttpSession session, @RequestParam("originalpw") String originalpw, @RequestParam("pw") String pw, HttpServletResponse response) {
		try {
			AdminUser user = (AdminUser) session.getAttribute("adminuser");
			ReInfo info = service.updatePw(user.getUsername(), originalpw, pw);
			if (info.rt == 0) {
				session.removeAttribute("adminuser");
			}
			HttpWrite.getInstance().writeMsg(response, info);
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
		} finally {
			service = null;
		}
	}
}
