package com.ssmLogin.springmvc.facde;

import com.ssmShare.platform.PlatformInfo;

/**
 * 平台游戏操作
 *
 */
public interface I_PlatGame {
	/**
	 * 获取默认资源地址
	 * @param gid
	 * @return
	 */
	public Object getGameLogin(String gid);
	/**
	 * 获取平台游戏信息
	 * @param name
	 * @return
	 */
	public Object getPlatGame(String name);
	
	/**
	 * 获取平台游戏列表
	 * @param plat
	 * @return
	 */
	public Object getPlatGameList();
	
	/**
	 * 平台游戏添加
	 * @param plat
	 * @return
	 */
	public Object addPlatGame(PlatformInfo plat);
	
	/**
	 * 平台游戏编辑
	 * @param plat
	 * @return
	 */
	public Object editPlatGame(PlatformInfo plat);
	
	/**
	 * 平台游戏删除
	 * @param pid  平台id
	 * @return
	 */
	public Object delPlatGame(String pid);
	
	
	public void Destory();
}
