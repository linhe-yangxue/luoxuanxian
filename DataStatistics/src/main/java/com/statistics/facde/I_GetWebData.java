package com.statistics.facde;

import com.ssmCore.constants.ReInfo;

public interface I_GetWebData {

	/**
	 * 获取统计数据
	 * 
	 * @param db_name
	 * @return
	 */
	public ReInfo handleGetWebData(String db_name);
}
