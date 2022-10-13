package com.ssmGame.module.duel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.tool.colligate.CommUtil;
import com.ssmCore.tool.colligate.DateUtil;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Dailytask;
import com.ssmData.config.entity.Katsuji;
import com.ssmData.config.entity.Maward;
import com.ssmData.config.entity.Victory;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerDuelDB;
import com.ssmData.dbase.PlayerDuelFightingDB;
import com.ssmData.dbase.PlayerDuelFightingInfo;
import com.ssmData.dbase.PlayerDuelInfo;
import com.ssmData.dbase.PlayerDuelTeamDB;
import com.ssmData.dbase.PlayerDuelTeamInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerRolesInfo;
import com.ssmData.dbase.PlayerRolesInfoDB;
import com.ssmData.dbase.PlayerScrollDB;
import com.ssmData.dbase.PlayerScrollInfo;
import com.ssmData.dbase.RoleInfo;
import com.ssmData.dbase.ScrollInfo;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.duel.DuelBattleMsg;
import com.ssmGame.defdata.msg.duel.DuelMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
import com.ssmGame.module.battle.ActorLogic;
import com.ssmGame.module.battle.BattlePack;
import com.ssmGame.module.battle.BattleResult;
import com.ssmGame.module.battle.BattleSide;
import com.ssmGame.module.battle.BattleSimulator;
import com.ssmGame.module.battle.BattleType;
import com.ssmGame.module.daily.DailyTaskImpl;
import com.ssmGame.module.daily.DailyTaskType;
import com.ssmGame.module.role.RoleAttrCalc;
import com.ssmGame.module.task.TaskImpl;
import com.ssmGame.module.task.TaskType;
import com.ssmGame.util.TimeUtils;

@Service
@Scope("prototype")
public class DuelImpl {
	private static final Logger log = LoggerFactory.getLogger(DuelImpl.class);
	
	private static final int OPEN_LV = 45;
	
	public final static DuelImpl getInstance(){
        return SpringContextUtil.getBean(DuelImpl.class);
	}
	
    @Autowired
    PlayerInfoDB m_player_db;
    PlayerInfo m_player = null;
    
    @Autowired
    PlayerBagDB m_bag_db;
    PlayerBagInfo m_bag = null;
	
    @Autowired
    PlayerRolesInfoDB m_roles_db;
    PlayerRolesInfo m_roles = null;
    
    @Autowired
    PlayerDuelDB m_duel_db;
    PlayerDuelInfo m_duel_info = null;
    
    @Autowired
    PlayerDuelTeamDB m_d_team_db;
    PlayerDuelTeamInfo m_d_team = null;
    
    @Autowired
    PlayerDuelFightingDB m_d_fight_db;
    PlayerDuelFightingInfo m_d_fight = null;
    
    @Autowired
    PlayerScrollDB m_scroll_db;
    PlayerScrollInfo m_scroll;
    
    public void destroy()
	{
		m_roles = null;
		m_roles_db = null;
		m_player = null;
		m_player_db = null;
		m_bag_db = null;
		m_bag = null;
		m_duel_db = null;
		m_duel_info = null;
		m_d_team_db = null;
		m_d_team = null;
		m_d_fight_db = null;
		m_d_fight = null;
		m_scroll_db = null;
		m_scroll = null;
	}
    
    private DuelMsg initMsg(CommonMsg respond)
    {
    	DuelMsg result = new DuelMsg();
    	result.success = false;
    	respond.body.duel = result;
    	return result;
    }
    
