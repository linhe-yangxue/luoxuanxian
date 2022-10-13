package com.ssmGame.module.activity;

import java.util.ArrayList;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.colligate.DateUtil;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Daypay;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerDaypayDB;
import com.ssmData.dbase.PlayerDaypayInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerMonthcardDB;
import com.ssmData.dbase.PlayerMonthcardInfo;
import com.ssmGame.defdata.msg.activity.ActivityMsg;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;

@Service
@Scope("prototype")
public class DaypayImpl {
	private static final Logger log = LoggerFactory.getLogger(DaypayImpl.class);
	@Autowired
	PlayerDaypayDB m_act_db;
	PlayerDaypayInfo m_act;
	
	public final static DaypayImpl getInstance(){
        return SpringContextUtil.getBean(DaypayImpl.class);
	}
	
	public void bill(int rmb, PlayerInfo p_info, long last_bill_time, long now)
	{
		if (rmb <= 0)
			return;
		
		if (!ActivityType.checkActivityOpen(ActivityType.Daypay, now, p_info))
			return;
		
		m_act = m_act_db.loadByUid(p_info._id);
		if (m_act == null){
			return;
		}
		
		if (!ActivityType.checkActivityOpen(ActivityType.Daypay, last_bill_time, p_info)){
			m_act.acc = 0;
			m_act.award.clear();
		}
    	int[] rt = new int[]{ConfigConstant.tConf.getRTime()};
    	if (m_act.acc != 0 && DateUtil.checkPassTime(last_bill_time, now, rt)) {
			m_act.acc = 0;
			m_act.award.clear();
    	}
		
		m_act.acc += rmb;
		m_act_db.save();
		
		m_act = null;
		m_act_db = null;
	}
	
	public CommonMsg handleInfo(CommonMsg respond)
	{
		String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
		
		PlayerMonthcardDB m_db = SpringContextUtil.getBean(PlayerMonthcardDB.class);
		PlayerMonthcardInfo m_info = m_db.loadByUid(uid);
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (p_info == null) {
			log.warn("DaypayImpl handleInfo() no player db {}", uid);
			return respond;
		}
		
		long now = Calendar.getInstance().getTimeInMillis();
		if (!ActivityType.checkActivityOpen(ActivityType.Daypay, now, p_info)) {
			return respond;
		}
		
		m_act = m_act_db.loadByUid(uid);
		if (m_act == null){
			log.warn("DaypayImpl handleInfo() no act db {}", uid);
			return respond;
		}
		
		if (m_info == null) {
			m_act.acc = 0;
			m_act.award.clear();
		}
		else {
			if (!ActivityType.checkActivityOpen(ActivityType.Daypay, m_info.bill_t, p_info)){
				m_act.acc = 0;
				m_act.award.clear();
			}
	    	int[] rt = new int[]{ConfigConstant.tConf.getRTime()};
	    	if (m_act.acc != 0 && DateUtil.checkPassTime(m_info.bill_t, now, rt)) {
				m_act.acc = 0;
				m_act.award.clear();
	    	}
		}
    	m_act_db.save();
    	
		msg.day_pay_acc = m_act.acc;
		msg.day_pay_award = m_act.award;
		
		p_db = null;
		m_db = null;
		msg.success = true;
		return respond;
	}
	
	public CommonMsg handleReward(CommonMsg respond, int reward_id)
	{
		String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (p_info == null){
			log.warn("DaypayImpl handleReward() no player db {}", uid);
			return respond;
		}
		
		PlayerBagDB bag_db = SpringContextUtil.getBean(PlayerBagDB.class);
		PlayerBagInfo bag = bag_db.loadByUid(uid);
		if (bag == null){
			log.warn("DaypayImpl handleReward() no bag db {}", uid);
			return respond;
		}
    	if (bag.getEquipBagCapacity() - bag.equips.size() < PlayerBagInfo.MIN_SPACE){
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
		
		long now = Calendar.getInstance().getTimeInMillis();
		if (!ActivityType.checkActivityOpen(ActivityType.Daypay, now, p_info)){
			log.warn("DaypayImpl handleReward() not open db {}", uid);
			return respond;
		}
		
		m_act = m_act_db.loadByUid(uid);
		if (m_act == null){
			log.warn("DaypayImpl handleReward() not act db db {}", uid);
			return respond;
		}
		
		if (m_act.award.contains(reward_id)){
			log.warn("DaypayImpl handleReward() already get {}", uid);
			return respond;
		}
		
		double lmt = reward_id;
		if (m_act.acc < lmt){
			log.warn("DaypayImpl handleReward() not enough get {}", uid);
			return respond;
		}
		
		Daypay d_cfg = ConfigConstant.tDaypay.get(reward_id);
		if (d_cfg == null) {
			log.warn("DaypayImpl handleReward() no cfg {}", reward_id);
			return respond;
		}
		
		m_act.award.add(reward_id);
		m_act_db.save();		
		
		if (d_cfg.getGold() > 0)
    	{
    		p_info.addGold(d_cfg.getGold());
    		if (respond.body.sync_player_info == null)
    			respond.body.sync_player_info = new SyncPlayerInfoMsg();
    		respond.body.sync_player_info.gold = d_cfg.getGold();
    		msg.r_gold = d_cfg.getGold();
    	}
    	if (d_cfg.getJewel() > 0)
    	{
    		p_info.addDiamond(d_cfg.getJewel());
    		if (respond.body.sync_player_info == null)
				respond.body.sync_player_info = new SyncPlayerInfoMsg();
			respond.body.sync_player_info.diamond = d_cfg.getJewel();
    		msg.r_diamond = d_cfg.getJewel();
    	}
		for (int i = 0; i < d_cfg.getItem().length; ++i)
		{
			int id = d_cfg.getItem()[i];
    		int cnt = d_cfg.getQuantity()[i];
    		bag.addItemCount(id, cnt);
    		
    		SyncBagItem a = new SyncBagItem();
    		a.id = id;
    		a.count = cnt;
    		if (msg.r_items == null)
    			msg.r_items = new ArrayList<SyncBagItem>();
    		msg.r_items.add(a);
    		if (respond.body.sync_bag == null)
    			respond.body.sync_bag = new SyncBagMsg();
    		respond.body.sync_bag.items.put(id, bag.getItemCount(id));
		}
		bag_db.save();
		
		bag_db = null;
		p_db = null;
		msg.success = true;
		return respond;
	}
}
