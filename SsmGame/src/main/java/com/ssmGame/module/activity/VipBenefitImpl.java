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
import com.ssmData.config.entity.Vipbonus;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerVipBenefitDB;
import com.ssmData.dbase.PlayerVipBenefitInfo;
import com.ssmGame.defdata.msg.activity.ActivityMsg;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;

@Service
@Scope("prototype")
public class VipBenefitImpl {
	
    @Autowired
    PlayerInfoDB m_player_db;
    PlayerInfo m_player = null;
    
    @Autowired
    PlayerBagDB m_bag_db;
    PlayerBagInfo m_bag = null;
    
    @Autowired
    PlayerVipBenefitDB m_act_db;
    PlayerVipBenefitInfo m_act;
    
    public void destroy()
	{
		m_player = null;
		m_player_db = null;
		m_bag_db = null;
		m_bag = null;
		m_act_db = null;
		m_act = null;
	}
	
	private static final Logger log = LoggerFactory.getLogger(VipBenefitImpl.class);
	
	public final static VipBenefitImpl getInstance(){
        return SpringContextUtil.getBean(VipBenefitImpl.class);
	}
	
	public CommonMsg handleInfo(CommonMsg respond, CommonMsg receive) {
		String uid = receive.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
		
		m_act = m_act_db.loadByUid(uid);
		if (m_act == null) {
			log.error("no player act db player {}", uid);
			return respond;
		}
		msg.vip_benefit_date = m_act.last_t;
		
		msg.success = true;
		return respond;
	}
	
	public CommonMsg handleExec(CommonMsg respond, CommonMsg receive) {
		String uid = receive.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
		
		m_act = m_act_db.loadByUid(uid);
		if (m_act == null) {
			log.error("no player act db player {}", uid);
			return respond;
		}
		
		m_player = m_player_db.loadById(uid);
		if (null == m_player) {
			log.error("no player {}", uid);
			return respond;
		}
		
		m_bag = m_bag_db.loadByUid(uid);
		if (null == m_bag) {
			log.error("no bag player {}", uid);
			return respond;
		}
    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
    	
    	int vip_lv = m_player.vip_level;
    	Vipbonus v_cfg = ConfigConstant.tVipbonus.get(vip_lv);
    	if (null == v_cfg) {
    		log.error("no vip lv {} player {}", vip_lv, uid);
    		return respond;
    	}
    	
    	Calendar now_cal = Calendar.getInstance();
    	long now = now_cal.getTimeInMillis();
    	if (!ActivityType.checkActivityOpen(ActivityType.Vipbonus, now, m_player)) {
    		log.error("act NOT OPEN ID {} act {}", uid, ActivityType.Vipbonus);
    		return respond;
    	}
    	
    	int today = now_cal.get(Calendar.DAY_OF_YEAR);
    	Calendar last_cal = Calendar.getInstance();
    	last_cal.setTimeInMillis(m_act.last_t);
    	if (last_cal.get(Calendar.DAY_OF_YEAR) == today) {
    		return respond;
    	}
    	
    	m_act.last_t = now;
    	
    	if (v_cfg.getVIPGold() > 0)
    	{
    		m_player.addGold(v_cfg.getVIPGold());
    		if (respond.body.sync_player_info == null)
    			respond.body.sync_player_info = new SyncPlayerInfoMsg();
    		respond.body.sync_player_info.gold = v_cfg.getVIPGold();
    		msg.r_gold = v_cfg.getVIPGold();
    	}
    	if (v_cfg.getVIPDiamond() > 0)
    	{
    		m_player.addDiamond(v_cfg.getVIPDiamond());
    		msg.r_diamond = v_cfg.getVIPDiamond();
    		if (respond.body.sync_player_info == null)
    			respond.body.sync_player_info = new SyncPlayerInfoMsg();
    		respond.body.sync_player_info.diamond = v_cfg.getVIPDiamond();
    	}
    	
    	boolean has_item = false;
    	for (int i = 0; i < v_cfg.getVIPItem().length; ++i) {
    		int id = v_cfg.getVIPItem()[i];
    		if (id == 0) {
    			continue;
    		}
    		int cnt = v_cfg.getCounts()[i];
    		if (cnt > 0) {
    			has_item = true;
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
    	}
    	if (has_item) {
    		m_bag_db.save();
    	}
  
		m_player_db.save();
    	m_act_db.save();
		msg.success = true;
		return respond;
	}
}
