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
import com.ssmData.config.entity.Duration;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerDurationDB;
import com.ssmData.dbase.PlayerDurationInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmGame.defdata.msg.activity.ActivityMsg;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
import com.ssmGame.util.TimeUtils;

@Service
@Scope("prototype")
public class DurationImpl {
	private static final Logger log = LoggerFactory.getLogger(DurationImpl.class);
	@Autowired
	PlayerDurationDB m_act_db;
	PlayerDurationInfo m_act;
	
    @Autowired
    PlayerInfoDB m_player_db;
    PlayerInfo m_player = null;
    
    @Autowired
    PlayerBagDB m_bag_db;
    PlayerBagInfo m_bag = null;
    
	public final static DurationImpl getInstance(){
        return SpringContextUtil.getBean(DurationImpl.class);
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
    
    private long refresh(PlayerDurationInfo info, long login) {
    	
    	Calendar now = Calendar.getInstance();
    	int now_day = now.get(Calendar.DAY_OF_YEAR);
    	Calendar l_date = Calendar.getInstance();
    	l_date.setTimeInMillis(login);
    	int l_day = l_date.get(Calendar.DAY_OF_YEAR);
    	Calendar last = Calendar.getInstance();
    	last.setTimeInMillis(info.last_t);
    	int last_day = last.get(Calendar.DAY_OF_YEAR);
    	
    	if (last_day != now_day) {
    		info.award.clear();
    		info.duration = 0;
    	}
    	if (info.last_t > login) {
    		if (last_day < now_day) {
    			info.duration = now.getTimeInMillis() - TimeUtils.TodayStart();
    		} else {
    			info.duration += now.getTimeInMillis() - info.last_t;
    		}
    	} else {
    		if (l_day < now_day) {
    			info.duration = now.getTimeInMillis() - TimeUtils.TodayStart();
    		} else {
    			info.duration += now.getTimeInMillis() - login;
    		}
    	}
    	info.last_t = now.getTimeInMillis();
    	
    	return info.duration;
    }

    public CommonMsg handleInfo(CommonMsg respond)
    {
    	String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
		
		m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.warn("DurationImpl.handleInfo() no player id {}", uid);
    		return respond;
    	}
    	
    	if (!ActivityType.checkActivityOpen(ActivityType.Duration, Calendar.getInstance().getTimeInMillis(), m_player)) {
    		//log.warn("DurationImpl.handleInfo() not open");
    		return respond;
    	}
    	
    	m_act = m_act_db.loadByUid(uid);
		if (m_act == null) {
			log.warn("DurationImpl.handleReward NO act db ID {}", uid);
			return respond;
		}
		refresh(m_act, m_player.login_t);
		m_act_db.save();
    	
		msg.duration_acc = m_act.duration;
		msg.duration_award = m_act.award;
		msg.success = true;
		return respond;
    }
    
    public CommonMsg handleReward(CommonMsg respond, int reward_id)
    {
    	String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
    	
    	m_bag = m_bag_db.loadByUid(uid);
    	if (m_bag == null) {
    		log.warn("DurationImpl.handleReward() no bag id {}", uid);
    		return respond;
    	}
    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
		
    	m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.warn("DurationImpl.handleReward() no player id {}", uid);
    		return respond;
    	}
    	
    	if (!ActivityType.checkActivityOpen(ActivityType.Duration, Calendar.getInstance().getTimeInMillis(), m_player)) {
    		log.warn("DurationImpl.handleReward() not open");
    		return respond;
    	}
    	
		m_act = m_act_db.loadByUid(uid);
		if (m_act == null) {
			log.warn("DurationImpl.handleReward() no act db id {}", uid);
			return respond;
		}
		    	
		Duration ta_cfg = ConfigConstant.tDuration.get(reward_id);
    	if (ta_cfg == null) {
    		log.warn("DurationImpl.handleReward() no reward_id {}", reward_id);
    		return respond;
    	}
    	
		long duration = refresh(m_act, m_player.login_t);
    	if (m_act.award.contains(reward_id)){
    		//log.warn("DurationImpl.handleReward() alread get reward_id {}", reward_id);
    		return respond;
    	}
    	if (duration < reward_id) {
    		m_act_db.save();
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
    	
    	for (int i = 0; i < ta_cfg.getItem().length; ++i)
    	{
    		int id = ta_cfg.getItem()[i];
    		int cnt = ta_cfg.getQuantity()[i];
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
