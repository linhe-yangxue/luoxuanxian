package com.ssmGame.module.arena;

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
import com.ssmData.config.entity.Arena;
import com.ssmData.config.entity.Dailytask;
import com.ssmData.config.entity.Rankaward;
import com.ssmData.config.entity.Robot;
import com.ssmData.config.entity.Scroll;
import com.ssmData.dbase.ArenaDB;
import com.ssmData.dbase.ArenaInfo;
import com.ssmData.dbase.ArenaRankInfo;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerMonthcardDB;
import com.ssmData.dbase.PlayerMonthcardInfo;
import com.ssmData.dbase.PlayerRolesInfo;
import com.ssmData.dbase.PlayerRolesInfoDB;
import com.ssmData.dbase.PlayerScrollDB;
import com.ssmData.dbase.PlayerScrollInfo;
import com.ssmData.dbase.RoleInfo;
import com.ssmData.dbase.ScrollInfo;
import com.ssmData.dbase.enums.ArenaPlayerType;
import com.ssmGame.defdata.msg.arena.ArenaEnemyInfo;
import com.ssmGame.defdata.msg.arena.ArenaMsg;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
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
import com.ssmGame.util.TimeUtils;

@Service
@Scope("prototype")
public class ArenaImpl {
	
	private static final Logger log = LoggerFactory.getLogger(ArenaImpl.class);
	
    @Autowired
    ArenaDB m_arena_db;
    ArenaInfo m_arena_info;
    
    @Autowired
    PlayerInfoDB m_player_db;
    PlayerInfo m_player = null;
    
    @Autowired
    PlayerScrollDB m_scroll_db;
    PlayerScrollInfo m_scroll;
    
    @Autowired
    PlayerRolesInfoDB m_roles_db;
    PlayerRolesInfo m_roles = null;
    
    @Autowired
    PlayerRolesInfoDB m_enemy_db;
    PlayerRolesInfo m_enemy = null;
    
    @Autowired
    PlayerBagDB m_bag_db;
    PlayerBagInfo m_bag = null;
    
    String m_uuid = null;
    
    private static final int GEN_NUM = 4; //最多随机4个对手
    
	public final static ArenaImpl getInstance(){
        return SpringContextUtil.getBean(ArenaImpl.class);
	}
    
    public ArenaImpl init(String uuid)
	{
    	m_uuid = uuid;
    	m_arena_info = m_arena_db.load();
    	m_player = m_player_db.loadById(uuid);
    	m_scroll = m_scroll_db.loadByUid(uuid);
    	m_roles = m_roles_db.load(uuid);
    	m_bag = m_bag_db.loadByUid(uuid);
    	
		if (null == m_player)
		{
			return null;
		}
		return this;
	}
    
	public void destroy()
	{
		m_arena_info = null;
		m_arena_db = null;
		m_player = null;
		m_player_db = null;
		m_scroll = null;
		m_scroll_db = null;
		m_roles = null;
		m_roles_db = null;
		m_bag_db = null;
		m_bag = null;
	}
	
	public CommonMsg handleNewPlayer(CommonMsg respond)
	{
		ArenaMsg a_msg = new ArenaMsg();
		respond.body.arena = a_msg;
		a_msg.success = false;
		
		int cur = 0;
		Entry<Integer, ArenaRankInfo> my_info_entry = m_arena_info.findMyRank(m_uuid);
		if (my_info_entry == null)
		{
			cur = m_arena_db.UpdateWithoutLock(m_uuid, 0, true);
			m_player.history_arena = cur;
		}
		else
		{
			cur = my_info_entry.getKey();
			if (m_player.history_arena <= 0 || cur < m_player.history_arena)
				m_player.history_arena = cur;
		}
		m_player_db.save();
		
		a_msg.success = true;
		a_msg.my_cur_rank = cur;
		a_msg.my_history_rank = cur;
		return respond;
	}
	
