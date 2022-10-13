package com.ssmGame.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Arena;
import com.ssmData.dbase.ArenaDB;
import com.ssmData.dbase.ArenaInfo;
import com.ssmData.dbase.ArenaRankInfo;
import com.ssmData.dbase.BaseDataSource;
import com.ssmData.dbase.FullBossDB;
import com.ssmData.dbase.FullBossInfo;
import com.ssmData.dbase.GuildDB;
import com.ssmData.dbase.GuildInfo;
import com.ssmData.dbase.PlayerMailDB;
import com.ssmData.dbase.PlayerMailInfo;
import com.ssmData.dbase.PlayerTaskInfo;
import com.ssmData.dbase.ServerInfo;
import com.ssmData.dbase.ServerInfoDB;
import com.ssmData.dbase.enums.ArenaPlayerType;
import com.ssmGame.module.activity.ActivityType;
import com.ssmGame.module.activity.PveLevelActivityImpl;
import com.ssmGame.module.activity.TeamLevelImpl;
import com.ssmGame.module.boss.BossImpl;
import com.ssmGame.module.mail.MailImpl;
import com.ssmGame.util.TimeUtils;


@Service
public class ArenaManager {	
	
	private static final Logger log = LoggerFactory.getLogger(ArenaManager.class);
	
    @Autowired
    ArenaDB m_arena_db;
    ArenaInfo m_arena_info;
    
    @Autowired
    ServerInfoDB m_server_db;
    
    @Autowired
    GuildDB m_guild_db;
    
    @Autowired
    FullBossDB m_boss_db;
    FullBossInfo m_boss_info;
    
    private static Calendar server_start_calc;
    
	public void start()
	{
		CleanServerMem();
		
		BaseDataSource x = m_arena_db;
		x.CleanMem(m_arena_db.ServerUUID());
		x = m_boss_db;
		x.CleanMem(m_boss_db.ServerUUID());
		init();
		
		initServerStartTime();
		System.out.println(ServerStartDate().getTime().toString());
	}
	
	private void init()
	{
		m_arena_info = m_arena_db.load();
		if (m_arena_info == null)
		{
			m_arena_db.createDB(m_arena_db.ServerUUID());
		}
		m_boss_info = m_boss_db.load();
		if (m_boss_info == null) {
			m_boss_info = (FullBossInfo)m_boss_db.createDB(m_boss_db.ServerUUID());
		}
		BossImpl.InitFullBossDB(m_boss_info);
		m_boss_db.save();
	}
	
	@Scheduled(cron="5 5 * * * *")
	private void CalcAward()
	{
		Lock lock = new ReentrantLock();  
		lock.lock();
		try
		{
			Calendar now = Calendar.getInstance();
			int now_year = now.get(Calendar.YEAR);
			int now_day = now.get(Calendar.DAY_OF_YEAR);
			boolean is_leap = false;
			
			//竞技场发奖
			int hour = now.get(Calendar.HOUR_OF_DAY);
			if (hour == ConfigConstant.tConf.getArenaEnd()[0])
			{
				init();
				handleDailyAward();
			}
			
			if (hour == 0) {
				log.info("ArenaManager.CalcAward() = zero");
				List<PlayerTaskInfo> all = BaseDaoImpl.getInstance().findAll(PlayerTaskInfo.class);
				//开服等级排名发奖
				long lv_rank_begin = ActivityType.getBeginOpen(ActivityType.LvRank);
				Calendar lv = Calendar.getInstance();
				lv.setTimeInMillis(lv_rank_begin);
				int lv_year = lv.get(Calendar.YEAR);
				is_leap = TimeUtils.isLeap(lv_year);
				int lv_day = lv.get(Calendar.DAY_OF_YEAR);
				int lv_dif = 0;
				if (now_year == lv_year) {
					lv_dif = now_day - lv_day;
				} else {
					lv_dif = (365 + (is_leap?1:0) - lv_day) + now_day;
				}
				TeamLevelImpl.handleAward(all, lv_dif);
				
				//开服关卡排名奖励
				long stage_rank_begin = ActivityType.getBeginOpen(ActivityType.PveRank);
				Calendar st = Calendar.getInstance();
				st.setTimeInMillis(stage_rank_begin);
				int st_year = st.get(Calendar.YEAR);
				is_leap = TimeUtils.isLeap(st_year);
				int st_day = st.get(Calendar.DAY_OF_YEAR);
				int st_dif = 0;
				if (now_year == st_year) {
					st_dif = now_day - st_day;
				} else {
					st_dif = (365 + (is_leap?1:0) - st_day) + now_day;
				}
					PveLevelActivityImpl.handleAward(all, st_dif);
			}
			
			ServerInfo server_info = m_server_db.load();
			if (server_info != null) {
				ActivityType.InitBasicTimeTable(server_info.start_time);
				ActivityType.InitBeginTimeTable(server_info.start_time);
			}
		}
		finally{
			lock.unlock();
		}
	}
	
	private void handleDailyAward()
	{
		Set<String> send = new HashSet<String>();		
		for (Entry<Integer, Arena> a_cfg : ConfigConstant.tArena.entrySet())
		{
			Arena config = a_cfg.getValue();
			int begin = config.getArenaNum()[0];
			if (begin < 1)
				begin = 1;
			int end = config.getArenaNum()[1];
			if (end <= 0 || end > m_arena_info.rank.size())
				end = m_arena_info.rank.size();
			String dmd = Integer.toString(config.getRankDiamonds());
			String pot = Integer.toString(config.getRankPoints());
			for (int i = begin; i <= end; ++i)
			{
				ArenaRankInfo info = m_arena_info.rank.get(i);
				if (info == null)
					continue;
				if (info.type == ArenaPlayerType.Robot)
					continue;
				if (send.contains(info.uuid))
					continue;
				PlayerMailDB mail_db = SpringContextUtil.getBean(PlayerMailDB.class);
				PlayerMailInfo mail_info = mail_db.loadByUid(info.uuid);
				if (mail_info == null)
					continue;
				send.add(info.uuid);
				List<String> args = new ArrayList<String>();
				args.add(Integer.toString(i));
				args.add(dmd);
				args.add(pot);
				MailImpl.AddMail(mail_info, config.getRankMail(), args, 1, null);
				mail_db.save();
				//log.info("Arena mail player {} rank {}", info.uuid, i);
			}
		}
	}
	
	private void initServerStartTime()
	{	
		m_server_db.CleanMem(m_server_db.ServerUUID());
		
		ServerInfo server_info = m_server_db.load();
		if (server_info == null)
		{
			server_info = (ServerInfo) m_server_db.createDB(m_server_db.ServerUUID());
		}
		if (server_info != null && server_info.start_time == 0)
		{
			server_info.start_time = Calendar.getInstance().getTimeInMillis();
			m_server_db.save();
		}
		server_start_calc = Calendar.getInstance();
		server_start_calc.setTimeInMillis(server_info.start_time);
		ActivityType.InitBasicTimeTable(server_info.start_time);
		ActivityType.InitBeginTimeTable(server_info.start_time);
	}
	
	public static Calendar ServerStartDate()
	{
		return server_start_calc;
	}
	
	private void CleanServerMem() {
		List<GuildInfo> all_guild = BaseDaoImpl.getInstance().findAll(GuildInfo.class);
		BaseDataSource x = m_guild_db;
		for (int i = 0; i < all_guild.size(); ++i) {
			x.CleanMem(all_guild.get(i).id);
		}
	}
}
