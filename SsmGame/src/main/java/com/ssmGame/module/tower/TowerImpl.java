package com.ssmGame.module.tower;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Dailytask;
import com.ssmData.config.entity.Role;
import com.ssmData.config.entity.Scroll;
import com.ssmData.config.entity.Tower;
import com.ssmData.config.entity.Toweraward;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerRolesInfo;
import com.ssmData.dbase.PlayerRolesInfoDB;
import com.ssmData.dbase.PlayerScrollDB;
import com.ssmData.dbase.PlayerScrollInfo;
import com.ssmData.dbase.PlayerTowerDB;
import com.ssmData.dbase.PlayerTowerInfo;
import com.ssmData.dbase.RoleInfo;
import com.ssmData.dbase.ScrollInfo;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
import com.ssmGame.defdata.msg.tower.TowerMsg;
import com.ssmGame.module.battle.BattlePack;
import com.ssmGame.module.battle.BattleResult;
import com.ssmGame.module.battle.BattleSimulator;
import com.ssmGame.module.battle.BattleType;
import com.ssmGame.module.broadcast.BroadcastImpl;
import com.ssmGame.module.daily.DailyTaskImpl;
import com.ssmGame.module.daily.DailyTaskType;
import com.ssmGame.module.dmgreward.DmgRewardImpl;
import com.ssmGame.module.task.TaskImpl;
import com.ssmGame.module.task.TaskType;
import com.ssmGame.util.AwardUtils;
import com.ssmGame.util.ItemCountPair;
import com.ssmGame.util.TimeUtils;

@Service
@Scope("prototype")
public class TowerImpl {

	private static final Logger log = LoggerFactory.getLogger(TowerImpl.class);
	
	public final static TowerImpl getInstance(){
        return SpringContextUtil.getBean(TowerImpl.class);
	}
	
	@Autowired
	PlayerTowerDB m_tower_db;
	PlayerTowerInfo m_tower = null;
	
    @Autowired
    PlayerBagDB m_bag_db;
    PlayerBagInfo m_bag = null;
    
    @Autowired
    PlayerInfoDB m_player_db;
    PlayerInfo m_player = null;
    
    @Autowired
    PlayerRolesInfoDB m_roles_db;
    PlayerRolesInfo m_roles = null;
    
    @Autowired
    PlayerScrollDB m_scroll_db;
    PlayerScrollInfo m_scroll = null;
    
    
    public void destroy()
    {
    	m_tower_db = null;
    	m_tower = null;
    	m_bag_db = null;
    	m_bag = null;
    	m_player_db = null;
    	m_player = null;
    	m_roles_db = null;
    	m_roles = null;
    	m_scroll_db = null;
    	m_scroll = null;
    }
    
    public CommonMsg handleInfo(CommonMsg respond)
    {
		String uid = respond.header.uid;
		TowerMsg msg = new TowerMsg();
		respond.body.tower = msg;
		msg.success = false;
		
		m_tower = m_tower_db.loadByUid(uid);
		if (m_tower == null)
		{
			m_tower = (PlayerTowerInfo)m_tower_db.createDB(uid);
		}
		msg.info = m_tower;
    	
		msg.success = true;
    	return respond;
    }
    
    public CommonMsg handleEnemy(CommonMsg respond)
    {
    	String uid = respond.header.uid;
		TowerMsg msg = new TowerMsg();
		respond.body.tower = msg;
		msg.success = false;
    	
    	m_tower = m_tower_db.loadByUid(uid);
    	if (null == m_tower)
    	{
    		log.warn("TowerImpl.handleEnemy() no tower db");
    		return respond;
    	}
    			
    	if (m_tower.win)
    	{
    		log.warn("TowerImpl.handleEnemy() already win");
    		return respond;
    	}
    	
    	if (m_tower.cur_lv != m_tower.history_open)
    	{
    		log.warn("TowerImpl.handleEnemy() not in same level");
    		return respond;
    	}
    	
    	int layer_index = m_tower.cur_lv % 10;
    	if (layer_index == 0)
    	{
    		log.warn("TowerImpl.handleEnemy() is boss level");
    		return respond;
    	}
    	
    	int tower_id = (m_tower.cur_lv / 10) + 1; 
    	if (tower_id == 0)
    	{
    		log.warn("TowerImpl.handleEnemy() no tower id");
    		return respond;
    	}
    	
    	Tower t_cfg = ConfigConstant.tTower.get(tower_id);
    	if (t_cfg == null)
    	{
    		log.warn("TowerImpl.handleEnemy() no tower cfg id {}", tower_id);
    		return respond;
    	}
    	
    	m_player = m_player_db.loadById(uid);
    	if (null == m_player)
    	{
    		log.warn("TowerImpl.handleEnemy() no player id {}", uid);
    		return respond;
    	}
    	if (m_player.team_current_fighting >= t_cfg.getTowerMonFC()[layer_index - 1])
    	{
    		msg.is_win = true;
    		m_tower.win = true;
    		m_tower_db.save();
    	}
    	else
    		msg.is_win = false;
    	
    	msg.success = true;
    	return respond;
    }
    
