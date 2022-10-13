package com.ssmLogin.defdata.facde;

import java.util.Map;

import com.ssmShare.facde.I_Platform;
import com.ssmShare.platform.DataConf;

public interface I_MsgPress {

	/**
	 * 用户信息处理
	 * 
	 * @param platform
	 * @param ubase
	 *            用户基本信息
	 * @param gid
	 * @param pInfo
	 * @param pInfo
	 *            平台对接文件
	 * @return 返回 用户登录token
	 */
	public String pressUser(DataConf dSource, I_Platform platform);

	/**
	 * 连接处理
	 * @param param
	 * @param dSource
	 * @return
	 */
	public String pressUrl(Map<String, Object> param, DataConf dSource);
	
	/**
	 * token用户存储
	 * @param param
	 * @param dSource
	 */
	public void pressStoreUser(Map<String, Object> param, DataConf dSource);
}
