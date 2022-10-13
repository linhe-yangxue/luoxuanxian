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
import com.ssmData.config.entity.Draward;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerSevenActivityDB;
import com.ssmData.dbase.PlayerSevenActivityInfo;
import com.ssmGame.defdata.msg.activity.ActivityMsg;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;

@Service
@Scope("prototype")
public class SevenImpl {
	private static final Logger log = LoggerFactory.getLogger(SevenImpl.class);
	@Autowired
	PlayerSevenActivityDB m_act_db;
	PlayerSevenActivityInfo m_act;
	
    @Autowired
    PlayerInfoDB m_player_db;
    PlayerInfo m_player = null;
    
    @Autowired
    PlayerBagDB m_bag_db;
    PlayerBagInfo m_bag = null;
    
	public final static SevenImpl getInstance(){
        return SpringContextUtil.getBean(SevenImpl.class);
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

    public CommonMsg handleInfo(CommonMsg respond)
    {
    	String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
		
		m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.warn("SevenImpl.handleInfo() no player id {}", uid);
    		return respond;
    	}
    	
    	if (!ActivityType.checkActivityOpen(ActivityType.Seven, Calendar.getInstance().getTimeInMillis(), m_player)) {
    		//log.warn("SevenImpl.handleInfo() not open");
    		return respond;
    	}
    	
    	m_act = m_act_db.loadByUid(uid);
		if (m_act == null) {
			log.warn("SevenImpl.handleInfo() no act db id {}", uid);
			return respond;
		}
		
		if (m_act.seven_acc < 7) {    	
			Calendar l = Calendar.getInstance();
			l.setTimeInMillis(m_act.seven_last_t);
			int l_year = l.get(Calendar.YEAR);
			int l_day = l.get(Calendar.DAY_OF_YEAR);
			Calendar now = Calendar.getInstance();
			int now_year = now.get(Calendar.YEAR);
			int now_day = now.get(Calendar.DAY_OF_YEAR);
	    	if (m_act.seven_last_t == 0 || now_year > l_year
	    			|| (now_year == l_year && now_day > l_day)) {
	    		m_act.seven_acc++;
				m_act.seven_last_t = now.getTimeInMillis();			
	    	}
		}
		m_act_db.save();
    	
		msg.seven_acc = m_act.seven_acc;
		msg.seven_award = m_act.seven_award;
		msg.success = true;
		return respond;
    }
    
    public CommonMsg handleReward(CommonMsg respond, int reward_id)
    {
    	String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
    	
    	m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.warn("SevenImpl.handleReward() no player id {}", uid);
    		return respond;
    	}
    	
    	if (!ActivityType.checkActivityOpen(ActivityType.Seven, Calendar.getInstance().getTimeInMillis(), m_player)) {
    		log.warn("SevenImpl.handleReward() not open");
    		return respond;
    	}
    	
		m_act = m_act_db.loadByUid(uid);
		if (m_act == null) {
			log.warn("SevenImpl.handleInfo() no act db id {}", uid);
			return respond;
		}
		    	
		Draward ta_cfg = ConfigConstant.tDraward.get(reward_id);
    	if (ta_cfg == null) {
    		log.warn("SevenImpl.handleReward() no reward_id {}", reward_id);
    		return respond;
    	}
    	
    	//1是永久7日，2是限时7日
    	if (ta_cfg.getType() != 1) { 
    		log.warn("SevenImpl.handleReward() reward type error {}", reward_id);
    		return respond;
    	}
    	
    	if (m_act.seven_acc < ta_cfg.getSky()){
    		log.warn("SevenImpl.handleReward() not enough LV {}", reward_id);
    		return respond;
    	}
    	
    	if (m_act.seven_award.contains(reward_id)){
    		//log.warn("SevenImpl.handleReward() alread get reward_id {}", reward_id);
    		return respond;
    	}
    	
    	m_bag = m_bag_db.loadByUid(uid);
    	if (m_bag == null) {
    		log.warn("SevenImpl.handleReward() no bag id {}", uid);
    		return respond;
    	}
    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
    	  	
    	m_act.seven_award.add(reward_id);
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
