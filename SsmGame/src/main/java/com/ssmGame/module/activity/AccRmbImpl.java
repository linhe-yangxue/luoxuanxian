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
import com.ssmData.config.entity.Accumulation;
import com.ssmData.dbase.PlayerActivityDB;
import com.ssmData.dbase.PlayerActivityInfo;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
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
public class AccRmbImpl {

	private static final Logger log = LoggerFactory.getLogger(AccRmbImpl.class);
	
	@Autowired
	PlayerActivityDB m_act_db;
	PlayerActivityInfo m_act;
	
	public final static AccRmbImpl getInstance(){
        return SpringContextUtil.getBean(AccRmbImpl.class);
	}
	
	public void bill(int rmb, PlayerInfo p_info, long last_bill_time)
	{
		if (rmb <= 0)
			return;
		
		long now = Calendar.getInstance().getTimeInMillis();
		if (!ActivityType.checkActivityOpen(ActivityType.AccRmb, now, p_info)){
			return;
		}
		
		m_act = m_act_db.loadByUid(p_info._id);
		if (m_act == null)
		{
			log.warn("AccRmbImpl bill() no act db {}", p_info._id);
			return;
		}
		
		if (!ActivityType.checkActivityOpen(ActivityType.AccRmb, last_bill_time, p_info))
		{
			m_act.acc_bill_rmb = 0;
			m_act.acc_bill_award.clear();
		}
		
		m_act.acc_bill_rmb += rmb;
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
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (p_info == null) {
			log.warn("AccRmbImpl handleInfo() no PlayerInfo db {}", uid);
			return respond;
		}
		
		long now = Calendar.getInstance().getTimeInMillis();
		if (!ActivityType.checkActivityOpen(ActivityType.AccRmb, now, p_info)) {
			return respond;
		}
		
		m_act = m_act_db.loadByUid(uid);
		if (m_act == null)
		{
			log.warn("AccRmbImpl handleInfo() no act db {}", uid);
			return respond;
		}
		
		PlayerMonthcardDB m_db = SpringContextUtil.getBean(PlayerMonthcardDB.class);
		PlayerMonthcardInfo m_info = m_db.loadByUid(uid);
		if (m_info != null) {
			if (m_info.bill_t == 0 || !ActivityType.checkActivityOpen(ActivityType.AccRmb, m_info.bill_t, p_info))
			{
				m_act.acc_bill_rmb = 0;
				m_act.acc_bill_award.clear();
				m_act_db.save();
			}
		}
	
		msg.acc_bill_rmb = m_act.acc_bill_rmb;
		msg.acc_bill_award = m_act.acc_bill_award;
		
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
		
		Accumulation a_cfg = ConfigConstant.tAccumulation.get(reward_id);
		if (a_cfg == null)
		{
			log.warn("AccRmbImpl handleReward() no cfg {}", reward_id);
			return respond;
		}
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (p_info == null)
		{
			log.warn("AccRmbImpl handleReward() no PlayerInfo db {}", uid);
			return respond;
		}
		
		PlayerBagDB bag_db = SpringContextUtil.getBean(PlayerBagDB.class);
		PlayerBagInfo bag = bag_db.loadByUid(uid);
		if (bag == null)
		{
			log.warn("AccRmbImpl handleReward() no PlayerBagInfo db {}", uid);
			return respond;
		}
    	if (bag.getEquipBagCapacity() - bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
		
		long now = Calendar.getInstance().getTimeInMillis();
		if (!ActivityType.checkActivityOpen(ActivityType.AccRmb, now, p_info))
		{
			log.warn("AccRmbImpl handleReward() not open {}", uid);
			return respond;
		}
		
		m_act = m_act_db.loadByUid(uid);
		if (m_act == null)
		{
			log.warn("AccRmbImpl handleReward() no Act db {}", uid);
			return respond;
		}
		
		PlayerMonthcardDB m_db = SpringContextUtil.getBean(PlayerMonthcardDB.class);
		PlayerMonthcardInfo m_info = m_db.loadByUid(uid);
		if (m_info != null) {
			if (m_info.bill_t == 0 || !ActivityType.checkActivityOpen(ActivityType.AccRmb, m_info.bill_t, p_info))
			{
				m_act.acc_bill_rmb = 0;
				m_act.acc_bill_award.clear();
				m_act_db.save();
			}
		}
		
		if (m_act.acc_bill_award.contains(reward_id))
		{
			log.warn("AccRmbImpl handleReward() Already get {}", uid);
			return respond;
		}
		
		if (m_act.acc_bill_rmb < a_cfg.getRmb())
		{
			log.warn("AccRmbImpl handleReward() not enough rmb {}", uid);
			return respond;
		}
		
		m_act.acc_bill_award.add(reward_id);
		m_act_db.save();
		
    	if (a_cfg.getGold() > 0)
    	{
    		p_info.addGold(a_cfg.getGold());
    		if (respond.body.sync_player_info == null)
    			respond.body.sync_player_info = new SyncPlayerInfoMsg();
    		respond.body.sync_player_info.gold = a_cfg.getGold();
    		msg.r_gold = a_cfg.getGold();
    	}
    	if (a_cfg.getDiamond() > 0)
    	{
    		p_info.addDiamond(a_cfg.getDiamond());
    		if (respond.body.sync_player_info == null)
				respond.body.sync_player_info = new SyncPlayerInfoMsg();
			respond.body.sync_player_info.diamond = a_cfg.getDiamond();
    		msg.r_diamond = a_cfg.getDiamond();
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
