package com.ssmLogin.springmvc.facde.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;
import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmLogin.springmvc.facde.I_Platform;
import com.ssmShare.entity.Docking;
import com.ssmShare.entity.role.GameMsg;
import com.ssmShare.entity.role.Platform;
import com.ssmShare.platform.PlatformInfo;

@Service
public class PlatDockImpl implements I_Platform {

	@Autowired
	BaseDaoImpl db;
	
	@Override
	public Object findAll(Integer opt, Integer start, Integer finshed,String gid) {
		Query queryinfo = null; 
		if(opt!=null && opt==1){
			QueryBuilder queryBuilder = new QueryBuilder();
			BasicDBObject fieldsObject = new BasicDBObject();
			fieldsObject.put("text",1);
			queryinfo = new BasicQuery(queryBuilder.get(), fieldsObject);
			List<Platform> ls = db.findAll(queryinfo, Platform.class);
			if(ls==null || ls.size()==0){
				Platform gm = new Platform();
				gm.setText("未找到相关数据");
				ls.add(gm);
			}
			return ls;
		}else{
			PlatformInfo info = db.find(new Query(Criteria.where("gid").is(gid)), PlatformInfo.class);
			if(info!=null)
				return info.getDocking();
		}
		return null;
	}

	@Override
	public GameMsg find(String pid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(Platform plat,Docking doc,String gid) {
		db.add(plat);
		doc.setPid(plat.getId());
		doc.setPname(plat.getText());
		db.saveOrUpdate(new Query(Criteria.where("gid").is(gid))
		,new Update().addToSet("docking",doc), PlatformInfo.class);
	}

	@Override
	public void edit(GameMsg wxp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String pid) {
		// TODO Auto-generated method stub
		
	}

}
