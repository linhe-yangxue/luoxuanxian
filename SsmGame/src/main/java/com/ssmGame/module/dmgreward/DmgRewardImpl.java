package com.ssmGame.module.dmgreward;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Damagereward;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerDmgRewardDB;
import com.ssmData.dbase.PlayerDmgRewardInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmGame.defdata.msg.battle.DmgMsg;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
import com.ssmGame.module.battle.ActorLogic;
import com.ssmGame.module.battle.BattlePack;
import com.ssmGame.module.battle.BattleSide;
import com.ssmGame.module.task.TaskImpl;
import com.ssmGame.module.task.TaskType;

@Service
@Scope("prototype")
public class DmgRewardImpl {
	@Autowired
    PlayerInfoDB m_player_db;
    PlayerInfo m_player = null;
    
    @Autowired
    PlayerBagDB m_bag_db;
    PlayerBagInfo m_bag = null;
    
    @Autowired
    PlayerDmgRewardDB m_dmg_db;
    PlayerDmgRewardInfo m_dmg = null;
    
    public void destroy()
	{
		m_player = null;
		m_player_db = null;
		m_bag_db = null;
		m_bag = null;
		m_dmg_db = null;
		m_dmg = null;
	}
	
	private static final Logger log = LoggerFactory.getLogger(DmgRewardImpl.class);
	
	public final static DmgRewardImpl getInstance(){
        return SpringContextUtil.getBean(DmgRewardImpl.class);
	}
	
	public static void RefreshMax(String uid, BattlePack bp){
		if (null == bp) {
			return;
		}
		int new_max = 0;
        for (ActorLogic a : bp.m_actors_data)
        {
        	if (a.m_side == BattleSide.Left && a.dps > new_max) {
        		new_max = a.dps;
        	}
        }
		PlayerDmgRewardDB db = SpringContextUtil.getBean(PlayerDmgRewardDB.class);
		PlayerDmgRewardInfo info = db.loadByUid(uid);
		if (info == null) {
			info = (PlayerDmgRewardInfo) db.createDB(uid);
		}
		if (new_max > info.max) {
			info.max = new_max;
			db.save();
		}
		db = null;
		info = null;
	}

	public CommonMsg handleInfo(CommonMsg respond)
	{
    	String uid = respond.header.uid;
		DmgMsg msg = new DmgMsg();
		respond.body.dmg = msg;
		msg.success = false;
		
    	m_dmg = m_dmg_db.loadByUid(uid);
    	if (m_dmg == null) {
    		m_dmg = (PlayerDmgRewardInfo) m_dmg_db.createDB(uid);
    	}
    	msg.award = m_dmg.award;
    	msg.max = m_dmg.max;
		msg.success = true;
		return respond;
	}
	
	public CommonMsg handleReward(CommonMsg respond, int award_id)
    {
    	String uid = respond.header.uid;
    	DmgMsg msg = new DmgMsg();
		respond.body.dmg = msg;
		msg.success = false;
		
    	m_bag = m_bag_db.loadByUid(uid);
    	if (m_bag == null) {
    		log.warn("DmgRewardImpl.handleReward() no bag id {}", uid);
    		return respond;
    	}
    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
    	
		Damagereward m_cfg = ConfigConstant.tDamagereward.get(award_id);
    	if (null == m_cfg)
    	{
    		log.warn("DmgRewardImpl.handleReward NO reward ID {}", award_id);
    		return respond;
    	}
    	
    	m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.warn("DmgRewardImpl.handleReward NO player ID {}", uid);
    		return respond;
    	}
    	
    	m_dmg = m_dmg_db.loadByUid(uid);
    	if (m_dmg == null) {
    		log.warn("DmgRewardImpl.handleReward NO DB {}", uid);
    		return respond;
    	}
    	
    	if (m_dmg.max < m_cfg.getDamage()) {
    		log.warn("DmgRewardImpl.handleReward NO Enough Dmg {}  max {}", uid, m_dmg.max);
    		return respond;
    	}
    	
    	if (m_dmg.award.contains(award_id)) {
    		log.warn("DmgRewardImpl.handleReward Already get Award {}", award_id);
    		return respond;
    	}
    	
    	m_dmg.award.add(award_id);
    	m_dmg_db.save();
    	
    	if (m_cfg.getMoney() > 0)
    	{
    		m_player.addGold(m_cfg.getMoney());
    		if (respond.body.sync_player_info == null)
    			respond.body.sync_player_info = new SyncPlayerInfoMsg();
    		respond.body.sync_player_info.gold = m_cfg.getMoney();
    		msg.r_gold = m_cfg.getMoney();
    	}
    	if (m_cfg.getDiamonds() > 0)
    	{
    		m_player.addDiamond(m_cfg.getDiamonds());
    		if (respond.body.sync_player_info == null)
				respond.body.sync_player_info = new SyncPlayerInfoMsg();
			respond.body.sync_player_info.diamond = m_cfg.getDiamonds();
    		msg.r_diamond = m_cfg.getDiamonds();
    	}
    	m_player_db.save();
    	
    	for (int i = 0; i < m_cfg.getItemId().length; ++i)
    	{
    		int id = m_cfg.getItemId()[i];
    		int cnt = m_cfg.getItemNum()[i];
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
    	
    	//主线任务
    	TaskImpl.doTask(respond.header.uid, TaskType.HAS_GET_DMG_AWARD_ID, award_id);
    	
    	msg.success = true;
    	return respond;
    }
}