    public CommonMsg handleInfo(CommonMsg respond)
    {
    	String uid = respond.header.uid;    	
    	DuelMsg msg = initMsg(respond);
    	
    	Calendar now_cal = Calendar.getInstance();
    	long today_start = TimeUtils.TodayStart();
    	m_duel_info = m_duel_db.loadByUid(uid);
    	if (m_duel_info == null)
    	{
    		m_duel_info = (PlayerDuelInfo)m_duel_db.createDB(uid);
    	}
    	Refresh(now_cal, today_start, m_duel_info);
    	m_duel_db.save();
    	
    	m_scroll = m_scroll_db.loadByUid(uid);
    	int scroll_id = ConfigConstant.tDuel.getConsume();
    	ScrollInfo s_info = m_scroll.Get(scroll_id);
    	if (s_info == null)
    	{
    		s_info = new ScrollInfo();
    		s_info.InitByConfig(scroll_id);
			m_scroll.scroll_list.add(s_info);
    	}
    	s_info.Refresh(now_cal, today_start);
    	m_scroll_db.save();
		respond.body.sync_scroll = new PlayerScrollInfo();
		respond.body.sync_scroll.scroll_list = new ArrayList<ScrollInfo>();
		respond.body.sync_scroll.scroll_list.add(s_info);
    	
    	msg.my_info = m_duel_info;
    	msg.my_teams = new ArrayList<List<Integer>>();
    	
    	m_d_team = m_d_team_db.loadByUid(uid);
    	boolean need_init = false;
    	if (m_d_team == null)
    	{
    		need_init = true;
    		m_d_team = (PlayerDuelTeamInfo)m_d_team_db.createDB(uid);
    	}
    	if (m_d_team.team_0.size() <= 0 && m_d_team.team_1.size() <= 0 && m_d_team.team_2.size() <= 0){
    		need_init = true;
    	}
    	if (need_init)
    	{
    		m_roles = m_roles_db.load(uid);
    		for (int i = 1; i <= 5; ++i)
    		{
    			if (m_roles.pve_team.containsKey(i))
    				m_d_team.team_0.add(m_roles.pve_team.get(i).intValue());
    			else
    				m_d_team.team_0.add(0);
    		}
    		for (int i = 0; i < 5; ++i)
    		{
    			m_d_team.team_1.add(0);
    			m_d_team.team_2.add(0);
    		}
    		m_d_team_db.save();
    	}
    	msg.my_teams.add(m_d_team.team_0);
    	msg.my_teams.add(m_d_team.team_1);
    	msg.my_teams.add(m_d_team.team_2);
    	
    	msg.success = true;
    	//log.info("handleInfo SUCCESS");
    	return respond;
    }
    
    //上阵
    public CommonMsg handleAddRole(CommonMsg respond, int team_id, int pos, int role_id)
    {
    	String uid = respond.header.uid;
    	DuelMsg msg = initMsg(respond);
    	
    	if (pos < 1 || pos > 5)
    	{
    		log.info("handleAddRole no pos {}", pos);
    		return respond;
    	}
    	
    	if (team_id < 0 || team_id > 2)
    	{
    		log.info("handleAddRole no team {}", team_id);
    		return respond;
    	}
    	
    	m_roles = m_roles_db.load(uid);
    	RoleInfo role = m_roles.GetRole(role_id);
    	if (role == null)
    	{
    		log.info("handleAddRole no role_id {}", role_id);
    		return respond;
    	}
    	
    	m_d_team = m_d_team_db.loadByUid(uid);
    	List<List<Integer>> all_team = new ArrayList<List<Integer>>();
    	all_team.add(m_d_team.team_0);
    	all_team.add(m_d_team.team_1);
    	all_team.add(m_d_team.team_2);
    	for (List<Integer> t : all_team)
    	{
    		for (int i = 0; i < t.size(); ++i)
    		{
    			if (t.get(i) == role_id)
    			{
    				t.set(i, 0);
    				break;
    			}
    		}
    	}
    	all_team.get(team_id).set(pos - 1, role_id);
    	m_d_team_db.save();
    	
    	msg.my_teams = all_team;
    	msg.success = true;
    	//log.info("handleAddRole SUCCESS");
    	return respond;
    }
    
    //布阵
    public CommonMsg handleChangePos(CommonMsg respond, int team_id, List<Integer> new_pos)
    {
    	String uid = respond.header.uid;
    	DuelMsg msg = initMsg(respond);
    	
    	if (team_id < 0 || team_id > 2)
    	{
    		log.info("handleChangePos no team {}", team_id);
    		return respond;
    	}
    	
    	m_d_team = m_d_team_db.loadByUid(uid);
    	List<Integer> team = null;
    	switch (team_id)
    	{
    	case 0: team = m_d_team.team_0; break;
    	case 1: team = m_d_team.team_1; break;
    	case 2: team = m_d_team.team_2; break;
    	}
    	m_roles = m_roles_db.load(uid);
    	for (int id : new_pos)
    	{
    		if (id == 0)
    			continue;
    		if (m_roles.GetRole(id) == null || !team.contains(id))
    		{
    			log.info("handleChangePos team has no role_id {}", id);
    			return respond;
    		}
    	}
    	
    	for (int i = 0; i < new_pos.size(); ++i)
    	{
    		team.set(i, new_pos.get(i).intValue());
    	}
    	m_d_team_db.save();
    	
    	msg.success = true;
    	//log.info("handleChangePos SUCCESS");
    	return respond;
    }
    
