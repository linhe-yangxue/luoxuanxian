package com.ssmLogin.springmvc.controller;

import javax.servlet.http.HttpServletResponse;

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
import com.ssmLogin.springmvc.facde.I_PlatDock;
import com.ssmShare.entity.Docking;

@Controller
@Scope("prototype")
@RequestMapping("platdock")
public class DockController {

	private static final Logger log = LoggerFactory.getLogger(DockController.class);
	@Autowired
	I_PlatDock platdock;
	@Autowired
	HttpWrite print;

	@RequestMapping(value = "dock")
	public void getDock(@RequestParam("gid") String gid, @RequestParam("pid") String pid, HttpServletResponse response) {
		try {
			print.writeMsg(response, platdock.getPlatDock(gid, pid));
		} catch (Exception e) {
			log.warn("获取平台游戏列表", e);
			print.writeMsg(response, new ReInfo(I_Error_Login.ERROR));
		} finally {
			platdock.Destory();
			platdock = null;
		}
	}

	@RequestMapping(value = "list")
	public void getLsGame(@RequestParam("gid") String gid, HttpServletResponse response) {
		try {
			print.writeMsg(response, platdock.getPlatDockList(gid));
		} catch (Exception e) {
			log.warn("获取平台游戏列表", e);
			print.writeMsg(response, new ReInfo(I_Error_Login.ERROR));
		} finally {
			platdock.Destory();
			platdock = null;
		}
	}

	@RequestMapping(value = "add")
	public void addGame(@RequestParam("gid") String gid, @ModelAttribute Docking dock, HttpServletResponse response) {
		try {
			print.writeMsg(response, platdock.addPlatDock(gid, dock));
		} catch (Exception e) {
			log.warn("添加平台游戏", e);
			print.writeMsg(response, new ReInfo(I_Error_Login.ERROR));
		} finally {
			platdock.Destory();
			platdock = null;
		}
	}

	@RequestMapping(value = "edit")
	public void editGame(@RequestParam("gid") String gid, @RequestParam("tmpid") String pid, @ModelAttribute Docking dock, HttpServletResponse response) {
		try {
			if (pid != null && !pid.trim().isEmpty())
				dock.setPid(pid);
			print.writeMsg(response, platdock.editPlatDock(gid, dock));
		} catch (Exception e) {
			log.warn("编辑平台游戏", e);
			print.writeMsg(response, new ReInfo(I_Error_Login.ERROR));
		} finally {
			platdock.Destory();
			platdock = null;
		}
	}

	@RequestMapping(value = "del")
	public void delGame(@RequestParam("gid") String gid, @RequestParam("pid") String pid, HttpServletResponse response) {
		try {
			print.writeMsg(response, platdock.delPlatDock(gid, pid));
		} catch (Exception e) {
			log.warn("删除平台游戏", e);
			print.writeMsg(response, new ReInfo(I_Error_Login.ERROR));
		} finally {
			platdock.Destory();
			platdock = null;
		}
	}

	@RequestMapping(value = "udpateFollow")
	public void changeFollow(@RequestParam("gid") String gid, @RequestParam("pid") String pid, @RequestParam("value") int value, HttpServletResponse response) {
		try {
			print.writeMsg(response, platdock.changeFollow(gid, pid, value));
		} catch (Exception e) {
			log.warn("修改关注状态", e);
			print.writeMsg(response, new ReInfo(I_Error_Login.ERROR));
		} finally {
			platdock.Destory();
			platdock = null;
		}
	}

	@RequestMapping(value = "changeShare")
	public void changeShare(@RequestParam("gid") String gid, @RequestParam("pid") String pid, @RequestParam("value") int value, HttpServletResponse response) {
		try {
			print.writeMsg(response, platdock.changeShare(gid, pid, value));
		} catch (Exception e) {
			log.warn("修改关注状态", e);
			print.writeMsg(response, new ReInfo(I_Error_Login.ERROR));
		} finally {
			platdock.Destory();
			platdock = null;
		}
	}

}
