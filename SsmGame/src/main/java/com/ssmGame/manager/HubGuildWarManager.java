package com.ssmGame.manager;

import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.tool.colligate.HttpRequest;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Hubmap;
import com.ssmData.dbase.BaseDataSource;
import com.ssmData.dbase.GuildDB;
import com.ssmData.dbase.HubGuildDB;
import com.ssmData.dbase.HubGuildInfo;
import com.ssmData.dbase.HubPlayerGuildDB;
import com.ssmData.dbase.HubPlayerGuildInfo;
import com.ssmData.dbase.HubSendDB;
import com.ssmData.dbase.HubSendInfo;
import com.ssmData.dbase.HubWarRcdDB;
import com.ssmData.dbase.HubWarRcdInfo;
import com.ssmGame.constants.DefConstant;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.hub.HubGuildWarMsg;
import com.ssmGame.defdata.msg.rank.RankMsg;
import com.ssmGame.module.battle.BattleResult;
import com.ssmGame.module.guild.GuildImpl;
import com.ssmGame.module.rank.RankImpl;
import com.ssmGame.util.TimeUtils;

@Service
public class HubGuildWarManager {	
	
	private @Value("${IS_HUB}") int is_hub;
	
	private static final Logger log = LoggerFactory.getLogger(HubGuildWarManager.class);
	
    public static Map<Integer, String> guild_war_rank; 	//工会战排名
    public static RankMsg guild_war_msg = null;
	
    @Autowired
    GuildDB m_guild_db;
     
	public void start()
	{
		CleanSendMem();
		CleanGuildMem();
		CalcGuildWar();
		log.info("Hub GuildWar Mng Start!");
	}
	
	@Scheduled(cron="15 0 * * * *")
	private void FetchMatchInfo()
	{
		if (is_hub == 0) {
			return;
		}
		
		Calendar now = Calendar.getInstance();
		if (now.get(Calendar.HOUR_OF_DAY) != ConfigConstant.tConf.getGuildMate()) {
			return;
		}
		log.info("Fetching MatchInfo ");
		
		Map<Integer, Hubmap> url_list = ConfigConstant.tHubmap;
		for (Entry<Integer, Hubmap> u : url_list.entrySet()) {
			CallMatch cm = new CallMatch();
			cm.url = u.getValue().getURL();
			cm.zid = u.getValue().getServerId();
			Thread t = new Thread(cm);
			t.start();
		}
	}
	
	@Scheduled(cron="15 20 * * * *")
	private void BeginMatch(){
		if (is_hub == 0) {
			return;
		}

		Calendar now = Calendar.getInstance();
		if (now.get(Calendar.HOUR_OF_DAY) != ConfigConstant.tConf.getGuildMate()) {
			return;
		}
		log.info("BeginMatch ");
		GuildImpl.getInstance().handleBeginMatch();
	}
	
	@Scheduled(cron="15 40 * * * *")
	private void SendMatchResult(){
		if (is_hub == 0) {
			return;
		}

		Calendar now = Calendar.getInstance();
		if (now.get(Calendar.HOUR_OF_DAY) != ConfigConstant.tConf.getGuildMate()) {
			return;
		}
		log.info("Sending MatchResult... ");
		
		Map<Integer, Hubmap> url_list = ConfigConstant.tHubmap;
		for (Entry<Integer, Hubmap> u : url_list.entrySet()) {
			SendMatchResult cm = new SendMatchResult();
			cm.url = u.getValue().getURL();
			cm.zid = u.getValue().getServerId();
			Thread t = new Thread(cm);
			t.start();
		}
	}
	
    //5 * 60 * 1000 = 300 000
    @Scheduled(fixedRate=300000)
	private void CalcGuildWar() {
		if (is_hub == 0) {
			return;
		}
		
    	List<HubGuildInfo> all = BaseDaoImpl.getInstance().findAll(HubGuildInfo.class);
    	
		all.sort(new WeekScoreSort());
		guild_war_rank = new HashMap<Integer, String>();
		int size = RankManager.RankSize;
		if (size > all.size())
		{
			size = all.size();
		}
		int r = 1;
		long now = Calendar.getInstance().getTimeInMillis();
		long diff = TimeUtils.ONE_DAY_TIME << 1;
		for (int i = 0; i < all.size(); ++i)
		{
			HubGuildInfo info = all.get(i);
			if (now - info.m_t > diff) {
				continue;
			}
			guild_war_rank.put(r++, info.id);
			if (r > size)
				break;
		}
		guild_war_msg = RankImpl.getInstance().getGuildWarMsg();
    }
    
