package com.ssmData.manager;

import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmData.dbase.PlayerInfo;

@Service
@Scope("prototype") //数据源控制管理器
public class DataSource {
	
	public PlayerInfo player;

	/**
	 * 公共：基于token加载数据
	 * @param token
	 */
	public void loadByToken(String token){
		BaseDaoImpl.getInstance().find(new Query(Criteria.where("token").is(token)), PlayerInfo.class);
	}

	/**
	 * 公共：加载所有数据
	 * @param uid
	 */
	public void load(String uid){

		// 加载用户信息
		PlayerInfo info = this.loadPlayer(uid);

		// 用户不存在，不执行具体模块数据的加载
		if(info == null){
			return;
		}

		// 执行具体数据的加载

	}

	/**
	 * 用户：创建新用户
	 */
	public void createPlayer(){
		player = new PlayerInfo();
	}

	/**
	 * 用户：加载用户信息
	 */
	public PlayerInfo loadPlayer(String uid){

		PlayerInfo info = BaseDaoImpl.getInstance().find(new Query(Criteria.where("username").is(uid)), PlayerInfo.class);
		if(info != null){
			this.player = info;
		}
		return info;
	}

	/**
	 * 用户：保存用户数据
	 */
	public void save(){
		BaseDaoImpl.getInstance().add(player);
	}

	/**
	 * 用户：重置token
	 */
	public void updateSession(){
		//this.player.sessionId = RandomStringUtils.randomAlphanumeric(16);
		BaseDaoImpl.getInstance().saveOrUpdate(this.player);
	}
}
