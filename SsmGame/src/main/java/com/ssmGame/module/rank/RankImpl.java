package com.ssmGame.module.rank;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.dbase.ArenaDB;
import com.ssmData.dbase.ArenaInfo;
import com.ssmData.dbase.ArenaRankInfo;
import com.ssmData.dbase.GuildDB;
import com.ssmData.dbase.GuildInfo;
import com.ssmData.dbase.HubGuildDB;
import com.ssmData.dbase.HubGuildInfo;
import com.ssmData.dbase.PlayerGuildDB;
import com.ssmData.dbase.PlayerGuildInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerLevelDB;
import com.ssmData.dbase.PlayerLevelInfo;
import com.ssmData.dbase.PlayerRolesInfo;
import com.ssmData.dbase.PlayerRolesInfoDB;
import com.ssmData.dbase.PlayerTowerDB;
import com.ssmData.dbase.PlayerTowerInfo;
import com.ssmData.dbase.enums.ArenaPlayerType;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.guild.GuildSoloMsg;
import com.ssmGame.defdata.msg.rank.RankMsg;
import com.ssmGame.defdata.msg.rank.RankPlayerInfo;
import com.ssmGame.manager.HubGuildWarManager;
import com.ssmGame.manager.RankManager;

@Service
@Scope("prototype")
public class RankImpl {
	private static final Logger log = LoggerFactory.getLogger(RankImpl.class);
	
	private static final int RankSize = 50;
	
    @Autowired
    PlayerRolesInfoDB m_roles_db;
    PlayerRolesInfo m_roles = null;
    
    @Autowired
    ArenaDB m_arena_db;
    ArenaInfo m_arena_info;
    
    @Autowired
    PlayerInfoDB m_player_db;
    
    @Autowired
    GuildDB m_guild_db;
    
    @Autowired
    PlayerLevelDB m_level_db;
    
    @Autowired
    PlayerTowerDB m_tower_db;
    
    
    public final static RankImpl getInstance(){
        return SpringContextUtil.getBean(RankImpl.class);
	}
    
    /*public RankImpl init(String uuid)
	{
    	m_roles = m_roles_db.load(uuid);
    	
		if (null == m_roles)
		{
			return null;
		}
		return this;
	}
    
	public void destroy()
	{
		m_roles = null;
		m_roles_db = null;
	}*/
	
	public CommonMsg handelReqArenaList(CommonMsg respond)
	{
		RankMsg r_msg = new RankMsg();
		respond.body.rank = r_msg;
		r_msg.success = false;
		
		m_arena_info = m_arena_db.load();
		if (null == m_arena_info)
		{
			log.info("ARENA DB ERROR");
			return respond;
		}
		
		int size = RankSize;
		if (size > m_arena_info.rank.size())
		{
			size = m_arena_info.rank.size();
		}
		r_msg.players = new ArrayList<RankPlayerInfo>();
		for (int i = 1; i <= size; ++i)
		{
			ArenaRankInfo info = m_arena_info.rank.get(i);
			RankPlayerInfo rank = new RankPlayerInfo();
			rank.type = info.type;
			r_msg.players.add(rank);
			if (info.type == ArenaPlayerType.Player)
			{
				PlayerInfo p = m_player_db.loadById(info.uuid);
				if (p != null)
				{
					rank.uid = info.uuid;
					rank.nickname = p.user_base.getNickname();
					rank.vip_lv = p.vip_level;
					rank.team_lv = p.team_lv;
					rank.guard_name = "";
					PlayerGuildDB gdb = SpringContextUtil.getBean(PlayerGuildDB.class);
					PlayerGuildInfo pg_info = gdb.loadByUid(info.uuid);
					if (pg_info != null && pg_info.gd_id.length() > 0) {
						GuildInfo g = SpringContextUtil.getBean(GuildDB.class).loadByGuildId(pg_info.gd_id);
						if (g != null) {
							rank.guard_name = g.name;
						}
					}
					rank.avatar_img = p.user_base.getuImg();
					rank.ranking = i;
					rank.fighting = p.team_current_fighting;
				}
			}
			else if (info.type == ArenaPlayerType.Robot)
			{
				rank.uid = info.uuid;
				rank.ranking = i;
			}
		}
		m_arena_db = null;
		m_player_db = null;
		r_msg.success = true;
		//log.info("handelReqArenaList() SUCCESS");
		return respond;
	}
	