    @Scheduled(cron="25 1 * * * *")
    private void CalcDailyReward() {
		if (is_hub == 0) {
			return;
		}
		
    	Calendar now_cal = Calendar.getInstance();
    	if (now_cal.get(Calendar.HOUR_OF_DAY) != ConfigConstant.tConf.getCutoff()[0]) {
    		return;
    	}
    	log.info("CalcDailyReward");
    	long diff = TimeUtils.ONE_HOUR_TIME * (1 + ConfigConstant.tConf.getWarRankTime()[0] - ConfigConstant.tConf.getGuildMate());
    	List<HubGuildInfo> all_hub_g = BaseDaoImpl.getInstance().findAll(HubGuildInfo.class);
    	int all_size = all_hub_g.size();
    	Map<String, Integer> score_list = new HashMap<String, Integer>(all_hub_g.size());
    	for (int i = 0; i < all_size; ++i) {
    		HubGuildInfo m = all_hub_g.get(i);
    		score_list.put(m.id, m.daily_s);
    	}
    	Map<Integer, HubGuildWarMsg> msg_list = new HashMap<Integer, HubGuildWarMsg>();
    	long now = now_cal.getTimeInMillis();
    	for (int i = 0; i < all_size; ++i) {
    		HubGuildInfo g = all_hub_g.get(i);
    		if (now - g.m_t > diff) {
    			continue;
    		}
    		if (g.enemy_id.length() <= 0) {
    			continue;
    		}
    		Integer my_s = score_list.get(g.id);
    		Integer e_s = score_list.get(g.enemy_id);
    		if (e_s == null) {
    			continue;
    		}
    		HubGuildWarMsg m = msg_list.get(g.zid.intValue());
    		if (m == null) {
    			m = new HubGuildWarMsg();
    			msg_list.put(g.zid.intValue(), m);
    		}
    		if (m.f_list == null) {
    			m.f_list = new HashMap<String, HubGuildWarMsg.I>();
    		}
    		HubGuildWarMsg.I g_m = m.new I();
    		m.f_list.put(g.id, g_m);	
    		if (my_s > e_s) {
    			g_m.win = BattleResult.LEFT_WIN;
    			g_m.contri = ConfigConstant.tConf.getGuildVictory();
    		} else if (my_s == e_s) {
    			g_m.win = BattleResult.DRAW;
    			g_m.contri = ConfigConstant.tConf.getGuildDraw();
    		} else if (my_s < e_s) {
    			g_m.win = BattleResult.LEFT_LOSE;
    			g_m.contri = ConfigConstant.tConf.getGuildFail();
    		}
    		g_m.contri += g.daily_s * ConfigConstant.tConf.getAddedGuild();
    	}
    	Map<Integer, Hubmap> url_list = ConfigConstant.tHubmap;
		for (Entry<Integer, Hubmap> u : url_list.entrySet()) {
			HubGuildWarMsg send = msg_list.get(u.getValue().getServerId());
			if (send == null) {
				continue;
			}
			SendDaily cm = new SendDaily();
			cm.url = u.getValue().getURL();
			cm.zid = u.getValue().getServerId();
			cm.send = send;
			Thread t = new Thread(cm);
			t.start();
		}
    }
    
    @Scheduled(cron="5 30 * * * *")
    private void CalcWeeklyReward() {
		if (is_hub == 0) {
			return;
		}
		
    	Calendar now_cal = Calendar.getInstance();
    	if (now_cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
    		return;
    	}
    	if (now_cal.get(Calendar.HOUR_OF_DAY) != ConfigConstant.tConf.getWarRankTime()[0]) {
    		return;
    	}
    	log.info("CalcWeeklyReward");
    	
    	long diff = TimeUtils.ONE_HOUR_TIME * (20 + ConfigConstant.tConf.getWarRankTime()[0] - ConfigConstant.tConf.getGuildMate());
    	List<HubGuildInfo> all_hub_g = BaseDaoImpl.getInstance().findAll(HubGuildInfo.class);
    	Map<String, Integer> rank_list = new HashMap<String, Integer>(guild_war_rank.size());
    	for (Entry<Integer, String> r : guild_war_rank.entrySet()) {
    		rank_list.put(r.getValue(), r.getKey());
    	}
    	int all_s = all_hub_g.size();
    	Map<Integer, HubGuildWarMsg> msg_list = new HashMap<Integer, HubGuildWarMsg>();
    	long now = now_cal.getTimeInMillis();
    	for (int i = 0; i < all_s; ++i) {
    		HubGuildInfo g = all_hub_g.get(i);
    		if (now - g.m_t > diff && g.weekly_s <= 0) {
    			continue;
    		}
    		HubGuildWarMsg m = msg_list.get(g.zid.intValue());
    		if (m == null) {
    			m = new HubGuildWarMsg();
    			msg_list.put(g.zid.intValue(), m);
    		}
    		if (m.f_list == null) {
    			m.f_list = new HashMap<String, HubGuildWarMsg.I>();
    		}
    		HubGuildWarMsg.I g_m = m.new I();
    		m.f_list.put(g.id, g_m);
    		g_m.rank = rank_list.size() + 1;
    		Integer rank = rank_list.get(g.id);
    		if (rank != null) {
    			g_m.rank = rank;
    		}
    	}
    	Map<Integer, Hubmap> url_list = ConfigConstant.tHubmap;
		for (Entry<Integer, Hubmap> u : url_list.entrySet()) {
			HubGuildWarMsg send = msg_list.get(u.getValue().getServerId());
			if (send == null) {
				continue;
			}
			SendWeekly cm = new SendWeekly();
			cm.url = u.getValue().getURL();
			cm.zid = u.getValue().getServerId();
			cm.send = send;
			Thread t = new Thread(cm);
			t.start();
		}
    }
	
