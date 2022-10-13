package com.ssmShare.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Uaction implements Serializable{

	private static final long serialVersionUID = 1L;
	/** 最后登录的游戏 */
	private String lastGid;
	/** 最后登录时间 */
	private Date   lastLogin;
	/** key:游戏id  游戏登录过的区*/
	private Map<String,LastGame> gTozid; //游戏记录 
	
	public String getLastGid() {
		return lastGid;
	}
	public void setLastGid(String lastGid) {
		this.lastGid = lastGid;
	}

	public void setLastZid(Integer lastZid,String gid) {
		this.setLastGid(gid);
		if(this.gTozid==null){
			gTozid = new HashMap<String,LastGame>();
			gTozid.put(gid, new LastGame(lastZid,1));
		}else{
			LastGame gm = gTozid.get(gid);
			if(gm!=null){
				gm.modfiy(gid,lastZid);
			}else{
				gTozid.put(gid, new LastGame(lastZid,gTozid.size()));
			}
		}
	}
	/**
	 * 获取登录过的区
	 * @param gid
	 * @return
	 */
	public Set<Integer> getlgZid(String gid){
		LastGame game = this.gTozid.get(gid);
		if(game!=null)
			return game.getLgZid();
		return null;
	}
	
	/**
	 * 获取游戏的索引
	 * @param gid
	 * @return
	 */
	public Integer getGameIndex(String gid){
		LastGame game = this.gTozid.get(gid);
		if(game!=null)
			return game.getGidIndex();
		return null;
	}
	/**
	 * 最后登录过的区
	 * @return
	 */
	public Integer getLastZid(String gid) {
		LastGame game = this.gTozid.get(gid);
		if(game!=null)
			return game.getLastZid();
		return null;
	}
	
	public Date getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
}