	public RankMsg getTowerMsg()
	{
		RankMsg result = new RankMsg();
		result.success = false;
		int size = RankSize;
		if (size > RankManager.tower_rank.size())
		{
			size = RankManager.tower_rank.size();
		}
		result.players = new ArrayList<RankPlayerInfo>();
		for (int i = 1; i <= size; ++i)
		{
			String uid = RankManager.tower_rank.get(i);
			
			PlayerInfo p = m_player_db.loadById(uid);
			PlayerTowerInfo l = m_tower_db.loadByUid(uid);
			if (p != null && l != null)
			{
				RankPlayerInfo rank = new RankPlayerInfo();
				result.players.add(rank);
				
				rank.uid = uid;
				rank.nickname = p.user_base.getNickname();
				rank.vip_lv = p.vip_level;
				rank.team_lv = p.team_lv;
				rank.guard_name = "";
				PlayerGuildDB gdb = SpringContextUtil.getBean(PlayerGuildDB.class);
				PlayerGuildInfo pg_info = gdb.loadByUid(uid);
				if (pg_info != null && pg_info.gd_id.length() > 0) {
					GuildInfo g = SpringContextUtil.getBean(GuildDB.class).loadByGuildId(pg_info.gd_id);
					if (g != null) {
						rank.guard_name = g.name;
					}
				}
				rank.avatar_img = p.user_base.getuImg();
				rank.ranking = i;
				rank.tower = l.history_box;
			}
		}
		result.success = true;
		return result;
	}
	
	public RankMsg GetLevelMsg()
	{
		RankMsg result = new RankMsg();
		result.success = false;
		int size = RankSize;
		if (size > RankManager.level_rank.size())
		{
			size = RankManager.level_rank.size();
		}
		result.players = new ArrayList<RankPlayerInfo>();
		for (int i = 1; i <= size; ++i)
		{
			String uid = RankManager.level_rank.get(i);
			
			PlayerInfo p = m_player_db.loadById(uid);
			PlayerLevelInfo l = m_level_db.loadByUid(uid);
			if (p != null && l != null)
			{
				RankPlayerInfo rank = new RankPlayerInfo();
				result.players.add(rank);
				rank.type = ArenaPlayerType.Player;
				rank.uid = uid;
				rank.nickname = p.user_base.getNickname();
				rank.vip_lv = p.vip_level;
				rank.team_lv = p.team_lv;
				rank.guard_name = "";
				PlayerGuildDB gdb = SpringContextUtil.getBean(PlayerGuildDB.class);
				PlayerGuildInfo pg_info = gdb.loadByUid(uid);
				if (pg_info != null && pg_info.gd_id.length() > 0) {
					GuildInfo g = SpringContextUtil.getBean(GuildDB.class).loadByGuildId(pg_info.gd_id);
					if (g != null) {
						rank.guard_name = g.name;
					}
				}
				rank.avatar_img = p.user_base.getuImg();
				rank.ranking = i;
				rank.fighting = p.team_current_fighting;
				rank.level = l.cur_level;
			}
		}
		m_player_db = null;
		m_level_db = null;
		result.success = true;
		return result;
	}
	
	public RankMsg getTeamLvMsg()
	{
		RankMsg result = new RankMsg();
		result.success = false;
		int size = RankSize;
		if (size > RankManager.team_lv_rank.size())
		{
			size = RankManager.team_lv_rank.size();
		}
		result.players = new ArrayList<RankPlayerInfo>();
		for (int i = 1; i <= size; ++i)
		{
			String uid = RankManager.team_lv_rank.get(i);
			
			PlayerInfo p = m_player_db.loadById(uid);
			if (p != null)
			{
				RankPlayerInfo rank = new RankPlayerInfo();
				result.players.add(rank);
				rank.type = ArenaPlayerType.Player;
				rank.uid = uid;
				rank.nickname = p.user_base.getNickname();
				rank.vip_lv = p.vip_level;
				rank.team_lv = p.team_lv;
				rank.guard_name = "";
				PlayerGuildDB gdb = SpringContextUtil.getBean(PlayerGuildDB.class);
				PlayerGuildInfo pg_info = gdb.loadByUid(uid);
				if (pg_info != null && pg_info.gd_id.length() > 0) {
					GuildInfo g = SpringContextUtil.getBean(GuildDB.class).loadByGuildId(pg_info.gd_id);
					if (g != null) {
						rank.guard_name = g.name;
					}
				}
				rank.avatar_img = p.user_base.getuImg();
				rank.ranking = i;
				rank.fighting = p.team_current_fighting;
			}
		}
		m_player_db = null;
		result.success = true;
		return result;
	}
	
	public RankMsg getGuildMsg()
	{
		RankMsg result = new RankMsg();
		result.success = false;
		int size = RankSize;
		if (size > RankManager.guild_rank.size())
		{
			size = RankManager.guild_rank.size();
		}
		result.guilds = new ArrayList<GuildSoloMsg>();
		for (int i = 1; i <= size; ++i)
		{
			String gid = RankManager.guild_rank.get(i);
			
			GuildDB g_db = SpringContextUtil.getBean(GuildDB.class);
			GuildInfo p = g_db.loadByGuildId(gid);
			if (p != null)
			{
				GuildSoloMsg rank = new GuildSoloMsg();
				result.guilds.add(rank);
				rank.id = gid;
				rank.rank = i;
				rank.name = p.name;
				rank.guild_lv = p.lv;
				rank.cnt_member = p.members.size();
				rank.chairman_uid = p.ceo_id;
				PlayerInfo test_info = SpringContextUtil.getBean(PlayerInfoDB.class).loadById(p.ceo_id);
				if (test_info != null) {
					rank.chairman_name = test_info.user_base.getNickname();
				}
				List<GuildInfo.Member> mlist = p.members;
				int ms = mlist.size();
				long sum_f = 0;
				for (int j = 0; j < ms; j++) {
					PlayerInfo pi = SpringContextUtil.getBean(PlayerInfoDB.class).loadById(mlist.get(j).id);
					if (pi != null) {
						sum_f += pi.team_history_max_fighting;
					}
				}
				rank.fighting = sum_f;
			}
		}
		result.success = true;
		return result;
	}
	