	private void CleanGuildMem() {		
		List<HubGuildInfo> all_hub_g = BaseDaoImpl.getInstance().findAll(HubGuildInfo.class);
		BaseDataSource x = SpringContextUtil.getBean(HubGuildDB.class);
		for (int i = 0; i < all_hub_g.size(); ++i) {
			x.CleanMem(all_hub_g.get(i).id);
		}
		
		List<HubPlayerGuildInfo> all_hub_p = BaseDaoImpl.getInstance().findAll(HubPlayerGuildInfo.class);
		x = SpringContextUtil.getBean(HubPlayerGuildDB.class);
		for (int i = 0; i < all_hub_p.size(); ++i) {
			x.CleanMem(all_hub_p.get(i).id);
		}
		
		List<HubWarRcdInfo> all_hub_w = BaseDaoImpl.getInstance().findAll(HubWarRcdInfo.class);
		x = SpringContextUtil.getBean(HubWarRcdDB.class);
		for (int i = 0; i < all_hub_w.size(); ++i) {
			x.CleanMem(all_hub_w.get(i).id);
		}
	}
	
	private void CleanSendMem() {
		List<HubSendInfo> all_send = BaseDaoImpl.getInstance().findAll(HubSendInfo.class); 
		BaseDataSource x = SpringContextUtil.getBean(HubSendDB.class);
		for (int i = 0; i < all_send.size(); ++i) {
			x.CleanMem(all_send.get(i).id);
		}
	}
}

class CallMatch implements Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(CallMatch.class);
	
	public String url;
	
	public Integer zid;
	
	@Override
	public void run() {
		try {
    		String result = HttpRequest.PostFunction("http://"+ url + I_DefMoudle.HUB_H2G_REQ_MATCH_INFO, "");
    		CommonMsg res = JsonTransfer._In(result, CommonMsg.class);
    		log.info("CallMatch " + " zid " + zid + " " + url + " " + result );
    		GuildImpl.getInstance().handleAckGsGuildMatchInfo(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class SendMatchResult implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(SendMatchResult.class);
	
	public String url;
	
	public Integer zid;
	
	@Override
	public void run(){
		try {
			CommonMsg send = new CommonMsg(0, DefConstant.HUB_ID);
			GuildImpl.getInstance().handleSendMatchResult(send, zid);
			log.info("SendMatchResult " + zid + " " + url + " r " + JsonTransfer.getJson(send));
    		String result = HttpRequest.PostFunction("http://"+ url + I_DefMoudle.HUB_H2G_MATCH_RESULT, JsonTransfer.getJson(send));
    		log.info("zid " + zid +" get " + result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class SendDaily implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(SendDaily.class);
	
	public String url;
	
	public Integer zid;
	
	public HubGuildWarMsg send;
	
	@Override
	public void run(){
		try {
			CommonMsg send_c = new CommonMsg(0, DefConstant.HUB_ID);
			send_c.body.hub_gw_msg = send;
			log.info("SendDaily gw " + zid + " " + url + " r " + JsonTransfer.getJson(send_c));
			String result = HttpRequest.PostFunction("http://"+ url + I_DefMoudle.HUB_H2G_GW_DAILY, JsonTransfer.getJson(send_c));
			log.info("result {}", result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info(e.getMessage());
		}
	}
}

class SendWeekly implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(SendWeekly.class);
	
	public String url;
	
	public Integer zid;
	
	public HubGuildWarMsg send;
	
	@Override
	public void run(){
		try {
			CommonMsg send_c = new CommonMsg(0, DefConstant.HUB_ID);
			send_c.body.hub_gw_msg = send;
			log.info("SendWeekly gw " + zid + " " + url + " r " + JsonTransfer.getJson(send_c));
    		String result = HttpRequest.PostFunction("http://"+ url + I_DefMoudle.HUB_H2G_GW_WEEKLY, JsonTransfer.getJson(send_c));
    		log.info("result {}", result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info(e.getMessage());
		}
	}
}

class WeekScoreSort implements Comparator<HubGuildInfo> {
	public int compare(HubGuildInfo arg0, HubGuildInfo arg1) {
		if (arg0 == null && arg1 != null)
			return -1;
		if (arg0 != null && arg1 == null)
			return 1;
		if (arg0 == null && arg1 == null)
			return 0;
		
		//按照weekly大小，降序排
		if (arg0.weekly_s < arg1.weekly_s)
			return 1;
		if (arg0.weekly_s > arg1.weekly_s)
			return -1;

		//按照时间，升序
		if (arg0.b_t < arg1.b_t)
			return -1;
		if (arg0.b_t > arg1.b_t)
			return 1;
		
		return 0;
	}
}
