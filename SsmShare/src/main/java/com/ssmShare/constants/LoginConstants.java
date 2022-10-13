package com.ssmShare.constants;

import java.util.List;
import java.util.Map;

import com.ssmShare.entity.Docking;
import com.ssmShare.entity.Notice;
import com.ssmShare.entity.ServerList;
import com.ssmShare.entity.WxPlatform;
import com.ssmShare.order.ShopItem;

public class LoginConstants {
	
	//=======SDK实现   前缀与后缀===========
	public static final String SDK = "Sdk";      
	public static final String IMPL = "Impl";
	
	public static final int FILE_LOAD = 0;
	public static final int FILE_ADD = 1;
	public static final int FILE_CHANGE = 2;
	
	//======内存加载   平台对接信息===========//
	public static Map<String, WxPlatform> wxPlat;
	//key=访问连接连接 val=对接信息
	public static Map<String,Docking> docking;
	//游戏商品 key=gid   val=商品信息  pid
	public static Map<String,Map<Integer,ShopItem>> items;
	//游戏服务器列表 key=gid   val=公告内容
	public static Map<String,List<ServerList>> Serlist;
	//游戏公告 key=gid   val=公告内容     pid
	public static Map<String,Map<String,Notice>> notice;
}