    public CommonMsg handleOneKeyPos(CommonMsg respond, List<List<Integer>> new_pos)
    {
    	String uid = respond.header.uid;
    	DuelMsg msg = initMsg(respond);
    	
    	if (new_pos.size() != 3 || new_pos.get(0).size() != 5)
    	{
    		log.info("handleOneKeyPos pos list size ERROR!");
    		return respond;
    	}
    	
    	m_roles = m_roles_db.load(uid);
    	Set<Integer> temp = new HashSet<Integer>();
    	for (int i = 0; i < new_pos.size(); ++i)
    	{
    		List<Integer> t = new_pos.get(i);
    		for (int j = 0; j < t.size(); ++j)
    		{
    			int id = t.get(j);
    			if (id == 0 || j >= 5)
    				continue;
    			if (temp.contains(id))
    			{
    				log.info("handleOneKeyPos pos list Has SAME ID {} !", id);
    				return respond;
    			}
    			temp.add(id);
    	    	RoleInfo role = m_roles.GetRole(id);
    	    	if (role == null)
    	    	{
    	    		log.info("handleOneKeyPos no role_id {}", id);
    	    		return respond;
    	    	}
    		}
    	}
    	
    	m_d_team = m_d_team_db.loadByUid(uid);
    	m_d_team.team_0 = new_pos.get(0);
    	m_d_team.team_1 = new_pos.get(1);
    	m_d_team.team_2 = new_pos.get(2);
    	m_d_team_db.save();
    	
    	msg.success = true;
    	//log.info("handleOneKeyPos SUCCESS");
    	return respond;
    }
    
