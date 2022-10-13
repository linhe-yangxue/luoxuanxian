package com.ssmGame.module.lvgift;

import java.util.ArrayList;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Lvgift;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerMonthcardDB;
import com.ssmData.dbase.PlayerMonthcardInfo;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.priv.PrivMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;

@Service
@Scope("prototype")
public class LvgiftImpl {
	private static final Logger log = LoggerFactory.getLogger(LvgiftImpl.class);
	
    @Autowired
    PlayerInfoDB m_player_db;
    PlayerInfo m_player = null;
    
    @Autowired
    private PlayerBagDB m_bag_db;
    private PlayerBagInfo m_bag = null;
    
    @Autowired
    private PlayerMonthcardDB m_month_db;
    private PlayerMonthcardInfo m_month = null;
    
	public void destroy()
	{		
		m_player_db = null;
		m_player = null;
		m_bag_db = null;
		m_bag = null;
		m_month = null;
		m_month_db = null;
	}
	
	public final static LvgiftImpl getInstance(){
        return SpringContextUtil.getBean(LvgiftImpl.class);
	}
	
	public CommonMsg handleInfo(CommonMsg respond)
	{
		String uid = respond.header.uid;
		PrivMsg msg = new PrivMsg();
		respond.body.priv = msg;
		msg.success = false;
    	
    	m_player = m_player_db.loadById(uid);
    	m_month = m_month_db.loadByUid(uid);
    	Refresh(m_month, m_player);
    	m_month_db.save();
    	
    	msg.lvgift_t = m_month.lvgift_t;
    	msg.lvgift_id = m_month.lvgift_id;
    	msg.lvgift_st = m_month.lvgift_st;
    	msg.success = true;
    	return respond;
	}
	
	public CommonMsg handleReward(CommonMsg respond)
	{
		String uid = respond.header.uid;
		PrivMsg msg = new PrivMsg();
		respond.body.priv = msg;
		msg.success = false;
    	
    	m_month = m_month_db.loadByUid(uid);
    	if (m_month == null) {
    		log.warn("LvgiftImpl.handleReward() no m_month_db id {}", uid);
    		return respond;
    	}
    	
    	int found = -1;
    	for (int i = 0; i < m_month.lvgift_id.size(); i++){
    		if (m_month.lvgift_st.get(i) == PlayerMonthcardInfo.LVGIFT_HOLDING) {
    			found = i;
    			break;
    		}
    	}
    	if (found < 0) {
    		log.warn("No lv gift holding {}", uid);
    		return respond;
    	}
    	
    	Lvgift lv_cfg = ConfigConstant.tLvgift.get(m_month.lvgift_id.get(found));
    	if (lv_cfg == null) {
    		log.warn("LvgiftImpl.handleReward() no Lvgift id {} cfg{}", uid, m_month.lvgift_id.get(found));
    		return respond;
    	}
    	
    	m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.warn("LvgiftImpl.handleReward() no m_player id {}", uid);
    		return respond;
    	}
    	
    	m_bag = m_bag_db.loadByUid(uid);
    	if (m_bag == null) {
    		log.warn("LvgiftImpl.handleReward() no m_bag id {}", uid);
    		return respond;
    	}
    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
    	m_month.lvgift_st.set(found, PlayerMonthcardInfo.LVGIFT_FINISH);
    	Refresh(m_month, m_player);
    	m_month_db.save();
    	
    	if (lv_cfg.getGold() > 0)
    	{
    		m_player.addGold(lv_cfg.getGold());
    		if (respond.body.sync_player_info == null)
    			respond.body.sync_player_info = new SyncPlayerInfoMsg();
    		respond.body.sync_player_info.gold = lv_cfg.getGold();
    		msg.r_gold = lv_cfg.getGold();
    	}
    	if (lv_cfg.getJewel() > 0)
    	{
    		m_player.addDiamond(lv_cfg.getJewel());
    		if (respond.body.sync_player_info == null)
				respond.body.sync_player_info = new SyncPlayerInfoMsg();
			respond.body.sync_player_info.diamond = lv_cfg.getJewel();
    		msg.r_diamond = lv_cfg.getJewel();
    	}
    	m_player_db.save();
		
    	for (int i = 0; i < lv_cfg.getItem().length; ++i)
    	{
    		int id = lv_cfg.getItem()[i];
    		int cnt = lv_cfg.getQuantity()[i];
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
	
	public static void DoLvUp(PlayerInfo player) {
		PlayerMonthcardDB t_db = SpringContextUtil.getBean(PlayerMonthcardDB.class);
		PlayerMonthcardInfo info = t_db.loadByUid(player._id);
		if (info != null) {
			Refresh(info, player);
			t_db.save();
		}
	}
	
	public static void Refresh(PlayerMonthcardInfo info, PlayerInfo player)
	{
		long now = Calendar.getInstance().getTimeInMillis();
		if (info.lvgift_id == null) {
			info.lvgift_id = new ArrayList<Integer>();
			info.lvgift_st = new ArrayList<Integer>();
			info.lvgift_t = 0L;
		}
    	for (int i = 0; i < info.lvgift_id.size(); i++){
    		Lvgift cfg = ConfigConstant.tLvgift.get(info.lvgift_id.get(i));
    		if (cfg == null) {
    			continue;
    		}
    		if (cfg.getTime() <= 0) {
    			continue;
    		}
    		if (info.lvgift_st.get(i) == PlayerMonthcardInfo.LVGIFT_ONSALE) {
    			if (info.lvgift_t >= now) {
    				return;
    			} else {
    				info.lvgift_st.set(i, PlayerMonthcardInfo.LVGIFT_OUT);
    				break;
    			}
    		}
    		else if (info.lvgift_st.get(i) == PlayerMonthcardInfo.LVGIFT_HOLDING) {
    			return;
    		}
    	}
    	
    	int lv_min = player.team_lv + 1;
		Lvgift found = null;
		for (Lvgift l_cfg : ConfigConstant.tLvgift.values()) {
			if (info.lvgift_id.contains(l_cfg.getID())) {
				continue;
			}
			if (l_cfg.getGiftLv() <= player.team_lv && l_cfg.getGiftLv() < lv_min) {
				lv_min = l_cfg.getGiftLv();
				found = l_cfg;
			}
		}
		if (found != null) {
			info.lvgift_id.add(found.getID());
			info.lvgift_st.add(PlayerMonthcardInfo.LVGIFT_ONSALE);
			info.lvgift_t = now + found.getTime() * 1000;
		}
	}
}
