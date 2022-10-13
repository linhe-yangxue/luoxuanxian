package com.ssmLogin.defdata.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ssmCore.tool.colligate.DateUtil;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmLogin.defdata.entity.Censuse;
import com.ssmLogin.defdata.entity.CensuseCriteria;
import com.ssmLogin.defdata.entity.CensuseKey;
import com.ssmLogin.defdata.mapper.CensuseMapper;

@Repository
public class CensuseImpl {

	private @Autowired CensuseMapper mapper;
	private @Autowired CensuseCriteria criteria;

	public static CensuseImpl instance(){
		return SpringContextUtil.getBean(CensuseImpl.class);
	}

	public List<Censuse> findList(String gid, String pid
			,Integer zid, String starTime, String endTime){
		criteria.clear();
		criteria.createCriteria().andGidEqualTo(gid)
		.andPidEqualTo(pid).andZidEqualTo(zid)
		.andLgdateBetween(DateUtil.strParesDate(starTime),DateUtil.strParesDate(endTime));
		return mapper.selectByExample(criteria);
	}

	public int addRecord(Censuse censuse){
		CensuseKey key = new CensuseKey();
		key.setGid(censuse.getGid());
		key.setPid(censuse.getPid());
		key.setZid(censuse.getZid());
		key.setLgdate(censuse.getLgdate());

		Censuse temp = mapper.selectByPrimaryKey(key);
		if(temp==null) {
			return mapper.insertSelective(censuse);
		} else {
			return mapper.updateByPrimaryKey(censuse);
		}
	}

	public int upRecord(Censuse censuse){
		return mapper.updateByPrimaryKeySelective(censuse);
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