	public RankMsg getGuildWarMsg()
	{
		RankMsg result = new RankMsg();
		result.success = false;
		int size = RankSize;
		if (size > HubGuildWarManager.guild_war_rank.size())
		{
			size = HubGuildWarManager.guild_war_rank.size();
		}
		result.guilds = new ArrayList<GuildSoloMsg>();
		for (int i = 1; i <= size; ++i)
		{
			String gid = HubGuildWarManager.guild_war_rank.get(i);
			
			HubGuildDB gdb = SpringContextUtil.getBean(HubGuildDB.class);
			HubGuildInfo pg_info = gdb.loadByGuildId(gid);
			if (pg_info != null)
			{
				GuildSoloMsg rank = new GuildSoloMsg();
				result.guilds.add(rank);
				rank.id = gid;
				rank.rank = i;
				rank.name = pg_info.name;
				rank.guild_lv = pg_info.lv;
				rank.cnt_member = pg_info.cnt;
				rank.chairman_uid = pg_info.ceo_id;
				rank.chairman_name = pg_info.ceo_name;
				rank.fighting = pg_info.fight.longValue();
				rank.score = pg_info.weekly_s.longValue();
				rank.zid = pg_info.zid;
			}
		}
		result.success = true;
		return result;
	}
	
	public CommonMsg handelReqPveList(CommonMsg respond)
	{
		respond.body.rank = RankManager.level_msg;
		//log.info("handelReqPveList() SUCCESS");
		return respond;
	}
	
	public CommonMsg handleTowerList(CommonMsg respond)
	{
		respond.body.rank = RankManager.tower_msg;
		return respond;
	}
	
	public CommonMsg handleTeamLvList(CommonMsg respond)
	{
		respond.body.rank = RankManager.team_lv_msg;
		return respond;
	}
	
	public CommonMsg handleGuildList(CommonMsg respond)
	{
		respond.body.rank = RankManager.guild_msg;
		return respond;
	}
	
	/*public CommonMsg handelReqMy(CommonMsg respond, int list_type, String uid)
	{
		RankMsg r_msg = new RankMsg();
		respond.body.rank = r_msg;
		r_msg.success = false;
		
		if (list_type == RankListType.ARENA)
		{
			m_arena_info = m_arena_db.load();
			if (null == m_arena_info)
			{
				log.info("ARENA DB ERROR");
				return respond;
			}
			Entry<Integer, ArenaRankInfo> my_info_entry = m_arena_info.findMyRank(uid);
			if (my_info_entry == null)
			{
				log.info("handelReqMy no player in rank. uuid {}", uid);
				return respond;
			}
			r_msg.my_rank = my_info_entry.getKey();
			m_arena_db = null;
			m_arena_info = null;
		}
		else if (list_type == RankListType.PVE)
		{
			r_msg.my_rank = -1;
			for (int i = 1; i <= RankManager.level_rank.size(); ++i)
			{
				if (RankManager.level_rank.get(i).equals(uid))
				{
					r_msg.my_rank = i;
					break;
				}
			}
		}
		else
		{
			log.info("handelReqMy() Type Error {}", list_type);
			return respond;
		}
		
		if (r_msg.my_rank > RankSize)
		{
			r_msg.my_rank = -1;
		}
		r_msg.success = true;
		//log.info("handelReqMy() SUCCESS");
		return respond;
	}*/
	
	public CommonMsg handelReqDetail(CommonMsg respond, String uid)
	{
		RankMsg r_msg = new RankMsg();
		respond.body.rank = r_msg;
		r_msg.success = false;
		
		PlayerRolesInfo ri = m_roles_db.load(uid);
		if (ri != null)
			r_msg.roles = ri;
		else
		{
			log.info("handelReqDetail() No roles info {}", uid);
			return respond;
		}

		PlayerInfo pi = m_player_db.loadById(uid);
		if(pi != null){
			r_msg.players = new ArrayList<RankPlayerInfo>();

			RankPlayerInfo rpi = new RankPlayerInfo();
			rpi.nickname = pi.user_base.getNickname();
			rpi.team_lv = pi.team_lv;
			r_msg.players.add(rpi);
		}
		else
		{
			log.info("handelReqDetail() No player info {}", uid);
			return respond;
		}
		
		r_msg.success = true;
		//log.info("handelReqDetail() SUCCESS");
		return respond;
	}
}
