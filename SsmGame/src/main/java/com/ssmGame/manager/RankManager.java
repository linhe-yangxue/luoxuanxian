package com.ssmGame.manager;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmData.dbase.GuildInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerLevelInfo;
import com.ssmData.dbase.PlayerTowerInfo;
import com.ssmGame.defdata.msg.rank.RankMsg;
import com.ssmGame.module.rank.RankImpl;

@Service
public class RankManager {

	//private static final Logger log = LoggerFactory.getLogger(RankManager.class);
	
    //@Autowired
    //PlayerInfoDB m_player_db;
    
    public static final int RankSize = 50;
    
    public static Map<Integer, String> level_rank;     //pve关卡排行
    public static RankMsg level_msg = null;
    
    public static Map<Integer, String> tower_rank;     //爬塔排行
    public static RankMsg tower_msg = null;
    
    public static Map<Integer, String> team_lv_rank;	//队伍等级排行
    public static RankMsg team_lv_msg = null;
    
    public static Map<Integer, String> guild_rank; 	//工会排名
    public static RankMsg guild_msg = null;
    
    public void start()
    {
    	CalcLevel();
    	CalcTower();
    	CalcTeamLv();
    	CalcGuild();
    }
    
    //3 * 60 * 1000 = 180 000
    @Scheduled(fixedRate=180000)
	private void CalcLevel()
	{
		List<PlayerLevelInfo> all = BaseDaoImpl.getInstance().findAll(PlayerLevelInfo.class);
		all.sort(new SortLevel());
		level_rank = new HashMap<Integer, String>();
		int size = RankSize;
		if (size > all.size())
		{
			size = all.size();
		}
		int r = 1;
		for (int i = 0; i < all.size(); ++i)
		{
			PlayerLevelInfo info = all.get(i);
			if (info.cur_level > 0)
				level_rank.put(r++, info.uid);
			if (r > size)
				break;
		}
		level_msg = RankImpl.getInstance().GetLevelMsg();
		//log.info("CalcLevel() Size {}", level_rank.size()); 
	}
    
    //5 * 60 * 1000 = 300 000
    @Scheduled(fixedRate=300000)
	private void CalcTower()
	{
		List<PlayerTowerInfo> all = BaseDaoImpl.getInstance().findAll(PlayerTowerInfo.class);
		all.sort(new SortTower());
		tower_rank = new HashMap<Integer, String>();
		int size = RankSize;
		if (size > all.size())
		{
			size = all.size();
		}
		int r = 1;
		for (int i = 0; i < all.size(); ++i)
		{
			PlayerTowerInfo info = all.get(i);
			if (info.box_time > 0)
				tower_rank.put(r++, info.uid);
			if (r > size)
				break;
		}
		tower_msg = RankImpl.getInstance().getTowerMsg();
		//log.info("CalcTower() Size {}", level_rank.size()); 
	}

    //4 * 60 * 1000 = 240 000
    @Scheduled(fixedRate=240000)
    private void CalcTeamLv() {
		List<PlayerInfo> all = BaseDaoImpl.getInstance().findAll(PlayerInfo.class);
		all.sort(new SortTeamLv());
		team_lv_rank = new HashMap<Integer, String>();
		int size = RankSize;
		if (size > all.size())
		{
			size = all.size();
		}
		int r = 1;
		for (int i = 0; i < all.size(); ++i)
		{
			PlayerInfo info = all.get(i);
			if (info.lv_t > 0)
				team_lv_rank.put(r++, info._id);
			if (r > size)
				break;
		}
		team_lv_msg = RankImpl.getInstance().getTeamLvMsg();
    }