	public CommonMsg handelReqChallengeList(CommonMsg respond)
	{
		ArenaMsg a_msg = new ArenaMsg();
		respond.body.arena = a_msg;
		a_msg.success = false;
		
		Entry<Integer, ArenaRankInfo> my_info_entry = m_arena_info.findMyRank(m_uuid);
		if (my_info_entry == null)
		{
			log.info("handelReqChallengeList no player in rank. uuid {}", m_uuid);
			return respond;
		}
		int my_rank = my_info_entry.getKey();
		ArenaRankInfo my_info = my_info_entry.getValue();
		if (my_info.type != ArenaPlayerType.Player)
		{
			log.info("handelReqChallengeList  ArenaPlayerType Error rank {}", my_rank);
			return respond;
		}
				
		List<Integer> genRankList = new ArrayList<Integer>();
		genRankList.add(1);
		genRankList.add(2);
		genRankList.add(3);
		List<Integer> can_challenge = new ArrayList<Integer>();
		//int arena_id = 0;
		for (Entry<Integer, Arena> a_cfg : ConfigConstant.tArena.entrySet())
		{
			Arena cfg = a_cfg.getValue();
			int begin = cfg.getArenaNum()[0];
			if (begin < 1)
				begin = 1;
			int end = cfg.getArenaNum()[1];
			if (end <= 0 || end > m_arena_info.rank.size())
				end = m_arena_info.rank.size();
			if (my_rank < begin || my_rank > end)
				continue;
			//arena_id = cfg.getId();
			if (cfg.getRankType() == RankType.TARGET)
			{
				for (int r : cfg.getRankInterval())
				{
					if (!can_challenge.contains(r))
						can_challenge.add(r);
					if (genRankList.contains(r))
						continue;
					genRankList.add(r);
				}
			}
			else if (cfg.getRankType() == RankType.RANGE)
			{
				int up_begin = my_rank - cfg.getRankInterval()[1];
				int up_end = my_rank - cfg.getRankInterval()[0];
				if (up_begin < 1)
					up_begin = 1;
				if (up_end > m_arena_info.rank.size())
					up_end = m_arena_info.rank.size();
				if (up_end < up_begin)
					up_end = up_begin;
				int temp_list_size = up_end - up_begin + 1;
				int[] temp_list = new int[temp_list_size];
				for (int i = 0; i < temp_list_size; ++i)
				{
					temp_list[i] = up_begin + i;
				}
				int rand_times = ArenaImpl.GEN_NUM > temp_list_size ? temp_list_size : ArenaImpl.GEN_NUM;
				for (int i = 0; i < rand_times; ++i)
				{
					int index = (int)(Math.floor(Math.random() * (temp_list_size - i)));
					if (!genRankList.contains(temp_list[index]))
					{
						genRankList.add(temp_list[index]);
						if (!can_challenge.contains(temp_list[index]))
							can_challenge.add(temp_list[index]);
					}
						
					int swap_t = temp_list[temp_list_size - 1 - i];
					temp_list[temp_list_size - 1 - i] = temp_list[index];
					temp_list[index] = swap_t;
				}
			}
		}
		if (my_rank < m_arena_info.rank.size())
		{
			int last = my_rank + 6;
			if (last > m_arena_info.rank.size())
				last = m_arena_info.rank.size();
			if (!genRankList.contains(last))
			{
				genRankList.add(last);
				if (!can_challenge.contains(last))
					can_challenge.add(last);
			}
				
		}
		genRankList.sort(null);
		//log.info("gen List {} my is {} arena is {}", genRankList.toArray(), my_rank, arena_id);
		
		List<ArenaEnemyInfo> en_list = new ArrayList<ArenaEnemyInfo>();
		for (int i = 0; i < genRankList.size(); ++i)
		{
			ArenaRankInfo r_info = m_arena_info.rank.get(genRankList.get(i));
			boolean can_chal = can_challenge.contains(genRankList.get(i).intValue());
			/*for (int c : can_challenge)
			{
				if (c == genRankList.get(i))
				{
					can_chal = true;
					break;
				}
			}*/
			if (r_info.type == ArenaPlayerType.Player)
			{
				PlayerInfo p_info = m_player_db.loadById(r_info.uuid);
				if (p_info == null)
				{
					log.info("gen List NO PLAYER UU ID {}", r_info.uuid);
					continue;
				}
				PlayerRolesInfo roles = m_roles_db.load(r_info.uuid);
				if (roles == null)
				{
					//log.info("gen List NO ROLES UU ID {}", r_info.uuid);
					continue;
				}
				ArenaEnemyInfo e = new ArenaEnemyInfo();
				en_list.add(e);
				e.type = r_info.type;
				e.uid = r_info.uuid;
				e.ranking = genRankList.get(i);
				for (Entry<Integer, Integer> team : roles.pve_team.entrySet())
				{
					if (team.getValue() <= 0)
						continue;
					e.role_id = team.getValue();
					break;
				}
				e.vip_lv = p_info.vip_level;
				e.avatar_img = p_info.user_base.getuImg();
				e.team_lv = p_info.team_lv;
				e.team_fighting = p_info.team_current_fighting;
				e.nickname = p_info.user_base.getNickname();
				e.challengable = can_chal;
				
				PlayerMonthcardDB mdb = SpringContextUtil.getBean(PlayerMonthcardDB.class);
				PlayerMonthcardInfo m = mdb.loadByUid(e.uid);
				if (m != null)
				{
					for (int mi = 0; mi < m.mc_ids.size(); ++mi)
						e.mc_ids.add(m.mc_ids.get(mi).intValue());
					m = null;
				}
				mdb = null;
			}
			else if(r_info.type == ArenaPlayerType.Robot)
			{
				ArenaEnemyInfo e = new ArenaEnemyInfo();
				en_list.add(e);
				e.type = r_info.type;
				e.uid = r_info.uuid;
				e.ranking = genRankList.get(i);
				e.challengable = can_chal;
			}
		}
		
		a_msg.success = true;
		a_msg.enemies = en_list;
		return respond;
	}

