package com.ssmLogin.defdata.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmShare.entity.PlatForm;
import com.ssmShare.entity.SelectData;

@Service
public class PlatFormList {
	
	public static PlatFormList getInstance(){
		return SpringContextUtil.getBean(PlatFormList.class);
	}
	
	public void init(){
		List<?> obj = BaseDaoImpl.getInstance().findAll(PlatForm.class);
		if(obj==null || obj.size()==0)
			BaseDaoImpl.getInstance().saveOrUpdate(new PlatForm());
	}
	/**
	 * 添加平台列表
	 * @param type
	 */
	public void addPlat(int type,SelectData slet){
		Query  query = new Query(Criteria.where("uid").is("platid"));
		Update update = new Update();
		if(type==1){
			update.addToSet("wx",slet);
		}else{
			update.addToSet("plat",slet);
		}
		BaseDaoImpl.getInstance().insertOrUpdate(query, update, PlatForm.class);
	}

	public List<SelectData>getPlat(int type){
		SelectData[] ls;
		if(type==1){
			ls = BaseDaoImpl.getInstance().find("platForm","wx", "'uid':'platid'", SelectData[].class);
		}else{
			ls = BaseDaoImpl.getInstance().find("platForm","plat", "'uid':'platid'", SelectData[].class);
		}
		List<SelectData> arrys = new ArrayList<>();
		arrys.add(0,new SelectData("0","请选择平台"));
		arrys.addAll(Arrays.asList(ls));
		return arrys;	
	}
}
