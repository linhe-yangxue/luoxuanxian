package com.ssmShare.facde;

import com.ssmCore.mongo.PageList;

public interface I_StatsPress {

	/**
	 * 获取登陆log
	 * 
	 * @param gid
	 *            游戏ID
	 * @param pid
	 *            平台ID
	 * @param zid
	 *            区ID -1： 为全部服务器
	 * @param startdata
	 *            开始日期 2015-05-06
	 * @param enddata
	 *            结束日期
	 * @param page
	 *            页码
	 * @return
	 * @throws Exception
	 */
	public PageList getLoginLog(String gid, String pid, int zid, String startdate, String enddate, int pagesize, int page) throws Exception;

	/**
	 * 创建角色日志
	 * 
	 * @param gid
	 *            游戏ID
	 * @param pid
	 *            平台ID
	 * @param zid
	 *            区ID -1： 为全部服务器
	 * @param startdata
	 *            开始日期 2015-05-06
	 * @param enddata
	 *            结束日期
	 * @param page
	 *            页码
	 * @return
	 * @throws Exception
	 */
	public PageList getCreateRoleLog(String gid, String pid, int zid, String startdate, String enddate, int pagesize, int page) throws Exception;

	/**
	 * 获取用户信息
	 * 
	 * @param gid
	 *            游戏ID
	 * @param pid
	 *            平台ID
	 * 
	 * @param uuid
	 *            角色ID
	 * @return
	 * @throws Exception
	 */
	public Object getUserInfo(String gid, String pid, String uuid) throws Exception;

	/**
	 * 
	 * @param gid
	 *            游戏ID
	 * @param pid
	 *            平台ID
	 * @param zid
	 *            区ID
	 * @param account
	 *            平台用户ID
	 * @return
	 * @throws Exception
	 */
	public Object getUserInfo(String gid, String pid, String zid, String account) throws Exception;

}