	public CommonMsg handleReqChallenge(CommonMsg respond, int goto_pos)
	{
		ArenaMsg a_msg = new ArenaMsg();
		respond.body.arena = a_msg;
		a_msg.success = false;
		
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if (hour >= ConfigConstant.tConf.getArenaEnd()[0] 
				&& hour < ConfigConstant.tConf.getArenaEnd()[1])
		{
			respond.header.rt_sub = 1124;
			return respond;
		}
		
		ScrollInfo s = m_scroll.Get(ConfigConstant.tConf.getArenaScroll());
		if (s == null)
		{
			//log.info("arena handleReqChallenge() player_id {} scroll_id {}, NO SCROLL ID");
			return respond;
		}
		
		Scroll s_cfg = s.Refresh(Calendar.getInstance(), TimeUtils.TodayStart());
		if (s_cfg == null)
		{
			//log.info("arena handleReqChallenge() player_id {} scroll_id {}, NO SCROLL Config");
			return respond;
		}
		if (s.count < 1)
		{
			//log.info("arena handleReqChallenge() Scroll Not Enough");
			return respond;
		}
		
		Entry<Integer, ArenaRankInfo> my_info_entry = m_arena_info.findMyRank(m_uuid);
		if (my_info_entry == null)
		{
			//log.info("arena handleReqChallenge no player in rank. uuid {}", m_uuid);
			return respond;
		}
		int my_rank = my_info_entry.getKey();
		ArenaRankInfo my_info = my_info_entry.getValue();
		if (my_info.type != ArenaPlayerType.Player)
		{
			//log.info("arena handleReqChallenge ArenaPlayerType Error rank {}", my_rank);
			return respond;
		}
		
		ArenaRankInfo target_info = m_arena_info.rank.get(goto_pos);
		if (target_info == null)
		{
			//log.info("arena handleReqChallenge no target in rank. goto_pos {}", goto_pos);
			return respond;
		}
		
		// 生成双方阵容
		Map<Integer, RoleInfo> enemy_roles = null;
		Robot r_cfg = null;
		if (target_info.type == ArenaPlayerType.Robot)
		{
			int r_id = Integer.parseInt(target_info.uuid.substring(ArenaDB.RobotPrefix.length()));
			r_cfg = ConfigConstant.tRobot.get(r_id);
			enemy_roles = genEnemyRoles(r_cfg);
		}
		else if (target_info.type == ArenaPlayerType.Player)
		{
			m_enemy = m_enemy_db.load(target_info.uuid);
			if (m_enemy != null)
			{
				enemy_roles = m_enemy.genRoles();
			}
		}
		if (enemy_roles == null)
		{
			log.warn("arena handleReqChallenge enemy_roles Error id {}", target_info.uuid);
			return respond;
		}
        Map<Integer, RoleInfo> my_roles = m_roles.genRoles();
        
        
        BattleSimulator sim = new BattleSimulator();
        sim.InitAllActorLogic(my_roles, enemy_roles,ConfigConstant.tConf.getArenaTime(), 0, BattleType.ARENA
        		, null, null, null, null);
        BattlePack battle_result = sim.Exe();
        DmgRewardImpl.RefreshMax(m_player._id, battle_result);
        a_msg.battle_data = battle_result;
		
        s.count -= 1;
		m_scroll_db.save();
		respond.body.sync_scroll = new PlayerScrollInfo();
		respond.body.sync_scroll.scroll_list = new ArrayList<ScrollInfo>();
		respond.body.sync_scroll.scroll_list.add(s);
		int v_id = ConfigConstant.tConf.getArenaItem();
		int v_count = 0;
		int cur = my_rank;
        if (battle_result.m_result == BattleResult.LEFT_WIN)
        {
        	v_count = ConfigConstant.tConf.getArenaVictory();
        	cur = m_arena_db.UpdateWithoutLock(m_uuid, goto_pos, false);
        	
        	//冠军公告
        	if (goto_pos == 1) {
            	String context = ConfigConstant.tWordconfig.get(BroadcastImpl.ARENA_CHAP).getWord_cn();
            	context = context.replace("$1", m_player.user_base.getNickname());
            	String t_name = "";
            	if (r_cfg != null) {
            		t_name = ConfigConstant.tWordconfig.get(r_cfg.getName()).getWord_cn();
            	} else {
            		PlayerInfoDB t_db = SpringContextUtil.getBean(PlayerInfoDB.class);
            		PlayerInfo t_info = t_db.loadById(target_info.uuid);
            		if (t_info != null)
            			t_name = t_info.user_base.getNickname();
            	}
            	context = context.replace("$2", t_name);
            	BroadcastImpl bi = BroadcastImpl.getInstance();
            	bi.SendBrocast(context, m_player.user_base.gid, m_player.user_base.zid);
        	}
        }
        else
        {
        	v_count = ConfigConstant.tConf.getArenaFailure();
        }
        m_bag.addItemCount(v_id, v_count);
        //m_bag_db.save();
        respond.body.sync_bag = new SyncBagMsg();
		respond.body.sync_bag.items.put(v_id, m_bag.getItemCount(v_id));
		a_msg.r_items = new ArrayList<SyncBagItem>();
		SyncBagItem sb = new SyncBagItem();
		a_msg.r_items.add(sb);
		sb.id = v_id;
		sb.count = v_count;
		
        //日常任务
        Dailytask d_cfg = DailyTaskImpl.getCfg(DailyTaskType.ARENA);
        int add_result = DailyTaskImpl.AddDaily(d_cfg, 1, m_bag.uid);
        if (d_cfg != null && add_result != 0)
        {
        	int id = ConfigConstant.tConf.getCharmItem();
        	int cnt = d_cfg.getTaskReward() * add_result;
        	m_bag.addItemCount(id, cnt);
        	respond.body.sync_bag.items.put(id, m_bag.getItemCount(id));
        }
        m_bag_db.save();
        
        //主线
        TaskImpl.doTask(m_player._id, TaskType.ARENA_CNT, 1);
		
     // 记录最后战斗时间
        m_player.last_active_time = Calendar.getInstance().getTimeInMillis();
		if (m_player.history_arena <= 0 || cur < m_player.history_arena)
		{
			m_player.history_arena = cur;
			a_msg.my_history_rank = cur;
		}
		else
		{
			a_msg.my_history_rank = m_player.history_arena;
		}
		m_player_db.save();
		a_msg.my_cur_rank = cur;
		
		a_msg.success = true;
		//log.info("arena handleReqChallenge SUCCESS");
		return respond;
	}
	
