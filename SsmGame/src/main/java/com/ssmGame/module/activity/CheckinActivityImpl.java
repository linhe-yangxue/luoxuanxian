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
import com.ssmData.config.entity.Aggregate;
import com.ssmData.config.entity.Registration;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerCheckinActivityDB;
import com.ssmData.dbase.PlayerCheckinActivityInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmGame.defdata.msg.activity.ActivityMsg;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
import com.ssmGame.module.player.PlayerImpl;

@Service
@Scope("prototype")
public class CheckinActivityImpl {
	private static final Logger log = LoggerFactory.getLogger(CheckinActivityImpl.class);
	@Autowired
	PlayerCheckinActivityDB m_act_db;
	PlayerCheckinActivityInfo m_act;
	
    @Autowired
    PlayerInfoDB m_player_db;
    PlayerInfo m_player = null;
    
    @Autowired
    PlayerBagDB m_bag_db;
    PlayerBagInfo m_bag = null;
    
	public final static CheckinActivityImpl getInstance(){
        return SpringContextUtil.getBean(CheckinActivityImpl.class);
	}
	
    public void destroy()
    {
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
		
		m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.warn("CheckinActivityImpl.handleInfo() no player id {}", uid);
    		return respond;
    	}
    	
    	if (!ActivityType.checkActivityOpen(ActivityType.CheckIn, Calendar.getInstance().getTimeInMillis(), m_player)) {
    		//log.warn("CheckinActivityImpl.handleInfo() not open");
    		return respond;
    	}
    	
    	m_act = m_act_db.loadByUid(uid);
		if (m_act == null) {
			log.warn("CheckinActivityImpl.handleInfo() no act db id {}", uid);
			return respond;
		}
		
		if (m_act.date.size() > 0) {
			int last_month = m_act.date.get(0) / 100;
			int now_month = Calendar.getInstance().get(Calendar.MONTH) + 1;
			if (last_month != now_month) {
				m_act.date.clear();
				m_act.award.clear();
				m_act.re = 0;
				m_act_db.save();
			}
		}
		
		msg.check_in_date = m_act.date;
		msg.check_in_award = m_act.award;
		msg.check_in_re = m_act.re;
    	
