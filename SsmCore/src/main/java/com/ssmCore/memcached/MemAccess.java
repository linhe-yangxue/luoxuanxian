package com.ssmCore.memcached;

import java.io.IOException;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.rubyeye.xmemcached.MemcachedClient;

@Service
public class MemAccess {

	private static final Logger log = LoggerFactory.getLogger(MemAccess.class);
	private static int expire = 0;
	private static MemcachedClient cache;

	public MemAccess(MemcachedClient memclient, Integer expire) {
		MemAccess.cache = memclient;
		if (expire != null && expire > 0)
			MemAccess.expire = expire;
	}

	public static boolean Add(String key, Object obj) {
		try {
			return cache.add(key, expire, obj);
		} catch (Exception e) {
			log.error("添加数据异常！", e);
		}
		return false;
	}

	public static boolean Add(String key, int expire, Object obj) {
		try {
			return cache.add(key, expire, obj); // 添加
		} catch (Exception e) {
			log.error("添加数据异常！", e);
		}
		return false;
	}

	public static boolean Update(String key, Object obj) {
		try {
			return cache.set(key, expire, obj);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("更新数据异常！", e);
		}
		return false;
	}

	public static boolean Update(String key, int expire, Object obj) {
		try {
			return cache.set(key, expire, obj);
		} catch (Exception e) {
			log.error("更新数据异常！", e);
		}
		return false;
	}

	public static boolean Delete(String key) {
		try {
			return cache.delete(key);
		} catch (Exception e) {
			log.error("删除数据异常！", e);
		}
		return false;
	}

	public static boolean Touch(String key, int time) {
		try {
			return cache.touch(key, time);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static Object GetValue(String key) {
		try {
			return cache.get(key);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取数异常！", e);
		}
		return null;
	}

	@PreDestroy
	public void destory() throws IOException {
		MemAccess.cache.shutdown();
	}

}
