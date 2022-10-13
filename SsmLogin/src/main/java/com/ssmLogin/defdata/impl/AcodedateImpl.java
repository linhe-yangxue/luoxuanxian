package com.ssmLogin.defdata.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmLogin.defdata.entity.Acodedate;
import com.ssmLogin.defdata.entity.AcodedateCriteria;
import com.ssmLogin.defdata.entity.AcodedateKey;
import com.ssmLogin.defdata.mapper.AcodedateMapper;

@Repository
public class AcodedateImpl {

	private @Autowired AcodedateMapper mapper;
	private @Autowired AcodedateCriteria criteria;

	public static AcodedateImpl instance(){
		return SpringContextUtil.getBean(AcodedateImpl.class);
	}

	public List<Acodedate> findList(){
		criteria.clear();
		return mapper.selectByExample(criteria);
	}
	
	public Acodedate find(String gid,String pid,String iden){
		AcodedateKey key = new AcodedateKey();
		key.setGid(gid);
		key.setPid(pid);
		key.setSign(iden); // 模版id
		return mapper.selectByPrimaryKey(key);
	}

	public int addRecord(Acodedate acodedate){
		return mapper.insertSelective(acodedate);
	}

	public int upRecord(Acodedate acodedate){
		return mapper.updateByPrimaryKeySelective(acodedate);
	}

	public int delAll(String tempid){
		criteria.clear();
		criteria.createCriteria().andSignEqualTo(tempid);
		return mapper.deleteByExample(criteria);
	}

	public int getCount(){
		criteria.clear();
		return mapper.countByExample(criteria);
	}

}