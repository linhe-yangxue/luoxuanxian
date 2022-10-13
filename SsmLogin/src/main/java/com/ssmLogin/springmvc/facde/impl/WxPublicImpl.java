package com.ssmLogin.springmvc.facde.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;
import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmLogin.springmvc.facde.I_WxPublic;
import com.ssmShare.entity.role.WxPublic;

@Service
public class WxPublicImpl implements I_WxPublic {

	@Autowired
	BaseDaoImpl db;
	
	public static WxPublicImpl getInstance(){
		return SpringContextUtil.getBean(WxPublicImpl.class);
	}
	
	@Override
	public List<WxPublic> findAll(Integer opt,Integer start, Integer finshed) {
		Query queryinfo = null; 
		if(opt!=null && opt==1){
			QueryBuilder queryBuilder = new QueryBuilder();
			BasicDBObject fieldsObject = new BasicDBObject();
			fieldsObject.put("text",1);
			queryinfo = new BasicQuery(queryBuilder.get(), fieldsObject);
			List<WxPublic> ls = db.findAll(queryinfo, WxPublic.class);
			if(ls==null || ls.size()==0){
				WxPublic wp = new WxPublic();
				wp.setText("未找到相关数据");
				ls.add(wp);
			}
			return ls;
		}else{
			
		}
		return null;
	}

	@Override
	public WxPublic find(String pid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(WxPublic wxp) {
		db.add(wxp);
	}

	@Override
	public void edit(WxPublic wxp) {
		db.saveOrUpdate(wxp);
	}

	@Override
	public void delete(String pid) {
		// TODO Auto-generated method stub

	}

}