    public CommonMsg handleBoss(CommonMsg respond)
    {
    	String uid = respond.header.uid;
		TowerMsg msg = new TowerMsg();
		respond.body.tower = msg;
		msg.success = false;
    	
    	m_tower = m_tower_db.loadByUid(uid);
    	if (null == m_tower)
    	{
    		log.warn("TowerImpl.handleBoss() no tower db");
    		return respond;
    	}
    	
    	if (m_tower.win)
    	{
    		log.warn("TowerImpl.handleBoss() already win");
    		return respond;
    	}
    	
    	if (m_tower.cur_lv != m_tower.history_open)
    	{
    		log.warn("TowerImpl.handleBoss() not in same level");
    		return respond;
    	}
    	
    	int layer_index = m_tower.cur_lv % 10;
    	if (layer_index != 0)
    	{
    		log.warn("TowerImpl.handleBoss() is not boss level");
    		return respond;
    	}
    	
    	int tower_id = m_tower.cur_lv / 10;
    	if (tower_id == 0)
    	{
    		log.warn("TowerImpl.handleBoss() no tower id");
    		return respond;
    	}
    	
    	Tower t_cfg = ConfigConstant.tTower.get(tower_id);
    	if (t_cfg == null)
    	{
    		log.warn("TowerImpl.handleBoss() no tower cfg id {}", tower_id);
    		return respond;
    	}
    	
    	m_roles = m_roles_db.load(uid);
    	if (null == m_roles)
    	{
    		log.warn("TowerImpl.handleBoss() no roles id {}", uid);
    		return respond;
    	}
    	
    	// 生成双方阵容
        Map<Integer, RoleInfo> my_roles = m_roles.genRoles();
        Map<Integer, RoleInfo> enemy_team = new HashMap<Integer, RoleInfo>();
        for (int i = 0; i < t_cfg.getBoss().length; ++i)
        {
			Role role_cfg = ConfigConstant.tRole.get(t_cfg.getBoss()[i]);
			if(role_cfg == null) {
				continue;
			}

        	RoleInfo r = new RoleInfo();
        	r.InitByRoleConfigIdAndLv(t_cfg.getBoss()[i], t_cfg.getBossLv()[i]);
        	r.skill_lv = ConfigConstant.tRole.get(t_cfg.getBoss()[i]).getSkillLv(); // 特殊技能等级
        	enemy_team.put(i+1, r);
        }
        
        BattleSimulator sim = new BattleSimulator();
        sim.InitAllActorLogic(my_roles, enemy_team, ConfigConstant.tConf.getTowerTime(), ConfigConstant.tConf.getBattletime(), BattleType.LEVEL_BOSS
        		, null, null, null, null);
        BattlePack battle_result = sim.Exe();
        DmgRewardImpl.RefreshMax(uid, battle_result);
        
        m_player = m_player_db.loadById(uid);
        msg.is_win = false;
        if (battle_result.m_result == BattleResult.LEFT_WIN)
        {
        	msg.is_win = true;
        	m_tower.win = true;
        	m_tower_db.save();
        	
        	if (t_cfg.getNotice() == 1)
        	{
        		String context = ConfigConstant.tWordconfig.get(BroadcastImpl.TOWERBOSS_KILL).getWord_cn();
            	context = context.replace("$1", m_player.user_base.getNickname());
            	context = context.replace("$2", Integer.toString(m_tower.cur_lv));
            	BroadcastImpl bi = BroadcastImpl.getInstance();
            	//System.out.println(context);
            	bi.SendBrocast(context, m_player.user_base.gid, m_player.user_base.zid);
        	}
        }
        
        // 记录最后战斗时间
        m_player.last_active_time = Calendar.getInstance().getTimeInMillis();
        m_player_db.save();
    	
        msg.battle_data = battle_result;
    	msg.success = true;
    	return respond;
    }

