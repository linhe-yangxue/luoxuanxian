package com.ssmLogin.springmvc.facde.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;
import com.ssmCore.constants.ReInfo;
import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.tool.colligate.PinYinUtil;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmLogin.constant.I_Error_Login;
import com.ssmLogin.defdata.impl.PlatformInfoImpl;
import com.ssmLogin.springmvc.facde.I_PlatGame;
import com.ssmShare.platform.PlatformInfo;

@Service
public class PlatGameService implements I_PlatGame {

	public static PlatGameService getInstance(){
		return SpringContextUtil.getBean(PlatGameService.class);
	}
	
	@Autowired
	BaseDaoImpl db;

	@Override
	public Object getGameLogin(String gid) {
		QueryBuilder queryBuilder = new QueryBuilder();
		queryBuilder.and(new BasicDBObject().append("gid", gid));
		BasicDBObject fieldsObject = new BasicDBObject();
		fieldsObject.put("gameUrl", 1);
		fieldsObject.put("loginURl", 1);
		Query query = new BasicQuery(queryBuilder.get(), fieldsObject);
		if(db==null)
			db = BaseDaoImpl.getInstance();
		PlatformInfo info = db.find(query, PlatformInfo.class);
		if(info==null)
			return "";
		return new UrlTemp(info.getLoginURl(),info.getGameUrl());
	}
	
	@Override
	public Object getPlatGame(String name) {
		QueryBuilder queryBuilder = new QueryBuilder();
		queryBuilder.and(new BasicDBObject().append("gameNa", name));
		BasicDBObject fieldsObject = new BasicDBObject();
		fieldsObject.put("gameNa", 1);
		fieldsObject.put("gid", 1);
		fieldsObject.put("loginURl", 1);
		fieldsObject.put("chatUrl", 1);
		fieldsObject.put("gameUrl", 1);
		fieldsObject.put("gameInerface", 1);
		fieldsObject.put("aTime", 1);
		Query query = new BasicQuery(queryBuilder.get(), fieldsObject);
		if(db==null)
			db = BaseDaoImpl.getInstance();
		PlatformInfo info = db.find(query, PlatformInfo.class);
		return new ReInfo(info);
	}

	@Override
	public Object getPlatGameList() {
		QueryBuilder queryBuilder = new QueryBuilder();
		queryBuilder.or(new BasicDBObject());
		BasicDBObject fieldsObject = new BasicDBObject();
		fieldsObject.put("gameNa", 1);
		fieldsObject.put("gid", 1);
		fieldsObject.put("chatUrl", 1);
		fieldsObject.put("name", 1);
		fieldsObject.put("gameUrl", 1);
		fieldsObject.put("aTime", 1);
		Query query = new BasicQuery(queryBuilder.get(), fieldsObject);
		if(db==null)
			db = BaseDaoImpl.getInstance();
		List<PlatformInfo> ls = db.findAll(query, PlatformInfo.class);
		return new ReInfo(ls);
	}

	@Override
	public Object addPlatGame(PlatformInfo plat) {
		String gid = PinYinUtil.getFirstSpell(plat.getGameNa());
		plat.setGid(gid);
		plat.setaTime(new Date());

		if(db==null)
			db = BaseDaoImpl.getInstance();
		List<PlatformInfo> ls = db.findAll(new Query(Criteria.where("gid")
			.regex(".*"+ gid +"*.")), PlatformInfo.class);
		if(ls!=null && ls.size()>0)
			plat.setGid(gid+ "_" + ls.size());
		db.add(plat);
		PlatformInfoImpl.getInstance().initDB();
		return new ReInfo("success");
	}

	@Override
	public Object editPlatGame(PlatformInfo plat) {
		Query query = new Query(Criteria.where("gameNa").is(plat.getGameNa()));
		Update update = new Update();
		
		if(plat.getLoginURl()!=null && !plat.getLoginURl().trim().isEmpty())
			update.set("loginURl",plat.getLoginURl());
		
		if(plat.getGameUrl()!=null && !plat.getGameUrl().trim().isEmpty())
			update.set("gameUrl", plat.getGameUrl());

		if(plat.getChatUrl()!=null && !plat.getChatUrl().trim().isEmpty())
			update.set("chatUrl",plat.getChatUrl());
		
		if(plat.getGameInerface()!=null && !plat.getGameInerface().trim().isEmpty())
			update.set("gameInerface",plat.getGameInerface());

		if(db==null)
			db = BaseDaoImpl.getInstance();
		db.saveOrUpdate(query, update, PlatformInfo.class);
		PlatformInfoImpl.getInstance().initDB();
		return new ReInfo("success");
	}

	@Override
	public Object delPlatGame(String name) {
		if(db==null)
			db = BaseDaoImpl.getInstance();
		db.remove(new Query(Criteria.where("gameNa").is(name)), PlatformInfo.class);
		PlatformInfoImpl.getInstance().initDB();
		return  new ReInfo(I_Error_Login.SUCCESS);
	}

	public void Destory() {
		db = null;
	}

}

class UrlTemp{
	public String gameUrl;
	public String loginUrl;
	
	public UrlTemp(String lg,String gm){
		loginUrl = lg;
		gameUrl = gm;
	}
}