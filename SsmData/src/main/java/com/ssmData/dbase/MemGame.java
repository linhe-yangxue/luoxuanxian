package com.ssmData.dbase;

import com.ssmCore.memcached.MemAccess;

import net.rubyeye.xmemcached.MemcachedClient;


public class MemGame implements GameConstant {


	public MemGame(MemcachedClient memclient, Integer expire) {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 获取登录性息
	 * 
	 * @param token
	 * @return
	 */
	public static Object getUser(String token) {
		return MemAccess.GetValue(token);
	}	
	
	public static boolean Update(String key, Object obj)
	{
		return MemAccess.Update(key, obj);
	}
	
	public static Object GetValue(String key){
		return MemAccess.GetValue(key);
	}
}
