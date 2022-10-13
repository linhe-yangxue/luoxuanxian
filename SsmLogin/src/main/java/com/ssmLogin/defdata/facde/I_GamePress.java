package com.ssmLogin.defdata.facde;

import java.util.List;

import com.ssmCore.mongo.PageList;
import com.ssmShare.entity.Docking;
import com.ssmShare.entity.ServerList;
import com.ssmShare.order.ShopItem;
import com.ssmShare.platform.PlatformInfo;
import com.ssmShare.platform.UserInfo;

public interface I_GamePress {

	/**
	 * 分页获取游戏列表
	 * 
	 * @param page
	 *            页码
	 * @return
	 */
	public List<PlatformInfo> getGamels(int page);

	/**
	 * 获取游戏列表(所有)
	 * 
	 * @return
	 */
	public List<PlatformInfo> getGamels_all();

	/**
	 * 获取游戏对接平台列表
	 * 
	 * @param gameId
	 * @return
	 */
	public List<Docking> getDockingByGameId(String gameId);

	/**
	 * 修改登陆公告
	 * 
	 * @param gameId
	 * @param pid
	 * @param content
	 * @return
	 */
	public boolean modifyLoginNotice(String gameId, String pid, String content);

	/**
	 * 更新聊天服务器地址
	 * 
	 * @param gid
	 * @param url
	 * @return
	 */
	public boolean updateChatUrl(String gid, String url);

	/**
	 * 根据游戏Id获取详细信息
	 * 
	 * @param gameId
	 * @return
	 */
	public PlatformInfo getGameById(String gameId);

	/**
	 * 获取游戏服务器列表
	 * 
	 * @param gameId
	 * @return
	 */
	public ServerList[] getServiceByGameId(String gameId);

	/**
	 * 获取游戏服务器列表
	 * 
	 * @param gameId
	 * @return
	 */
	public ServerList getServiceBySerId(String gameId, int serId);

	/**
	 * 删除游戏服务器列表
	 * 
	 * @param gameId
	 * @return
	 */
	public boolean delServiceBySerId(String gameId, int serId);

	/**
	 * 修改游戏服务器信息
	 * 
	 * @param gameId
	 * @param ser
	 * @return
	 */
	public boolean modifySerInfo(String gameId, ServerList ser);

	/**
	 * 添加游戏服务器信息
	 * 
	 * @param gameId
	 * @param ser
	 * @return
	 */
	public boolean addSerInfo(String gameId, ServerList ser);

	/**
	 * 添加商品
	 * 
	 * @param gameId
	 * @param item
	 * @return
	 */
	public boolean addShopItem(String gameId, ShopItem item);

	/**
	 * 修改商品
	 * 
	 * @param gameId
	 * @param item
	 * @return
	 */
	public boolean modifyShopItem(String gameId, ShopItem item);

	/**
	 * 删除商品
	 * 
	 * @param gameId
	 * @param item
	 * @return
	 */
	public boolean delShopItem(String gameId, int itemId);

	/**
	 * 查询所有商品
	 * 
	 * @param gameId
	 * @return
	 */
	public List<ShopItem> getShopItemls(String gameId);

	/**
	 * 查询玩家信息
	 * 
	 * @param plat
	 * @param account
	 * @param nikeName
	 * @return
	 */
	public PageList getUserInfo(String gameId, int zid, String plat, String account, String nikeName, int pageNo, int pageSize);

	/**
	 * 查询玩家列表
	 * 
	 * @param idls
	 * @return
	 */
	public List<UserInfo> getUserByIdls(List<Integer> idls);

	/**
	 * 查询所有用户
	 * @param gameId
	 * @param zid
	 * @return
	 */
	public List<UserInfo> getGidZidAllUser(String gameId, int zid);
}
