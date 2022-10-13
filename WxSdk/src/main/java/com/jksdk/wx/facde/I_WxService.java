package com.jksdk.wx.facde;

import java.io.IOException;

import com.jksdk.wx.WxUser;
import com.ssmShare.entity.WxEntity;
import com.ssmShare.entity.WxPlatform;
import com.ssmShare.platform.DataConf;

public interface I_WxService {
	/**
	 * 微信web用户信息
	 * @param token
	 * @return
	 * @throws IOException
	 */
	public WxUser getUserWeb(String code,WxPlatform wx) throws IOException;
	/**
	 * 获取微信用户完整信息信息
	 * @param wx
	 */
	public WxUser getWxUserInfo(String token,String account) throws Exception;
	/**
	 * 微信统一订单
	 * @return
	 * @throws Exception
	 */
	public String uintOrder(DataConf dSource) throws Exception;
	
	/**
	 * 获取微信token
	 * @param wx
	 */
	public String getToken(WxEntity wx,String pid)throws Exception;
	
	/**
	 * 获取微信Ticket
	 * @param wx
	 */
	public void getJsTicket(WxEntity wx,String pid)throws Exception;

}
