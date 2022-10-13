package com.ssmLogin.springmvc.controller;

import javax.servlet.http.HttpServletRequest;
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
import com.ssmCore.tool.colligate.RegExp;
import com.ssmLogin.constant.I_Error_Login;
import com.ssmLogin.springmvc.facde.I_ActiveCode;
import com.ssmShare.order.ActiveTemp;

@Controller
@Scope("prototype")
@RequestMapping("activecode")
public class ActiveCodeController {

	private static final Logger log = LoggerFactory.getLogger(ActiveCodeController.class);
	@Autowired
	I_ActiveCode active;

	@RequestMapping(value = "addtemplate")
	public void addTemplate(@ModelAttribute ActiveTemp act, HttpServletResponse response) {
		try {
			if (act != null) {
				HttpWrite.getInstance().writeMsg(response, new ReInfo(active.createTemplate(act)));
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("创建激活码模版失败");
		}
		HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR, "创建激活码失败"));
	}

	@RequestMapping(value = "viewtemplate")
	public void viewTemplate(HttpServletRequest request, HttpServletResponse response) {
		try {
			String tempid = request.getParameter("tempid");
			HttpWrite.getInstance().writeMsg(response
						,new ReInfo(active.viewTemplate(tempid)));
			return;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("创建激活码模版失败");
		}
		HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR, "创建激活码失败"));
	}
	
	@RequestMapping(value = "deltemplate")
	public void delTemplate(HttpServletRequest request, HttpServletResponse response) {
		try {
			String tempid = request.getParameter("tempid");
			if(active.delTemplate(tempid)){
				HttpWrite.getInstance().writeMsg(response,new ReInfo());
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("删除激活码模版失败");
		}
		HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR, "删除激活码失败"));
	}

	@RequestMapping(value = "create")
	public void createCode(@RequestParam("gid") String gid, @RequestParam("pid") String pid,
			@RequestParam("sign") String sign, @RequestParam("num") String num, HttpServletResponse response) {
		try {
			if (!gid.isEmpty() && !pid.isEmpty() && !sign.isEmpty() && RegExp.isNumber(num)) {
				active.createCode(gid, pid, sign, Integer.parseInt(num));
				HttpWrite.getInstance().writeMsg(response, new ReInfo());
				return;
			}
		} catch (Exception e) {
			log.warn("创建激活码失败");
		}
		HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR, "创建激活码失败"));
	}

	@RequestMapping(value = "view")
	public void viewCode(@RequestParam("gid") String gid,@RequestParam("pid") String pid,
			@RequestParam("sgin") String sgin,HttpServletResponse response) {
		try {
			if (!gid.isEmpty() && !pid.isEmpty() && RegExp.isNumber(sgin)) {
				Object obj = active.viewCode(gid, pid,Integer.parseInt(sgin));
				HttpWrite.getInstance().writeMsg(response, new ReInfo(obj));
				return;
			}
		} catch (Exception e) {
			log.warn("创建激活码失败");
		}
		HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR, "查找激活码错误！"));
	}
}
