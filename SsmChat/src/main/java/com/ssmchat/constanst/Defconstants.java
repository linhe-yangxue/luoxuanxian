package com.ssmchat.constanst;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.ssmCore.jetty.EventSocket;
import com.ssmShare.entity.ChartUser;

@Service
public class Defconstants {

	/**
	 * 在线用户 Integer 通道hashCode SockeChannel 用户连接通道
	 */
	public static Map<String, EventSocket> online;

	public static Map<String, Set<String>> untion; // 公会对应id
	/**
	 * 区索引
	 */
	public static Map<String, Set<String>> gidToGuid;
	/**
	 * 区索引
	 */
	public static Map<String, Set<String>> zidToGuid;

	@PostConstruct
	public void init() {
		online = new ConcurrentHashMap<String, EventSocket>();
		untion = new ConcurrentHashMap<String, Set<String>>();
		gidToGuid = new ConcurrentHashMap<String, Set<String>>();
		zidToGuid = new ConcurrentHashMap<String, Set<String>>();
	}

	public static void addChannle(EventSocket event) {
		ChartUser cUser = (ChartUser) event.getObj();
		/** 创建在线用户 **/
		online.put(cUser.getGid() + "_" + cUser.getUid(), event);// 游戏id + 用户id
		
		if (cUser.getGuildId() != null) {
			Set<String> uids = untion.get(cUser.getGid() + "_" + cUser.getGuildId());
			if (uids == null)
				uids = new HashSet<String>();
			/** 创建工会索引 **/
			uids.add(cUser.getUid());
			untion.put(cUser.getGid() + "_" + cUser.getGuildId(), uids);
		}
	
		Set<String> guids = gidToGuid.get(cUser.getGid());
		if(guids==null)
			 guids = new HashSet<String>();
		guids.add(cUser.getUid());
		gidToGuid.put(cUser.getGid(), guids);
		
		Set<String> zuids = zidToGuid.get(cUser.getGid() + "_" + cUser.getZid());
		if (zuids == null)
			zuids = new HashSet<String>();

		zuids.add(cUser.getUid());
		zidToGuid.put(cUser.getGid() + "_" + cUser.getZid(), zuids); // 游戏区位
	}

	public static void delChannle(Object obj) {
		ChartUser cUser = (ChartUser) obj;
		online.remove(cUser.getGid() + "_" + cUser.getUid()); // 删除一个用户

		if (cUser.getGuildId() != null) {
			Set<String> uids = untion.get(cUser.getGid() + "_" + cUser.getGuildId());
			uids.remove(cUser.getUid());
		}
	}
}
