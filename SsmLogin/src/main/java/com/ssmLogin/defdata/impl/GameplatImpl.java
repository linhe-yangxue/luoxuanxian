package com.ssmLogin.defdata.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmLogin.defdata.entity.Gameplat;
import com.ssmLogin.defdata.entity.GameplatCriteria;
import com.ssmLogin.defdata.mapper.GameplatMapper;

@Repository
public class GameplatImpl {

	private @Autowired GameplatMapper mapper;
	private @Autowired GameplatCriteria criteria;

	public static GameplatImpl instance(){
		return SpringContextUtil.getBean(GameplatImpl.class);
	}

	public List<Gameplat> findList(){
		criteria.clear();
		return mapper.selectByExample(criteria);
	}

	public int addRecord(Gameplat gameplat){
		return mapper.insertSelective(gameplat);
	}

	public int upRecord(Gameplat gameplat){
		return mapper.updateByPrimaryKeySelective(gameplat);
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