    //2.5 * 60 * 1000 = 150 000
    @Scheduled(fixedRate=150000)
    private void CalcGuild() {
		List<GuildInfo> all = BaseDaoImpl.getInstance().findAll(GuildInfo.class);
		all.sort(new SortGuild());
		guild_rank = new HashMap<Integer, String>();
		int size = all.size();
		int r = 1;
		for (int i = 0; i < all.size(); ++i)
		{
			GuildInfo info = all.get(i);
			if (info.lv_t > 0)
				guild_rank.put(r++, info.id);
			if (r > size)
				break;
		}
		guild_msg = RankImpl.getInstance().getGuildMsg();
    }
}

class SortLevel implements Comparator<PlayerLevelInfo>
{
	public int compare(PlayerLevelInfo arg0, PlayerLevelInfo arg1) 
	{
		if (arg0 == null && arg1 != null)
			return -1;
		if (arg0 != null && arg1 == null)
			return 1;
		if (arg0 == null && arg1 == null)
			return 0;
		
		//按照cur_level大小，降序排
		if (arg0.cur_level < arg1.cur_level)
			return 1;
		if (arg0.cur_level > arg1.cur_level)
			return -1;
		
		if (arg0.time == 0 && arg1.time != 0)
			return -1;
		if (arg0.time != 0 && arg1.time == 0)
			return 1;
		//按照时间，升序
		if (arg0.time < arg1.time)
			return -1;
		if (arg0.time > arg1.time)
			return 1;
		
		return 0;
	}
}

class SortTower implements Comparator<PlayerTowerInfo>
{
	public int compare(PlayerTowerInfo arg0, PlayerTowerInfo arg1)
	{
		if (arg0 == null && arg1 != null)
			return -1;
		if (arg0 != null && arg1 == null)
			return 1;
		if (arg0 == null && arg1 == null)
			return 0;
		
		if (arg0.history_box == 0 && arg1.history_box != 0)
			return 1;
		if (arg0.history_box != 0 && arg1.history_box == 0)
			return -1;
		//按照history_box大小，降序排
		if (arg0.history_box < arg1.history_box)
			return 1;
		if (arg0.history_box > arg1.history_box)
			return -1;
		
		if (arg0.box_time == 0 && arg1.box_time != 0)
			return 1;
		if (arg0.box_time != 0 && arg1.box_time == 0)
			return -1;
		//按照时间，升序
		if (arg0.box_time < arg1.box_time)
			return -1;
		if (arg0.box_time > arg1.box_time)
			return 1;
		
		return 0;
	}
}

class SortTeamLv implements Comparator<PlayerInfo>
{
	public int compare(PlayerInfo arg0, PlayerInfo arg1)
	{
		if (arg0 == null && arg1 != null)
			return -1;
		if (arg0 != null && arg1 == null)
			return 1;
		if (arg0 == null && arg1 == null)
			return 0;
		
		//按照team_lv大小，降序排
		if (arg0.team_lv < arg1.team_lv)
			return 1;
		if (arg0.team_lv > arg1.team_lv)
			return -1;
		
		if (arg0.lv_t == 0 && arg1.lv_t != 0)
			return 1;
		if (arg0.lv_t != 0 && arg1.lv_t == 0)
			return -1;
		//按照时间，升序
		if (arg0.lv_t < arg1.lv_t)
			return -1;
		if (arg0.lv_t > arg1.lv_t)
			return 1;
		
		return 0;
	}
}

class SortGuild implements Comparator<GuildInfo>
{
	public int compare(GuildInfo arg0, GuildInfo arg1)
	{
		if (arg0 == null && arg1 != null)
			return -1;
		if (arg0 != null && arg1 == null)
			return 1;
		if (arg0 == null && arg1 == null)
			return 0;
		
		//按照team_lv大小，降序排
		if (arg0.lv < arg1.lv)
			return 1;
		if (arg0.lv > arg1.lv)
			return -1;
		
		//按照时间，升序
		if (arg0.lv_t < arg1.lv_t)
			return -1;
		if (arg0.lv_t > arg1.lv_t)
			return 1;
		
		return 0;
	}
}
