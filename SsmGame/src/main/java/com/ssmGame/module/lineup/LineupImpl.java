package com.ssmGame.module.lineup;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Lineup;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerLineupDB;
import com.ssmData.dbase.PlayerLineupInfo;
import com.ssmData.dbase.PlayerRolesInfo;
import com.ssmData.dbase.PlayerRolesInfoDB;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.role.RoleResultMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
import com.ssmGame.module.task.TaskImpl;
import com.ssmGame.module.task.TaskType;

@Service
@Scope("prototype")
public class LineupImpl {
	@Autowired
    PlayerInfoDB m_player_db;
    PlayerInfo m_player = null;
    
	@Autowired
    PlayerRolesInfoDB m_roles_db;
    PlayerRolesInfo m_roles = null;
    
    @Autowired
    PlayerBagDB m_bag_db;
    PlayerBagInfo m_bag = null;
    
    @Autowired
    PlayerLineupDB m_act_db;
    PlayerLineupInfo m_act;
    
    public void destroy()
	{
		m_player = null;
		m_player_db = null;
		m_bag_db = null;
		m_bag = null;
		m_act_db = null;
		m_act = null;
	}
	
	private static final Logger log = LoggerFactory.getLogger(LineupImpl.class);
	
	public final static LineupImpl getInstance(){
        return SpringContextUtil.getBean(LineupImpl.class);
	}
	
	public CommonMsg handleInfo(CommonMsg respond)
	{
    	String uid = respond.header.uid;
		RoleResultMsg msg = new RoleResultMsg();
		respond.body.role_result = msg;
		msg.is_success = false;
		
    	m_act = m_act_db.loadByUid(uid);
    	if (m_act == null) {
    		m_act = (PlayerLineupInfo) m_act_db.createDB(uid);
    	}
    	    	
    	msg.award = m_act.award;
		msg.is_success = true;
		return respond;
	}
	
	public CommonMsg handleReward(CommonMsg respond, int reward_id)
    {
    	String uid = respond.header.uid;
    	RoleResultMsg msg = new RoleResultMsg();
		respond.body.role_result = msg;
		msg.is_success = false;
    	
    	Lineup m_cfg = ConfigConstant.tLineup.get(reward_id);
    	if (null == m_cfg) {
    		log.warn("LineupImpl.handleReward NO reward ID {}", reward_id);
    		return respond;
    	}
    	
    	m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.warn("LineupImpl.handleReward NO player ID {}", uid);
    		return respond;
    	}
    	
    	m_roles = m_roles_db.load(uid);
    	if (m_roles == null) {
    		log.warn("LineupImpl.handleReward NO roles ID {}", uid);
    		return respond;
    	}
    	
    	m_bag = m_bag_db.loadByUid(uid);
    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE){
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
    	
    	m_act = m_act_db.loadByUid(uid);
    	if (m_act == null) {
    		log.warn("LineupImpl.handleReward NO DB {}", uid);
    		return respond;
    	}
    	
    	if (m_act.award.contains(reward_id)) {
    		//log.warn("LineupImpl.handleReward ALREADY ID {}", uid);
    		return respond;
    	}
    	
    	for (int i = 0; i < m_cfg.getLineupRole().length; ++i) {
    		if (m_roles.GetRole(m_cfg.getLineupRole()[i]) == null) {
    			log.warn("LineupImpl.handleReward Not Have Role ID {}", m_cfg.getLineupRole()[i]);
    			return respond;
    		}
    	}
    	
    	m_act.award.add(reward_id);
    	m_act_db.save();
    	
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
    	
    	//主线任务
    	TaskImpl.doTask(respond.header.uid, TaskType.HAS_GET_LINEUP_AWARD_ID, reward_id);
    	
    	msg.is_success = true;
    	return respond;
    }
}
