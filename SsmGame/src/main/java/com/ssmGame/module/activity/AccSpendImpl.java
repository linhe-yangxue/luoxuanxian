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
import com.ssmData.config.entity.Consume;
import com.ssmData.dbase.PlayerAccSpendDB;
import com.ssmData.dbase.PlayerAccSpendInfo;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmGame.defdata.msg.activity.ActivityMsg;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;

@Service
@Scope("prototype")
public class AccSpendImpl {

	private static final Logger log = LoggerFactory.getLogger(AccSpendImpl.class);
	
	@Autowired
	PlayerAccSpendDB m_act_db;
	PlayerAccSpendInfo m_act;
	
	public final static AccSpendImpl getInstance(){
        return SpringContextUtil.getBean(AccSpendImpl.class);
	}
	
	public void destroy() {
		m_act_db = null;
		m_act = null;
	}
	
	public CommonMsg handleInfo(CommonMsg respond)
	{
		String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (p_info == null) {
			log.warn("AccSpendImpl.handleInfo no player db {}", uid);
			return respond;
		}
		
		long now = Calendar.getInstance().getTimeInMillis();
		if (!ActivityType.checkActivityOpen(ActivityType.AccSpend, now, p_info)) {
			return respond;
		}
		
		m_act = m_act_db.loadByUid(uid);
		if (m_act == null){
			log.warn("AccSpendImpl.handleInfo no act db {}", uid);
			return respond;
		}
		
		if (m_act.last_t == 0L || !ActivityType.checkActivityOpen(ActivityType.AccSpend, m_act.last_t, p_info)){
			m_act.last_t = 0L;
			m_act.acc_spend = 0.0;
			m_act.award.clear();
		}
		msg.acc_spend = m_act.acc_spend;
		msg.acc_spend_award = m_act.award;
		
		p_db = null;
		msg.success = true;
		return respond;
	}
	
	public CommonMsg handleReward(CommonMsg respond, int reward_id)
	{
		String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
		
		Consume a_cfg = ConfigConstant.tConsume.get(reward_id);
		if (a_cfg == null){
			log.warn("AccSpendImpl.handleReward no a_cfg  {}", reward_id);
			return respond;
		}
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (p_info == null){
			log.warn("AccSpendImpl.handleReward no player db  {}", uid);
			return respond;
		}
		
		PlayerBagDB bag_db = SpringContextUtil.getBean(PlayerBagDB.class);
		PlayerBagInfo bag = bag_db.loadByUid(uid);
		if (bag == null){
			log.warn("AccSpendImpl.handleReward no bag db  {}", uid);
			return respond;
		}
    	if (bag.getEquipBagCapacity() - bag.equips.size() < PlayerBagInfo.MIN_SPACE){
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
		
		long now = Calendar.getInstance().getTimeInMillis();
		if (!ActivityType.checkActivityOpen(ActivityType.AccSpend, now, p_info)){
			log.warn("AccSpendImpl.handleReward no open {}", uid);
			return respond;
		}
		
		m_act = m_act_db.loadByUid(uid);
		if (m_act == null){
			log.warn("AccSpendImpl.handleReward no act db {}", uid);
			return respond;
		}
		
		if (m_act.award.contains(reward_id)){
			log.warn("AccSpendImpl.handleReward already get {}", uid);
			return respond;
		}
		
		double lmt = a_cfg.getLimit();
		if (m_act.acc_spend < lmt){
			log.warn("AccSpendImpl.handleReward not enough {}", uid);
			return respond;
		}
		
		m_act.award.add(reward_id);
		m_act_db.save();		
		
		if (a_cfg.getGold() > 0)
    	{
    		p_info.addGold(a_cfg.getGold());
    		if (respond.body.sync_player_info == null)
    			respond.body.sync_player_info = new SyncPlayerInfoMsg();
    		respond.body.sync_player_info.gold = a_cfg.getGold();
    		msg.r_gold = a_cfg.getGold();
    	}
    	if (a_cfg.getJewel() > 0)
    	{
    		p_info.addDiamond(a_cfg.getJewel());
    		if (respond.body.sync_player_info == null)
				respond.body.sync_player_info = new SyncPlayerInfoMsg();
			respond.body.sync_player_info.diamond = a_cfg.getJewel();
    		msg.r_diamond = a_cfg.getJewel();
    	}
		for (int i = 0; i < a_cfg.getItem().length; ++i)
		{
			int id = a_cfg.getItem()[i];
    		int cnt = a_cfg.getQuantity()[i];
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