    //发起挑战
    public CommonMsg handleDuel(CommonMsg respond)
    {
    	//log.info("BEGIN DUEL =================");
    	String uid = respond.header.uid;
    	DuelMsg msg = initMsg(respond);
    	
    	m_scroll = m_scroll_db.loadByUid(uid);
    	
    	///////////检查队伍是否3个都有
    	m_d_team = m_d_team_db.loadByUid(uid);
    	List<List<Integer>> all_team = new ArrayList<List<Integer>>();
    	all_team.add(m_d_team.team_0);
    	all_team.add(m_d_team.team_1);
    	all_team.add(m_d_team.team_2);
    	for (List<Integer> t : all_team)
    	{
    		boolean has = false;
    		for (int i : t)
    		{
    			if (i != 0)
    			{
    				has = true;
    				break;
    			}
    		}
    		if (!has)
    		{
    			log.info("handleDuel Not Enough Team");
    			respond.header.rt_sub = 1141;
    			return respond;
    		}
    	}
    	
    	///////////检查挑战卷
    	int scroll_id = ConfigConstant.tDuel.getConsume();
    	ScrollInfo s_info = m_scroll.Get(scroll_id);
    	if (s_info == null)
    	{
    		log.info("handleDuel No Scroll Info id {}", scroll_id);
    		return respond;
    	}
    	Calendar now_cal = Calendar.getInstance();
    	long today_start = TimeUtils.TodayStart();
    	s_info.Refresh(now_cal, today_start);
    	if (s_info.count < ConfigConstant.tDuel.getQuantityC())
    	{
    		log.info("handleDuel Not Enough Scroll cnt {}", s_info.count);
    		respond.header.rt_sub = 1127;
    		m_scroll_db.save();
    		return respond;
    	}
    	
    	////////////更新战队人物数据和用于匹配的队伍战力数据，同时保存我自己的战斗数据
    	List<PlayerRolesInfo> all_infos = new ArrayList<PlayerRolesInfo>();
    	all_infos.add(m_d_team.roles_0);
    	all_infos.add(m_d_team.roles_1);
    	all_infos.add(m_d_team.roles_2);
    	m_roles = m_roles_db.load(uid);
    	m_d_fight = m_d_fight_db.loadByUid(uid);
    	if (m_d_fight == null)
    	{
    		m_d_fight = (PlayerDuelFightingInfo)m_d_fight_db.createDB(uid);
    	}
    	m_d_fight.fight_all = 0;
    	m_d_fight.team_fight.clear();
    	List<Map<Integer, RoleInfo>> all_my_fight_info = new ArrayList<Map<Integer, RoleInfo>>();
    	long my_all_hp = 0;
    	for (int i = 0; i < all_team.size(); ++i)
    	{
    		List<Integer> ids = all_team.get(i);
    		PlayerRolesInfo infos = all_infos.get(i);
    		
    		infos.InitEmpty();
    		for (Integer id : ids)
    		{
    			if (id <= 0)
    				continue;
    			infos.roles.add(m_roles.GetRole(id).Clone());
    		}
    		for (int pos = 1; pos <= ids.size(); ++pos)
    		{
    			infos.pve_team.put(pos, ids.get(pos - 1));
    		}
    		for (Integer id : ids)
    		{
    			if (id <= 0)
    				continue;
    			RoleAttrCalc.RefreshRoleAttr(id, infos);
    			my_all_hp += infos.GetRole(id).max_hp;
    		}
    		
    		int fight = infos.CalcTeamFighting();
    		m_d_fight.team_fight.add(fight);
    		m_d_fight.fight_all += fight;
    		all_my_fight_info.add(infos.genRoles());
    	}
    	m_d_team_db.save();
    	m_d_fight_db.save();
    	
    	////////////////////////开始构建匹配池
    	m_player = m_player_db.loadById(uid);
    	m_duel_info = m_duel_db.loadByUid(uid);
    	int min_lv = m_player.team_lv - ConfigConstant.tConf.getDuelMatch()[0] + (m_duel_info.wins >> 1); //* 0.5f
    	if (min_lv < OPEN_LV) {
    		min_lv = OPEN_LV;
    	}
    	int max_lv = m_player.team_lv - ConfigConstant.tConf.getDuelMatch()[1] + (m_duel_info.wins >> 1); //* 0.5f
    	if (max_lv < OPEN_LV) {
    		max_lv = OPEN_LV;
    	}
    	//log.info("duel min {} max {}", min_lv, max_lv);
    	List<PlayerDuelFightingInfo> duel_pool = new ArrayList<PlayerDuelFightingInfo>();
    	List<PlayerDuelFightingInfo> all_fight = BaseDaoImpl.getInstance().findAll(PlayerDuelFightingInfo.class);
    	//List<Integer> lv_list = new ArrayList<Integer>();
    	//List<PlayerInfo> p_list = new ArrayList<PlayerInfo>();
    	for (PlayerDuelFightingInfo i : all_fight)
    	{
    		PlayerDuelTeamInfo e_team = SpringContextUtil.getBean(PlayerDuelTeamDB.class).loadByUid(i.uid);
        	List<List<Integer>> e_all_team = new ArrayList<List<Integer>>();
        	e_all_team.add(e_team.team_0);
        	e_all_team.add(e_team.team_1);
        	e_all_team.add(e_team.team_2);
    		boolean has = true;
    		for (List<Integer> t : e_all_team)
        	{
        		for (int it : t)
        		{
        			if (it == 0)
        			{
        				has = false;
        				break;
        			}
        		}
        		if (!has)
        		{
        			break;
        		}
        	}
    		if (!has) {
    			continue;
    		}
    		PlayerInfo e = SpringContextUtil.getBean(PlayerInfoDB.class).loadById(i.uid);
    		//lv_list.add(e.team_lv);
    		if (e.team_lv >= min_lv && e.team_lv <= max_lv && !i.uid.equals(uid))
    		{
    			duel_pool.add(i);
    			//p_list.add(e);
    		}
    	}
    	//Collections.sort(lv_list);
    	//StringBuffer sb = new StringBuffer();
    	//for (PlayerInfo i : p_list) {
    	//	sb.append("[").append(i.user_base.getNickname()).append(" lv ").append(i.team_lv).append("] ");
    	//}
    	//log.info("pool all {}", sb.toString());
    	
    	/////////////////////////////////用匹配池数据构建战斗用的数据
    	msg.battle = new DuelBattleMsg();
    	msg.battle.e_team_fighting = new ArrayList<Integer>();
    	List<Map<Integer, RoleInfo>> enemy_fight_info = new ArrayList<Map<Integer, RoleInfo>>();
    	if (duel_pool.size() != 0)  //有玩家
    	{
    		int rand = CommUtil.getRandom(0, duel_pool.size() - 1);
    		//log.info("pool hit {}", rand);
    		PlayerDuelFightingInfo enemy = duel_pool.get(rand);
    		msg.battle.e_team_fighting = enemy.team_fight;
    		PlayerDuelTeamInfo enemy_team = SpringContextUtil.getBean(PlayerDuelTeamDB.class).loadByUid(enemy.uid);
        	List<PlayerRolesInfo> all_enemy_infos = new ArrayList<PlayerRolesInfo>();
        	all_enemy_infos.add(enemy_team.roles_0);
        	all_enemy_infos.add(enemy_team.roles_1);
        	all_enemy_infos.add(enemy_team.roles_2);
    		for (int i = 0; i < all_enemy_infos.size(); ++i)
    		{
    			enemy_fight_info.add(all_enemy_infos.get(i).genRoles());
    		}
    		
    		PlayerInfo e = SpringContextUtil.getBean(PlayerInfoDB.class).loadById(enemy.uid);
    		msg.battle.e_name = e.user_base.getNickname();
    		msg.battle.e_avatar = e.user_base.getuImg();
    		msg.battle.e_vip_lv = e.vip_level;
    		msg.battle.e_is_player = true;
    		msg.battle.e_team_lv = e.team_lv;
    	}
    	else // 机器人
    	{
    		msg.battle.e_is_player = false;
    		msg.battle.e_team_lv = m_player.team_lv;
    		genEnemyRoles(enemy_fight_info, all_infos);
    		for (int i = 0; i < enemy_fight_info.size(); ++i)
    		{
    			int team_f = 0;
    			for (Entry<Integer, RoleInfo> j : enemy_fight_info.get(i).entrySet())
        		{
    				team_f += j.getValue().fighting;
        		}
    			msg.battle.e_team_fighting.add(team_f);
    		}
    	}
    	msg.battle.e_team_leader = new ArrayList<RoleInfo>();
    	for (int i = 0; i < enemy_fight_info.size(); ++i)
    	{
    		for (Entry<Integer, RoleInfo> j : enemy_fight_info.get(i).entrySet())
    		{
    			msg.battle.e_team_leader.add(j.getValue());
    			break;
    		}
    	}
    	if (enemy_fight_info.size() != 3)
    	{
    		log.info("handleDuel Not Enough Enemy Team cnt {}", enemy_fight_info.size());
    		return respond;
    	}
    	
    	////////////更新挑战卷
    	s_info.count -= ConfigConstant.tDuel.getQuantityC();
    	m_scroll_db.save();
		respond.body.sync_scroll = new PlayerScrollInfo();
		respond.body.sync_scroll.scroll_list = new ArrayList<ScrollInfo>();
		respond.body.sync_scroll.scroll_list.add(s_info);
    	
    	///////////刷新挑战次数
    	m_duel_info = m_duel_db.loadByUid(uid);
    	Refresh(now_cal, today_start, m_duel_info);
    	m_duel_info.duel_count++;
    	
    	////////////////////开始战斗
    	Map<Integer, Integer> modify_left_hp = new HashMap<Integer, Integer>();
		Map<Integer, Integer> modify_left_rage = new HashMap<Integer, Integer>();
		Map<Integer, Integer> modify_right_hp = new HashMap<Integer, Integer>();
		Map<Integer, Integer> modify_right_rage = new HashMap<Integer, Integer>();
		List<BattlePack> result_list = new ArrayList<BattlePack>();
		msg.battle.script = result_list;
		int left = 0, right = 0;
		long last_left_team_hp = 0;
		//int round = 1;
    	for (; left < all_my_fight_info.size() && right < enemy_fight_info.size();)
    	{
    		//log.info("Round {} BEGIN :  Left {} right {}", round, left, right);
    		int battle_type = BattleType.DUEL;
    		if (left == 0 && right == 0)
    			battle_type = BattleType.DUEL_FIRST;
    		
    		int l_all_hp = 0; 
    		Map<Integer, RoleInfo> lt = all_my_fight_info.get(left);
    		for (RoleInfo r : lt.values())
    		{
    			l_all_hp += r.max_hp;
    		}
    		
    		int r_all_hp = 0; 
    		Map<Integer, RoleInfo> rt = enemy_fight_info.get(right);
    		for (RoleInfo r : rt.values())
    		{
    			r_all_hp += r.max_hp;
    		}
    		
    		last_left_team_hp = 0;
            BattleSimulator sim = new BattleSimulator();
            sim.InitAllActorLogic(all_my_fight_info.get(left), enemy_fight_info.get(right)
            		,ConfigConstant.tDuel.getCombatT(), 0, battle_type
            		, modify_left_hp, modify_left_rage, modify_right_hp, modify_right_rage);
            BattlePack battle_result = sim.Exe();
            battle_result.left_all_hp = l_all_hp;
            battle_result.right_all_hp = r_all_hp;
            result_list.add(battle_result);
            
            modify_left_hp.clear();
            modify_left_rage.clear();
            Map<Integer, RoleInfo> m_l = all_my_fight_info.get(left);
            for (RoleInfo r : m_l.values())
            {
            	modify_left_hp.put(r.role_id, 0);
            }
            modify_right_hp.clear();
            modify_right_rage.clear();
            m_l = enemy_fight_info.get(right);
            for (RoleInfo r : m_l.values())
            {
            	modify_right_hp.put(r.role_id, 0);
            }
            
            for (ActorLogic a : sim.m_all_actors_logic.values())
            {
            	if (!a.m_is_alive)
            	{
            		continue;
            	}
            	else if (a.m_side == BattleSide.Left)
            	{
            		modify_left_hp.put(a.m_r_inf.role_id, a.m_current_hp);
            		modify_left_rage.put(a.m_r_inf.role_id, a.m_current_rage);
            		last_left_team_hp += a.m_current_hp;
            	}
            	else
            	{
            		modify_right_hp.put(a.m_r_inf.role_id, a.m_current_hp);
            		modify_right_rage.put(a.m_r_inf.role_id, a.m_current_rage);
            	}
            }
            
            if (battle_result.m_result == BattleResult.DRAW)
            {
            	modify_left_hp.clear();
                modify_left_rage.clear();
                modify_right_hp.clear();
                modify_right_rage.clear();
            	left++;
            	right++;
            }
            else if (battle_result.m_result == BattleResult.LEFT_LOSE)
            {
            	modify_left_hp.clear();
                modify_left_rage.clear();
            	left++;
            }
            else if (battle_result.m_result == BattleResult.LEFT_WIN)
            {
                modify_right_hp.clear();
                modify_right_rage.clear();
            	right++;
            }
            else
            {
            	break;
            }
            
            //log.info("Round {} END :  Left {} right {}, result: {}", round, left, right, battle_result.m_result);
            //round++;
    	}
    	
    	///////////////////////////////胜利结算
    	m_bag = m_bag_db.loadByUid(uid);
    	int score_id = ConfigConstant.tDuel.getFeatsID(); 
    	int score_cnt = 0;
    	if (right >= all_my_fight_info.size() && left >= all_my_fight_info.size())
    	{
    		msg.battle.is_win = BattleResult.DRAW;
    		score_cnt = ConfigConstant.tDuel.getDraw();
    	}
    	else if (left >= all_my_fight_info.size())  //左边输了
    	{
    		msg.battle.is_win = BattleResult.LEFT_LOSE;
    		score_cnt = ConfigConstant.tDuel.getFloored();
    	}
    	else if (right >= enemy_fight_info.size()) //右边输了
    	{
    		msg.battle.is_win = BattleResult.LEFT_WIN;
    		int i = left + 1;
    		while (i < all_my_fight_info.size())
    		{
    			Map<Integer, RoleInfo> m = all_my_fight_info.get(i++);
    			for (RoleInfo r : m.values())
    			{
    				last_left_team_hp += r.max_hp;
    			}
    		}
    		float win_hp_rate = (float)last_left_team_hp / my_all_hp;
    		int win_hp_int = (int)Math.floor(win_hp_rate * 100);
    		for (Victory v : ConfigConstant.tVictory.values())
    		{
    			if (v.getHP()[0] < win_hp_int && win_hp_int <= v.getHP()[1])
    			{
    				score_cnt = v.getFeats();
    				break;
    			}
    		}
    		
    		m_duel_info.wins++;
    	}
    	else
    	{
    		log.info("handleDuel Round ERROR!");
    		return respond;
    	}
    	
    	if (score_cnt > 0)
    	{
    		m_bag.addItemCount(score_id, score_cnt);
        	msg.r_items = new ArrayList<SyncBagItem>();
        	SyncBagItem a = new SyncBagItem();
        	a.id = score_id;
        	a.count = score_cnt;
        	msg.r_items.add(a);
        	respond.body.sync_bag = new SyncBagMsg();
    		respond.body.sync_bag.items.put(score_id, m_bag.getItemCount(score_id));
        	
        	m_duel_info.max_score += score_cnt;
        	if (m_duel_info.max_score > ConfigConstant.tDuel.getFeatsMax())
        	{
        		m_duel_info.max_score = ConfigConstant.tDuel.getFeatsMax();
        	}
        	m_duel_db.save();
    	}
    	
    	//日常任务
        Dailytask d_cfg = DailyTaskImpl.getCfg(DailyTaskType.DUEL);
        int add_result = DailyTaskImpl.AddDaily(d_cfg, 1, m_bag.uid);
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
    	
    	//主线
        TaskImpl.doTask(uid, TaskType.DUEL, 1);
        
        // 记录最后战斗时间
        m_player.last_active_time = Calendar.getInstance().getTimeInMillis();
        m_player_db.save();
    	
    	msg.success = true;
    	//log.info("handleDuel SUCCESS");
    	//log.info("END DUEL =================");
    	return respond;
    }