	private Map<Integer, RoleInfo> genEnemyRoles(Robot rb_cfg)
	{
		if (rb_cfg == null)
		{
			//log.info("arena genEnemyRoles no robot_cfg");
			return null;
		}
		
		Map<Integer, RoleInfo> enemy_team = new HashMap<Integer, RoleInfo>();
		for (int i = 0; i < rb_cfg.getTeams().length; ++i)
		{
        	RoleInfo r = new RoleInfo();
        	r.InitByRoleConfigIdAndLv(rb_cfg.getTeams()[i], rb_cfg.getLv()[i]);
        	enemy_team.put(i+1, r);
		}
		return enemy_team;
	}
	
	public CommonMsg handleReqRaid(CommonMsg respond)
	{
		ArenaMsg a_msg = new ArenaMsg();
		respond.body.arena = a_msg;
		a_msg.success = false;
		
		ScrollInfo s = m_scroll.Get(ConfigConstant.tConf.getArenaScroll());
		if (s == null)
		{
			//log.info("arena handleReqRaid() player_id {} scroll_id {}, NO SCROLL ID");
			return respond;
		}
		
		Scroll s_cfg = s.Refresh(Calendar.getInstance(), TimeUtils.TodayStart());
		if (s_cfg == null)
		{
			//log.info("arena handleReqRaid() player_id {} scroll_id {}, NO SCROLL Config");
			return respond;
		}
		if (s.count < 1)
		{
			//log.info("arena handleReqRaid() Scroll Not Enough");
			return respond;
		}
		
		
		int actual_count = 5;
		if (actual_count > s.count)
		{
			actual_count = s.count;
		}
		s.count -= actual_count;
		m_scroll_db.save();
		respond.body.sync_scroll = new PlayerScrollInfo();
		respond.body.sync_scroll.scroll_list = new ArrayList<ScrollInfo>();
		respond.body.sync_scroll.scroll_list.add(s);
		
		int v_id = ConfigConstant.tConf.getArenaItem();
		int v_count = ConfigConstant.tConf.getArenaVictory() * actual_count;
		m_bag.addItemCount(v_id, v_count);
        //m_bag_db.save();
        respond.body.sync_bag = new SyncBagMsg();
		respond.body.sync_bag.items.put(v_id, m_bag.getItemCount(v_id));
		a_msg.r_items = new ArrayList<SyncBagItem>();
		SyncBagItem sb = new SyncBagItem();
		a_msg.r_items.add(sb);
		sb.id = v_id;
		sb.count = v_count;
		
		//日常任务
        Dailytask d_cfg = DailyTaskImpl.getCfg(DailyTaskType.ARENA);
        int add_result = DailyTaskImpl.AddDaily(d_cfg, actual_count, m_bag.uid);
        if (d_cfg != null && add_result != 0)
        {
        	int id = ConfigConstant.tConf.getCharmItem();
        	int cnt = d_cfg.getTaskReward() * add_result;
        	m_bag.addItemCount(id, cnt);
        	respond.body.sync_bag.items.put(id, m_bag.getItemCount(id));
        }
        m_bag_db.save();
        
        //主线
        TaskImpl.doTask(m_player._id, TaskType.ARENA_CNT, actual_count);
		
		a_msg.success = true;
		//log.info("arena handleReqRaid SUCCESS");
		return respond;
	}

