package com.ssmLogin.defdata.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmLogin.defdata.entity.Logindata;
import com.ssmLogin.defdata.entity.LogindataCriteria;
import com.ssmLogin.defdata.entity.LogindataKey;
import com.ssmLogin.defdata.mapper.LogindataMapper;

@Repository
public class LogindataImpl {

	private @Autowired LogindataMapper mapper;
	private @Autowired LogindataCriteria criteria;

	public static LogindataImpl instance(){
		return SpringContextUtil.getBean(LogindataImpl.class);
	}

	public List<Logindata> findList(){
		criteria.clear();
		return mapper.selectByExample(criteria);
	}

	public int addRecord(Logindata logindata){
		
		LogindataKey key = new LogindataKey();
		key.setGuid(logindata.getGuid());
		key.setLgdate(logindata.getLgdate());
		
		if(mapper.selectByPrimaryKey(key)==null)
			return mapper.insertSelective(logindata);
		else
			return mapper.updateByPrimaryKey(logindata);
	}

	public int upRecord(Logindata logindata){
		return mapper.updateByPrimaryKeySelective(logindata);
	}

	public int delAll(){
		criteria.clear();
		return mapper.deleteByExample(criteria);
	}

	public int getCount(){
		criteria.clear();
		return mapper.countByExample(criteria);
	}

}