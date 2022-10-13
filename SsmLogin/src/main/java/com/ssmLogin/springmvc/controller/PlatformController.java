package com.ssmLogin.springmvc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssmCore.constants.HttpWrite;
import com.ssmCore.constants.ReInfo;
import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmLogin.constant.I_Error_Login;
import com.ssmLogin.defdata.impl.PlatFormList;
import com.ssmLogin.defdata.impl.WxPlatformImpl;
import com.ssmShare.entity.PlatForm;
import com.ssmShare.entity.SelectData;
import com.ssmShare.entity.WxPlatform;

@Controller
@Scope("prototype")
@RequestMapping("platform")
public class PlatformController {
	private static final Logger log = LoggerFactory.getLogger(PlatformController.class);
	@Autowired
	BaseDaoImpl db;

	@RequestMapping(value = "getplatlist")
	public void getPlatForm(HttpServletResponse response) {
		try {
			List<?> ls = db.findAll(WxPlatform.class);
			HttpWrite.getInstance().writeMsg(response, new ReInfo(ls));
			return;
		} catch (Exception e) {
			log.warn("getplatlist", e);
		} finally {
			db = null;
		}
		HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR));
	}

	@RequestMapping(value = "getplat")
	public void getPlatMsg(@RequestParam("pid") String pid, HttpServletResponse response) {
		try {
			WxPlatform wx = db.find(new Query(Criteria.where("pid").is(pid)), WxPlatform.class);
			HttpWrite.getInstance().writeMsg(response, new ReInfo(wx));
			return;
		} catch (Exception e) {
			log.warn("getplat", e);
		} finally {
			db = null;
		}
		HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR));
	}

	@RequestMapping(value = "editplat")
	public void editPlatMsg(@ModelAttribute WxPlatform plat, HttpServletRequest request, HttpServletResponse response) {
		try {
			String add = request.getParameter("add");
			if (add.equals("1")) {
				db.add(plat);
				PlatFormList.getInstance().addPlat(1, new SelectData(plat.getPid(), plat.getWxname()));
			} else {
				db.saveOrUpdate(plat);
			}
			WxPlatformImpl.getInstance().initDB();
			HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.SUCCESS));
			return;
		} catch (Exception e) {
			log.warn("getplat", e);
		} finally {
			db = null;
		}
		HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR));
	}

	@RequestMapping(value = "delplat")
	public void deletPlatMsg(@RequestParam("pid") String pid, HttpServletResponse response) {
		try {
			db.remove(new Query(Criteria.where("pid").is(pid)), WxPlatform.class);
			WxPlatformImpl.getInstance().initDB();
			HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.SUCCESS));
			return;
		} catch (Exception e) {
			log.warn("getplat", e);
		} finally {
			db = null;
		}
		HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR));
	}

	@RequestMapping(value = "findall")
	public void findall(HttpServletResponse response) {
		try {
			PlatForm form = db.find(new Query(), PlatForm.class);
			HttpWrite.getInstance().writeMsg(response, new ReInfo(form));
			return;
		} catch (Exception e) {
			log.warn("getplat", e);
		} finally {
			db = null;
		}
		HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR));
	}
}
