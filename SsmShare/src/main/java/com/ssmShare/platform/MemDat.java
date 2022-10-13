package com.ssmShare.platform;

import java.util.Map;

import com.ssmCore.memcached.MemAccess;
import com.ssmShare.entity.Docking;
import com.ssmShare.entity.Notice;
import com.ssmShare.entity.ServerList;
import com.ssmShare.order.ActiveTemp;
import com.ssmShare.order.ShopItem;

public class MemDat {

	private static final String WXTOKEN = "wxtoken_time_have";
	private static final String WXTICKET = "wxticket_time_";
	
	public final static String LOGIN_URL = "_loginurl";
	public final static String GAME_URL  = "_gameurl";
	public final static String CHART_URL = "_webChat";
	public final static String ORDER_URL = "_orderurl";
	
	public final static String LOAD = "_loadconf";
	public final static String DOCKING = "_dock_";
	public final static String SHOPS = "_shops";
	public final static String NOTICE = "_notice_";
	public final static String SERVERLIST = "_serList";
	public final static String MKEY = "_mkey";
	public final static String WX = "_wxdoc_";
	public final static String ACTIVECODE="active_code_";
	
	/**
	 * 判断配置文件是否加载
	 * 
	 * @return
	 */
	public static boolean isExit() {
		Object obj = MemAccess.GetValue(LOAD);
		if (obj == null)
			return true;
		return false;
	}

	/**
	 * 获取guid
	 * 
	 * @param key
	 * @return
	 */
	public static ReturnMag getServerMsg(String key) {
		return (ReturnMag) MemAccess.GetValue(key + MKEY);
	}

	public static void setServerMsg(String key, int time, ReturnMag rtn) {
		MemAccess.Update(key + MKEY, time, rtn);
	}

	public static void delServerMsg(String key) {
		MemAccess.Delete(key + MKEY);
	}

	/**
	 * 设置配置文件加载开关
	 */
	public static void setLoad() {
		MemAccess.Update(LOAD, LOAD);
	}

	/**
	 * 获取平台对接文档
	 * 
	 * @param gid
	 * @param pid
	 * @return
	 */
	public static Docking getDocking(String gid, String pid) {
		return (Docking) MemAccess.GetValue(gid + DOCKING + pid);
	}

	public static void setDocking(String gid, String pid, Docking doc) {
		MemAccess.Update(gid + DOCKING + pid, doc);
	}

	/**
	 * 获取商品信息 月卡，终身卡，等等 在login中配置
	 * 
	 * @param gid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<Integer,ShopItem> getShops(String gid) {
		return (Map<Integer,ShopItem>) MemAccess.GetValue(gid + SHOPS);
	}

	public static void setShops(String gid, Map<Integer,ShopItem> lists) {
		MemAccess.Update(gid + SHOPS, lists);
	}

	/**
	 * 获取平台公告；
	 * 
	 * @param gid
	 * @return
	 */
	public static Notice getNotice(String gid, String pid) {
		return (Notice) MemAccess.GetValue(gid + NOTICE + pid);
	}

	public static void setNotice(String gid, String pid, String notice) {
		MemAccess.Update(gid + NOTICE + pid, notice);
	}

	/**
	 * 获取游戏服务器列表
	 * 
	 * @param gid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<Integer,ServerList> getSvList(String gid) {
		return (Map<Integer,ServerList>) MemAccess.GetValue(gid + SERVERLIST);
	}

	public static void setSvList(String gid, Map<Integer,ServerList> svs) {
		MemAccess.Update(gid + SERVERLIST, svs);
	}

	/** 聊天服务器地址 **/
	public static void setChatUrl(String gid, String chatUrl) {
		MemAccess.Update(gid + CHART_URL, chatUrl);
	}

	public static String getChatUrl(String gid) {
		return (String) MemAccess.GetValue(gid + CHART_URL);
	}

	/**
	 * 游戏连接地址
	 * @param gid
	 * @param gameUrl
	 */
	public static void setGameUrl(String gid, String gameUrl) {
		MemAccess.Update(gid + GAME_URL , gameUrl);
	}
	
	public static String getGameUrl(String gid) {
		return (String) MemAccess.GetValue(gid + GAME_URL);
	}
	
	/**
	 * 游戏订单地址
	 * @param gid
	 * @param gameUrl
	 */
	public static void setOrderUrl(String gid, String orderUrl) {
		MemAccess.Update(gid + ORDER_URL , orderUrl);
	}
	public static String getOrderUrl(String gid) {
		return (String) MemAccess.GetValue(gid + ORDER_URL);
	}
	
	
	/**
	 * 游戏登录服务器地址
	 * @param gid
	 * @param gameUrl
	 */
	public static void setLoginUrl(String gid, String lgUrl) {
		MemAccess.Update(gid + LOGIN_URL , lgUrl);
	}
	public static String getLoginUrl(String gid) {
		return (String) MemAccess.GetValue(gid + LOGIN_URL);
	}
	/**
	 * 微信tonken
	 * @param pid
	 * @return
	 */
	public static String getWxToken(String pid) {
		return (String) MemAccess.GetValue(WXTOKEN + pid);
	}
	public static void setWxToken(String pid,String access_token, int intValue) {
		MemAccess.Update(WXTOKEN + pid,intValue, access_token);
	}
	/**
	 * 微信ticket
	 * @param pid
	 * @return
	 */
	public static String getWxTicket(String pid) {
		return (String) MemAccess.GetValue(WXTICKET+ pid);
	}
	public static void setWxTicket(String pid,String ticket, int intValue) {
		MemAccess.Update(WXTICKET+ pid,intValue, ticket);
	}

	/**
	 * 存储服务器地址
	 * @param gid
	 * @param zid
	 * @param dress
	 */
	public static void setSvMap(String gid, int zid, String dress) {
		MemAccess.Update(gid + "_" + zid, dress);
	}
	/**
	 * 获取服务器地址
	 * @param gid
	 * @param zid
	 * @return
	 */
	public static String getSvMap(String gid, int zid) {
		return (String) MemAccess.GetValue(gid + "_" + zid);
	}

	public static void setActiveCode(String get_id, ActiveTemp act) {
		 String key =  ACTIVECODE + get_id;
		 MemAccess.Update(key, act);
	}
	
	public static ActiveTemp getActiveCode(String get_id) {
		 String key =  ACTIVECODE + get_id;
		return (ActiveTemp) MemAccess.GetValue(key);
	}
}
