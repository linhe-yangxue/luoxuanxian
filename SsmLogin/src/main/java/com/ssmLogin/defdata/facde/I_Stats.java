package com.ssmLogin.defdata.facde;

import java.util.Map;

import com.ssmShare.platform.DataConf;

/**
 * 统计数据
 * 
 * @author Only
 *
 */
public interface I_Stats {

	/**
	 * 获取统计数据
	 * 
	 * @param param
	 * @param dSource
	 * @return
	 */
	public Object statslog(Map<String, Object> param, DataConf dSource);

	/**
	 * 获取用户信息
	 * 
	 * @param param
	 * @param dSource
	 * @return
	 */
	public Object getUserinfo(Map<String, Object> param, DataConf dSource);

	/**
	 * 获取充值信息
	 * 
	 * @param param
	 * @param dSource
	 * @return
	 */
	public Object getPayInfo(Map<String, Object> param, DataConf dSource);

}
