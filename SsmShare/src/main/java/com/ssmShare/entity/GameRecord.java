package com.ssmShare.entity;

import java.util.Date;
import java.util.Set;

public class GameRecord {

	/** 登录过的区 */
	private Set<Integer> zids;
	/** 登录次数 */
	private int    times = 1; 
	/** 最后登录 时间 */
	public Date    lastTime; 
	/** 游戏充值金额 */
	public float   money = 0;
	
	public Set<Integer> getZids() {
		return zids;
	}
	public void setZids(Set<Integer> zids) {
		this.zids = zids;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public Date getLastTime() {
		return lastTime;
	}
	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}
	public float getMoney() {
		return money;
	}
	public void setMoney(float money) {
		this.money = money;
	}	
}
