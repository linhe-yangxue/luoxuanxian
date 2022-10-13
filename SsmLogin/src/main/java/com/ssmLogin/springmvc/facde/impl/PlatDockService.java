package com.ssmLogin.springmvc.facde.impl;

import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.ssmCore.constants.ReInfo;
import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.tool.colligate.PinYinUtil;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmLogin.constant.I_Error_Login;
import com.ssmLogin.defdata.impl.PlatFormList;
import com.ssmLogin.defdata.impl.PlatformInfoImpl;
import com.ssmLogin.springmvc.facde.I_PlatDock;
import com.ssmShare.constants.LoginConstants;
import com.ssmShare.entity.Docking;
import com.ssmShare.entity.SelectData;
import com.ssmShare.entity.WxPlatform;
import com.ssmShare.platform.PlatformInfo;

@Service
public class PlatDockService implements I_PlatDock {

	@Autowired
	BaseDaoImpl db;

	public static PlatDockService getInstance() {
		return SpringContextUtil.getBean(PlatDockService.class);
	}

	@Override
	public Object getPlatDock(String gid, String pid) throws Exception {
		if (db == null)
			db = BaseDaoImpl.getInstance();

		Docking[] docks = db.find("platformInfo", "docking", "'gid':'" + gid + "'", Docking[].class);

		String[] login = db.find("platformInfo", "loginURl", "'gid':'" + gid + "'", String[].class);

		for (Docking dock : docks) {
			if (dock.getPid().equals(pid)) {
				if (dock.getIsWx() == 1) {
					WxPlatform wx = LoginConstants.wxPlat.get(dock.getWxPlat());
					String res = login[0];
					String url = getWxUrl(gid,dock.getPid(),wx, res).toLowerCase();
					dock.setGameUrl_login(url);
				}
				return new ReInfo(dock);
			}
		}
		return new ReInfo(new Docking[0]);
	}

	@Override
	public Object getPlatDockList(String gid) {
		if (db == null)
			db = BaseDaoImpl.getInstance();

		Docking[] docks = db.find("platformInfo", "docking", "'gid':'" + gid + "'", Docking[].class);
		return new ReInfo(docks);
	}

	@Override
	public Object addPlatDock(String gid, Docking dock) {
		if (dock.getRate() == null)
			dock.setRate((float) 1.0);
		
		dock.setPid(PinYinUtil.getFirstSpell(dock.getPname()));
		if (dock.getIsWx() == 0)
			PlatFormList.getInstance().addPlat(dock.getIsWx(), new SelectData(dock.getPid(), dock.getPname()));

		if (dock.getGameUrl_res() != null && dock.getGameUrl_res().equals(""))
			dock.setGameUrl_res(null);

		if (dock.getLoginKey() != null && dock.getLoginKey().equals(""))
			dock.setLoginKey(null);

		if (dock.getPayKey() != null && dock.getPayKey().equals(""))
			dock.setPayKey(null);

		Update update = new Update();
		update.addToSet("docking", dock);

		if (db == null)
			db = BaseDaoImpl.getInstance();

		db.insertOrUpdate(new Query(Criteria.where("gid").is(gid)), update, PlatformInfo.class);
		PlatformInfoImpl.getInstance().initDB();
		return new ReInfo(I_Error_Login.SUCCESS);
	}

	@Override
	public Object editPlatDock(String gid, Docking dock) {
		Update update = new Update();
		if (dock.getRate() != null)
			update.set("docking.$.rate", dock.getRate());
		if (dock.getLoginKey() != null && !dock.getLoginKey().trim().isEmpty())
			update.set("docking.$.loginKey", dock.getLoginKey());
		if (dock.getPayKey() != null && !dock.getPayKey().trim().isEmpty())
			update.set("docking.$.payKey", dock.getPayKey());

		if (dock.getGameUrl_res() != null && !dock.getGameUrl_res().trim().isEmpty()) {
			if (dock.getGameUrl_res().equals("-1"))
				update.unset("docking.$.gameUrl_res");
			else
				update.set("docking.$.gameUrl_res", dock.getGameUrl_res());
		}

		if (dock.getGameUrl_login() != null && !dock.getGameUrl_login().trim().isEmpty()) {
			if (dock.getGameUrl_login().equals("-1"))
				update.unset("docking.$.gameUrl_login");
			else
				update.set("docking.$.gameUrl_login", dock.getGameUrl_login());
		}

		if (db == null)
			db = BaseDaoImpl.getInstance();
		db.saveOrUpdate(new Query(Criteria.where("gid").is(gid).andOperator(Criteria.where("docking").elemMatch(Criteria.where("pid").is(dock.getPid())))), update, PlatformInfo.class);
		PlatformInfoImpl.getInstance().initDB();
		return new ReInfo(I_Error_Login.SUCCESS);
	}

	@Override
	public Object delPlatDock(String gid, String pid) {
		if (db == null)
			db = BaseDaoImpl.getInstance();

		Update update = new Update();
		Docking dock = new Docking();
		dock.setPid(pid);
		update.pull("docking", dock);
		db.saveOrUpdate(new Query(Criteria.where("gid").is(gid)), update, PlatformInfo.class);
		PlatformInfoImpl.getInstance().initDB();
		return new ReInfo(I_Error_Login.SUCCESS);
	}

	@Override
	public void Destory() {
		db = null;
	}

	public static String getWxUrl(String gid, String pid, WxPlatform dock, String loginUrl) throws Exception {
		String head = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + dock.getAppid() + "&redirect_uri=";
		String bottom = "&response_type=code&scope=snsapi_userinfo&state=#wechat_redirect";
		String urlecode = URLEncoder.encode(loginUrl + "/" + pid + "/" + gid + "/login", "UTF-8");
		return head + urlecode + bottom;
	}

	@Override
	public Object changeFollow(String gid, String pid, int value) {
		if (db == null)
			db = BaseDaoImpl.getInstance();

		Update update = new Update();
		update.set("docking.$.disFollow", value);
		db.saveOrUpdate(new Query(Criteria.where("gid").is(gid).andOperator(Criteria.where("docking").elemMatch(Criteria.where("pid").is(pid)))), update, PlatformInfo.class);
		PlatformInfoImpl.getInstance().initDB();
		return new ReInfo(I_Error_Login.SUCCESS);
	}

	@Override
	public Object changeShare(String gid, String pid, int value) {
		if (db == null)
			db = BaseDaoImpl.getInstance();

		Update update = new Update();
		update.set("docking.$.disShare", value);
		db.saveOrUpdate(new Query(Criteria.where("gid").is(gid).andOperator(Criteria.where("docking").elemMatch(Criteria.where("pid").is(pid)))), update, PlatformInfo.class);
		PlatformInfoImpl.getInstance().initDB();
		return new ReInfo(I_Error_Login.SUCCESS);
	}
}