    public CommonMsg handleBox(CommonMsg respond)
    {
    	String uid = respond.header.uid;
		TowerMsg msg = new TowerMsg();
		respond.body.tower = msg;
		msg.success = false;
    	
		m_tower = m_tower_db.loadByUid(uid);
    	if (null == m_tower)
    	{
    		log.warn("TowerImpl.handleBox() no tower db {}", uid);
    		return respond;
    	}
    	
    	if (!m_tower.win)
    	{
    		//log.warn("TowerImpl.handleBox() not win {}", uid);
    		return respond;
    	}
    	
    	if (m_tower.history_box >= m_tower.cur_lv)
    	{
    		log.warn("TowerImpl.handleBox() already get {}", uid);
    		return respond;
    	}
    	
    	if (m_tower.cur_lv != m_tower.history_open)
    	{
    		log.warn("TowerImpl.handleBox() not in same level {}", uid);
    		return respond;
    	}
    	
    	int layer_index = m_tower.cur_lv % 10;
    	boolean is_boss = layer_index == 0;
    	
    	int tower_id = m_tower.cur_lv / 10;
    	if (!is_boss)
    		tower_id += 1;
    	if (tower_id == 0)
    	{
    		log.warn("TowerImpl.handleBox() no tower id {}", uid);
    		return respond;
    	}
    	
    	Tower t_cfg = ConfigConstant.tTower.get(tower_id);
    	if (t_cfg == null)
    	{
    		log.warn("TowerImpl.handleBox() no tower cfg id {}", uid);
    		return respond;
    	}
    	
    	m_bag = m_bag_db.loadByUid(uid);
    	if (m_bag == null)
    	{
    		log.warn("TowerImpl.handleBox() no bag id {}", uid);
    		return respond;
    	}
    	
    	m_player = m_player_db.loadById(uid);
    	if (m_player == null)
    	{
    		log.warn("TowerImpl.handleBox() no player id {}", uid);
    		return respond;
    	}
    	
    	m_tower.box_time = Calendar.getInstance().getTimeInMillis();
    	m_tower.history_box = m_tower.cur_lv;
    	int new_tower_id = ((m_tower.cur_lv + 1) / 10) + 1;
    	Tower new_t_cfg = ConfigConstant.tTower.get(new_tower_id);
    	if (new_t_cfg != null)
    	{
    		m_tower.cur_lv++;
    		m_tower.history_open++;
    		m_tower.win = false;
    	}
    	m_tower_db.save();
    	
    	int gold_cnt = 0;
		respond.body.sync_bag = new SyncBagMsg();
		Map<Integer, Integer> sync_bags = new HashMap<Integer, Integer>();
		List<SyncBagItem> r_bag_items = new ArrayList<SyncBagItem>();
		
    	if (is_boss)
    	{
    		gold_cnt = t_cfg.getGold()[1];
    		
    		List<ItemCountPair> r_bag = AwardUtils.GetAward(t_cfg.getAward());
    		for (ItemCountPair i : r_bag)
    		{
    			if (m_bag.addItemCount(i.m_item_id, i.m_count))
    			{
    				sync_bags.put(i.m_item_id, m_bag.getItemCount(i.m_item_id));
    	            SyncBagItem item = new SyncBagItem();
    	            item.id = i.m_item_id;
    	            item.count = i.m_count;
    	            r_bag_items.add(item);
    			}
    		}
    	}
    	else
    	{
    		gold_cnt = t_cfg.getGold()[0];
    	}
    	m_player.addGold(gold_cnt);
    	for (int i = 0; i < t_cfg.getTowerItem().length; ++i) {
    		int id = t_cfg.getTowerItem()[i];
    		int cnt = t_cfg.getItemNum()[i];
			if (m_bag.addItemCount(id, cnt))
			{
				if (!sync_bags.containsKey(id))
					sync_bags.put(id, 0);
				sync_bags.put(id, m_bag.getItemCount(id));
	            SyncBagItem item = new SyncBagItem();
	            item.id = id;
	            item.count = cnt;
	            r_bag_items.add(item);
			}
    	}    	

    	msg.r_items = r_bag_items;
    	msg.r_gold = gold_cnt;
    	respond.body.sync_bag.items = sync_bags;
    	respond.body.sync_player_info = new SyncPlayerInfoMsg();
    	respond.body.sync_player_info.gold = gold_cnt;
    	m_bag_db.save();
    	m_player_db.save();
    	
		//主线任务
		TaskImpl.doTask(respond.header.uid, TaskType.HAS_TOWER_LV, m_tower.history_box);
		
    	msg.success = true;
    	return respond;
    }
    