	public CommonMsg handleReqRankReward(CommonMsg respond, int rank_id)
	{
		ArenaMsg a_msg = new ArenaMsg();
		respond.body.arena = a_msg;
		a_msg.success = false;
		
    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
		
		if (m_player.history_arena <= 0 || rank_id < m_player.history_arena)
		{
			//log.info("arena handleReqRankReward Max HistoryNotEnough");
			return respond;
		}
		if (m_player.arena_rank_award.contains(rank_id))
		{
			//log.info("arena handleReqRankReward Max Already GetReward");
			return respond;
		}
		
		Rankaward ra_cfg = ConfigConstant.tRankaward.get(rank_id);
		if (ra_cfg == null)
		{
			log.warn("arena handleReqRankReward No Rankaward cfg id {}", rank_id);
			return respond;
		}
		
		m_player.arena_rank_award.add(rank_id);
		m_player.addDiamond(ra_cfg.getType());
		m_player_db.save();
		respond.body.sync_player_info = new SyncPlayerInfoMsg();
		respond.body.sync_player_info.diamond = ra_cfg.getType();
		a_msg.r_diamond = ra_cfg.getType();
		 
		for (int i = 0; i < ra_cfg.getItemId().length; ++i)
		{
			int id = ra_cfg.getItemId()[i];
			int cnt = ra_cfg.getItemNum()[i];
			if (m_bag.addItemCount(id, cnt))
			{
				if (respond.body.sync_bag == null)
					respond.body.sync_bag = new SyncBagMsg();
				respond.body.sync_bag.items.put(id, m_bag.getItemCount(id));
				if (a_msg.r_items == null)
					a_msg.r_items = new ArrayList<SyncBagItem>(); 
				SyncBagItem sb = new SyncBagItem();
				a_msg.r_items.add(sb);
				sb.id = id;
				sb.count = cnt;
			}
		}
		m_bag_db.save();
		
		//主线任务
		TaskImpl.doTask(respond.header.uid, TaskType.HAS_GET_ARENA_AWARD_ID, rank_id);
		
		a_msg.success = true;
		//log.info("arena handleReqRankReward SUCCESS");
		return respond;
	}
	
