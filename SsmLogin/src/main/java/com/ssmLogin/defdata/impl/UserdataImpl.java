package com.ssmLogin.defdata.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmLogin.defdata.entity.Userdata;
import com.ssmLogin.defdata.entity.UserdataCriteria;
import com.ssmLogin.defdata.mapper.UserdataMapper;

@Repository
public class UserdataImpl {

	private @Autowired UserdataMapper mapper;
	private @Autowired UserdataCriteria criteria;

	public static UserdataImpl instance(){
		return SpringContextUtil.getBean(UserdataImpl.class);
	}

	public Userdata find(Long guid,Integer zid){
		criteria.clear();
		criteria.createCriteria().andGuidEqualTo(guid)
		.andZidEqualTo(zid);
		List<Userdata>ls =  mapper.selectByExample(criteria);
		if(ls!=null && ls.size()>0){
			return ls.get(0);
		}
		return null;
	}

	public int addRecord(Userdata userdata){
		return mapper.insertSelective(userdata);
	}

	public int upRecord(Userdata userdata){
		return mapper.updateByPrimaryKeySelective(userdata);
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