    private static final int S_ID = 501; //重置票ID；
    public CommonMsg handleReset(CommonMsg respond)
    {
    	String uid = respond.header.uid;
		TowerMsg msg = new TowerMsg();
		respond.body.tower = msg;
		msg.success = false;
		
		m_bag = m_bag_db.loadByUid(uid);
		if (null == m_bag) {
    		log.warn("TowerImpl.handleReset() no bag db");
    		return respond;
		}
    	
    	m_tower = m_tower_db.loadByUid(uid);
    	if (null == m_tower)
    	{
    		log.warn("TowerImpl.handleReset() no tower db");
    		return respond;
    	}
    	
    	m_scroll = m_scroll_db.loadByUid(uid);
    	if (null == m_scroll)
    	{
    		log.warn("TowerImpl.handleReset() no scroll db");
    		return respond;
    	}
    	
    	ScrollInfo s_info = m_scroll.Get(S_ID);
    	if (null == s_info)
    	{
    		log.warn("TowerImpl.handleReset() no scroll info id {}", S_ID);
    		return respond;
    	}
    	s_info.Refresh(Calendar.getInstance(), TimeUtils.TodayStart());
    	Scroll s_cfg = ConfigConstant.tScroll.get(S_ID);
    	
    	boolean use_scroll = true;
    	if (s_info.count <= 0)
    	{
    		use_scroll = false;
			if (!m_bag.hasItemCount(s_cfg.getItem(), 1)) {
				respond.header.rt_sub = 1109;
				//log.warn("TowerImpl.handleReset() no scroll enough");
				return respond;
			}
    	}
    	if (use_scroll) {
	    	s_info.count--;
	    	respond.body.sync_scroll = new PlayerScrollInfo();
			respond.body.sync_scroll.scroll_list = new ArrayList<ScrollInfo>();
			respond.body.sync_scroll.scroll_list.add(s_info);
	    	m_scroll_db.save();
    	}
    	else {
    		Map<Integer, Integer> sync_bags = new HashMap<Integer, Integer>();
			m_bag.subItemCount(s_cfg.getItem(), 1);
			sync_bags.put(s_cfg.getItem(), m_bag.getItemCount(s_cfg.getItem()));
        	if (respond.body.sync_bag == null)
        		respond.body.sync_bag = new SyncBagMsg();
			respond.body.sync_bag.items = sync_bags;
    	}
    	m_tower.cur_lv = 1;
    	m_tower_db.save();
    	
    	//日常任务
        Dailytask d_cfg = DailyTaskImpl.getCfg(DailyTaskType.TOWER_RESET);
        int add_result = DailyTaskImpl.AddDaily(d_cfg, 1, uid);
        if (d_cfg != null && add_result != 0)
        {
        	int id = ConfigConstant.tConf.getCharmItem();
        	int cnt = d_cfg.getTaskReward() * add_result;
        	m_bag.addItemCount(id, cnt);
        	if (respond.body.sync_bag == null)
        		respond.body.sync_bag = new SyncBagMsg();
        	respond.body.sync_bag.items.put(id, m_bag.getItemCount(id));
        }
        m_bag_db.save();
		
    	msg.success = true;
    	return respond;
    }
    
