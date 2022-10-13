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
import com.ssmData.config.entity.Invest;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerInvestActivityDB;
import com.ssmData.dbase.PlayerInvestActivityInfo;
import com.ssmGame.defdata.msg.activity.ActivityMsg;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;

@Service
@Scope("prototype")
public class InvestImpl {
	private static final Logger log = LoggerFactory.getLogger(InvestImpl.class);
	
    public final static InvestImpl getInstance(){
        return SpringContextUtil.getBean(InvestImpl.class);
	}
    
	@Autowired
	PlayerInvestActivityDB m_act_db;
	PlayerInvestActivityInfo m_act;
	
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
			log.warn("InvestImpl.handleInfo() no act db id {}", uid);
			return respond;
		}
		
    	m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.warn("InvestImpl.handleInfo() no player id {}", uid);
    		return respond;
    	}
    	
    	if (!ActivityType.checkActivityOpen(ActivityType.Invest, Calendar.getInstance().getTimeInMillis(), m_player)) {
    		//log.warn("InvestImpl.handleReward() not open");
    		return respond;
    	}
    	
    	msg.has_invest = m_act.has_invest;
    	msg.invest_award = m_act.invest_award;
		
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
    		log.warn("InvestImpl.handleReward() no player id {}", uid);
    		return respond;
    	}
    	
    	if (!ActivityType.checkActivityOpen(ActivityType.Invest, Calendar.getInstance().getTimeInMillis(), m_player)) {
    		log.warn("InvestImpl.handleReward() not open");
    		return respond;
    	}
    	
		m_act = m_act_db.loadByUid(uid);
		if (m_act == null) {
			log.warn("InvestImpl.handleReward() no act db id {}", uid);
			return respond;
		}
		
		if (!m_act.has_invest) {
			log.warn("InvestImpl.handleReward() not invest yet id {}", uid);
			return respond;
		}
		
		if (m_player.team_lv < reward_id) {
			log.warn("InvestImpl.handleReward() not level enough yet lv {}", reward_id);
			return respond;
		}
		    	
		Invest ta_cfg = ConfigConstant.tInvest.get(reward_id);
    	if (ta_cfg == null) {
    		log.warn("InvestImpl.handleReward() no reward_id {}", reward_id);
    		return respond;
    	}
    	    	
    	if (m_act.invest_award.contains(reward_id)){
    		log.warn("InvestImpl.handleReward() alread get reward_id {}", reward_id);
    		return respond;
    	}
    	
    	m_bag = m_bag_db.loadByUid(uid);
    	if (m_bag == null) {
    		log.warn("InvestImpl.handleReward() no bag id {}", uid);
    		return respond;
    	}
    	  	
    	m_act.invest_award.add(reward_id);
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