	public CommonMsg handleReqRankRewardInfo(CommonMsg respond)
	{
		ArenaMsg a_msg = new ArenaMsg();
		respond.body.arena = a_msg;
		a_msg.success = true;
		a_msg.rank_reward_info = m_player.arena_rank_award;
		return respond;
	}
	
	public CommonMsg handleGetMyRank(CommonMsg respond)
	{
		ArenaMsg a_msg = new ArenaMsg();
		respond.body.arena = a_msg;
		a_msg.success = false;
		
		Entry<Integer, ArenaRankInfo> my_info_entry = m_arena_info.findMyRank(m_uuid);
		if (my_info_entry == null)
		{
			//log.info("arena handleGetMyRank no player in rank. uuid {}", m_uuid);
			return respond;
		}
		
		a_msg.my_cur_rank = my_info_entry.getKey();
		a_msg.my_history_rank = m_player.history_arena;
		a_msg.success = true;
		//log.info("arena handleGetMyRank SUCCESS");
		return respond;
	}
	
	/*public CommonMsg handleGetRankList(CommonMsg respond)
	{
		Entry<Integer, ArenaRankInfo> my_info_entry = m_arena_info.findMyRank(m_uuid);
		if (my_info_entry == null)
		{
			log.info("arena handleGetRankList no player in rank. uuid {}", m_uuid);
			return respond;
		}
		
		int size = ArenaManager.RankSize;
		if (size > m_arena_info.rank.size())
		{
			size = m_arena_info.rank.size();
		}
		List<PlayerInfo> re = new ArrayList<PlayerInfo>();
		for (int i = 1; i <= size; ++i)
		{
			//todo 如果是机器人，填id，判断类型
			ArenaRankInfo info = m_arena_info.rank.get(i);
			if (info.type == ArenaPlayerType.Player)
			{
				PlayerInfo p = m_player_db.loadById(info.uuid);
				if (p != null)
					re.add(p);
			}
			else if (info.type == ArenaPlayerType.Robot)
			{
				//todo 机器人的下发方式
			}
		}
		return respond;
	}*/
}
