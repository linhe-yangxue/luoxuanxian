package com.ssmLogin.defdata.impl;

import java.util.HashMap;
import java.util.Map;

import com.ssmLogin.defdata.entity.Censuse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmLogin.defdata.mapper.ProcedureMapper;

@Repository
public class CensusPlatImpl {

	private @Autowired ProcedureMapper mapper;
	
	public static CensusPlatImpl getInstance(){
		return SpringContextUtil.getBean(CensusPlatImpl.class);
	}
	
	public Map<String,Object> censusplatmsg(String gid,String pid,Integer zid){
		Map<String,Object>param = new HashMap<>();
		param.put("gid", gid);
		param.put("pid", pid);
		param.put("zid", zid);
		param.put("upays", 0);
		mapper.selectProedure(param);
 		return param;
	}
	
	public Censuse dayCensus(String gid, String pid, Integer zid, String start, String end){
		Map<String,Object>param = new HashMap<>();
		param.put("gid", gid);
		param.put("pid", pid);
//		param.put("zid", zid);
		param.put("startTime", start);
		param.put("endTime",end);
		return mapper.dayCensus(param);
//		return param;
	}
}
