package com.ssmGame.module.activity;

import java.util.ArrayList;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Investvip;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerRoleInvestDB;
import com.ssmData.dbase.PlayerRoleInvestInfo;
import com.ssmGame.defdata.msg.activity.ActivityMsg;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
import com.ssmGame.module.player.PlayerImpl;
import com.ssmGame.util.TimeUtils;

@Service
@Scope("prototype")
public class RoleInvestImpl {
	private static final Logger log = LoggerFactory.getLogger(RoleInvestImpl.class);
	
    public final static RoleInvestImpl getInstance(){
        return SpringContextUtil.getBean(RoleInvestImpl.class);
	}
    
	@Autowired
	PlayerRoleInvestDB m_act_db;
	PlayerRoleInvestInfo m_act;
	
    @Autowired
    PlayerInfoDB m_player_db;
    PlayerInfo m_player = null;
    
    @Autowired
    PlayerBagDB m_bag_db;
    PlayerBagInfo m_bag = null;
    
    public void destroy() {
    	m_bag_db = null;
    	m_bag = null;
    	m_player_db = null;
    	m_player = null;
    	m_act_db = null;
    	m_act = null;
    }
    
    public CommonMsg handleInfo(CommonMsg respond) {
    	String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
		
		m_act = m_act_db.loadByUid(uid);
		if (m_act == null) {
			log.error("no act db id {}", uid);
			return respond;
		}
		
    	m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.error("no player id {}", uid);
    		return respond;
    	}
    	
    	if (!m_act.has_invest) {
    		if (!ActivityType.checkActivityOpen(ActivityType.RoleInvest, Calendar.getInstance().getTimeInMillis(), m_player)) {
        		return respond;
        	}
    	}
    	
    	msg.has_invest = m_act.has_invest;
    	msg.invest_award = m_act.invest_award;
		
    	msg.success = true;
		return respond;
    }
    
    public CommonMsg handleBuy(CommonMsg respond) {
    	String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
		
		m_act = m_act_db.loadByUid(uid);
		if (m_act == null) {
			log.error("no act db id {}", uid);
			return respond;
		}
		
    	m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.error("no player id {}", uid);
    		return respond;
    	}
    	
    	if (!ActivityType.checkActivityOpen(ActivityType.RoleInvest, Calendar.getInstance().getTimeInMillis(), m_player)) {
    		return respond;
    	}
    	
    	if (m_act.has_invest) {
    		log.error("already buy player {}", uid);
    		return respond;
    	}
    	
    	if (m_player.vip_level < ConfigConstant.tConf.getInvestRole()[0]) {
    		respond.header.rt_sub = 1129;
    		return respond;
    	}
    	
    	int cost_dmd = ConfigConstant.tConf.getInvestRole()[1];
    	if (!m_player.hasDiamond(cost_dmd)) {
    		respond.header.rt_sub = 1005;
    		return respond;
    	}
    	
    	PlayerImpl.SubDiamond(m_player, cost_dmd);
    	m_player_db.save();
    	m_act.has_invest = true;
    	m_act_db.save();
    	respond.body.sync_player_info = new SyncPlayerInfoMsg();
		respond.body.sync_player_info.diamond = -cost_dmd;
		
    	msg.success = true;
		return respond;
    }
    
    public CommonMsg handleReward(CommonMsg respond, CommonMsg receive)
    {
    	String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
		int reward_id = receive.body.activity.reward_id;
    	
    	m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.error("no player id {}", uid);
    		return respond;
    	}
    	
    	m_act = m_act_db.loadByUid(uid);
		if (m_act == null) {
			log.error("no act db id {}", uid);
			return respond;
		}
		
		if (!m_act.has_invest) {
			log.error("not invest yet id {}", uid);
			return respond;
		}
		
		if (m_act.invest_award.contains(reward_id)) {
			log.error("already get {}, id {}", uid, reward_id);
			return respond;
		}
		
		int[] role_ids = ConfigConstant.tConf.getInvestRoleID();
		boolean found = false;
		for (int i = role_ids[0]; i <= role_ids[1]; ++i) {
			if (i == reward_id) {
				found = true;
				break;
			}
		}
		if (!found) {
			log.error("id error {}, id {}", uid, reward_id);
			return respond;
		}
		
		Investvip i_cfg = ConfigConstant.tInvestvip.get(reward_id);
		if (i_cfg == null) {
			log.error("no cfg id {} uid {}", reward_id, uid);
			return respond;
		}
		
		boolean can_get = false;
		if (i_cfg.getType() == 1) {
			if (m_player.team_lv >= i_cfg.getValue()) {
				can_get = true;
			}
		} else if (i_cfg.getType() == 2) {
			Calendar cal_start = Calendar.getInstance();
			cal_start.setTimeInMillis(m_player.creat_t);
			int day_diff = TimeUtils.natureDayDiff(Calendar.getInstance(), cal_start);
			if (day_diff >= i_cfg.getValue()) {
				can_get = true;
			}
		}
		if (!can_get) {
			log.error("not enough value uid {}", uid);
			return respond;
		}
		
		m_bag = m_bag_db.loadByUid(uid);
    	if (m_bag == null) {
    		log.error("no bag id {}", uid);
    		return respond;
    	}
    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
    		  	
    	m_act.invest_award.add(reward_id);
    	m_act_db.save();
    	
    	for (int i = 0; i < i_cfg.getItem().length; ++i)
    	{
    		int id = i_cfg.getItem()[i];
    		if (id == 0)
    			continue;
    		int cnt = i_cfg.getCounts()[i];
    		if (cnt <= 0)
    			continue;
    		m_bag.addItemCount(id, cnt);
    		
    		SyncBagItem a = new SyncBagItem();
    		a.id = id;
    		a.count = cnt;
    		if (msg.r_items == null)
    			msg.r_items = new ArrayList<SyncBagItem>();
    		msg.r_items.add(a);
    		if (respond.body.sync_bag == null)
    			respond.body.sync_bag = new SyncBagMsg();
    		respond.body.sync_bag.items.put(id, m_bag.getItemCount(id));
    	}
    	m_bag_db.save();
		
    	msg.success = true;
    	return respond;
    }
}
