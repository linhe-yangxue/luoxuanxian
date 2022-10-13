package com.ssmLogin.springmvc.facde.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;
import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmLogin.springmvc.facde.I_Games;
import com.ssmShare.entity.role.GameMsg;
import com.ssmShare.platform.PlatformInfo;
@Service
public class GamesImpl implements I_Games {

	@Autowired
	BaseDaoImpl db;
	
	public static GamesImpl getInstance(){
		return SpringContextUtil.getBean(GamesImpl.class);
	}
	
	@Override
	public GameMsg find(String pid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GameMsg> findAll(Integer opt,Integer start, Integer finshed) {
		Query queryinfo = null; 
		if(opt==1){
			QueryBuilder queryBuilder = new QueryBuilder();
			BasicDBObject fieldsObject = new BasicDBObject();
			fieldsObject.put("text",1);
			queryinfo = new BasicQuery(queryBuilder.get(), fieldsObject);
			List<GameMsg> ls = db.findAll(queryinfo, GameMsg.class);
			if(ls==null || ls.size()==0){
				GameMsg gm = new GameMsg();
				gm.setText("未找到相关数据");
				ls.add(gm);
			}
			return ls;
		}else{
			
		}
		return null;
	}

	@Override
	public void add(GameMsg gm) {
		gm.setRegTime(new Date());
		db.add(gm);//添加游戏列表
		PlatformInfo info = new PlatformInfo();//为当前游戏创建对接信息
		info.setGid(gm.getId());
		info.setChatUrl(gm.getChatUrl());
		info.setLoginURl(gm.getLoginUrl());
		info.setGameUrl(gm.getGameUrl());
		db.add(info);
	}

	@Override
	public void edit(GameMsg gm) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String pid) {
		// TODO Auto-generated method stub

	}

}