    public CommonMsg handleRaid(CommonMsg respond)
    {
    	String uid = respond.header.uid;
		TowerMsg msg = new TowerMsg();
		respond.body.tower = msg;
		msg.success = false;
		
    	m_tower = m_tower_db.loadByUid(uid);
    	if (null == m_tower)
    	{
    		log.warn("TowerImpl.handleRaid() no tower db");
    		return respond;
    	}
    	
    	if (m_tower.cur_lv != 1)
    	{
    		log.warn("TowerImpl.handleRaid() cur_lv != 1");
    		return respond;
    	}
    	
    	if (m_tower.history_open <= 0)
    	{
    		log.warn("TowerImpl.handleRaid() history_open <= 0");
    		return respond;
    	}
    	
    	m_bag = m_bag_db.loadByUid(uid);
    	if (m_bag == null)
    	{
    		log.warn("TowerImpl.handleRaid() no bag id {}", uid);
    		return respond;
    	}
    	
    	m_player = m_player_db.loadById(uid);
    	if (m_player == null)
    	{
    		log.warn("TowerImpl.handleRaid() no player id {}", uid);
    		return respond;
    	}
    	
    	m_tower.cur_lv = m_tower.history_open;
    	m_tower_db.save();
    	
    	double gold_cnt = 0;
    	
		Map<Integer, Integer> add_bags = new HashMap<Integer, Integer>();
    	for (int lv = 1; lv <= m_tower.history_open; ++lv)
    	{
    		int layer_index = lv % 10;
        	boolean is_boss = layer_index == 0;
        	
        	int tower_id = (lv / 10) + 1;
        	if (tower_id == 0)
        	{
        		log.warn("TowerImpl.handleRaid() no tower id");
        		continue;
        	}
        	
        	Tower t_cfg = ConfigConstant.tTower.get(tower_id);
        	if (t_cfg == null)
        	{
        		log.warn("TowerImpl.handleRaid() no tower cfg id {}", tower_id);
        		continue;
        	}
        	
        	if (is_boss)
        	{
        		gold_cnt += t_cfg.getGold()[1];
        		
        		List<ItemCountPair> r_bag = AwardUtils.GetAward(t_cfg.getAward());
        		for (ItemCountPair i : r_bag)
        		{
        			if (!add_bags.containsKey(i.m_item_id))
        				add_bags.put(i.m_item_id, 0);
        			add_bags.put(i.m_item_id, add_bags.get(i.m_item_id).intValue() + i.m_count);
        		}
        	}
        	else
        	{
        		gold_cnt += t_cfg.getGold()[0];
        	}
        	
        	for (int i = 0; i < t_cfg.getTowerItem().length; ++i) {
        		int id = t_cfg.getTowerItem()[i];
        		int cnt = t_cfg.getItemNum()[i];
    			if (!add_bags.containsKey(id))
    				add_bags.put(id, 0);
    			add_bags.put(id, add_bags.get(id).intValue() + cnt);
        	}
    	}
		Map<Integer, Integer> sync_bags = new HashMap<Integer, Integer>();
		List<SyncBagItem> r_bag_items = new ArrayList<SyncBagItem>();
		for (Entry<Integer, Integer> e : add_bags.entrySet())
		{
			m_bag.addItemCount(e.getKey(), e.getValue());
			sync_bags.put(e.getKey(), m_bag.getItemCount(e.getKey()));
            SyncBagItem item = new SyncBagItem();
            item.id = e.getKey();
            item.count = e.getValue();
            r_bag_items.add(item);
		}
    	
    	m_player.addGold(gold_cnt);
    	msg.r_gold = gold_cnt;
    	msg.r_items = r_bag_items;    	
    	respond.body.sync_bag = new SyncBagMsg();
    	respond.body.sync_bag.items = sync_bags;
    	respond.body.sync_player_info = new SyncPlayerInfoMsg();
    	respond.body.sync_player_info.gold = gold_cnt;
    	m_bag_db.save();
    	m_player_db.save();
    	
    	msg.success = true;
    	return respond;
    }

    public CommonMsg handleReward(CommonMsg respond, int reward_id)
    {
    	String uid = respond.header.uid;
		TowerMsg msg = new TowerMsg();
		respond.body.tower = msg;
		msg.success = false;
    	
    	m_tower = m_tower_db.loadByUid(uid);
    	if (null == m_tower)
    	{
    		//log.warn("TowerImpl.handleReward() no tower db");
    		return respond;
    	}
    	
    	if (m_tower.first_award.contains(reward_id))
    	{
    		//log.warn("TowerImpl.handleReward() alread get reward_id {}", reward_id);
    		return respond;
    	}
    	
    	Toweraward ta_cfg = ConfigConstant.tToweraward.get(reward_id);
    	if (ta_cfg == null)
    	{
    		//log.warn("TowerImpl.handleReward() no reward_id {}", reward_id);
    		return respond;
    	}
    	
    	if (m_tower.history_box < reward_id)
    	{
    		//log.warn("TowerImpl.handleReward() not enough LV {}", reward_id);
    		return respond;
    	}
    	
    	m_bag = m_bag_db.loadByUid(uid);
    	if (m_bag == null)
    	{
    		//log.warn("TowerImpl.handleReward() no bag id {}", uid);
    		return respond;
    	}
    	
    	m_player = m_player_db.loadById(uid);
    	if (m_player == null)
    	{
    		//log.warn("TowerImpl.handleReward() no player id {}", uid);
    		return respond;
    	}
    	
    	m_tower.first_award.add(reward_id);
    	m_tower_db.save();
    	
    	for (int i = 0; i < ta_cfg.getItemId().length; ++i)
    	{
    		int id = ta_cfg.getItemId()[i];
    		int cnt = ta_cfg.getItemNum()[i];
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
    	
    	m_player.addDiamond(ta_cfg.getDiamonds());
    	respond.body.sync_player_info = new SyncPlayerInfoMsg();
    	respond.body.sync_player_info.diamond = ta_cfg.getDiamonds();
    	msg.r_diamond = ta_cfg.getDiamonds();
    	m_player_db.save();
    	
		//主线任务
		TaskImpl.doTask(respond.header.uid, TaskType.HAS_GET_TOWER_AWARD_ID, reward_id);
		
    	msg.success = true;
    	return respond;
    }
}
