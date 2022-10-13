package com.ssmLogin.defdata.facde;

import java.util.Map;

import com.ssmCore.constants.ReInfo;
import com.ssmShare.platform.DataConf;

public interface I_Login {
	/**
	 * 平台登录
	 * @param request   发过来的参数
	 * @param pid       平台id
	 * @return
	 */
	public ReInfo Login(Map<String,Object> param,DataConf dSource) throws Exception;
	
	/**
	 * 平台登录
	 * @param param
	 * @param dSource
	 * @return
	 * @throws Exception
	 */
	public ReInfo pLogin(Map<String,Object> param,DataConf dSource) throws Exception;
	
	/**
	 *通过tonken获取用户
	 * @param param
	 * @param dSource
	 * @return
	 * @throws Exception
	 */
	public ReInfo tokenUser(Map<String,Object> param,DataConf dSource)throws Exception;
}
