package com.ssmLogin.defdata.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmLogin.defdata.entity.Activecode;
import com.ssmLogin.defdata.entity.ActivecodeCriteria;
import com.ssmLogin.defdata.mapper.ActivecodeMapper;

@Repository
public class ActivecodeImpl {

	private @Autowired ActivecodeMapper mapper;
	private @Autowired ActivecodeCriteria criteria;

	public static ActivecodeImpl instance(){
		return SpringContextUtil.getBean(ActivecodeImpl.class);
	}

	public List<Activecode> findList(String gid,String pid,Integer sign){
		criteria.clear();
		criteria.createCriteria().andGidEqualTo(gid)
				.andPidEqualTo(pid).andStatusEqualTo(sign).andUidIsNull();
		return mapper.selectByExample(criteria);
	}

	public int addRecord(Activecode activecode){
		return mapper.insertSelective(activecode);
	}

	public int upRecord(Activecode activecode){
		return mapper.updateByPrimaryKeySelective(activecode);
	}

	public int delAll(String tempid){
		criteria.clear();
		criteria.createCriteria().andStatusEqualTo(Integer.parseInt(tempid));
		return mapper.deleteByExample(criteria);
	}

	public int getCount(String uid,Integer zid,Long status){
		criteria.clear();
		criteria.createCriteria().andUidEqualTo(uid)
				.andZidEqualTo(zid)
				.andStatusEqualTo(status.intValue());
		return mapper.countByExample(criteria);
	}

	public Activecode find(String code) {
		return mapper.selectByPrimaryKey(code);
	}

}