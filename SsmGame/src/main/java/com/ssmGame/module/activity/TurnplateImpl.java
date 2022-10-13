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
import com.ssmData.config.entity.Turnplate;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerTurnplateDB;
import com.ssmData.dbase.PlayerTurnplateInfo;
import com.ssmGame.defdata.msg.activity.ActivityMsg;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.util.RandomMethod;

@Service
@Scope("prototype")
public class TurnplateImpl {
	
    @Autowired
    PlayerInfoDB m_player_db;
    PlayerInfo m_player = null;
    
    @Autowired
    PlayerBagDB m_bag_db;
    PlayerBagInfo m_bag = null;
    
    @Autowired
    PlayerTurnplateDB m_act_db;
    PlayerTurnplateInfo m_act;
    
    public void destroy()
	{
		m_player = null;
		m_player_db = null;
		m_bag_db = null;
		m_bag = null;
		m_act_db = null;
		m_act = null;
	}
	
	private static final Logger log = LoggerFactory.getLogger(TurnplateImpl.class);
	
	public final static TurnplateImpl getInstance(){
        return SpringContextUtil.getBean(TurnplateImpl.class);
	}
	
	public CommonMsg handleInfo(CommonMsg respond)
	{
    	String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
		
    	m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.warn("TurnplateImpl.handleReward NO player ID {}", uid);
    		return respond;
    	}
    	
    	long now = Calendar.getInstance().getTimeInMillis();
    	if (!ActivityType.checkActivityOpen(ActivityType.Turnplate
    			, now, m_player)) {
    		return respond;
    	}
		
    	m_act = m_act_db.loadByUid(uid);
    	if (m_act == null) {
    		log.warn("TurnplateImpl.handleInfo NO DB {}", uid);
    		return respond;
    	}
    	
    	if (!ActivityType.checkActivityOpen(ActivityType.Turnplate
    			, m_act.last_t, m_player)){
    		m_act.cnts.clear();
    		int cnt = ConfigConstant.tTurnplate.size();
    		for (int i = 0; i < cnt; ++i) {
    			m_act.cnts.add(0);
    		}
    		m_act.last_t = now;
    		m_act_db.save();
    	}
    	
    	//msg.lvgrow_award = m_act.lvgrow_award;
		msg.success = true;
		return respond;
	}
	
	public CommonMsg handleRoll(CommonMsg respond)
    {
    	String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
    	
    	m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.warn("TurnplateImpl.handleRoll NO player ID {}", uid);
    		return respond;
    	}
    	
    	long now = Calendar.getInstance().getTimeInMillis();
    	if (!ActivityType.checkActivityOpen(ActivityType.Turnplate
    			, now, m_player)) {
    		log.warn("TurnplateImpl.handleInfo NO OPEN {}", uid);
    		return respond;
    	}
    	
    	m_act = m_act_db.loadByUid(uid);
    	if (m_act == null){
    		log.warn("TurnplateImpl.handleRoll no act_db {}", uid);
    		return respond;
    	}
    	if (m_act.total_cnt == null) {
    		m_act.total_cnt = 0;
    	}
    	
    	m_bag = m_bag_db.loadByUid(uid);
    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE){
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
    	int cost_id = ConfigConstant.tConf.getDialItem();
    	if (!m_bag.hasItemCount(cost_id, 1)) {
    		respond.header.rt_sub = 1109;
    		return respond;
    	}
    	m_bag.subItemCount(cost_id, 1);
		if (respond.body.sync_bag == null)
			respond.body.sync_bag = new SyncBagMsg();
		respond.body.sync_bag.items.put(cost_id, m_bag.getItemCount(cost_id));
    	
    	if (m_act.cnts.size() != ConfigConstant.tTurnplate.size() 
    		|| !ActivityType.checkActivityOpen(ActivityType.Turnplate, m_act.last_t, m_player)) {
    		m_act.cnts.clear();
    		int cnt = ConfigConstant.tTurnplate.size();
    		for (int i = 0; i < cnt; ++i) {
    			m_act.cnts.add(0);
    		}
    		m_act.total_cnt = 0;
    	}
		
    	m_act.last_t = now;
    	int dail_value = ConfigConstant.tConf.getDialValue();
    	if (dail_value == 0) {
    		dail_value = m_act.total_cnt+1;
    	}
    	int[] pros = new int[m_act.cnts.size()]; 
    	for (int i = 0; i < pros.length; ++i) {
    		m_act.cnts.set(i, m_act.cnts.get(i) + 1);
    		pros[i] = 0;
    		Turnplate t_cfg = ConfigConstant.tTurnplate.get(i);
    		if (t_cfg != null) {
    			pros[i] = t_cfg.getWeight() + t_cfg.getAddition() * (int)Math.floor((float)m_act.total_cnt / dail_value);
    		}
    	}
    	int hit = RandomMethod.CalcHitWhichIndex(pros);
    	if (hit < 0) {
    		log.warn("TurnplateImpl.handleRoll Rand Error {}", uid);
    		return respond;
    	}
    	m_act.total_cnt++;
    	Turnplate t_cfg = ConfigConstant.tTurnplate.get(hit);
    	if (t_cfg.getTimeReset() == TurnplateTimeReset.Reset) {
    		m_act.cnts.set(hit, 0);
    		m_act.total_cnt = 0;
    	}
    	m_act_db.save();
    	//System.out.println("total_cnt " + m_act.total_cnt + " item " + m_bag.getItemCount(cost_id) + " hit " + hit);
    	int id = t_cfg.getItem();
		int cnt = t_cfg.getQuantity();
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
    	
    	msg.turn_id = hit;
    	msg.success = true;
    	return respond;
    }
}

class TurnplateTimeReset {
	public static final int Reset = 1; //清空
	public static final int NotReset = 2; //不清空
}
