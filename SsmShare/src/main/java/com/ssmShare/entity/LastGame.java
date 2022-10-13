package com.ssmShare.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class LastGame implements Serializable{
	private static final long serialVersionUID = 1L;
	/**游戏id索引*/
	private Integer  gidIndex;
	/** 最后登录的区 */
	private Integer lastZid;
	/** 登录次数*/
	private Integer logTimes;
	/** 登录过的区 */
	private Set<Integer> lgZid; 
	
	public LastGame(){}
	
	public LastGame(Integer zid,int index) {
		setLastZid(zid);
		setLogTimes(1);
		setGidIndex(index);
		setLgZid(new HashSet<Integer>());
		this.lgZid.add(zid);
	}

	public Integer getLastZid() {
		return lastZid;
	}
	public void setLastZid(Integer lastZid) {
		this.lastZid = lastZid;
	}
	public Integer getLogTimes() {
		return logTimes;
	}
	public void setLogTimes(Integer logTimes) {
		this.logTimes = logTimes;
	}

	public Set<Integer> getLgZid() {
		return lgZid;
	}
	public void setLgZid(Set<Integer> lgZid) {
		this.lgZid = lgZid;
	}

	public void modfiy(String gid2, Integer lastzid) {
		setLastZid(lastzid);
		this.lgZid.add(lastzid);
		++logTimes;
	}
	public Integer getGidIndex() {
		return gidIndex;
	}
	public void setGidIndex(Integer gidIndex) {
		this.gidIndex = gidIndex;
	}

}