		msg.success = true;
		return respond;
    }
    
    public CommonMsg handleCheckin(CommonMsg respond, int checkin_id) {
    	String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
    	
		m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.warn("CheckinActivityImpl.handleCheckin() no player id {}", uid);
    		return respond;
    	}
		
    	if (!ActivityType.checkActivityOpen(ActivityType.CheckIn,
				Calendar.getInstance().getTimeInMillis(), m_player)) {
    		log.warn("CheckinActivityImpl.handleCheckin() not open");
    		return respond;
    	}
    	
		m_bag = m_bag_db.loadByUid(uid);
    	if (m_bag == null) {
    		log.warn("CheckinActivityImpl.handleReward() no bag id {}", uid);
    		return respond;
    	}
    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
    	
    	m_act = m_act_db.loadByUid(uid);
		if (m_act == null) {
			log.warn("CheckinActivityImpl.handleCheckin() no act db id {}", uid);
			return respond;
		}
		
		if (m_act.date.contains(checkin_id)) {
			//log.warn("CheckinActivityImpl.handleCheckin() already checkin id {}", uid);
			return respond;
		}
		
		Calendar now = Calendar.getInstance();
		int now_month = now.get(Calendar.MONTH) + 1;
		int id_month = checkin_id / 100;
		if (now_month != id_month) {
			log.warn("CheckinActivityImpl.handleCheckin() Month Error id {}", uid);
			return respond;
		}
		
		int id_date = checkin_id % 100;
		int now_year = now.get(Calendar.YEAR);
		if (id_date == 29 && id_month == 2) { //闰年判断
			boolean test = false;
			if (now_year % 400 == 0 || (now_year % 100 != 0 && now_year % 4 == 0)) {
				test = true;
			}
			if (!test) {
				log.warn("CheckinActivityImpl.handleCheckin() Year 0229 check Error id {}", uid);
				return respond;
			}
		}
		
		Registration r_cfg = ConfigConstant.tRegistration.get(checkin_id);
		if (r_cfg == null) {
			log.warn("CheckinActivityImpl.handleCheckin() no Registration id {}", checkin_id);
			return respond;
		}
		
		int vip_f = 1;
		if (r_cfg.getVIPGrade() > 0 && m_player.vip_level >= r_cfg.getVIPGrade()) {
			vip_f = 2;
		}
		
		double delta_dmd = 0.0;
		int now_date = now.get(Calendar.DAY_OF_MONTH);
		if (now_date > id_date) { // 补签
			int cost_dmd = ConfigConstant.tConf.getRetroactive() * (m_act.re + 1);
			if (m_player.hasDiamond(cost_dmd)) {
				PlayerImpl.SubDiamond(m_player, cost_dmd);
				m_act.re += 1;
				delta_dmd -= cost_dmd;
			}
			else {
				respond.header.rt_sub = 1005;
				return respond;
			}
		}
		else if (now_date < id_date) {
			//log.warn("CheckinActivityImpl.handleCheckin() date check Error id {}", uid);
			return respond;
		}
		
		m_act.date.add(checkin_id);
		m_act_db.save();
		
		if (r_cfg.getGold() > 0)
    	{
    		m_player.addGold(r_cfg.getGold() * vip_f);
    		if (respond.body.sync_player_info == null)
    			respond.body.sync_player_info = new SyncPlayerInfoMsg();
    		respond.body.sync_player_info.gold = r_cfg.getGold() * vip_f;
    		msg.r_gold = r_cfg.getGold() * vip_f;
    	}
    	if (r_cfg.getJewel() > 0)
    	{
    		delta_dmd += r_cfg.getJewel() * vip_f;
    		m_player.addDiamond(r_cfg.getJewel() * vip_f);
    		msg.r_diamond = r_cfg.getJewel() * vip_f;
    	}
		if (respond.body.sync_player_info == null)
			respond.body.sync_player_info = new SyncPlayerInfoMsg();
		respond.body.sync_player_info.diamond = delta_dmd;
    	m_player_db.save();
    	
    	int id = r_cfg.getItem();
		int cnt = r_cfg.getQuantity() * vip_f;
		if (cnt > 0) {
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
	    	m_bag_db.save();
		}
		
    	msg.success = true;	
    	return respond;
    }
    
    public CommonMsg handleReward(CommonMsg respond, int reward_id) {
    	String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
    	
    	m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.warn("SevenImpl.handleReward() no player id {}", uid);
    		return respond;
    	}
    	
    	if (!ActivityType.checkActivityOpen(ActivityType.CheckIn, Calendar.getInstance().getTimeInMillis(), m_player)) {
    		//log.warn("SevenImpl.handleReward() not open");
    		return respond;
    	}
    	
		m_act = m_act_db.loadByUid(uid);
		if (m_act == null) {
			return respond;
		}
		    	
		Aggregate ta_cfg = ConfigConstant.tAggregate.get(reward_id);
    	if (ta_cfg == null) {
    		log.warn("CheckinActivityImpl.handleReward() no reward_id {}", reward_id);
    		return respond;
    	}
    	
    	if (m_act.award.contains(reward_id)) {
    		log.warn("CheckinActivityImpl.handleReward() ALREADY AWARD {}", reward_id);
    		return respond;
    	}
    	
    	if (m_act.date.size() < reward_id) {
    		log.warn("CheckinActivityImpl.handleReward() not enough date {}", m_act.date.size());
    		return respond;
    	}
    	
		m_bag = m_bag_db.loadByUid(uid);
    	if (m_bag == null) {
    		log.warn("CheckinActivityImpl.handleReward() no bag id {}", uid);
    		return respond;
    	}
    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
    	  	
    	m_act.award.add(reward_id);
    	m_act_db.save();
    	
    	if (ta_cfg.getGold() > 0)
    	{
    		m_player.addGold(ta_cfg.getGold());
    		if (respond.body.sync_player_info == null)
    			respond.body.sync_player_info = new SyncPlayerInfoMsg();
    		respond.body.sync_player_info.gold = ta_cfg.getGold();
    		msg.r_gold = ta_cfg.getGold();
    	}
    	if (ta_cfg.getJewel() > 0)
    	{
    		m_player.addDiamond(ta_cfg.getJewel());
    		if (respond.body.sync_player_info == null)
				respond.body.sync_player_info = new SyncPlayerInfoMsg();
			respond.body.sync_player_info.diamond = ta_cfg.getJewel();
    		msg.r_diamond = ta_cfg.getJewel();
    	}
    	m_player_db.save();
    	
    	int id = ta_cfg.getItem();
		int cnt = ta_cfg.getQuantity();
		if (cnt > 0) {
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
	    	m_bag_db.save();
		}
	
    	msg.success = true;
    	return respond;
    }
}
