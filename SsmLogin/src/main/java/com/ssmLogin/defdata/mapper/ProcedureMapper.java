package com.ssmLogin.defdata.mapper;

import com.ssmLogin.defdata.entity.Censuse;

import java.util.Map;

public interface ProcedureMapper {
	/**
	 * 平台综合信息查询
	 * @param parms
	 * @return
	 */
	void selectProedure(Map<String, Object> parms);
	
	Censuse dayCensus(Map<String, Object> parms);
}
