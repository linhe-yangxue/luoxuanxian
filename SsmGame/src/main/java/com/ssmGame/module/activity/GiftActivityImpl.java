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
import com.ssmData.config.entity.Gift;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerGiftActivityDB;
import com.ssmData.dbase.PlayerGiftActivityInfo;
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
public class GiftActivityImpl {
	@Autowired
    PlayerInfoDB m_player_db;
    PlayerInfo m_player = null;
    
    @Autowired
    PlayerBagDB m_bag_db;
    PlayerBagInfo m_bag = null;
    
    @Autowired
    PlayerGiftActivityDB m_act_db;
    PlayerGiftActivityInfo m_act;
    
    public void destroy()
	{
		m_player = null;
		m_player_db = null;
		m_bag_db = null;
		m_bag = null;
		m_act_db = null;
		m_act = null;
	}
	
	private static final Logger log = LoggerFactory.getLogger(GiftActivityImpl.class);
	
	public final static GiftActivityImpl getInstance(){
        return SpringContextUtil.getBean(GiftActivityImpl.class);
	}
	
	public CommonMsg handleInfo(CommonMsg respond)
	{
    	String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
		
    	m_act = m_act_db.loadByUid(uid);
    	if (m_act == null) {
    		log.warn("GiftActivityImpl.handleInfo NO DB {}", uid);
    		return respond;
    	}
    	
    	m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.warn("GiftActivityImpl.handleReward NO player ID {}", uid);
    		return respond;
    	}
    	
    	long now = Calendar.getInstance().getTimeInMillis();
    	
    	if (!ActivityType.checkActivityOpen(ActivityType.Gift, now, m_player)) {
    		return respond;
    	}
    	
    	long begin = ActivityType.getBasicOpen(ActivityType.Gift);
    	long t = ActivityType.getBeginOpen(ActivityType.Gift);
    	if (t < begin)
    		begin = t;
    	
    	if (m_act.last_t < begin) {
    		m_act.cnts.clear();
    		m_act.ids.clear();
    	}
    	m_act.last_t = now;
    	m_act_db.save();
    	
    	msg.gift_ids = m_act.ids;
    	msg.gift_cnts = m_act.cnts;
		msg.success = true;
		return respond;
	}
	
	public CommonMsg handleBuy(CommonMsg respond, int buy_id)
    {
    	String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
    	
    	Gift m_cfg = ConfigConstant.tGift.get(buy_id);
    	if (null == m_cfg) {
    		log.warn("GiftActivityImpl.handleBuy NO reward ID {}", uid);
    		return respond;
    	}
    	if (m_cfg.getType() != ActivityType.GiftTypeBegin) {
    		log.warn("GiftActivityImpl.handleBuy Type ERROR {}", uid);
    		return respond;
    	}
    	
    	m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.warn("GiftActivityImpl.handleBuy NO player ID {}", uid);
    		return respond;
    	}
    	
    	if (!ActivityType.checkActivityOpen(ActivityType.Gift
    			, Calendar.getInstance().getTimeInMillis(), m_player)) {
    		log.warn("GiftActivityImpl.handleBuy NOT OPEN ID {}", uid);
    		return respond;
    	}
    	
    	m_bag = m_bag_db.loadByUid(uid);
    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE){
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
    	
    	m_act = m_act_db.loadByUid(uid);
    	if (!m_act.ids.contains(buy_id)) {
    		m_act.ids.add(buy_id);
    		m_act.cnts.add(0);
    	}
    	
    	int test_idx = -1;
    	for (int i = 0; i < m_act.ids.size(); ++i) {
    		if (m_act.ids.get(i) == buy_id && m_act.cnts.get(i) < m_cfg.getNext()) {
    			test_idx = i;
    			break;
    		}
    	}
    	if (test_idx < 0) {
    		log.warn("GiftActivityImpl.handleBuy sold out {}", buy_id);
    		return respond;
    	}
    	
    	if (!m_player.hasDiamond(m_cfg.getPrice())) {
    		respond.header.rt_sub = 1005;
    		return respond;
    	}
    	PlayerImpl.SubDiamond(m_player, m_cfg.getPrice());
    	m_player_db.save();
    	respond.body.sync_player_info = new SyncPlayerInfoMsg();
		respond.body.sync_player_info.diamond = -m_cfg.getPrice();
    	
    	m_act.cnts.set(test_idx, m_act.cnts.get(test_idx) + 1);
    	m_act_db.save();
    	
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
