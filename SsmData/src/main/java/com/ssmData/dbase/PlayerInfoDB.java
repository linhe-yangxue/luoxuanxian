package com.ssmData.dbase;

import java.util.ArrayList;
import java.util.Calendar;

import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Config;
import com.ssmShare.entity.UserBase;

@Service
@Scope("prototype")
public class PlayerInfoDB extends BaseDataSource {
	private static final long serialVersionUID = 1L;
	
	public boolean isNew = false;
	
	protected PlayerInfo getData() {
		try {
			m_object = (PlayerInfo) m_object;
			return (PlayerInfo) m_object;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	protected void setData(Object obj) {
		m_object = (PlayerInfo) obj;
	}

	/// 联通login server后，不再使用username查找
	public PlayerInfo loadByUsernameNotCreate(String username) {
		if (getData() != null && getData().username.equals(username)) {
			return getData();
		}
		m_object = BaseDaoImpl.getInstance().find(new Query(Criteria.where("username").is(username)), PlayerInfo.class);
		if (m_object == null) {
			return null;
		}
		m_uuid = getData()._id;
		return getData();
	}

	public PlayerInfo loadByUsername(String username) {
		PlayerInfo info = BaseDaoImpl.getInstance().find(new Query(Criteria.where("username").is(username)),
				PlayerInfo.class);
		if (null != info) {
			m_object = info;
		} else {
			CreateInstance();
			getData().username = username;
			
			BaseDaoImpl.getInstance().add(m_object);
		}
		m_uuid = getData()._id;
		return getData();
	}

	public PlayerInfo loadById(String _uid) {
		LoadObjectByUUID(_uid, "_id", PlayerInfo.class);
		return getData();
	}
	
	public PlayerInfo loadById(String uid, UserBase userbase) {
		LoadObjectByUUID_PlayerInfo(uid, "_id", PlayerInfo.class, userbase);
		return getData();
	}
	
	@Override
	protected boolean GetDataExsit() {
		return (null != m_object && getData() != null && getData()._id.equals(m_uuid));
	}

	@Override
	protected void CreateInstance() {
		m_object = new PlayerInfo();
		Config c = ConfigConstant.tConf;
		System.out.print(c);

		PlayerInfo tmp = (PlayerInfo) m_object;
		tmp.user_base = new UserBase();
		isNew = true;
		tmp.gold = c.getIniGold();
		tmp.diamond = c.getIniDiamond();
		tmp.team_lv = 1;
		tmp.team_exp = 0;
		tmp.vip_level = 0;
		tmp.acc_rmb = 0;
		tmp.team_current_fighting = 0;
		tmp.team_history_max_fighting = 0;
		//tmp.nickname = "";
		tmp.last_active_time = Calendar.getInstance().getTimeInMillis();
		tmp.month_card_expired = 0;
		tmp.lifetime_card_expired = 0;
		tmp.arena_rank_award = new ArrayList<Integer>();
		tmp.vip_award = new ArrayList<Integer>();
	}
	
	//=======仅角色创建使用！！！！！
	//TODO  代码有重复，因为load和create连在一起了，要重构掉=========仅角色创建使用！！！！！
	protected void CreateInstance_playerinfo(UserBase userbase) {
		m_object = new PlayerInfo();
		Config c = ConfigConstant.tConf;
		//System.out.print(c);

		PlayerInfo tmp = (PlayerInfo) m_object;
		tmp._id = userbase.getUid();
		tmp.user_base = userbase;
		//if(userbase.getNickname()==null 
		//		|| userbase.getuImg()==null)
		isNew = true;
		//tmp.guid = userbase.getGuid();
		tmp.gold = c.getIniGold();
		tmp.diamond = c.getIniDiamond();
		tmp.team_lv = 1;
		tmp.team_exp = 0;
		tmp.vip_level = 0;
		tmp.acc_rmb = 0;
		tmp.team_current_fighting = 0;
		tmp.team_history_max_fighting = 0;
		tmp.last_active_time = Calendar.getInstance().getTimeInMillis();
		tmp.month_card_expired = 0;
		tmp.lifetime_card_expired = 0;
		tmp.arena_rank_award = new ArrayList<Integer>();
		tmp.vip_award = new ArrayList<Integer>();
	}
}
