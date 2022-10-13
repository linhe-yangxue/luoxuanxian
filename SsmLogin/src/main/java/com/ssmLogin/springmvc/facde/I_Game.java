package com.ssmLogin.springmvc.facde;

import java.util.List;

import com.ssmCore.constants.ReInfo;
import com.ssmShare.entity.ServerList;
import com.ssmShare.order.ShopItem;
import com.ssmShare.platform.UserInfo;

public interface I_Game {

	/**
	 * 获取游戏列表
	 * 
	 * @param page
	 *            页数
	 * @return
	 */
	public ReInfo getGamels(int page);

	/**
	 * 获取游戏列表
	 * 
	 * @return
	 */
	public ReInfo getGamels();

	/**
	 * 获取游戏对接平台列表
	 * 
	 * @param gameId
	 * @return
	 */
	public ReInfo getDockingByGameId(String gameId);

	/**
	 * 修改登陆公告
	 * 
	 * @param gameId
	 * @param pid
	 * @param content
	 * @return
	 */
	public ReInfo modifyLoginNotice(String gameId, String pid, String content);

	/**
	 * 根据Id获取详细信息
	 * 
	 * @return
	 */
	public ReInfo getGameById(String id);

	/**
	 * 根据Id获取详细信息
	 * 
	 * @return
	 */
	public ReInfo getServiceByGameId(String id);

	/**
	 * 修改游戏服务器信息
	 * 
	 * @param gameId
	 *            游戏Id
	 * @param ser
	 *            服务器信息
	 * @return
	 */
	public ReInfo modifySerInfo(String gameId, ServerList ser);

	/**
	 * 添加游戏服务器信息
	 * 
	 * @param gameId
	 *            游戏Id
	 * @param ser
	 *            服务器信息
	 * @return
	 */
	public ReInfo addSerInfo(String gameId, ServerList ser);

	/**
	 * 删除游戏服务器信息
	 * 
	 * @param gameId
	 *            游戏Id
	 * @param ser
	 *            服务器信息
	 * @return
	 */
	public ReInfo delSerInfo(String gameId, int serId);

	/**
	 * 更新聊天服务器地址
	 * 
	 * @param gid
	 * @param url
	 * @return
	 */
	public ReInfo updateChatUrl(String gid, String url);

	/**
	 * 添加商品
	 * 
	 * @param gameId
	 * @param item
	 * @return
	 */
	public ReInfo addShopItem(String gameId, ShopItem item);

	/**
	 * 修改商品
	 * 
	 * @param gameId
	 * @param item
	 * @return
	 */
	public ReInfo modifyShopItem(String gameId, ShopItem item);

	/**
	 * 删除商品
	 * 
	 * @param gameId
	 * @param item
	 * @return
	 */
	public ReInfo delShopItem(String gameId, int itemId);

	/**
	 * 查询所有商品
	 * 
	 * @param gameId
	 * @return
	 */
	public ReInfo getShopItemls(String gameId);

	/**
	 * 获取玩家信息
	 * 
	 * @param plat
	 * @param account
	 * @param nikeName
	 * @return
	 */
	public ReInfo getUserInfo(String gameId, int zid, String plat, String account, String nikeName, int pageNum);

	/**
	 * 查询玩家列表
	 * 
	 * @param idls
	 * @return
	 */
	public ReInfo getUserByIdls(List<Integer> idls);

	/**
	 * 获取一个区所有用户
	 * @param gameId
	 * @param zid
	 * @return
	 */
	public List<UserInfo> getGidZidAllUser(String gameId, int zid);

	/**
	 * 踢掉所有区里的用户
	 * @param gameId
	 * @param zid
	 * @return
	 */
	public void repairToRollOff(String gameId,int zid) ;

}