    //领取胜利次数奖
    public CommonMsg handleWinsCntAward(CommonMsg respond, int award_id)
    {
    	String uid = respond.header.uid;
    	DuelMsg msg = initMsg(respond);
    	
    	m_bag = m_bag_db.loadByUid(uid);
    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
    	
    	Katsuji k_config = ConfigConstant.tKatsuji.get(award_id);
    	if (null == k_config)
    	{
    		log.info("handleWinsCntAward NO Katsuji ID {}", award_id);
    		return respond;
    	}
    	
    	m_duel_info = m_duel_db.loadByUid(uid);
    	Refresh(Calendar.getInstance(), TimeUtils.TodayStart(), m_duel_info);
    	
    	if (m_duel_info.wins_awards.contains(award_id))
    	{
    		log.info("handleWinsCntAward ALREADY AWARD {}", award_id);
    		return respond;
    	}
    	
    	if (m_duel_info.wins < award_id)
    	{
    		log.info("handleWinsCntAward NOT ENOUGH AWARD {}", m_duel_info.wins);
    		return respond;
    	}
    	
    	m_duel_info.wins_awards.add(award_id);
    	respond.body.sync_player_info = new SyncPlayerInfoMsg();

    	m_player = m_player_db.loadById(uid);
    	if (k_config.getGold() > 0)
    	{
    		m_player.addGold(k_config.getGold());
    		msg.r_gold = k_config.getGold();
			respond.body.sync_player_info.gold = k_config.getGold();
    	}
    	if (k_config.getJewel() > 0)
    	{
    		m_player.addDiamond(k_config.getJewel());
    		msg.r_diamond = k_config.getJewel();
			respond.body.sync_player_info.diamond = k_config.getJewel();
    	}
    	m_player_db.save();
    	int score_id = ConfigConstant.tDuel.getFeatsID();
    	for (int i = 0; i < k_config.getItem().length; ++i)
    	{
    		int id = k_config.getItem()[i];
    		int cnt = k_config.getItemN()[i];
    		m_bag.addItemCount(id, cnt);
    		if (id == score_id)
    		{
    			m_duel_info.max_score += cnt;
    			if (m_duel_info.max_score > ConfigConstant.tDuel.getFeatsMax())
            	{
            		m_duel_info.max_score = ConfigConstant.tDuel.getFeatsMax();
            	}
    		}
        	
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
    	m_duel_db.save();
    	
    	msg.success = true;
    	//log.info("handleWinsCntAward SUCCESS");
    	return respond;
    }
    
    public CommonMsg handleHistoryScoreAward(CommonMsg respond, int award_id)
    {
    	String uid = respond.header.uid;
    	DuelMsg msg = initMsg(respond);
    	
    	Maward m_cfg = ConfigConstant.tMaward.get(award_id);
    	if (null == m_cfg)
    	{
    		log.info("handleHistoryScoreAward NO Maward ID {}", award_id);
    		return respond;
    	}
    	
    	m_bag = m_bag_db.loadByUid(uid);
    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
    	
    	m_duel_info = m_duel_db.loadByUid(uid);
    	if (m_duel_info.max_score < award_id)
    	{
    		log.info("handleHistoryScoreAward NOT ENOUGH AWARD {}", m_duel_info.max_score);
    		return respond;
    	}
    	
    	if (m_duel_info.score_awards.contains(award_id))
    	{
    		log.info("handleHistoryScoreAward ALREADY AWARD {}", award_id);
    		return respond;
    	}
    	m_duel_info.score_awards.add(award_id);
    	m_duel_db.save();
    	
    	m_player = m_player_db.loadById(uid);
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
    		int cnt = m_cfg.getItemN()[i];
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
    	
		//主线任务
		TaskImpl.doTask(respond.header.uid, TaskType.HAS_GET_DUEL_AWARD_ID, award_id);
		
    	return respond;
    }
    
    private static void Refresh(Calendar now_cal, long today_start, PlayerDuelInfo info)
    {
		long now = now_cal.getTimeInMillis();
    	int[] rt = new int[]{ConfigConstant.tConf.getRTime()};
    	if (DateUtil.checkPassTime(info.last_reset_wins, now, rt))
    	{
    		info.wins = 0;
			info.last_reset_wins = now;
			info.wins_awards.clear();
			info.duel_count = 0;
    	}
    }
    
    private void genEnemyRoles(List<Map<Integer, RoleInfo>> enemy_fight_info, List<PlayerRolesInfo> my_info)
    {
    	int[] robots = new int[ConfigConstant.tDuel.getRobot().length];
    	for (int i = 0; i < robots.length; ++i)
    	{
    		robots[i] = ConfigConstant.tDuel.getRobot()[i];
    	}
    	
    	int rand_index = 0;
    	for (int i = 0; i < my_info.size(); ++i)
    	{
    		Map<Integer, RoleInfo> e_team = new HashMap<Integer, RoleInfo>();
    		enemy_fight_info.add(e_team);
    		PlayerRolesInfo my_team = my_info.get(i);
    		for (int pos = 1; pos <= my_team.pve_team.size(); ++pos)
    		{
    			int level = 1;
    			int my_id = my_team.pve_team.get(pos);
    			if (my_id != 0)
    			{
    				level = my_team.GetRole(my_id).lv;
    			}
    			
    			int r_idx = (int)(Math.random() * (robots.length - rand_index));
    			int robot_id = robots[r_idx];
    			robots[r_idx] = robots[robots.length - rand_index - 1];
    			rand_index++;
    			RoleInfo r = new RoleInfo();
            	r.InitByRoleConfigIdAndLv(robot_id, level);
    			e_team.put(pos, r);
    			//todo 敌人战力
    		}
    	}
    }

    public CommonMsg handleDropTeam(CommonMsg respond, CommonMsg receive) {
    	String uid = respond.header.uid;
    	DuelMsg msg = initMsg(respond);
    	int team_id = receive.body.duel.team_id;
    	
    	m_d_team = m_d_team_db.loadByUid(uid);
    	if (m_d_team == null) {
    		log.error("no team db {}", uid);
    		return respond;
    	}
    	List<Integer> t = null;
    	switch (team_id) {
    		case 0:
    			t = m_d_team.team_0;
    			break;
    		case 1:
    			t = m_d_team.team_1;
    			break;
    		case 2:
    			t = m_d_team.team_2;
    			break;
    	}
    	if (t == null) {
    		log.error("no team idx  player {} idx ", uid, team_id);
    		return respond;
    	}
		for (int i = 0; i < t.size(); ++i)
		{
			t.set(i, 0);
		}
		m_d_team_db.save();
		
		msg.success = true;
    	return respond;
    }
}
