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
import com.ssmData.config.entity.Lvgrow;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerLvgrowActivityDB;
import com.ssmData.dbase.PlayerLvgrowActivityInfo;
import com.ssmGame.defdata.msg.activity.ActivityMsg;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;

@Service
@Scope("prototype")
public class LvGrowActivityImpl {
	
    @Autowired
    PlayerInfoDB m_player_db;
    PlayerInfo m_player = null;
    
    @Autowired
    PlayerBagDB m_bag_db;
    PlayerBagInfo m_bag = null;
    
    @Autowired
    PlayerLvgrowActivityDB m_act_db;
    PlayerLvgrowActivityInfo m_act;
    
    public void destroy()
	{
		m_player = null;
		m_player_db = null;
		m_bag_db = null;
		m_bag = null;
		m_act_db = null;
		m_act = null;
	}
	
	private static final Logger log = LoggerFactory.getLogger(LvGrowActivityImpl.class);
	
	public final static LvGrowActivityImpl getInstance(){
        return SpringContextUtil.getBean(LvGrowActivityImpl.class);
	}
	
	public CommonMsg handleInfo(CommonMsg respond)
	{
    	String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
		
    	m_act = m_act_db.loadByUid(uid);
    	if (m_act == null) {
    		log.warn("LvGrowActivityImpl.handleInfo NO DB {}", uid);
    		return respond;
    	}
    	msg.lvgrow_award = m_act.lvgrow_award;
		msg.success = true;
		return respond;
	}
	
	public CommonMsg handleReward(CommonMsg respond, int award_id)
    {
    	String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
    	
    	Lvgrow m_cfg = ConfigConstant.tLvgrow.get(award_id);
    	if (null == m_cfg)
    	{
    		log.warn("LvGrowActivityImpl.handleReward NO reward ID {}", award_id);
    		return respond;
    	}
    	
    	m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.warn("LvGrowActivityImpl.handleReward NO player ID {}", uid);
    		return respond;
    	}
    	
    	if (!ActivityType.checkActivityOpen(ActivityType.LvGrow, Calendar.getInstance().getTimeInMillis(), m_player)) {
    		log.warn("LvGrowActivityImpl.handleReward NOT OPEN ID {}", uid);
    		return respond;
    	}
    	
    	if (m_player.team_lv < award_id)
    	{
    		log.warn("LvGrowActivityImpl.handleReward NOT ENOUGH AWARD {}", m_player.team_lv);
    		return respond;
    	}
    	
    	m_act = m_act_db.loadByUid(uid);
    	if (m_act.lvgrow_award.contains(award_id))
    	{
    		log.warn("LvGrowActivityImpl.handleReward ALREADY AWARD {}", award_id);
    		return respond;
    	}
    	
    	m_bag = m_bag_db.loadByUid(uid);
    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
    	
    	m_act.lvgrow_award.add(award_id);
    	m_act_db.save();
    	
    	if (m_cfg.getGold() > 0)
    	{
    		m_player.addGold(m_cfg.getGold());
    		if (respond.body.sync_player_info == null)
    			respond.body.sync_player_info = new SyncPlayerInfoMsg();
    		respond.body.sync_player_info.gold = m_cfg.getGold();
    		msg.r_gold = m_cfg.getGold();
    	}
    	if (m_cfg.getJewel() > 0)
    	{
    		m_player.addDiamond(m_cfg.getJewel());
    		if (respond.body.sync_player_info == null)
				respond.body.sync_player_info = new SyncPlayerInfoMsg();
			respond.body.sync_player_info.diamond = m_cfg.getJewel();
    		msg.r_diamond = m_cfg.getJewel();
    	}
    	m_player_db.save();
    	
    	for (int i = 0; i < m_cfg.getItem().length; ++i)
    	{
    		int id = m_cfg.getItem()[i];
    		int cnt = m_cfg.getQuantity()[i];
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
