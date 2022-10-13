package com.ssmLogin.springmvc.facde;

import com.ssmShare.entity.Docking;

public interface I_PlatDock {
	/**
	 * 获取游戏 平台对接文档
	 * 
	 * @param gid
	 * @param pid
	 * @return
	 */
	public Object getPlatDock(String gid, String pid) throws Exception;

	/**
	 * 获取游戏平台对接列表
	 * 
	 * @param gid
	 * @return
	 */
	public Object getPlatDockList(String gid);

	/**
	 * 添加平台对接文档
	 * 
	 * @param dock
	 * @return
	 */
	public Object addPlatDock(String gid, Docking dock);

	/**
	 * 编辑平台对接文档
	 * 
	 * @param dock
	 * @return
	 */
	public Object editPlatDock(String gid, Docking dock);

	/**
	 * 删除平台对接文档
	 * 
	 * @param gid
	 * @param pid
	 * @return
	 */
	public Object delPlatDock(String gid, String pid);

	/**
	 * 修改关注状态
	 * 
	 * @param gid
	 * @param gid
	 * @param value
	 * @return
	 */
	public Object changeFollow(String gid, String pid, int value);

	/**
	 * 修改分享状态
	 * 
	 * @param gid
	 * @param gid
	 * @param value
	 * @return
	 */
	public Object changeShare(String gid, String pid, int value);

	public void Destory();

}
