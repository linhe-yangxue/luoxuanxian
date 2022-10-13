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
import com.ssmData.config.entity.Vipgift;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerVipGiftDB;
import com.ssmData.dbase.PlayerVipGiftInfo;
import com.ssmGame.defdata.msg.activity.ActivityMsg;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
import com.ssmGame.module.player.PlayerImpl;
import com.ssmGame.util.TimeUtils;

@Service
@Scope("prototype")
public class VipGiftImpl {
	@Autowired
    PlayerInfoDB m_player_db;
    PlayerInfo m_player = null;
    
    @Autowired
    PlayerBagDB m_bag_db;
    PlayerBagInfo m_bag = null;
    
    @Autowired
    PlayerVipGiftDB m_act_db;
    PlayerVipGiftInfo m_act;
    
    public void destroy()
	{
		m_player = null;
		m_player_db = null;
		m_bag_db = null;
		m_bag = null;
		m_act_db = null;
		m_act = null;
	}
	
	private static final Logger log = LoggerFactory.getLogger(VipGiftImpl.class);
	
	public final static VipGiftImpl getInstance(){
        return SpringContextUtil.getBean(VipGiftImpl.class);
	}
	
	public CommonMsg handleInfo(CommonMsg respond)
	{
    	String uid = respond.header.uid;
		ActivityMsg msg = new ActivityMsg();
		respond.body.activity = msg;
		msg.success = false;
		
    	m_act = m_act_db.loadByUid(uid);
    	if (m_act == null) {
    		log.error(" NO DB {}", uid);
    		return respond;
    	}
    	
    	m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.error(" NO player ID {}", uid);
    		return respond;
    	}
    	
    	Calendar now_cal = Calendar.getInstance();
    	long now = now_cal.getTimeInMillis();
    	
    	if (!ActivityType.checkActivityOpen(ActivityType.Vipgift, now, m_player)) {
    		return respond;
    	}
    	
    	long begin = ActivityType.getBasicOpen(ActivityType.Vipgift);
    	long t = ActivityType.getBeginOpen(ActivityType.Vipgift);
    	if (t < begin)
    		begin = t;
    	
    	long week_t = begin;
    	int now_week_day = now_cal.get(Calendar.DAY_OF_WEEK);
    	int week_len = ConfigConstant.tConf.getWeekTime().length;
    	for (int i = 0; i < week_len; ++i){
    		if (now_week_day == TimeUtils.GetConfigWeekTime(i)) {
        		week_t = TimeUtils.TodayStart() + ConfigConstant.tConf.getRTime() * 60 * 60 * 1000;
        		break;
        	}
    	}
    	
    	if ((now > begin && m_act.last_t < begin) || (now > week_t && m_act.last_t < week_t)) {
    		m_act.cnts.clear();
    		m_act.ids.clear();
    		m_act.last_t = now;
    	}
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
    	
		Vipgift m_cfg = ConfigConstant.tVipgift.get(buy_id);
    	if (null == m_cfg) {
    		log.error(" NO reward ID {}", uid);
    		return respond;
    	}
    	
    	m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.error(" NO player ID {}", uid);
    		return respond;
    	}
    	
    	if (m_player.vip_level < m_cfg.getVIP()) {
    		return respond;
    	}
    	int cost_dmd = m_cfg.getDiamond();
    	if (!m_player.hasDiamond(cost_dmd)) {
    		respond.header.rt_sub = 1005;
    		return respond;
    	}
    	
    	m_act = m_act_db.loadByUid(uid);
    	if (m_act == null) {
    		log.error(" NO DB {}", uid);
    		return respond;
    	}
    	
    	Calendar now_cal = Calendar.getInstance();
    	long now = now_cal.getTimeInMillis();
    	
    	if (!ActivityType.checkActivityOpen(ActivityType.Vipgift, now, m_player)) {
    		log.error(" NOT OPEN ID {}", uid);
    		return respond;
    	}
    	
    	long begin = ActivityType.getBasicOpen(ActivityType.Vipgift);
    	long t = ActivityType.getBeginOpen(ActivityType.Vipgift);
    	if (t < begin)
    		begin = t;
    	
    	long week_t = begin;
    	int now_week_day = now_cal.get(Calendar.DAY_OF_WEEK);
    	int week_len = ConfigConstant.tConf.getWeekTime().length;
    	for (int i = 0; i < week_len; ++i){
    		if (now_week_day == TimeUtils.GetConfigWeekTime(i)) {
        		week_t = TimeUtils.TodayStart() + ConfigConstant.tConf.getRTime() * 60 * 60 * 1000;
        		break;
        	}
    	}
    	
    	
    	if ((now > begin && m_act.last_t < begin) || (now > week_t && m_act.last_t < week_t)) {
    		m_act.cnts.clear();
    		m_act.ids.clear();
    		m_act.last_t = now;
    	}
    	
    	m_bag = m_bag_db.loadByUid(uid);
    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE){
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
    	
    	if (!m_act.ids.contains(buy_id)) {
    		m_act.ids.add(buy_id);
    		m_act.cnts.add(0);
    	}
    	
    	int test_idx = -1;
    	for (int i = 0; i < m_act.ids.size(); ++i) {
    		if (m_act.ids.get(i) == buy_id && m_act.cnts.get(i) < m_cfg.getTimes()) {
    			test_idx = i;
    			break;
    		}
    	}
    	if (test_idx < 0) {
    		log.error(" sold out {}", buy_id);
    		return respond;
    	}
    	
    	PlayerImpl.SubDiamond(m_player, cost_dmd);
    	
    	respond.body.sync_player_info = new SyncPlayerInfoMsg();
		respond.body.sync_player_info.diamond = -cost_dmd;
    	
    	m_act.cnts.set(test_idx, m_act.cnts.get(test_idx) + 1);
    	m_act_db.save();
 
    	if (m_cfg.getGold() > 0) {
        	m_player.addGold(m_cfg.getGold());
        	respond.body.sync_player_info.gold = m_cfg.getGold();
        	msg.r_gold = m_cfg.getGold();
    	}
    	
    	m_player_db.save();
    	for (int i = 0; i < m_cfg.getItem().length; ++i)
    	{
    		int id = m_cfg.getItem()[i];
    		if (id == 0)
    			continue;
    		int cnt = m_cfg.getCounts()[i];
    		if (cnt == 0)
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
