package com.ssmGame.module.guild;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.tool.colligate.CommUtil;
import com.ssmCore.tool.colligate.HttpRequest;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Guild;
import com.ssmData.config.entity.Hubmap;
import com.ssmData.config.entity.Mail;
import com.ssmData.config.entity.Tech;
import com.ssmData.config.entity.Techup;
import com.ssmData.config.entity.Vip;
import com.ssmData.config.entity.Warrank;
import com.ssmData.dbase.GuildDB;
import com.ssmData.dbase.GuildInfo;
import com.ssmData.dbase.HubGuildDB;
import com.ssmData.dbase.HubGuildInfo;
import com.ssmData.dbase.HubWarRcdDB;
import com.ssmData.dbase.HubWarRcdInfo;
import com.ssmData.dbase.MailInfo;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerGuildDB;
import com.ssmData.dbase.PlayerGuildInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerMailDB;
import com.ssmData.dbase.PlayerMailInfo;
import com.ssmData.dbase.PlayerRolesInfo;
import com.ssmData.dbase.PlayerRolesInfoDB;
import com.ssmData.dbase.RoleInfo;
import com.ssmData.dbase.ServerInfo;
import com.ssmData.dbase.ServerInfoDB;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.MsgCode;
import com.ssmGame.defdata.msg.guild.GuildMsg;
import com.ssmGame.defdata.msg.guild.GuildPlayerMsg;
import com.ssmGame.defdata.msg.guild.GuildSoloMsg;
import com.ssmGame.defdata.msg.guildwar.GuildWarLogMsg;
import com.ssmGame.defdata.msg.guildwar.GuildWarMsg;
import com.ssmGame.defdata.msg.guildwar.GuildWarPlayerMsg;
import com.ssmGame.defdata.msg.hub.HubGuildWarMsg;
import com.ssmGame.defdata.msg.hub.HubGuildWarMsg.Player;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
import com.ssmGame.manager.HubGuildWarManager;
import com.ssmGame.manager.RankManager;
import com.ssmGame.module.battle.BattlePack;
import com.ssmGame.module.battle.BattleResult;
import com.ssmGame.module.battle.BattleSimulator;
import com.ssmGame.module.battle.BattleType;
import com.ssmGame.module.mail.MailImpl;
import com.ssmGame.module.player.PlayerImpl;
import com.ssmGame.module.role.RoleAttrCalc;
import com.ssmGame.util.TimeUtils;

@Service
@Scope("prototype")
public class GuildImpl {
	private static final Logger log = LoggerFactory.getLogger(GuildImpl.class);
	private static final ReentrantLock lock = new ReentrantLock();
	
	private @Value("${HUB_URL}")String HUB_URL;
	private @Value("${IS_HUB}") int IS_HUB;
	
	private static final int APPLY_SIZE = 5;		//1人最多申请5个
	private static final int APPLY_LIST_SIZE = 10;  //1人刷新最多10个返回
	private static final int WAITING_LIST = 30;		//工会申请列表最多
	private static final int VP_SIZE = 2; //副会长人数
	
	private static final int WAR_STAT_IN = 0; //战斗中
	private static final int WAR_STAT_NOT_ENOUGH = 1; //匹配时人数不足
	private static final int WAR_STAT_OUT = 2; //匹配轮空
	private static final int WAR_CLOSE = 3; //战斗未开始
	private static final int WAR_ERROR = 4;
	
	private static final int MSG_NOT_ALLOW_CHG_GUILD = 1205; //公会战开始后不允许加入、创建、离开公会，不允许解散公会
	private static final int MSG_NOT_ENOUGH_WAR_CNT = 1108;		//公会战挑战次数不足
	
	private static final int DAILY_STARS = 5;  //每天最多被扣5星
	private static final int WIN_STARS_MAX = 3;	//一场最多得3星
	
	private static final int CODE_ERROR = 1; //通用错误
	private static final int CODE_DAILY_FULL = 2; //对手的每日扣星达到上线，不可挑战
	
	private static final int GW_MIN_CNT = 10;
	
    @Autowired
    private PlayerInfoDB m_player_db;
    private PlayerInfo m_player = null;
    
    @Autowired
    private PlayerBagDB m_bag_db;
    private PlayerBagInfo m_bag = null;
    
    @Autowired
    private GuildDB m_guild_db;
    private GuildInfo m_guild = null;
    
    @Autowired
    private PlayerGuildDB m_p_guild_db;
    private PlayerGuildInfo m_p_guild = null;
    
    @Autowired
    ServerInfoDB m_server_db;
    
	public void destroy()
	{		
		m_player_db = null;
		m_player = null;
		m_guild_db = null;
		m_guild = null;
		m_bag = null;
		m_bag_db = null;
		m_p_guild_db = null;
		m_p_guild = null;
		m_server_db = null;
	}
	
	public final static GuildImpl getInstance(){
        return SpringContextUtil.getBean(GuildImpl.class);
	}
	
	//个人工会基本信息
	public CommonMsg handlePlayerInfo(CommonMsg respond)
	{
		String uid = respond.header.uid;
		respond.body.guild = new GuildMsg();
		GuildMsg msg = respond.body.guild;
		msg.success = false;
		
		m_p_guild = m_p_guild_db.loadByUid(uid);
		if (null == m_p_guild) {
			log.error("no player guild db player {}", uid);
			return respond;
		}
		String g_id = m_p_guild.gd_id;
		if (g_id.length() <= 0) {
			log.error("player not in guild player {}", uid);
			return respond;
		}
		
		
		
    	return respond;
	}
	
	//随机申请队列(只读)
	public CommonMsg handleApplyList(CommonMsg respond)
	{
		String uid = respond.header.uid;
		respond.body.guild = new GuildMsg();
		GuildMsg msg = respond.body.guild;
		msg.success = false;
		
		m_p_guild = m_p_guild_db.loadByUid(uid);
		if (null == m_p_guild) {
			log.error("no player guild db player {}", uid);
			return respond;
		}
		String g_id = m_p_guild.gd_id;
		if (g_id.length() > 0) {
			log.error("player already in guild player {}, guild {}", uid, g_id);
			return respond;
		}
		int apply_list_size = APPLY_LIST_SIZE;
		if (apply_list_size < m_p_guild.apply.size()) {
			apply_list_size = m_p_guild.apply.size();
		}
		
		List<GuildInfo> apply_list = new ArrayList<GuildInfo>(apply_list_size);
		for (int i = 0; i < m_p_guild.apply.size(); ++i) {
			GuildInfo g_info = SpringContextUtil.getBean(GuildDB.class).loadByGuildId(m_p_guild.apply.get(i));
			if (null == g_info) {
				continue;
			}
			apply_list.add(g_info);
		}
		
		int rand_list_size = apply_list_size - apply_list.size();
		List<GuildInfo> rand_list = new ArrayList<GuildInfo>(rand_list_size);
		int rand_size = 0;
		if (rand_list_size > 0) {
			List<GuildInfo> all = BaseDaoImpl.getInstance().findAll(GuildInfo.class);
			for (int i = 0, j = all.size() - 1; i <= j;) {
				GuildInfo g_info = all.get(i);
				Guild g_cfg = ConfigConstant.tGuild.get(g_info.lv);
				boolean swap = false;
				if (g_cfg == null || g_info.waitings.size() >= WAITING_LIST) {
					swap = true;
				}
				if (!swap) {
					for (int k = 0; k < apply_list.size(); ++k) {
						if (apply_list.get(k).id.equals(g_info.id)) {
							swap = true;
							break;
						}
					}
				}
				if (!swap) {
					i++;
					rand_size = i;
					continue;
				}
				all.set(i, all.get(j--));
			}
			if (rand_size <= rand_list_size) {
				for (int i = 0; i < rand_size; ++i) {
					rand_list.add(all.get(i));
				}
			} else {
				for (int i = rand_size, j = 0; i > 0 && j < rand_list_size; --i, ++j) {
					int rand_idx = (int)(Math.random() * i);
					rand_list.add(all.get(rand_idx));
					all.set(rand_idx, all.get(i-1));
				}
			}
		}
		
		Map<Integer, String> rank = RankManager.guild_rank;
		List<Integer> rank_list = new ArrayList<Integer>(rank.keySet());
		int rnk_s = rank_list.size();
		int a_s = apply_list.size();
		msg.apply_list = new ArrayList<GuildSoloMsg>(a_s);
		for (int i = 0; i < a_s; ++i) {
			GuildInfo gi = apply_list.get(i);
			GuildSoloMsg gsm = new GuildSoloMsg();
			msg.apply_list.add(gsm);
			gsm.id = gi.id;
			for (int j = 0; j < rnk_s; ++j) {
				int key = rank_list.get(j);
				if (rank.get(key).equals(gsm.id)) {
					gsm.rank = key;
				}
			}
			gsm.name = gi.name;
			gsm.guild_lv = gi.lv;
			gsm.cnt_member = gi.members.size();
			gsm.chairman_uid = gi.ceo_id;
			m_player = m_player_db.loadById(gi.ceo_id);
			if (m_player != null) {
				gsm.chairman_name = m_player.user_base.getNickname();
			}
		}
		
		int r_s = rand_list.size();
		msg.guild_list = new ArrayList<GuildSoloMsg>(r_s);
		for (int i = 0; i < r_s; ++i) {
			GuildInfo gi = rand_list.get(i);
			GuildSoloMsg gsm = new GuildSoloMsg();
			msg.guild_list.add(gsm);
			gsm.id = gi.id;
			for (Entry<Integer, String> m : rank.entrySet()) {
				if (m.getValue().equals(gsm.id)) {
					gsm.rank = m.getKey();
				}
			}
			gsm.name = gi.name;
			gsm.guild_lv = gi.lv;
			gsm.cnt_member = gi.members.size();
			gsm.chairman_uid = gi.ceo_id;
			m_player = m_player_db.loadById(gi.ceo_id);
			if (m_player != null) {
				gsm.chairman_name = m_player.user_base.getNickname();
			}
		}
		
		msg.success = true;
    	return respond;
	}
	
	//查找具体工会加入（只读）
	public CommonMsg handleSearchGuild(CommonMsg respond, String s_name)
	{
		respond.body.guild = new GuildMsg();
		GuildMsg msg = respond.body.guild;
		msg.success = false;

		/*String target_id = s_id;
		m_guild = m_guild_db.loadByGuildId(target_id);
		if (null == m_guild) {
			respond.header.rt_sub = 1202;
			return respond;
		}*/
		//List<GuildInfo> find_all = BaseDaoImpl.getInstance().findAll(new Query(Criteria.where("name").is(s_name)), GuildInfo.class);
		List<GuildInfo> find_all = BaseDaoImpl.getInstance().findAll(new Query(Criteria.where("name").regex(".*" + s_name + ".*")), GuildInfo.class);
		if (find_all.size() > 0) {
			msg.guild_list = new ArrayList<GuildSoloMsg>(find_all.size());
			for (int i = 0; i < find_all.size(); ++i) {
				m_guild = find_all.get(i);
				GuildSoloMsg gm = new GuildSoloMsg();
				msg.guild_list.add(gm);
				gm.id = m_guild.id;
				gm.name = m_guild.name;
				gm.guild_lv = m_guild.lv;
				gm.cnt_member = m_guild.members.size();
				gm.chairman_uid = m_guild.ceo_id;
				msg.success = true;
				m_player = m_player_db.loadById(m_guild.ceo_id);
				if (m_player != null) {
					gm.chairman_name = m_player.user_base.getNickname();
				}
				Map<Integer, String> rank = RankManager.guild_rank;
				List<Integer> rank_list = new ArrayList<Integer>(rank.keySet());
				int rnk_s = rank_list.size();
				for (int j = 0; j < rnk_s; ++j) {
					int key = rank_list.get(j);
					if (rank.get(key).equals(gm.id)) {
						gm.rank = key;
					}
				}
			}
		} else {
			respond.header.rt_sub = 1155;
			return respond;
		}
	
		msg.success = true;
    	return respond;
	}
	
	//申请
	public CommonMsg handleApply(CommonMsg respond, String apply_gid)
	{	
		lock.lock();
		try {
			String uid = respond.header.uid;
			respond.body.guild = new GuildMsg();
			GuildMsg msg = respond.body.guild;
			msg.success = false;
			
			m_p_guild = m_p_guild_db.loadByUid(uid);
			if (null == m_p_guild) {
				log.error("no player guild db player {}", uid);
				return respond;
			}
			String g_id = m_p_guild.gd_id;
			if (g_id.length() > 0) {
				log.error("player already in guild player {}, guild {}", uid, g_id);
				return respond;
			}
			if (m_p_guild.quit_t > 0L 
			&& Calendar.getInstance().getTimeInMillis() - m_p_guild.quit_t < ConfigConstant.tConf.getGuildCd() * 60 * 60 * 1000) {
				//log.error("player in guild CD player {}, last_t {}", uid, m_p_guild.quit_t);
				respond.header.rt_sub = 1146;
				return respond;
			}
			
			List<String> my_apply = m_p_guild.apply;
			int my_size = my_apply.size();
			if (my_size >= APPLY_SIZE) {
				//log.error("player already MAX APPLY player {}, apply_size {}", uid, my_size);
				respond.header.rt_sub = 1147;
				return respond;
			}
			
			String target_id = apply_gid;
			m_guild = m_guild_db.loadByGuildId(target_id);
			if (m_guild == null) {
				//log.error("guild not exist player {}, apply_id {}", uid, target_id);
				respond.header.rt_sub = 1148;
				return respond;
			}
			List<String> guild_apply_list = m_guild.waitings;
			int guild_size = guild_apply_list.size();
			if (guild_size >= WAITING_LIST) {
				//log.error("guild already full player {}, apply_id {}, size {}", uid, target_id, guild_size);
				respond.header.rt_sub = 1149;
				return respond;
			}
			
			boolean add_my = true;
			for (int i = 0; i < my_size; ++i) {
				if (my_apply.get(i).equals(target_id)) {
					add_my = false;
					break;
				}
			}
			if (add_my) {
				my_apply.add(target_id);
				m_p_guild_db.save();
			}
			boolean add_guild = true;
			for (int i = 0; i < guild_size; ++i) {
				if (guild_apply_list.get(i).equals(uid)) {
					add_guild = false;
					break;
				}
			}
			if (add_guild) {
				guild_apply_list.add(uid);
				m_guild_db.save();
			}
			
			msg.success = true;
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();	
		}
	} 
	
	//取消申请
	public CommonMsg handleCancelApply(CommonMsg respond, String apply_gid)
	{
		lock.lock();
		try {
			String uid = respond.header.uid;
			respond.body.guild = new GuildMsg();
			GuildMsg msg = respond.body.guild;
			msg.success = false;
			
			m_p_guild = m_p_guild_db.loadByUid(uid);
			if (null == m_p_guild) {
				log.error("no player guild db player {}", uid);
				return respond;
			}
			String g_id = m_p_guild.gd_id;
			if (g_id.length() > 0) {
				log.error("player already in guild player {}, guild {}", uid, g_id);
				return respond;
			}
			List<String> my_apply = m_p_guild.apply;
			int my_size = my_apply.size();
			boolean del_my = false;

			String target_id = apply_gid;
			for (int i = 0; i < my_size; ++i) {
				if (my_apply.get(i).equals(target_id)) {
					my_apply.remove(i);
					del_my = true;
					break;
				}
			}
			if (del_my) {
				m_p_guild_db.save();
			}
			m_guild = m_guild_db.loadByGuildId(target_id);
			if (m_guild == null) {
				log.error("guild not exist id {}", target_id);
				return respond;
			}
			boolean del_guild = false;
			List<String> guild_apply_list = m_guild.waitings;
			int guild_size = guild_apply_list.size();
			for (int i = 0; i < guild_size; ++i) {
				if (guild_apply_list.get(i).equals(uid)) {
					guild_apply_list.remove(i);
					del_guild = true;
					break;
				}
			}
			if (del_guild) {
				m_guild_db.save();
			}
			
			msg.success = true;
			
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}	
	}
	
	//申请创建工会
	public CommonMsg handleCreateGuild(CommonMsg respond, String guild_name)
	{
		lock.lock();
		try {
			String uid = respond.header.uid;
			respond.body.guild = new GuildMsg();
			GuildMsg msg = respond.body.guild;
			msg.success = false;
			
			ServerInfo svr_info = m_server_db.load();
			if (svr_info == null) {
				log.error("no server info player {}", uid);
				return respond;
			}
			if (svr_info.guild_idx == null) {
				svr_info.guild_idx = 1;
			}
			else {
				svr_info.guild_idx++;
			}
			
			m_player = m_player_db.loadById(uid);
			if (m_player == null) {
				log.error("no player db player {}", uid);
				return respond;
			}
			if (m_player.vip_level < ConfigConstant.tConf.getGuildVip()){
				respond.header.rt_sub = 1152;
				return respond;
			}
			int cost = ConfigConstant.tConf.getGuildCreate();
			if (!m_player.hasDiamond(cost)) {
				respond.header.rt_sub = 1005;
				return respond;
			}
			m_p_guild = m_p_guild_db.loadByUid(uid);
			if (null == m_p_guild) {
				log.error("no player guild db player {}", uid);
				return respond;
			}
			String g_id = m_p_guild.gd_id;
			if (g_id.length() > 0) {
				respond.header.rt_sub = 1153;
				return respond;
			}

			String target_name = guild_name;
			if (target_name == null || target_name.length() <= 0 || target_name.length() >= 30) {
				log.warn("guild name len error, len {}", target_name.length());
				return respond;
			}
			GuildInfo test_info = SpringContextUtil.getBean(GuildDB.class).loadByGuildName(target_name);
			if (test_info != null) {
				respond.header.rt_sub = 1198;
				return respond;
			}
			
			StringBuffer sb = new StringBuffer(20);
			//本游戏工会id + UID的最后六位
			String guild_id = sb.append(svr_info.guild_idx).append(m_player.user_base.gid).append(m_player._id.substring(m_player._id.length()-3)).toString();
			test_info = SpringContextUtil.getBean(GuildDB.class).loadByGuildId(guild_id);
			if (test_info != null) {
				log.error("guild has same id player {}, guild id{}", uid, guild_id);
				return respond;
			}
			
			log.info("Creating Guild by player {}, guild id {}, guild name {}...", uid, guild_id, target_name);
			m_guild = (GuildInfo)m_guild_db.createDB(guild_id);
			m_guild.ceo_id = uid;
			m_guild.name = target_name;
			m_guild.funds = (double) ConfigConstant.tConf.getGuildCapital();
			GuildInfo.Member m = m_guild.new Member();
			m.id = uid;
			m_guild.members.add(m);
			
			PlayerImpl.SubDiamond(m_player, cost);
			respond.body.sync_player_info = new SyncPlayerInfoMsg();
			respond.body.sync_player_info.diamond = -cost;
			
			m_p_guild.apply.clear();
			m_p_guild.quit_t = 0L;
			m_p_guild.gd_id = guild_id;
			
			m_player_db.save();
			m_p_guild_db.save();
			m_guild_db.save();
			m_server_db.save();
			log.info("Creating Guild by player {}, guild id {}, guild name {} SUCESSS!", uid, guild_id, target_name);
			msg.success = true;
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}	
	}

	//申请工会详情
	public CommonMsg handleMyGuildInfo(CommonMsg respond)
	{
		String uid = respond.header.uid;
		respond.body.guild = new GuildMsg();
		GuildMsg msg = respond.body.guild;
		msg.success = false;
		
		m_p_guild = m_p_guild_db.loadByUid(uid);
		if (null == m_p_guild) {
			log.error("no player guild db player {}", uid);
			return respond;
		}
		/*String g_id = m_p_guild.gd_id;
		if (g_id.length() <= 0) {
			//log.error("player not in guild player {}, guild {}", uid, g_id);
			respond.header.rt_sub = 1201;
			return respond;
		}*/
	
		m_guild = m_guild_db.loadByGuildId(m_p_guild.gd_id);
		/*if (null == m_guild) {
			//log.error("no guild db player {}, guild {}", uid, g_id);
			respond.header.rt_sub = 1202;
			return respond;
		}*/

		msg.my_info = m_p_guild;
		msg.my_info.apply = null;
		if (m_guild != null) {
			msg.g_info = m_guild;
			msg.g_info.waitings = null;
			msg.g_info.members = null;
		}
		msg.success = true;
		
		return respond;
	}
	
	//金币捐献
	public CommonMsg handleGoldDonate(CommonMsg respond)
	{
		lock.lock();
		try {
			String uid = respond.header.uid;
			respond.body.guild = new GuildMsg();
			GuildMsg msg = respond.body.guild;
			msg.success = false;
			
			m_p_guild = m_p_guild_db.loadByUid(uid);
			if (null == m_p_guild) {
				log.error("no player guild db player {}", uid);
				return respond;
			}
			String g_id = m_p_guild.gd_id;
			if (g_id.length() <= 0) {
				//log.error("player not in guild player {}, guild {}", uid, g_id);
				respond.header.rt_sub = 1201;
				return respond;
			}
			
			m_guild = m_guild_db.loadByGuildId(m_p_guild.gd_id);
			if (null == m_guild) {
				//log.error("no guild db player {}, guild {}", uid, g_id);
				respond.header.rt_sub = 1202;
				return respond;
			}
			GuildInfo.Member mb = null;
			for (int i = 0; i < m_guild.members.size(); ++i) {
				if (m_guild.members.get(i).id.equals(uid)) {
					mb = m_guild.members.get(i);
					break;
				}
			}
			if (mb == null) {
				log.error("no player in guild player {}, guild {}", uid, g_id);
				return respond;	
			}
			m_player = m_player_db.loadById(uid);
			if (m_player == null) {
				log.error("no player db player {}", uid);
				return respond;
			}
			int cost = ConfigConstant.tConf.getDonateMoney();
			if (!m_player.hasGold(cost)){
				respond.header.rt_sub = 1004;
				return respond;
			}
			m_bag = m_bag_db.loadByUid(uid);
	    	if (m_bag == null) {
	    		log.error(" no bag id {}", uid);
	    		return respond;
	    	}
	    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
	    	{
	    		respond.header.rt_sub = 1168;
	    		return respond;
	    	}
			Vip v_cfg = ConfigConstant.tVip.get(m_player.vip_level);
			if (v_cfg == null){
				log.error("no vip cfg player {}, guild {}", uid, m_player.vip_level);
				return respond;
			}
			//获取捐献次数，跟vip有关
			int don_lmt = v_cfg.getVipDonate1();
			Calendar now = Calendar.getInstance();
			Calendar last = Calendar.getInstance();
			last.setTimeInMillis(m_p_guild.last_gold_dnt_t);
			boolean refresh = false;
			if (now.get(Calendar.DAY_OF_YEAR) != last.get(Calendar.DAY_OF_YEAR)) {
				m_p_guild.daily_gold_dnt_cnt = 0;
				refresh = true;
			}
			if (m_p_guild.daily_gold_dnt_cnt >= don_lmt) {
				if (refresh) {
					m_p_guild_db.save();
				}
				respond.header.rt_sub = 1162;
				return respond;
			}
			Calendar guild_last_t = Calendar.getInstance();
			guild_last_t.setTimeInMillis(mb.last_con_t);
			if (now.get(Calendar.DAY_OF_YEAR) != guild_last_t.get(Calendar.DAY_OF_YEAR)) {
				mb.daily_con = 0.0;
			}
			mb.last_con_t = Calendar.getInstance().getTimeInMillis();
			
			m_player.subGold(cost);
    		if (respond.body.sync_player_info == null)
    			respond.body.sync_player_info = new SyncPlayerInfoMsg();
    		respond.body.sync_player_info.gold = -cost;
    		
    		int id = ConfigConstant.tConf.getDonate1();
    		int cnt = ConfigConstant.tConf.getDonateNum1();
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
    		mb.daily_con += cnt;
    		mb.acc_con += cnt;
    		mb.last_con_t = now.getTimeInMillis();
    		
    		m_p_guild.daily_gold_dnt_cnt++;
    		m_p_guild.last_gold_dnt_t = now.getTimeInMillis();
			
			m_guild.exp += ConfigConstant.tConf.getDonateExp1();
			int lv = m_guild.lv;
			Guild g_cfg = ConfigConstant.tGuild.get(lv);
			if (g_cfg != null) {
				while (g_cfg.getGuildExp() != 0) {
					int last_exp = g_cfg.getGuildExp();
					if (m_guild.exp >= last_exp) {
						lv++;
						g_cfg = ConfigConstant.tGuild.get(lv);
						if (g_cfg == null) {
							m_guild.exp = (double)last_exp;
							lv--;
							break;
						}
					}
					break;
				}
			}
			m_guild.lv = lv;
			m_guild.funds += (double)ConfigConstant.tConf.getMoneyCapital();
			
			m_p_guild_db.save();
			m_guild_db.save();	
			m_bag_db.save();
			m_player_db.save();
			
			msg.lv = m_guild.lv;
			msg.exp = m_guild.exp;
			msg.daily_gold_dnt_cnt = (int)m_p_guild.daily_gold_dnt_cnt;
			msg.last_gold_dnt_t = m_p_guild.last_gold_dnt_t;
			msg.success = true;
			
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}
	}
	
	//钻石捐献
	public CommonMsg handleDmdDonate(CommonMsg respond)
	{
		lock.lock();
		try {
			String uid = respond.header.uid;
			respond.body.guild = new GuildMsg();
			GuildMsg msg = respond.body.guild;
			msg.success = false;
			
			m_p_guild = m_p_guild_db.loadByUid(uid);
			if (null == m_p_guild) {
				log.error("no player guild db player {}", uid);
				return respond;
			}
			String g_id = m_p_guild.gd_id;
			if (g_id.length() <= 0) {
				//log.error("player not in guild player {}, guild {}", uid, g_id);
				respond.header.rt_sub = 1201;
				return respond;
			}
			
			m_guild = m_guild_db.loadByGuildId(m_p_guild.gd_id);
			if (null == m_guild) {
				//log.error("no guild db player {}, guild {}", uid, g_id);
				respond.header.rt_sub = 1202;
				return respond;
			}
			GuildInfo.Member mb = null;
			for (int i = 0; i < m_guild.members.size(); ++i) {
				if (m_guild.members.get(i).id.equals(uid)) {
					mb = m_guild.members.get(i);
					break;
				}
			}
			if (mb == null) {
				log.error("no player in guild player {}, guild {}", uid, g_id);
				return respond;	
			}
			m_player = m_player_db.loadById(uid);
			if (m_player == null) {
				log.error("no player db player {}", uid);
				return respond;
			}
			int cost = ConfigConstant.tConf.getDonateDiamonds();
			if (!m_player.hasDiamond(cost)){
				respond.header.rt_sub = 1005;
				return respond;
			}
			m_bag = m_bag_db.loadByUid(uid);
	    	if (m_bag == null) {
	    		log.error(" no bag id {}", uid);
	    		return respond;
	    	}
	    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
	    	{
	    		respond.header.rt_sub = 1168;
	    		return respond;
	    	}
			Vip v_cfg = ConfigConstant.tVip.get(m_player.vip_level);
			if (v_cfg == null){
				log.error("no vip cfg player {}, guild {}", uid, m_player.vip_level);
				return respond;
			}
			//获取捐献次数，跟vip有关
			int don_lmt = v_cfg.getVipDonate2();
			Calendar now = Calendar.getInstance();
			Calendar last = Calendar.getInstance();
			last.setTimeInMillis(m_p_guild.last_dmd_dnt_t);
			boolean refresh = false;
			if (now.get(Calendar.DAY_OF_YEAR) != last.get(Calendar.DAY_OF_YEAR)) {
				m_p_guild.daily_dmd_dnt_cnt = 0;
				refresh = true;
			}
			if (m_p_guild.daily_dmd_dnt_cnt >= don_lmt) {
				if (refresh) {
					m_p_guild_db.save();
				}
				respond.header.rt_sub = 1162;
				return respond;
			}
			Calendar guild_last_t = Calendar.getInstance();
			guild_last_t.setTimeInMillis(mb.last_con_t);
			if (now.get(Calendar.DAY_OF_YEAR) != guild_last_t.get(Calendar.DAY_OF_YEAR)) {
				mb.daily_con = 0.0;
			}
			mb.last_con_t = Calendar.getInstance().getTimeInMillis();
			
			PlayerImpl.SubDiamond(m_player, cost);
    		if (respond.body.sync_player_info == null)
    			respond.body.sync_player_info = new SyncPlayerInfoMsg();
    		respond.body.sync_player_info.diamond = -cost;
			
    		int id = ConfigConstant.tConf.getDonate2();
    		int cnt = ConfigConstant.tConf.getDonateNum2();
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
    		mb.daily_con += cnt;
    		mb.acc_con += cnt;
    		mb.last_con_t = now.getTimeInMillis();
    		
    		m_p_guild.daily_dmd_dnt_cnt++;
    		m_p_guild.last_dmd_dnt_t = now.getTimeInMillis();
    		
    		m_guild.exp += ConfigConstant.tConf.getDonateExp2();
			int lv = m_guild.lv;
			Guild g_cfg = ConfigConstant.tGuild.get(lv);
			if (g_cfg != null) {
				while (g_cfg.getGuildExp() != 0) {
					int last_exp = g_cfg.getGuildExp();
					if (m_guild.exp >= last_exp) {
						lv++;
						g_cfg = ConfigConstant.tGuild.get(lv);
						if (g_cfg == null) {
							m_guild.exp = (double)last_exp;
							lv--;
							break;
						}
					}
					break;
				}
			}
			m_guild.lv = lv;
			m_guild.funds += (double)ConfigConstant.tConf.getDiamondsCapital();
			
			m_p_guild_db.save();
			m_guild_db.save();	
			m_bag_db.save();
			m_player_db.save();
			
			msg.lv = m_guild.lv;
			msg.exp = m_guild.exp;
			msg.daily_dmd_dnt_cnt = (int)m_p_guild.daily_dmd_dnt_cnt;
			msg.last_dmd_dnt_t = m_p_guild.last_dmd_dnt_t;
			msg.success = true;
    		
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}
	}
	
	//查询其他工会详情
	/*public CommonMsg handleOtherGuildInfo(CommonMsg respond)
	{
		return respond;
	}*/
	
	//修改公告
	public CommonMsg handleChgWords(CommonMsg respond, String new_words)
	{
		lock.lock();
		try {
			String uid = respond.header.uid;
			respond.body.guild = new GuildMsg();
			GuildMsg msg = respond.body.guild;
			msg.success = false;
			
			m_p_guild = m_p_guild_db.loadByUid(uid);
			if (null == m_p_guild) {
				log.error("no player guild db player {}", uid);
				return respond;
			}
			String g_id = m_p_guild.gd_id;
			if (g_id.length() <= 0) {
				//log.error("player not in guild player {}, guild {}", uid, g_id);
				respond.header.rt_sub = 1201;
				return respond;
			}
			
			m_guild = m_guild_db.loadByGuildId(m_p_guild.gd_id);
			if (null == m_guild) {
				//log.error("no guild db player {}, guild {}", uid, g_id);
				respond.header.rt_sub = 1202;
				return respond;
			}
			
			if (!m_guild.ceo_id.equals(uid)) {
				log.error("player is not ceo player {}, guild {}, ceo_id {}", uid, g_id, m_guild.ceo_id);
				return respond;
			}
			
			String words = new_words;
			if (words.length() >= 500) {
				log.warn("Wors is begger than 500 , len {}", words.length());
				return respond;
			}
			m_guild.words = words;
			m_guild_db.save();
			
			msg.success = true;
			
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}
	}
	
	//转让
	public CommonMsg handleTransfer(CommonMsg respond, String trans_id)
	{
		lock.lock();
		try {
			String uid = respond.header.uid;
			respond.body.guild = new GuildMsg();
			GuildMsg msg = respond.body.guild;
			msg.success = false;
			
			m_p_guild = m_p_guild_db.loadByUid(uid);
			if (null == m_p_guild) {
				log.error("no player guild db player {}", uid);
				return respond;
			}
			String g_id = m_p_guild.gd_id;
			if (g_id.length() <= 0) {
				//log.error("player not in guild player {}, guild {}", uid, g_id);
				respond.header.rt_sub = 1201;
				return respond;
			}
			
			m_guild = m_guild_db.loadByGuildId(m_p_guild.gd_id);
			if (null == m_guild) {
				//log.error("no guild db player {}, guild {}", uid, g_id);
				respond.header.rt_sub = 1202;
				return respond;
			}
			
			if (!m_guild.ceo_id.equals(uid)) {
				log.error("player is not ceo player {}, guild {}, ceo_id {}", uid, g_id, m_guild.ceo_id);
				return respond;
			}
			
			String target_uid = trans_id;
			boolean found = false;
			int size = m_guild.members.size();
			for (int i = 0; i < size; ++i) {
				if (m_guild.members.get(i).id.equals(target_uid)) {
					found = true;
					break;
				}
			}
			if (!found) {
				log.error("target player is not in guild player {}, guild {}", target_uid, g_id);
				return respond;
			}
			
			m_guild.ceo_id = target_uid;
			for (int i = 0; i < m_guild.vp_ids.size(); ++i) {
				if (m_guild.vp_ids.get(i).equals(target_uid)) {
					m_guild.vp_ids.remove(i);
					break;
				}
			}
			m_guild_db.save();
			
			msg.success = true;
			
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}
	}
	
	//任命副会长
	public CommonMsg handleAppoint(CommonMsg respond, String vp_uid)
	{
		lock.lock();
		try {
			String uid = respond.header.uid;
			respond.body.guild = new GuildMsg();
			GuildMsg msg = respond.body.guild;
			msg.success = false;
			
			m_p_guild = m_p_guild_db.loadByUid(uid);
			if (null == m_p_guild) {
				log.error("no player guild db player {}", uid);
				return respond;
			}
			String g_id = m_p_guild.gd_id;
			if (g_id.length() <= 0) {
				//log.error("player not in guild player {}, guild {}", uid, g_id);
				respond.header.rt_sub = 1201;
				return respond;
			}
			
			m_guild = m_guild_db.loadByGuildId(m_p_guild.gd_id);
			if (null == m_guild) {
				//log.error("no guild db player {}, guild {}", uid, g_id);
				respond.header.rt_sub = 1202;
				return respond;
			}
			
			if (!m_guild.ceo_id.equals(uid)) {
				log.error("player is not ceo player {}, guild {}, ceo_id {}", uid, g_id, m_guild.ceo_id);
				return respond;
			}
			
			String target_uid = vp_uid;
			if (uid.equals(target_uid)) {
				log.error("ceo cant not be vp guild {}, ceo_id {}, target_id {}", g_id, m_guild.ceo_id, target_uid);
				return respond;
			}
			int vp_size = m_guild.vp_ids.size();
			if (vp_size >= VP_SIZE) {
				respond.header.rt_sub = 1204;
				return respond;
			}
			boolean found_vp = false;
			for (int i = 0; i < vp_size; ++i) {
				if (m_guild.vp_ids.get(i).equals(target_uid)) {
					found_vp = true;
					break;
				}
			}
			if (found_vp) {
				log.error("target is already vp guild {}, target_uid {}", g_id, target_uid);
				return respond;
			}
			boolean found = false;
			int size = m_guild.members.size();
			for (int i = 0; i < size; ++i) {
				if (m_guild.members.get(i).id.equals(target_uid)) {
					found = true;
					break;
				}
			}
			if (!found) {
				log.error("target player is not in guild player {}, guild {}", target_uid, g_id);
				return respond;
			}
			
			m_guild.vp_ids.add(target_uid);
			m_guild_db.save();
			
			msg.success = true;
			
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}
	}
	
	//解职
	public CommonMsg handleDischarge(CommonMsg respond, String dis)
	{
		lock.lock();
		try {
			String uid = respond.header.uid;
			respond.body.guild = new GuildMsg();
			GuildMsg msg = respond.body.guild;
			msg.success = false;
			
			m_p_guild = m_p_guild_db.loadByUid(uid);
			if (null == m_p_guild) {
				log.error("no player guild db player {}", uid);
				return respond;
			}
			String g_id = m_p_guild.gd_id;
			if (g_id.length() <= 0) {
				//log.error("player not in guild player {}, guild {}", uid, g_id);
				respond.header.rt_sub = 1201;
				return respond;
			}
			
			m_guild = m_guild_db.loadByGuildId(m_p_guild.gd_id);
			if (null == m_guild) {
				//log.error("no guild db player {}, guild {}", uid, g_id);
				respond.header.rt_sub = 1202;
				return respond;
			}
			
			if (!m_guild.ceo_id.equals(uid)) {
				log.error("player is not ceo player {}, guild {}, ceo_id {}", uid, g_id, m_guild.ceo_id);
				return respond;
			}
			
			String target_uid = dis;
			boolean found_vp = false;
			int vp_size = m_guild.vp_ids.size();
			for (int i = 0; i < vp_size; ++i) {
				if (m_guild.vp_ids.get(i).equals(target_uid)) {
					m_guild.vp_ids.remove(i);
					found_vp = true;
					break;
				}
			}
			if (!found_vp) {
				log.error("target is not vp guild {}, target_uid {}", g_id, target_uid);
				return respond;
			}
			m_guild_db.save();
			
			msg.success = true;
			
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}
	}
	
	//踢人(注意是否副会长和会长)
	public CommonMsg handleDismiss(CommonMsg respond, String dismiss_id)
	{
		lock.lock();
		try {
			String uid = respond.header.uid;
			respond.body.guild = new GuildMsg();
			GuildMsg msg = respond.body.guild;
			msg.success = false;
			
			m_p_guild = m_p_guild_db.loadByUid(uid);
			if (null == m_p_guild) {
				log.error("no player guild db player {}", uid);
				return respond;
			}
			String g_id = m_p_guild.gd_id;
			if (g_id.length() <= 0) {
				//log.error("player not in guild player {}, guild {}", uid, g_id);
				respond.header.rt_sub = 1201;
				return respond;
			}
			
			m_guild = m_guild_db.loadByGuildId(m_p_guild.gd_id);
			if (null == m_guild) {
				//log.error("no guild db player {}, guild {}", uid, g_id);
				respond.header.rt_sub = 1202;
				return respond;
			}
			
			if (guildIsInWarNow(m_guild)) {
				respond.header.rt_sub = MSG_NOT_ALLOW_CHG_GUILD;
				return respond;
			}
			
			boolean is_ceo = m_guild.ceo_id.equals(uid)?true:false;
			boolean is_vp = false;
			int vp_size = m_guild.vp_ids.size();
			if (!is_ceo) {
				for (int i = 0; i < vp_size; ++i) {
					if (m_guild.vp_ids.get(i).equals(uid)) {
						is_vp = true;
						break;
					}
				}
			}
			if (!is_ceo && !is_vp) {
				//log.error("player is not ceo and not vp player {}, guild {}", uid, g_id);
				respond.header.rt_sub = 1200;
				return respond;
			}
			
			String target_uid = dismiss_id;
			if (m_guild.ceo_id.equals(target_uid)) {
				log.error("target is ceo target {}, guild {}, ceo {}", target_uid, g_id, m_guild.ceo_id);
				return respond;
			}
			int target_is_vp_idx = -1;
			for (int i = 0; i < vp_size; ++i) {
				if (m_guild.vp_ids.get(i).equals(target_uid)) {
					target_is_vp_idx = i;
					break;
				}
			}
			if (is_vp && target_is_vp_idx >= 0) {
				log.error("player and target both vp target {}, guild {}, player {}", target_uid, g_id, uid);
				return respond;
			}
			
			if (target_is_vp_idx >= 0) {
				m_guild.vp_ids.remove(target_is_vp_idx);
			}
			int size = m_guild.members.size();
			for (int i = 0; i < size; ++i) {
				if (m_guild.members.get(i).id.equals(target_uid)) {
					m_guild.members.remove(i);
					break;
				}
			}
			m_guild_db.save();
			
			PlayerGuildDB g_db = SpringContextUtil.getBean(PlayerGuildDB.class);
			PlayerGuildInfo g_info = g_db.loadByUid(target_uid);
			if (g_info != null) {
				g_info.gd_id = "";
				g_info.quit_t = Calendar.getInstance().getTimeInMillis();
				g_info.apply.clear();
				g_db.save();
			}
			
			msg.success = true;
			
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}
	}
	
	//解散公会
	public CommonMsg handleDissolve(CommonMsg respond, String dis_id)
	{
		lock.lock();
		try {
			String uid = respond.header.uid;
			respond.body.guild = new GuildMsg();
			GuildMsg msg = respond.body.guild;
			msg.success = false;
			
			m_p_guild = m_p_guild_db.loadByUid(uid);
			if (null == m_p_guild) {
				log.error("no player guild db player {}", uid);
				return respond;
			}
			String g_id = m_p_guild.gd_id;
			if (g_id.length() <= 0) {
				respond.header.rt_sub = 1201;
				return respond;
			}
			
			if (!g_id.equals(dis_id)) {
				log.error("guild id not the same player {}, guild {} dis_id {}", uid, g_id, dis_id);
				return respond;
			}
			
			m_guild = m_guild_db.loadByGuildId(g_id);
			if (null == m_guild) {
				//log.error("no guild db player {}, guild {}", uid, g_id);
				respond.header.rt_sub = 1202;
				return respond;
			}
			
			if (!m_guild.ceo_id.equals(uid)) {
				log.error("player is not ceo player {}, guild {}, ceo_id {}", uid, g_id, m_guild.ceo_id);
				return respond;
			}
			
			if (guildIsInWarNow(m_guild)) {
				respond.header.rt_sub = MSG_NOT_ALLOW_CHG_GUILD;
				return respond;
			}
			
			int waiting_size = m_guild.waitings.size();
			for (int i = 0; i < waiting_size; ++i) {
				String w_id = m_guild.waitings.get(i);
				PlayerGuildDB g_db = SpringContextUtil.getBean(PlayerGuildDB.class);
				PlayerGuildInfo g_info = g_db.loadByUid(w_id);
				if (g_info != null) {
					for (int j = 0; j < g_info.apply.size(); ++j) {
						if (g_info.apply.get(j).equals(g_id)) {
							g_info.apply.remove(j);
							g_db.save();
							break;
						}
					}
				}
			}
			List<GuildInfo.Member> m_list = m_guild.members;
			long now = Calendar.getInstance().getTimeInMillis();
			int mb_size = m_list.size();
			for (int i = 0; i < mb_size; ++i) {
				String w_id = m_list.get(i).id;
				PlayerGuildDB g_db = SpringContextUtil.getBean(PlayerGuildDB.class);
				PlayerGuildInfo g_info = g_db.loadByUid(w_id);
				if (g_info != null && g_info.gd_id.equals(g_id)) {
					g_info.apply.clear();
					g_info.gd_id = "";
					g_info.quit_t = now;
					g_db.save();
				}
			}
			m_p_guild_db.CleanMem(g_id);
			BaseDaoImpl.getInstance().remove(new Query(Criteria.where("id").is(g_id)), GuildInfo.class);
			
			msg.success = true;
			
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}
	}
	
	//退出公会（会长不能退出）
	public CommonMsg handleQuit(CommonMsg respond)
	{
		lock.lock();
		try {
			String uid = respond.header.uid;
			respond.body.guild = new GuildMsg();
			GuildMsg msg = respond.body.guild;
			msg.success = false;
			
			m_p_guild = m_p_guild_db.loadByUid(uid);
			if (null == m_p_guild) {
				log.error("no player guild db player {}", uid);
				return respond;
			}
			String g_id = m_p_guild.gd_id;
			if (g_id.length() <= 0) {
				//log.error("player not in guild player {}, guild {}", uid, g_id);
				respond.header.rt_sub = 1201;
				return respond;
			}
			
			m_guild = m_guild_db.loadByGuildId(m_p_guild.gd_id);
			if (null == m_guild) {
				//log.error("no guild db player {}, guild {}", uid, g_id);
				respond.header.rt_sub = 1202;
				return respond;
			}
			
			if(m_guild.ceo_id.equals(uid)) {
				log.error("ceo cannot quit player {}, guild {}, ceo {}", uid, g_id, m_guild.ceo_id);
				return respond;
			}
			
			if (guildIsInWarNow(m_guild)) {
				respond.header.rt_sub = MSG_NOT_ALLOW_CHG_GUILD;
				return respond;
			}
			
			m_p_guild.gd_id = "";
			m_p_guild.apply.clear();
			m_p_guild.quit_t = Calendar.getInstance().getTimeInMillis();
			m_p_guild_db.save();
			
			for (int i = 0; i < m_guild.vp_ids.size(); ++i) {
				if (m_guild.vp_ids.get(i).equals(uid)) {
					m_guild.vp_ids.remove(i);
					break;
				}
			}
			for (int i = 0; i < m_guild.members.size(); ++i) {
				if (m_guild.members.get(i).id.equals(uid)) {
					m_guild.members.remove(i);
					break;
				}
			}
			m_guild_db.save();
			
			msg.success = true;
			
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}
	}
	
	//同意申请
	public CommonMsg handlePassApply(CommonMsg respond, String pass_id)
	{
		lock.lock();
		try {
			String uid = respond.header.uid;
			respond.body.guild = new GuildMsg();
			GuildMsg msg = respond.body.guild;
			msg.success = false;
			
			m_p_guild = m_p_guild_db.loadByUid(uid);
			if (null == m_p_guild) {
				log.error("no player guild db player {}", uid);
				return respond;
			}
			String g_id = m_p_guild.gd_id;
			if (g_id.length() <= 0) {
				//log.error("player not in guild player {}, guild {}", uid, g_id);
				respond.header.rt_sub = 1201;
				return respond;
			}
			
			m_guild = m_guild_db.loadByGuildId(m_p_guild.gd_id);
			if (null == m_guild) {
				//log.error("no guild db player {}, guild {}", uid, g_id);
				respond.header.rt_sub = 1202;
				return respond;
			}
			
			/*if (guildIsInWarNow(m_guild)) {
				respond.header.rt_sub = MSG_NOT_ALLOW_CHG_GUILD;
				return respond;
			}*/
			
			Guild g_cfg = ConfigConstant.tGuild.get(m_guild.lv);
			if (g_cfg == null) {
				log.error("no guild cfg guild {}, lv {}", m_p_guild.gd_id, m_guild.lv);
				return respond;
			}
			if (g_cfg.getGuildPeople() <= m_guild.members.size()) {
				respond.header.rt_sub = 1158;
				return respond;
			}
			
			boolean is_ceo = m_guild.ceo_id.equals(uid)?true:false;
			boolean is_vp = false;
			int vp_size = m_guild.vp_ids.size();
			if (!is_ceo) {
				for (int i = 0; i < vp_size; ++i) {
					if (m_guild.vp_ids.get(i).equals(uid)) {
						is_vp = true;
						break;
					}
				}
			}
			if (!is_ceo && !is_vp) {
				//log.error("player is not ceo and not vp player {}, guild {}", uid, g_id);
				respond.header.rt_sub = 1200;
				return respond;
			}
			
			String target_uid = pass_id;
			
			int found_waiting_idx = -1;
			for (int i = 0; i < m_guild.waitings.size(); ++i) {
				if (m_guild.waitings.get(i).equals(target_uid)){
					found_waiting_idx = i;
					break;
				}
			}
			if (found_waiting_idx < 0) {
				respond.header.rt_sub = 1156;
				return respond;
			}
			m_guild.waitings.remove(found_waiting_idx);
			
			PlayerGuildDB g_db = SpringContextUtil.getBean(PlayerGuildDB.class);
			PlayerGuildInfo g_info = g_db.loadByUid(target_uid);
			if (g_info == null) {
				log.error("no target player gdb player {}", target_uid);
				m_guild_db.save();
				return respond;
			}
			if (g_info.gd_id.length() > 0) {
				respond.header.rt_sub = 1157;
				m_guild_db.save();
				return respond;
			}
			int found_apply_idx = -1;
			for (int i = 0; i < g_info.apply.size(); ++i) {
				if (g_info.apply.get(i).equals(m_p_guild.gd_id)) {
					found_apply_idx = i;
					break;
				}
			}
			if (found_apply_idx < 0) {
				respond.header.rt_sub = 1156;
				m_guild_db.save();
				return respond;
			}
			g_info.apply.clear();;
			g_info.gd_id = m_p_guild.gd_id;
			g_info.quit_t = 0L;
			g_info.in_t = Calendar.getInstance().getTimeInMillis();
			g_db.save();
			
			GuildInfo.Member m = m_guild.new Member();
			m.id = target_uid;
			m_guild.members.add(m);
			m_guild_db.save();
			
			msg.success = true;
			
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}
	}
	
	//否决申请
	public CommonMsg handleDenyApply(CommonMsg respond, String deny_id)
	{
		lock.lock();
		try {
			String uid = respond.header.uid;
			respond.body.guild = new GuildMsg();
			GuildMsg msg = respond.body.guild;
			msg.success = false;
			
			m_p_guild = m_p_guild_db.loadByUid(uid);
			if (null == m_p_guild) {
				log.error("no player guild db player {}", uid);
				return respond;
			}
			String g_id = m_p_guild.gd_id;
			if (g_id.length() <= 0) {
				//log.error("player not in guild player {}, guild {}", uid, g_id);
				respond.header.rt_sub = 1201;
				return respond;
			}
			
			m_guild = m_guild_db.loadByGuildId(m_p_guild.gd_id);
			if (null == m_guild) {
				//log.error("no guild db player {}, guild {}", uid, g_id);
				respond.header.rt_sub = 1202;
				return respond;
			}
			
			Guild g_cfg = ConfigConstant.tGuild.get(m_guild.lv);
			if (g_cfg == null) {
				log.error("no guild cfg guild {}, lv {}", m_p_guild.gd_id, m_guild.lv);
				return respond;
			}
			if (g_cfg.getGuildPeople() <= m_guild.members.size()) {
				respond.header.rt_sub = 1158;
				return respond;
			}
			
			boolean is_ceo = m_guild.ceo_id.equals(uid)?true:false;
			boolean is_vp = false;
			int vp_size = m_guild.vp_ids.size();
			if (!is_ceo) {
				for (int i = 0; i < vp_size; ++i) {
					if (m_guild.vp_ids.get(i).equals(uid)) {
						is_vp = true;
						break;
					}
				}
			}
			if (!is_ceo && !is_vp) {
				//log.error("player is not ceo and not vp player {}, guild {}", uid, g_id);
				respond.header.rt_sub = 1200;
				return respond;
			}
			
			String target_uid = deny_id;
			
			for (int i = 0; i < m_guild.waitings.size(); ++i) {
				if (m_guild.waitings.get(i).equals(target_uid)){
					m_guild.waitings.remove(i);
					m_guild_db.save();
					break;
				}
			}

			PlayerGuildDB g_db = SpringContextUtil.getBean(PlayerGuildDB.class);
			PlayerGuildInfo g_info = g_db.loadByUid(target_uid);
			if (g_info == null) {
				log.error("no target player gdb player {}", target_uid);
				return respond;
			}
			for (int i = 0; i < g_info.apply.size(); ++i) {
				if (g_info.apply.get(i).equals(m_p_guild.gd_id)) {
					g_info.apply.remove(i);
					g_db.save();
					break;
				}
			}
			
			msg.success = true;
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}
	}

	//批量同意申请
	public CommonMsg handleOneKeyPass(CommonMsg respond) {
		lock.lock();
		try {
			String uid = respond.header.uid;
			respond.body.guild = new GuildMsg();
			GuildMsg msg = respond.body.guild;
			msg.success = false;
			
			m_p_guild = m_p_guild_db.loadByUid(uid);
			if (null == m_p_guild) {
				log.error("no player guild db player {}", uid);
				return respond;
			}
			String g_id = m_p_guild.gd_id;
			if (g_id.length() <= 0) {
				respond.header.rt_sub = 1201;
				return respond;
			}
			
			m_guild = m_guild_db.loadByGuildId(m_p_guild.gd_id);
			if (null == m_guild) {
				respond.header.rt_sub = 1202;
				return respond;
			}
			
			/*if (guildIsInWarNow(m_guild)) {
				respond.header.rt_sub = MSG_NOT_ALLOW_CHG_GUILD;
				return respond;
			}*/
			
			Guild g_cfg = ConfigConstant.tGuild.get(m_guild.lv);
			if (g_cfg == null) {
				log.error("no guild cfg guild {}, lv {}", m_p_guild.gd_id, m_guild.lv);
				return respond;
			}
			if (g_cfg.getGuildPeople() <= m_guild.members.size()) {
				respond.header.rt_sub = 1158;
				return respond;
			}
			
			boolean is_ceo = m_guild.ceo_id.equals(uid)?true:false;
			boolean is_vp = false;
			int vp_size = m_guild.vp_ids.size();
			if (!is_ceo) {
				for (int i = 0; i < vp_size; ++i) {
					if (m_guild.vp_ids.get(i).equals(uid)) {
						is_vp = true;
						break;
					}
				}
			}
			if (!is_ceo && !is_vp) {
				respond.header.rt_sub = 1200;
				return respond;
			}
			
			int add_size = g_cfg.getGuildPeople() - m_guild.members.size();
			if (add_size > m_guild.waitings.size()) {
				add_size = m_guild.waitings.size();
			}
			msg.uid_list = new ArrayList<String>(add_size);
			long now = Calendar.getInstance().getTimeInMillis();
			for (int i = 0; i < add_size;i++) {
				String target_uid = m_guild.waitings.remove(0);

				PlayerGuildDB g_db = SpringContextUtil.getBean(PlayerGuildDB.class);
				PlayerGuildInfo g_info = g_db.loadByUid(target_uid);
				if (g_info == null) {
					continue;
				}
				for (int j = 0; j < g_info.apply.size(); ++j) {
					if (g_info.apply.get(j).equals(m_p_guild.gd_id)) {						
						g_info.apply.clear();;
						g_info.gd_id = m_p_guild.gd_id;
						g_info.quit_t = 0L;
						g_info.in_t = now;
						g_db.save();
						msg.uid_list.add(g_info.id);
						
						GuildInfo.Member m = m_guild.new Member();
						m.id = target_uid;
						m_guild.members.add(m);

						break;
					}
				}
			}
			m_guild_db.save();
			msg.success = true;
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}
	}
	
	//批量拒绝申请
	public CommonMsg handleOneKeyDeny(CommonMsg respond) {
		lock.lock();
		try {
			String uid = respond.header.uid;
			respond.body.guild = new GuildMsg();
			GuildMsg msg = respond.body.guild;
			msg.success = false;
			
			m_p_guild = m_p_guild_db.loadByUid(uid);
			if (null == m_p_guild) {
				log.error("no player guild db player {}", uid);
				return respond;
			}
			String g_id = m_p_guild.gd_id;
			if (g_id.length() <= 0) {
				respond.header.rt_sub = 1201;
				return respond;
			}
			
			m_guild = m_guild_db.loadByGuildId(m_p_guild.gd_id);
			if (null == m_guild) {
				respond.header.rt_sub = 1202;
				return respond;
			}
			
			Guild g_cfg = ConfigConstant.tGuild.get(m_guild.lv);
			if (g_cfg == null) {
				log.error("no guild cfg guild {}, lv {}", m_p_guild.gd_id, m_guild.lv);
				return respond;
			}
			
			boolean is_ceo = m_guild.ceo_id.equals(uid)?true:false;
			boolean is_vp = false;
			int vp_size = m_guild.vp_ids.size();
			if (!is_ceo) {
				for (int i = 0; i < vp_size; ++i) {
					if (m_guild.vp_ids.get(i).equals(uid)) {
						is_vp = true;
						break;
					}
				}
			}
			if (!is_ceo && !is_vp) {
				respond.header.rt_sub = 1200;
				return respond;
			}
			
			int del_size = m_guild.waitings.size();
			for (int i = 0; i < del_size;i++) {
				String target_uid = m_guild.waitings.get(0);
				
				PlayerGuildDB g_db = SpringContextUtil.getBean(PlayerGuildDB.class);
				PlayerGuildInfo g_info = g_db.loadByUid(target_uid);
				if (g_info == null) {
					continue;
				}
				for (int j = 0; j < g_info.apply.size(); ++j) {
					if (g_info.apply.get(j).equals(m_p_guild.gd_id)) {
						g_info.apply.remove(i);
						g_db.save();
						break;
					}
				}
			}
			m_guild.waitings.clear();
			m_guild_db.save();
			msg.success = true;
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}
	}
	
	//获取工会的申请列表
	public CommonMsg handleGuildWaitingList(CommonMsg respond) {
		lock.lock();
		try {
			String uid = respond.header.uid;
			respond.body.guild = new GuildMsg();
			GuildMsg msg = respond.body.guild;
			msg.success = false;
			
			m_p_guild = m_p_guild_db.loadByUid(uid);
			if (null == m_p_guild) {
				log.error("no player guild db player {}", uid);
				return respond;
			}
			String g_id = m_p_guild.gd_id;
			if (g_id.length() <= 0) {
				log.error("player not in guild player {}, guild {}", uid, g_id);
				return respond;
			}
			
			m_guild = m_guild_db.loadByGuildId(m_p_guild.gd_id);
			if (null == m_guild) {
				log.error("no guild db player {}, guild {}", uid, g_id);
				return respond;
			}
			
			int w_s = m_guild.waitings.size();
			List<String> new_waitings = new ArrayList<String>(w_s);
			List<GuildPlayerMsg> gpm_list = new ArrayList<GuildPlayerMsg>(w_s);
			boolean chg = false;
			for (int i = 0; i < w_s; i++) {
				String p = m_guild.waitings.get(i);
				PlayerGuildDB g_db = SpringContextUtil.getBean(PlayerGuildDB.class);
				PlayerGuildInfo g_info = g_db.loadByUid(p);
				if (g_info == null || !g_info.apply.contains(g_id)) {
					chg = true;
					continue;
				}
				PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
				PlayerInfo p_info = p_db.loadById(p);
				if (p_info == null) {
					chg = true;
					continue;
				}
				new_waitings.add(p);
				
				GuildPlayerMsg gpm = new GuildPlayerMsg();
				gpm_list.add(gpm);
				gpm.uid = p_info._id;
				gpm.username = p_info.user_base.getNickname();
				gpm.avatar = p_info.user_base.getuImg();
				gpm.vip = p_info.vip_level;
				gpm.team_lv = p_info.team_lv;
				gpm.fighting = p_info.team_current_fighting;		
			}
			if (chg) {
				m_guild.waitings = new_waitings;
				m_guild_db.save();
			}
			
			msg.user_list = gpm_list;
			msg.success = true;
			
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}
	}

	//申请工会成员列表详情
	public CommonMsg handleMembersInfo(CommonMsg respond)
	{
		String uid = respond.header.uid;
		respond.body.guild = new GuildMsg();
		GuildMsg msg = respond.body.guild;
		msg.success = false;
		
		m_p_guild = m_p_guild_db.loadByUid(uid);
		if (null == m_p_guild) {
			log.error("no player guild db player {}", uid);
			return respond;
		}
		String g_id = m_p_guild.gd_id;
		if (g_id.length() <= 0) {
			//log.error("player not in guild player {}, guild {}", uid, g_id);
			respond.header.rt_sub = 1201;
			return respond;
		}
		
		lock.lock();
		try {
			m_guild = m_guild_db.loadByGuildId(m_p_guild.gd_id);
			if (null == m_guild) {
				//log.error("no guild db player {}, guild {}", uid, g_id);
				respond.header.rt_sub = 1202;
				return respond;
			}
			Calendar now = Calendar.getInstance();
			Calendar guild_last_t = Calendar.getInstance();
			List<GuildPlayerMsg> user_list = new ArrayList<GuildPlayerMsg>(m_guild.members.size());
			int ms = m_guild.members.size();
			for (int i = 0; i < ms; ++i) {
				GuildInfo.Member m = m_guild.members.get(i);
				
				PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
				PlayerInfo p_info = p_db.loadById(m.id);
				if (p_info == null) {
					continue;
				}
				GuildPlayerMsg gpm = new GuildPlayerMsg();
				user_list.add(gpm);
				gpm.uid = m.id;
				gpm.username = p_info.user_base.getNickname();
				gpm.avatar = p_info.user_base.getuImg();
				gpm.vip = p_info.vip_level;
				gpm.team_lv = p_info.team_lv;
				gpm.fighting = p_info.team_current_fighting;
				gpm.dnt = m.acc_con;
				guild_last_t.setTimeInMillis(m.last_con_t);
				if (now.get(Calendar.DAY_OF_YEAR) != guild_last_t.get(Calendar.DAY_OF_YEAR)) {
					m.daily_con = 0.0;
				}
				gpm.dnt_kyo = m.daily_con;
				gpm.last_btt = p_info.last_active_time;
			}
			m_guild_db.save();
			msg.user_list = user_list;
			msg.success = true;
			
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			return respond;
		} finally {
			lock.unlock();
		}
	}

	public CommonMsg handleTechUpgrade(CommonMsg respond, CommonMsg receive) {
		String uid = respond.header.uid;
		respond.body.guild = new GuildMsg();
		GuildMsg msg = respond.body.guild;
		msg.success = false;
		
		lock.lock();
		try {
			m_p_guild = m_p_guild_db.loadByUid(uid);
			if (null == m_p_guild) {
				log.error("no player guild db player {}", uid);
				return respond;
			}
			String g_id = m_p_guild.gd_id;
			if (g_id.length() <= 0) {
				//log.error("player not in guild player {}, guild {}", uid, g_id);
				respond.header.rt_sub = 1201;
				return respond;
			}
			
			m_guild = m_guild_db.loadByGuildId(m_p_guild.gd_id);
			if (null == m_guild) {
				respond.header.rt_sub = 1202;
				return respond;
			}
			
			boolean is_ceo = m_guild.ceo_id.equals(uid)?true:false;
			boolean is_vp = false;
			int vp_size = m_guild.vp_ids.size();
			if (!is_ceo) {
				for (int i = 0; i < vp_size; ++i) {
					if (m_guild.vp_ids.get(i).equals(uid)) {
						is_vp = true;
						break;
					}
				}
			}
			if (!is_ceo && !is_vp) {
				respond.header.rt_sub = 1200;
				return respond;
			}
			
			int tech_id = receive.body.guild.tech_id;
			Tech t_cfg = ConfigConstant.tTech.get(tech_id);
			if (null == t_cfg) {
				log.error("tech up cfg error {} player {}", tech_id, uid);
				return respond;
			}
			
			if (m_guild.tech_ids == null) {
				m_guild.tech_ids = new ArrayList<Integer>();
				m_guild.tech_lvs = new ArrayList<Integer>();
			}
			int found_idx = -1;
			for (int i = 0; i < m_guild.tech_ids.size(); ++i) {
				if (m_guild.tech_ids.get(i).equals(tech_id)) {
					found_idx = i;
					break;
				}
			}
			if (found_idx < 0) {
				found_idx = m_guild.tech_ids.size();
				m_guild.tech_ids.add(tech_id);
				m_guild.tech_lvs.add(0);
			}
			int old_lv = m_guild.tech_lvs.get(found_idx);
			if (old_lv == t_cfg.getLvMax()) {
				respond.header.rt_sub = 1111;
				return respond;
			}
			Techup new_cfg = ConfigConstant.tTechup.get(old_lv + 1);
			if (new_cfg == null) {
				log.error("no next tech up cfg {}, player {]", old_lv + 1, uid);
				return respond;
			}
			Techup old_cfg = ConfigConstant.tTechup.get(old_lv);
			if (old_cfg == null) {
				log.error("no tech up cfg {}, player {]", old_lv, uid);
				return respond;
			}
			int need_fund = old_cfg.getTechResearch();
			if (m_guild.funds < need_fund) {
				respond.header.rt_sub = 1219;
				return respond;
			}
			
			m_guild.funds -= need_fund;
			m_guild.tech_lvs.set(found_idx, old_lv + 1);
			m_guild_db.save();
			
			msg.success = true;
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}
	}
	
	public CommonMsg handleTechLevelup(CommonMsg respond, CommonMsg receive) {
		String uid = respond.header.uid;
		respond.body.guild = new GuildMsg();
		GuildMsg msg = respond.body.guild;
		msg.success = false;
		int tech_id = receive.body.guild.tech_id;
		
		m_player = m_player_db.loadById(uid);
		if (m_player == null) {
			log.error("no playre {}", uid);
			return respond;
		}
		
		m_bag = m_bag_db.loadByUid(uid);
		if (m_bag == null) {
			log.error("no bag db {}", uid);
			return respond;
		}
		
		m_p_guild = m_p_guild_db.loadByUid(uid);
		if (null == m_p_guild) {
			log.error("no player guild db player {}", uid);
			return respond;
		}
		String g_id = m_p_guild.gd_id;
		if (g_id.length() <= 0) {
			//log.error("player not in guild player {}, guild {}", uid, g_id);
			respond.header.rt_sub = 1201;
			return respond;
		}
		
		m_guild = m_guild_db.loadByGuildId(m_p_guild.gd_id);
		if (null == m_guild) {
			respond.header.rt_sub = 1202;
			return respond;
		}
		
		int guild_tech_lv = -1;
		for (int i = 0; i < m_guild.tech_ids.size(); ++i) {
			if (m_guild.tech_ids.get(i).equals(tech_id)) {
				guild_tech_lv = m_guild.tech_lvs.get(i);
				break;
			}
		}
		Techup guild_cfg = ConfigConstant.tTechup.get(guild_tech_lv);
		if (null == guild_cfg) {
			log.error("guild techup cfg error {}, uid {}", guild_tech_lv, uid);
			return respond;
		}
		
		PlayerRolesInfoDB r_db = SpringContextUtil.getBean(PlayerRolesInfoDB.class);
		PlayerRolesInfo r_info = r_db.load(uid);
		if (r_info.tech_ids == null) {
			r_info.tech_ids = new ArrayList<Integer>();
			r_info.tech_lvs = new ArrayList<Integer>();
			r_db.save();
		}
		int player_old_lv = 0;
		int found_idx = -1;
		for (int i = 0; i < r_info.tech_ids.size(); ++i) {
			if (r_info.tech_ids.get(i).equals(tech_id)) {
				player_old_lv = r_info.tech_lvs.get(i);
				found_idx = i;
				break;
			}
		}
		if (found_idx == -1) {
			r_info.tech_ids.add(tech_id);
			r_info.tech_lvs.add(player_old_lv);
			found_idx = r_info.tech_lvs.size() - 1;
		}
		if (player_old_lv >= guild_cfg.getTechLvUp()) {
			respond.header.rt_sub = 1220;
			return respond;
		}
		Techup player_cfg = ConfigConstant.tTechup.get(player_old_lv);
		int id = ConfigConstant.tConf.getDonate1();
		int need_cnt = player_cfg.getMoney();
		if (!m_bag.hasItemCount(id, need_cnt)) {
			respond.header.rt_sub = 1221;
			return respond;
		}
		
		m_bag.subItemCount(id, need_cnt);
		r_info.tech_lvs.set(found_idx, player_old_lv + 1);
		m_bag_db.save();
		
		// 刷新所有人的属性
		respond.body.sync_roles = new PlayerRolesInfo();
		respond.body.sync_roles.roles = new ArrayList<RoleInfo>();
		for (Entry<Integer, Integer> zhu_jiang : r_info.pve_team.entrySet()) {
			if (zhu_jiang.getValue() == 0) {
				continue;
			}
			RoleAttrCalc.RefreshRoleAttr(zhu_jiang.getValue(), r_info);
			respond.body.sync_roles.roles.add(r_info.GetRole(zhu_jiang.getValue()));
		}
		PlayerImpl.UpdateTeamFightingCheckMax(m_player, r_info.CalcTeamFighting());
		r_db.save();
		m_player_db.save();
		respond.body.sync_player_info = new SyncPlayerInfoMsg();
		respond.body.sync_player_info.is_refresh = false;
		respond.body.sync_player_info.team_current_fighting = m_player.team_current_fighting;
		respond.body.sync_player_info.team_history_max_fighting = m_player.team_history_max_fighting;
		respond.body.sync_bag = new SyncBagMsg();
		respond.body.sync_bag.items = new HashMap<Integer, Integer>();
		respond.body.sync_bag.items.put(id, m_bag.getItemCount(id));
		
		msg.success = true;
		return respond;
	}

/*================公会战相关 BEGIN============================*/	
	private boolean guildIsInWarNow(GuildInfo g){
		Calendar now_cal = Calendar.getInstance();
		long now_hour = now_cal.get(Calendar.HOUR_OF_DAY);
		if (now_hour >= ConfigConstant.tConf.getGuildMate()
			&& now_hour < ConfigConstant.tConf.getGuildWar()[1]) {
			return true;
		}
		return false;
	}
	
	private boolean guildIsInWarFighting(GuildInfo g){
		Calendar now_cal = Calendar.getInstance();
		long now_hour = now_cal.get(Calendar.HOUR_OF_DAY);
		if (now_hour >= ConfigConstant.tConf.getGuildWar()[0]
			&& now_hour < ConfigConstant.tConf.getGuildWar()[1]) {
			return true;
		}
		return false;
	}
	
	private long guildMateStart() {
		return TimeUtils.TodayStart() + ConfigConstant.tConf.getGuildMate() * 60 * 60 * 1000;
	}
	
	public CommonMsg handleHubReqMatchInfo(CommonMsg respond) {
		lock.lock();
		try {
			HubGuildWarMsg msg = new HubGuildWarMsg();
			respond.body.hub_gw_msg = msg;
			msg.code = MsgCode.FAILED;
			
			Calendar cal_start = Calendar.getInstance();
			cal_start.setTimeInMillis(m_server_db.load().start_time);
			
			Calendar today_match_time = Calendar.getInstance();
			today_match_time.set(Calendar.HOUR_OF_DAY, ConfigConstant.tConf.getGuildMate());
			long today_mt = today_match_time.getTimeInMillis();
			
			int day_diff = TimeUtils.natureDayDiff(Calendar.getInstance(), cal_start);
			msg.is_local = day_diff >= ConfigConstant.tConf.getOpenDay() ? (byte)0 : (byte)1;
			
			List<GuildInfo> all_g = BaseDaoImpl.getInstance().findAll(GuildInfo.class);
			msg.f_list = new HashMap<String, HubGuildWarMsg.I>(all_g.size());
			final long MATCH_START = guildMateStart();
			int zid = 0;
			for (int i = 0; i < all_g.size(); ++i) {
				GuildDB g_db = SpringContextUtil.getBean(GuildDB.class);
				GuildInfo gi = g_db.loadByGuildId(all_g.get(i).id);
				gi.em_is_net = msg.is_local == (byte)1 ? (byte)0 : (byte)1;
				gi.war_stat = WAR_STAT_NOT_ENOUGH;
				g_db.save();				
				if (gi.members.size() < GW_MIN_CNT && msg.is_local != 1) {
					continue;
				}
				if (gi.creat_t > MATCH_START) {
					continue;
				}
				int m_s = gi.members.size();
				double sum_f = 0.0;
				List<PlayerInfo> temp_list = new ArrayList<PlayerInfo>();
				for (int j = 0; j < m_s; ++j) {
					String p_id = gi.members.get(j).id;
					PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
					PlayerInfo p_info = p_db.loadById(p_id);
					if (p_info == null) {
						continue;
					}
					if (zid == 0) {
						zid = p_info.user_base.zid;
					}
					temp_list.add(p_info);
				}
				temp_list.sort(new PlayerFightSort());
				for (int j = 0; j < 10 && j < temp_list.size(); ++j) {
					sum_f += temp_list.get(j).team_history_max_fighting;
				}
				HubGuildWarMsg.I new_i = msg.new I();
				new_i.f = sum_f;
				new_i.zid = zid;
				new_i.name = gi.name;
				new_i.lv = gi.lv;
				new_i.cnt = gi.members.size();
				new_i.mem_ids = new ArrayList<String>(new_i.cnt);
				new_i.ceo_id = gi.ceo_id;
				PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
				PlayerInfo p_info = p_db.loadById(gi.ceo_id);
				new_i.ceo_name = p_info.user_base.getNickname();
				for (int j = 0; j < gi.members.size(); ++j) {
					String mid = gi.members.get(j).id;
					PlayerGuildDB pg_db = SpringContextUtil.getBean(PlayerGuildDB.class);
					PlayerGuildInfo pg_info = pg_db.loadByUid(mid);
					if (pg_info == null) {
						continue;
					}
					if (pg_info.in_t != null && pg_info.in_t > today_mt) {
						continue;
					}
					new_i.mem_ids.add(mid);
				}
				msg.f_list.put(gi.id, new_i);
			}			
			msg.code = MsgCode.SUCCESS;
			return respond;
		}
		catch (Exception e) {
			e.printStackTrace();
			HubGuildWarMsg msg = new HubGuildWarMsg();
			respond.body.hub_gw_msg = msg;
			msg.code = MsgCode.FAILED;
			return respond;
		}
		finally {
			lock.unlock();
		}
	}
	
	public void handleAckGsGuildMatchInfo(CommonMsg receive) {
		if (receive == null || receive.body == null || receive.body.hub_gw_msg == null){
			log.error("null = {}", JsonTransfer.getJson(receive));
			return;
		}
		HubGuildWarMsg msg = receive.body.hub_gw_msg;
		boolean clean_week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY ? true : false;
		List<HubGuildInfo> all_g = BaseDaoImpl.getInstance().findAll(HubGuildInfo.class);
		Long now = Calendar.getInstance().getTimeInMillis();
		for (Entry<String, HubGuildWarMsg.I> msg_g : msg.f_list.entrySet()) {
			HubGuildWarMsg.I msg_i = msg_g.getValue();	
			HubGuildDB g_db = SpringContextUtil.getBean(HubGuildDB.class);
			HubGuildInfo found_g = g_db.loadByGuildId(msg_g.getKey());
		
			if (found_g == null) {
				found_g = (HubGuildInfo)g_db.createDB(msg_g.getKey());
				found_g.weekly_s = 0;
				all_g.add(found_g);
			}
			found_g.members = new HashMap<String, HubGuildInfo.Player>();
			for (int i = 0; i < msg_i.mem_ids.size(); ++i) {
				found_g.members.put(msg_i.mem_ids.get(i), found_g.new Player());
			}
			if (clean_week)
				found_g.weekly_s = 0;
			found_g.name = msg_i.name;
			found_g.zid = msg_i.zid;
			found_g.daily_s = 0;
			found_g.enemy_id = "";
			found_g.lv = msg_i.lv;
			found_g.fight = msg_i.f;
			found_g.cnt = msg_i.cnt;
			found_g.ceo_id	= msg_i.ceo_id;
			found_g.ceo_name = msg_i.ceo_name;
			found_g.local = msg.is_local;
			found_g.m_t = now;
			found_g.war_stat = GuildImpl.WAR_CLOSE;
			g_db.save();
		}
	}
	
	public void handleSendMatchResult(CommonMsg send, int zid) {
		HubGuildWarMsg msg = new HubGuildWarMsg();
		send.body.hub_gw_msg = msg;
		msg.f_list = new HashMap<String, HubGuildWarMsg.I>();
		List<HubGuildInfo> all_g = BaseDaoImpl.getInstance().findAll(HubGuildInfo.class);
		final int A_SIZE = all_g.size();
		long now = Calendar.getInstance().getTimeInMillis();
		for (int i = 0; i < A_SIZE; ++i) {
			HubGuildInfo g_i = all_g.get(i);
			if (g_i.zid != zid) {
				continue;
			}
			if (now - g_i.m_t > TimeUtils.ONE_HOUR_TIME) {
				continue;
			}
			HubGuildWarMsg.I msg_i = msg.new I();
			msg.f_list.put(g_i.id, msg_i);
			msg_i.stat = g_i.war_stat;
			if (g_i.war_stat == 0) {
				for (int j = 0; j < A_SIZE; ++j) {
					HubGuildInfo e_i = all_g.get(j);
					if (!e_i.id.equals(g_i.enemy_id)) {
						continue;
					}
					msg_i.em_cnt = e_i.cnt;
					msg_i.em_id = e_i.id;
					msg_i.em_lv =e_i.lv;
					msg_i.em_name = e_i.name;
					msg_i.em_zid = e_i.zid;
					break;
				}
			}
		}
	}
	
	public void handleBeginMatch() {
		List<HubGuildInfo> all_g = BaseDaoImpl.getInstance().findAll(HubGuildInfo.class);
		Map<Integer, List<HubGuildInfo>> pools = new HashMap<Integer, List<HubGuildInfo>>();
		final int ALL_POOL_KEY = -1;
		List<HubGuildInfo> all_pool = new ArrayList<HubGuildInfo>();
		pools.put(ALL_POOL_KEY, all_pool);
		int all_size = all_g.size();
		long now = Calendar.getInstance().getTimeInMillis();
		for (int i = 0; i < all_size; ++i) {
			HubGuildInfo g_i = all_g.get(i);
			if (now - g_i.m_t > TimeUtils.ONE_HOUR_TIME) {
				continue;
			}
			if (g_i.local == 0) {
				all_pool.add(g_i);
			} else {
				List<HubGuildInfo> z_list = pools.get(g_i.zid);
				if (z_list == null) {
					z_list = new ArrayList<HubGuildInfo>();
					pools.put(g_i.zid, z_list);
				}
				z_list.add(g_i);
			}
		}
		
		final int GROUP = ConfigConstant.tConf.getGrouping();
		List<HubGuildInfo> group_list = new ArrayList<HubGuildInfo>(GROUP);
		for (Entry<Integer, List<HubGuildInfo>> p : pools.entrySet()) {
			List<HubGuildInfo> p_list = p.getValue();
			p_list.sort(new HubGuildInfoFightSort());
			int p_size = p_list.size();
			for (int i = 0; i < p_size; i += GROUP) {
				group_list.clear();
				for (int j = i; j < p_size && j < i + GROUP; ++j) {
					group_list.add(p_list.get(j));
				}
				log.info("match group begin ======");
				group_list.sort(new HubGuildInfoRandSort());
				int g_size = group_list.size();
				for (int j = 0; j < g_size; j += 2) {
					HubGuildDB j_db = SpringContextUtil.getBean(HubGuildDB.class);
					HubGuildInfo j_info = j_db.loadByGuildId(group_list.get(j).id);
					if (j + 1 < g_size) {
						HubGuildDB j_1_db = SpringContextUtil.getBean(HubGuildDB.class);
						HubGuildInfo j_1_info = j_1_db.loadByGuildId(group_list.get(j+1).id);
						j_info.enemy_id = j_1_info.id;
						j_1_info.enemy_id = j_info.id;
						j_info.war_stat = GuildImpl.WAR_STAT_IN;
						j_1_info.war_stat = GuildImpl.WAR_STAT_IN;
						j_1_db.save();
						log.info("match team id {} name {} z {} f {} VS team id {} name {} z {} f {} "
								, j_info.id, j_info.name, j_info.zid, j_info.fight
								, j_1_info.id, j_1_info.name, j_1_info.zid, j_1_info.fight);
					}
					else {
						j_info.war_stat = GuildImpl.WAR_STAT_OUT;
						j_info.enemy_id = "";
						log.info("match team id {} name {} z {} f {} OUT ", j_info.id, j_info.name, j_info.zid, j_info.fight);
					}
					j_db.save();
				}
			}
		}
	}
	
	
	public CommonMsg handleMatchResult(CommonMsg respond, CommonMsg receive) {
		lock.lock();
		try {
			log.info("handleMatchResult " + JsonTransfer.getJson(receive));
			HubGuildWarMsg msg = new HubGuildWarMsg();
			respond.body.hub_gw_msg = msg;
			msg.code = MsgCode.FAILED;
			
			HubGuildWarMsg receive_msg = receive.body.hub_gw_msg;
			if (receive_msg == null) {
				return respond;
			}
			
			for (Entry<String, HubGuildWarMsg.I> g_msg : receive_msg.f_list.entrySet()) {
				GuildDB g_db = SpringContextUtil.getBean(GuildDB.class);
				GuildInfo gi = g_db.loadByGuildId(g_msg.getKey());
				if (gi == null) {
					continue;
				}
				HubGuildWarMsg.I msg_i = g_msg.getValue();
				gi.war_stat = msg_i.stat;
				gi.em_cnt = msg_i.em_cnt;
				gi.em_id = msg_i.em_id;
				gi.em_lv = msg_i.em_lv;
				gi.em_name = msg_i.em_name;
				gi.em_zid = msg_i.em_zid;
				gi.seed = CommUtil.getRandom(10000000, 99999999);
				g_db.save();
				
				final int MSIZE = gi.members.size();
				for (int i = 0; i < MSIZE; ++i) {
					PlayerGuildDB p_db = SpringContextUtil.getBean(PlayerGuildDB.class);
					PlayerGuildInfo pi = p_db.loadByUid(gi.members.get(i).id);
					if (pi == null) {
						continue;
					}
					pi.daily_war_cnt = 0;
					pi.raid_s = 0;
					p_db.save();
				}
			}
			
			msg.code = MsgCode.SUCCESS;
			return respond;
		}
		catch (Exception e) {
			e.printStackTrace();
			HubGuildWarMsg msg = new HubGuildWarMsg();
			respond.body.hub_gw_msg = msg;
			msg.code = MsgCode.FAILED;
			return respond;
		}
		finally {
			lock.unlock();
		}
	}
		
	public CommonMsg handleWarInfo(CommonMsg respond) {
		String uid = respond.header.uid;
		respond.body.guild_war = new GuildWarMsg();
		GuildWarMsg msg = respond.body.guild_war;
		msg.success = false;
		msg.status = GuildImpl.WAR_ERROR;
		
		m_p_guild = m_p_guild_db.loadByUid(uid);
		if (null == m_p_guild) {
			log.error("no player guild db player {}", uid);
			return respond;
		}
		String g_id = m_p_guild.gd_id;
		if (g_id.length() <= 0) {
			//log.error("player not in guild player {}, guild {}", uid, g_id);
			respond.header.rt_sub = 1201;
			return respond;
		}
		
		m_guild = m_guild_db.loadByGuildId(m_p_guild.gd_id);
		if (null == m_guild) {
			//log.error("no guild db player {}, guild {}", uid, g_id);
			respond.header.rt_sub = 1202;
			return respond;
		}
		
		if (!guildIsInWarFighting(m_guild)) {
			msg.status = GuildImpl.WAR_CLOSE;
			return respond;
		}
		
		msg.status = (int)m_guild.war_stat;
		msg.match_type = (int)m_guild.em_is_net;
		msg.g_info = new GuildSoloMsg();
		msg.g_info.id = m_guild.em_id;
		msg.g_info.name = m_guild.em_name;
		msg.g_info.guild_lv = m_guild.em_lv;
		msg.g_info.cnt_member = m_guild.em_cnt;
		msg.g_info.zid = m_guild.em_zid;	
		
		msg.success = true;
		return respond;
	}
	
	public CommonMsg handleWarScore(CommonMsg respond) {
		String uid = respond.header.uid;
		respond.body.guild_war = new GuildWarMsg();
		GuildWarMsg msg = respond.body.guild_war;
		msg.success = false;
		
		m_p_guild = m_p_guild_db.loadByUid(uid);
		if (null == m_p_guild) {
			log.error("no player guild db player {}", uid);
			return respond;
		}
		String g_id = m_p_guild.gd_id;
		if (g_id.length() <= 0) {
			//log.error("player not in guild player {}, guild {}", uid, g_id);
			respond.header.rt_sub = 1201;
			return respond;
		}
		
		m_guild = m_guild_db.loadByGuildId(m_p_guild.gd_id);
		if (null == m_guild) {
			//log.error("no guild db player {}, guild {}", uid, g_id);
			respond.header.rt_sub = 1202;
			return respond;
		}
		
		msg.my_score = 0;
		msg.ene_score = 0;
		
		CommonMsg send = new CommonMsg(0, g_id);
		//log.info("Query WarScore g_id " + g_id);
		try {
			String result = HttpRequest.PostFunction("http://"+ HUB_URL + I_DefMoudle.HUB_G2H_WAR_SCORE, JsonTransfer.getJson(send));
			//log.info("Query WarScore result g_id " + g_id + " res " + result);
			CommonMsg result_obj = JsonTransfer._In(result, CommonMsg.class);			
			if (result_obj != null) {
				HubGuildWarMsg.I result_i = result_obj.body.hub_gw_msg.f_list.get(g_id);
				msg.my_score = result_i.my_score;
				msg.ene_score = result_i.ene_score;
			}
			msg.success = true;
			return respond;
		}
		catch (Exception e) {
			e.printStackTrace();
			return respond;
		}
	}
	
	public CommonMsg handleWarListEnemy(CommonMsg respond) {
		String uid = respond.header.uid;
		respond.body.guild_war = new GuildWarMsg();
		GuildWarMsg msg = respond.body.guild_war;
		msg.success = false;
		
		m_p_guild = m_p_guild_db.loadByUid(uid);
		if (null == m_p_guild) {
			log.error("no player guild db player {}", uid);
			return respond;
		}
		String g_id = m_p_guild.gd_id;
		if (g_id.length() <= 0) {
			//log.error("player not in guild player {}, guild {}", uid, g_id);
			respond.header.rt_sub = 1201;
			return respond;
		}
		
		m_guild = m_guild_db.loadByGuildId(m_p_guild.gd_id);
		if (null == m_guild) {
			//log.error("no guild db player {}, guild {}", uid, g_id);
			respond.header.rt_sub = 1202;
			return respond;
		}
		
		msg.seed = m_guild.seed;
		
		CommonMsg send = new CommonMsg(0, g_id);
		//log.info("Query ListEnemy g_id " + g_id);
		try {
			String result = HttpRequest.PostFunction("http://"+ HUB_URL + I_DefMoudle.HUB_G2H_ENEMY_LIST, JsonTransfer.getJson(send));
			//log.info("Query ListEnemy result g_id " + g_id + " res " + result);
			CommonMsg result_obj = JsonTransfer._In(result, CommonMsg.class);			
			if (result_obj == null) {
				return respond;
			}
			Map<String, HubGuildWarMsg.Player> result_list = result_obj.body.hub_gw_msg.p_list;
			msg.players = new ArrayList<GuildWarPlayerMsg>(result_list.size());
			for (Entry<String, HubGuildWarMsg.Player> r : result_list.entrySet()) {
				String id = r.getKey();
				HubGuildWarMsg.Player p = r.getValue();
				GuildWarPlayerMsg p_m = new GuildWarPlayerMsg();
				msg.players.add(p_m);
				p_m.username = p.name;
				p_m.uid = id;
				p_m.vip = p.vip;
				p_m.fighting = p.fight;
				p_m.star_lost = p.daily;
				p_m.star_gain = p.raid;
				p_m.tries = (int)p.cnt;
			}
			msg.success = true;
			return respond;
		}
		catch (Exception e) {
			e.printStackTrace();
			return respond;
		}
	}
	
	public CommonMsg handleWarListMy(CommonMsg respond) {
		String uid = respond.header.uid;
		respond.body.guild_war = new GuildWarMsg();
		GuildWarMsg msg = respond.body.guild_war;
		msg.success = false;
		
		m_p_guild = m_p_guild_db.loadByUid(uid);
		if (null == m_p_guild) {
			log.error("no player guild db player {}", uid);
			return respond;
		}
		String g_id = m_p_guild.gd_id;
		if (g_id.length() <= 0) {
			//log.error("player not in guild player {}, guild {}", uid, g_id);
			respond.header.rt_sub = 1201;
			return respond;
		}
		
		m_guild = m_guild_db.loadByGuildId(m_p_guild.gd_id);
		if (null == m_guild) {
			//log.error("no guild db player {}, guild {}", uid, g_id);
			respond.header.rt_sub = 1202;
			return respond;
		}
		Calendar today_match_time = Calendar.getInstance();
		today_match_time.set(Calendar.HOUR_OF_DAY, ConfigConstant.tConf.getGuildMate());
		long today_mt = today_match_time.getTimeInMillis();
		
		msg.seed = m_guild.seed;
		msg.players = new ArrayList<GuildWarPlayerMsg>(m_guild.members.size());
		for (int i = 0; i < m_guild.members.size(); ++i) {
			PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
			PlayerInfo p_info = p_db.loadById(m_guild.members.get(i).id);
			if (p_info == null) {
				continue;
			}
			PlayerGuildDB pg_db = SpringContextUtil.getBean(PlayerGuildDB.class); 
			PlayerGuildInfo pg_info = pg_db.loadByUid(p_info._id);
			if (pg_info == null) {
				continue;
			}
			if (pg_info.in_t != null && pg_info.in_t > today_mt) {
				continue;
			}
			GuildWarPlayerMsg p_m = new GuildWarPlayerMsg();
			msg.players.add(p_m);
			p_m.uid = p_info._id;
			p_m.tries = (int)pg_info.daily_war_cnt;
			p_m.fighting = p_info.team_current_fighting;
			p_m.username = p_info.user_base.getNickname();
			p_m.star_gain = pg_info.raid_s;
			p_m.vip = p_info.vip_level;
		}
		
		CommonMsg send = new CommonMsg(0, g_id);
		//log.info("Query WarListMy g_id " + g_id);
		try {
			String result = HttpRequest.PostFunction("http://"+ HUB_URL + I_DefMoudle.HUB_G2H_MY_LIST, JsonTransfer.getJson(send));
			//log.info("Query WarListMy result g_id " + g_id + " res " + result);
			CommonMsg result_obj = JsonTransfer._In(result, CommonMsg.class);			
			if (result_obj == null) {
				return respond;
			}
			Map<String, HubGuildWarMsg.Player> result_list = result_obj.body.hub_gw_msg.p_list;
			for (Entry<String, HubGuildWarMsg.Player> r : result_list.entrySet()) {
				for (int i = 0; i < msg.players.size(); ++i) {
					GuildWarPlayerMsg p_m = msg.players.get(i);
					if (!p_m.uid.equals(r.getKey())) {
						continue;
					}
					p_m.star_lost = r.getValue().daily;
					break;
				}
			}
			msg.success = true;
			return respond;
		}
		catch (Exception e) {
			e.printStackTrace();
			return respond;
		}
	}
	
	public CommonMsg handleWarAttack(CommonMsg respond, String attack_uid) {
		String uid = respond.header.uid;
		respond.body.guild_war = new GuildWarMsg();
		GuildWarMsg msg = respond.body.guild_war;
		msg.success = false;
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (p_info == null) {
			log.error("no player db player {}", uid);
			return respond;
		}
		
		Calendar today_match_time = Calendar.getInstance();
		today_match_time.set(Calendar.HOUR_OF_DAY, ConfigConstant.tConf.getGuildMate());
		long today_mt = today_match_time.getTimeInMillis();
		
		m_p_guild = m_p_guild_db.loadByUid(uid);
		if (null == m_p_guild) {
			log.error("no player guild db player {}", uid);
			return respond;
		}
		String g_id = m_p_guild.gd_id;
		if (g_id.length() <= 0) {
			log.error("player not in guild player {}, guild {}", uid, g_id);
			respond.header.rt_sub = 1201;
			return respond;
		}
		if (m_p_guild.in_t != null && m_p_guild.in_t > today_mt) {
			respond.header.rt_sub = 1233;
			return respond;
		}
		
		m_guild = m_guild_db.loadByGuildId(g_id);
		if (null == m_guild) {
			log.error("no guild db player {}, guild {}", uid, g_id);
			respond.header.rt_sub = 1202;
			return respond;
		}
		
		if (!guildIsInWarFighting(m_guild)) {
			log.error("no guild war open uid {} gid {}", uid, g_id);
			return respond;
		}
		
		if (m_guild.war_stat != WAR_STAT_IN) {
			log.error("no guild war open uid {} gid {} stat {}", uid, g_id, m_guild.war_stat);
			return respond;
		}
		
		Calendar now_cal = Calendar.getInstance();
		Calendar last_cal = Calendar.getInstance();
		last_cal.setTimeInMillis(m_p_guild.last_war_t);
		if (last_cal.get(Calendar.DAY_OF_YEAR) != now_cal.get(Calendar.DAY_OF_YEAR)) {
			m_p_guild.daily_war_cnt = 0;
		}

		if (m_p_guild.daily_war_cnt >= ConfigConstant.tConf.getGuildDekaron()) {
			//log.error("guild war cnt max uid {} cnt {}", uid, m_p_guild.daily_war_cnt);
			respond.header.rt_sub = MSG_NOT_ENOUGH_WAR_CNT;
			return respond;
		}
		
		Map<Integer, RoleInfo> my_roles = genMyRoles(uid);
		if (null == my_roles) {
			log.error("guild war roles null uid {}", uid);
			return respond;
		}
		
		HubGuildWarMsg result_msg = null;
		try {
			//初步检测通过，发送挑战请求到hub
			CommonMsg send = new CommonMsg(0, g_id);
			send.body.hub_gw_msg = new HubGuildWarMsg();
			send.body.hub_gw_msg.target_uid = attack_uid;
			send.body.hub_gw_msg.roles = my_roles;
			send.body.hub_gw_msg.attacker_uid = uid;
			HubGuildWarMsg.Player wp = send.body.hub_gw_msg.new Player();
			send.body.hub_gw_msg.one = wp;
			wp.name = p_info.user_base.getNickname();
			wp.team_lv = p_info.team_lv;
			wp.img = p_info.user_base.getuImg();
			wp.cnt = m_p_guild.daily_war_cnt;
			
			//log.info("handleWarAttack uid {} atk {}", uid, attack_uid);
			String result = HttpRequest.PostFunction("http://"+ HUB_URL + I_DefMoudle.HUB_G2H_GWAR_ATTACK, JsonTransfer.getJson(send));
			//log.info("attack result {} ", result);
			CommonMsg result_obj = JsonTransfer._In(result, CommonMsg.class);
			if (result_obj == null) {
				return respond;
			}
			result_msg = result_obj.body.hub_gw_msg;
			respond.body.guild_war = result_obj.body.guild_war;
		}
		catch (Exception e) {
			e.printStackTrace();
			return respond;
		}
		
		lock.lock();
		try {
			if (result_msg != null && result_msg.code == 0) {
				PlayerGuildDB pg_db = SpringContextUtil.getBean(PlayerGuildDB.class); 
				PlayerGuildInfo pg_info = pg_db.loadByUid(uid);
				pg_info.daily_war_cnt++;
				pg_info.last_war_t = now_cal.getTimeInMillis();
				pg_info.raid_s += respond.body.guild_war.r_stars;
				pg_db.save();
				m_bag = m_bag_db.loadByUid(uid);
				int id = ConfigConstant.tConf.getDonate1();
	    		int cnt = result_msg.contri_cnt;
	    		m_bag.addItemCount(id, cnt);
	    		respond.body.guild_war.r_dnt = cnt;
	    		SyncBagItem a = new SyncBagItem();
	    		a.id = id;
	    		a.count = cnt;
	    		if (respond.body.sync_bag == null)
	    			respond.body.sync_bag = new SyncBagMsg();
	    		respond.body.sync_bag.items.put(id, m_bag.getItemCount(id));
	    		m_bag_db.save();
				GuildDB g_db = SpringContextUtil.getBean(GuildDB.class); 
				GuildInfo g_info = g_db.loadByGuildId(pg_info.gd_id);
				for (int i = 0; i < g_info.members.size(); ++i) {
					GuildInfo.Member m = g_info.members.get(i);
					if (m.id.equals(pg_info.id)) {
						m.last_con_t = Calendar.getInstance().getTimeInMillis();
						m.daily_con += cnt;
						g_db.save();
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}
		msg.success = true;
		return respond;
	}
	
	public CommonMsg handleWarPlayer(CommonMsg respond, String target_uid, String target_gid) {
		String uid = respond.header.uid;
		respond.body.guild_war = new GuildWarMsg();
		GuildWarMsg msg = respond.body.guild_war;
		msg.success = false;
		msg.player = new GuildWarPlayerMsg();
		
		m_p_guild = m_p_guild_db.loadByUid(uid);
		if (null == m_p_guild) {
			log.error("no player guild db player {}", uid);
			return respond;
		}
		String g_id = m_p_guild.gd_id;
		if (g_id.length() <= 0) {
			//log.error("player not in guild player {}, guild {}", uid, g_id);
			respond.header.rt_sub = 1201;
			return respond;
		}
		
		Byte is_enemy = 1;
		//自己人
		if (g_id.equals(target_gid)) {
			is_enemy = 0;
			PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
			PlayerInfo p_info = p_db.loadById(target_uid);
			if (p_info == null) {
				return respond;
			}
			PlayerGuildDB pg_db = SpringContextUtil.getBean(PlayerGuildDB.class); 
			PlayerGuildInfo pg_info = pg_db.loadByUid(target_uid);
			if (pg_info == null) {
				return respond;
			}
			if (!pg_info.gd_id.equals(target_gid)) {
				return respond;
			}
			
			msg.player.username = p_info.user_base.getNickname();
			msg.player.vip = p_info.vip_level;
			msg.player.team_lv = p_info.team_lv;
			msg.player.fighting = p_info.team_current_fighting;
			msg.player.avatar = p_info.user_base.getuImg();
			msg.player.star_gain = pg_info.raid_s;
			msg.player.tries = (int)pg_info.daily_war_cnt;
		}
		
		CommonMsg send = new CommonMsg(0, g_id);
		send.body.guild_war = new GuildWarMsg();
		send.body.guild_war.uid = target_uid;
		send.body.guild_war.guild_id = target_gid;
		
		//log.info("Query WarPlayer from {} query {} in {} ", uid, target_uid, target_gid);
		try {
			String result = HttpRequest.PostFunction("http://"+ HUB_URL + I_DefMoudle.HUB_G2H_WAR_PLAYER, JsonTransfer.getJson(send));
			//log.info("Query WarPlayer result g_id " + g_id + " res " + result);
			CommonMsg result_obj = JsonTransfer._In(result, CommonMsg.class);			
			if (result_obj != null) {
				HubGuildWarMsg.Player result_i = result_obj.body.hub_gw_msg.p_list.get(target_uid);
				if (result_i == null) {
					return respond;
				}
				if (is_enemy == 1) {
					msg.player.username = result_i.name;
					msg.player.vip = result_i.vip;
					msg.player.team_lv = result_i.team_lv;
					msg.player.fighting = result_i.fight;
					msg.player.avatar = result_i.img;
					msg.player.star_gain = result_i.raid;
					msg.player.tries = (int)result_i.cnt;
				}
				msg.player.star_lost = result_i.daily;
				msg.logs = result_obj.body.hub_gw_msg.logs;
			}
			msg.success = true;
			return respond;
		}
		catch (Exception e) {
			e.printStackTrace();
			return respond;
		}
	}
	
	
	public CommonMsg handleWarLogPlay(CommonMsg respond, String log_id) {
		respond.body.guild_war = new GuildWarMsg();
		GuildWarMsg msg = respond.body.guild_war;
		msg.success = false;
		
		try {
			CommonMsg send = new CommonMsg(0, log_id);
			log.info("handleWarLogPlay log {} send {}",log_id, JsonTransfer.getJson(send));
			String result = HttpRequest.PostFunction("http://"+ HUB_URL + I_DefMoudle.HUB_G2H_LOGPLAY, JsonTransfer.getJson(send));
			log.info("receive rcd");
			CommonMsg result_obj = JsonTransfer._In(result, CommonMsg.class);
			if (result_obj == null) {
				return respond;
			}
			respond.body.guild_war = result_obj.body.guild_war;
			msg.success = true;
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		}
	}

	public CommonMsg handleGsQueryWarScore(CommonMsg respond, String my_gid) {
		
		HubGuildWarMsg msg = new HubGuildWarMsg();
		respond.body.hub_gw_msg = msg;
		HubGuildWarMsg.I msg_i = msg.new I();
		msg_i.my_score = 0;
		msg_i.ene_score = 0;
		msg.f_list = new HashMap<String, HubGuildWarMsg.I>(1);
		msg.f_list.put(my_gid, msg_i);
		
		HubGuildDB g_db = SpringContextUtil.getBean(HubGuildDB.class);
		HubGuildInfo my_info = g_db.loadByGuildId(my_gid);
		if (my_info != null) {
			HubGuildDB g_db_enemy = SpringContextUtil.getBean(HubGuildDB.class);
			HubGuildInfo enemy_info = g_db_enemy.loadByGuildId(my_info.enemy_id);
			if (enemy_info != null) {
				msg_i.my_score = my_info.daily_s;
				msg_i.ene_score = enemy_info.daily_s;
			}
		}
		log.debug("handleGsQueryWarScore from gid " + my_gid);
		return respond;
	}
	
	public CommonMsg handleGsQueryEnemyList(CommonMsg respond, String my_gid) {
		HubGuildWarMsg msg = new HubGuildWarMsg();
		respond.body.hub_gw_msg = msg;
		
		msg.p_list = new HashMap<String, HubGuildWarMsg.Player>();
		
		HubGuildDB g_db = SpringContextUtil.getBean(HubGuildDB.class);
		HubGuildInfo my_info = g_db.loadByGuildId(my_gid);
		if (my_info == null) {
			return respond;
		}
		HubGuildDB g_db_enemy = SpringContextUtil.getBean(HubGuildDB.class);
		HubGuildInfo enemy_info = g_db_enemy.loadByGuildId(my_info.enemy_id);
		if (enemy_info == null) {
			return respond;
		}
		String enemy_url = null;
		try {
			Map<Integer, Hubmap> url_list = ConfigConstant.tHubmap;
			for (Entry<Integer, Hubmap> u : url_list.entrySet()) {
				if (u.getValue().getServerId() != enemy_info.zid) {
					continue;
				}
				enemy_url = u.getValue().getURL();
				break;
			}
			if (enemy_url == null) {
				return respond;
			}
			
			CommonMsg send = new CommonMsg(0, enemy_info.id);
			//log.info("handleGsQueryEnemyList zid " + enemy_info.zid + " r " + JsonTransfer.getJson(send));
    		String result = HttpRequest.PostFunction("http://"+ enemy_url + I_DefMoudle.HUB_H2G_FW_ENEMY_LIST, JsonTransfer.getJson(send));
    		//log.info("zid " + enemy_info.zid +" get " + result);
    		CommonMsg result_obj = JsonTransfer._In(result, CommonMsg.class);
    		if (result_obj == null) {
    			return respond;
    		}
    		
    		HubGuildWarMsg rec_msg = result_obj.body.hub_gw_msg;
    		msg.p_list = rec_msg.p_list;
    		for (Entry<String, Player> p : msg.p_list.entrySet()) {
    			String id = p.getKey();
    			HubGuildWarMsg.Player player = p.getValue();
    			HubGuildInfo.Player e_info = enemy_info.members.get(id);
    			if (e_info.touched == 0) {
    				player.name = null;
    				player.vip = 0;
    				player.fight = 0;
    			}
    			player.daily = e_info.daily_s; 
    		}
		}
		catch (Exception e) {
			e.printStackTrace();
			return respond;
		}
		
		return respond;
	}
	
	public CommonMsg handleFwEnemyList(CommonMsg respond, String em_gid) {
		HubGuildWarMsg msg = new HubGuildWarMsg();
		respond.body.hub_gw_msg = msg;
		msg.p_list = new HashMap<String, HubGuildWarMsg.Player>();
		
		m_guild = m_guild_db.loadByGuildId(em_gid);
		if (null == m_guild) {
			return respond;
		}
		Calendar today_match_time = Calendar.getInstance();
		today_match_time.set(Calendar.HOUR_OF_DAY, ConfigConstant.tConf.getGuildMate());
		long today_mt = today_match_time.getTimeInMillis();
		//log.info("handleFwEnemyList get " + em_gid);
		for (int i = 0; i < m_guild.members.size(); ++i) {
			PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
			PlayerInfo p_info = p_db.loadById(m_guild.members.get(i).id);
			if (p_info == null) {
				continue;
			}
			PlayerGuildDB pg_db = SpringContextUtil.getBean(PlayerGuildDB.class); 
			PlayerGuildInfo pg_info = pg_db.loadByUid(p_info._id);
			if (pg_info == null) {
				continue;
			}
			if (pg_info.in_t != null && pg_info.in_t > today_mt) {
				continue;
			}
			HubGuildWarMsg.Player p = msg.new Player();
			msg.p_list.put(p_info._id, p);
			p.cnt = pg_info.daily_war_cnt;
			p.fight = p_info.team_current_fighting;
			p.name = p_info.user_base.getNickname();
			p.raid = pg_info.raid_s;
			p.vip = p_info.vip_level;
		}
		
		return respond;
	}
	
	public CommonMsg handleGsQueryMyList(CommonMsg respond, String my_gid) {
		HubGuildWarMsg msg = new HubGuildWarMsg();
		respond.body.hub_gw_msg = msg;
		
		msg.p_list = new HashMap<String, HubGuildWarMsg.Player>();
		
		HubGuildDB g_db = SpringContextUtil.getBean(HubGuildDB.class);
		HubGuildInfo my_info = g_db.loadByGuildId(my_gid);
		if (my_info == null) {
			return respond;
		}
		
		//log.info("handleGsQueryMyList get " + my_gid);
		
		msg.p_list = new HashMap<String, HubGuildWarMsg.Player>(my_info.members.size());
		for (Entry<String, HubGuildInfo.Player> i : my_info.members.entrySet()) {
			HubGuildWarMsg.Player p = msg.new Player();
			msg.p_list.put(i.getKey(), p);
			p.daily = i.getValue().daily_s;
		}
		
		return respond;
	}
	
	public CommonMsg handleWarMyself(CommonMsg respond) {
		String uid = respond.header.uid;
		respond.body.guild_war = new GuildWarMsg();
		GuildWarMsg msg = respond.body.guild_war;
		msg.success = false;
		
		m_p_guild = m_p_guild_db.loadByUid(uid);
		if (null == m_p_guild) {
			log.error("no player guild db player {}", uid);
			return respond;
		}
		String g_id = m_p_guild.gd_id;
		if (g_id.length() <= 0) {
			//log.error("player not in guild player {}, guild {}", uid, g_id);
			respond.header.rt_sub = 1201;
			return respond;
		}
		//log.info("handleWarMyself uid {}", uid);
		msg.csd = (int)m_p_guild.daily_war_cnt;
		msg.last_csd = m_p_guild.last_war_t;
		msg.success = true;
		return respond;
	}
	
	public CommonMsg handleG2HWarPlayer(CommonMsg respond, CommonMsg receive) {
		String my_gid = receive.header.uid;
		String target_uid = receive.body.guild_war.uid;
		String target_gid = receive.body.guild_war.guild_id;
		
		HubGuildWarMsg msg = new HubGuildWarMsg();
		respond.body.hub_gw_msg = msg;
		
		msg.p_list = new HashMap<String, HubGuildWarMsg.Player>(1);
		
		HubGuildDB g_db = SpringContextUtil.getBean(HubGuildDB.class);
		HubGuildInfo my_info = g_db.loadByGuildId(my_gid);
		if (my_info == null) {
			return respond;
		}
		if (my_gid.equals(target_gid)) {
			HubGuildInfo.Player t_p = my_info.members.get(target_uid);
			if (t_p == null) {
				return respond;
			}
			HubGuildWarMsg.Player result = msg.new Player();
			msg.p_list.put(target_uid, result);
			result.daily = t_p.daily_s;
			
			msg.logs = new ArrayList<GuildWarLogMsg>(t_p.rcd.size());
			for (int i = 0; i < t_p.rcd.size(); ++i) {
				HubWarRcdDB w_db = SpringContextUtil.getBean(HubWarRcdDB.class);
				HubWarRcdInfo w_info = w_db.loadById(t_p.rcd.get(i));
				if (w_info == null) {
					continue;
				}
				GuildWarLogMsg log = new GuildWarLogMsg();
				msg.logs.add(log);
				log.id = w_info.id;
				log.result = w_info.result;
				log.atk_uid = w_info.l_id;
				log.def_uid = w_info.r_id;
				log.star = (int)w_info.star;
			}
		}
		else if (my_info.enemy_id.equals(target_gid)) {
			HubGuildDB e_g_db = SpringContextUtil.getBean(HubGuildDB.class);
			HubGuildInfo e_g_info = e_g_db.loadByGuildId(target_gid);
			if (e_g_info == null) {
				return respond;
			}
			HubGuildInfo.Player t_p = e_g_info.members.get(target_uid);
			if (t_p == null) {
				return respond;
			}
			HubGuildWarMsg.Player result = msg.new Player();
			msg.p_list.put(target_uid, result);
			
			//发送异地查询
			String enemy_url = null;
			try {
				Map<Integer, Hubmap> url_list = ConfigConstant.tHubmap;
				for (Entry<Integer, Hubmap> u : url_list.entrySet()) {
					if (u.getValue().getServerId() != e_g_info.zid) {
						continue;
					}
					enemy_url = u.getValue().getURL();
					break;
				}
				if (enemy_url == null) {
					return respond;
				}
				
				CommonMsg send = new CommonMsg(0, target_uid);
				//log.info("handleG2HWarPlayer zid " + e_g_info.zid + " r " + JsonTransfer.getJson(send));
	    		String r_str = HttpRequest.PostFunction("http://"+ enemy_url + I_DefMoudle.HUB_H2G_FW_WAR_PLAYER, JsonTransfer.getJson(send));
	    		//log.info("zid " + e_g_info.zid +" get " + r_str);
	    		CommonMsg result_obj = JsonTransfer._In(r_str, CommonMsg.class);
	    		if (result_obj == null) {
	    			return respond;
	    		}
	    		
	    		HubGuildWarMsg rec_msg = result_obj.body.hub_gw_msg;
	    		HubGuildWarMsg.Player rec_p = rec_msg.p_list.get(target_uid);
	    		if (rec_p != null) {
	    			msg.p_list.put(target_uid, rec_p);
	    			result = rec_p;
	    		}
	    		result.daily = t_p.daily_s;
	    		if (t_p.touched == 0) {
	    			result.name = "";
	    			result.vip = 0;
	    			result.team_lv = 0;
	    			result.fight = 0;
	    			result.img = "";
	    		}
	    		
	    		msg.logs = new ArrayList<GuildWarLogMsg>(t_p.rcd.size());
				for (int i = 0; i < t_p.rcd.size(); ++i) {
					HubWarRcdDB w_db = SpringContextUtil.getBean(HubWarRcdDB.class);
					HubWarRcdInfo w_info = w_db.loadById(t_p.rcd.get(i));
					if (w_info == null) {
						continue;
					}
					if (w_info.l_id.equals(target_uid) && t_p.touched == 0) {
						continue;
					}
					GuildWarLogMsg log = new GuildWarLogMsg();
					msg.logs.add(log);
					log.id = w_info.id;
					log.result = w_info.result;
					log.atk_uid = w_info.l_id;
					log.def_uid = w_info.r_id;
					log.star = (int)w_info.star;
				}
			}
			catch (Exception e){
				e.printStackTrace();
				return respond;
			}
		}
		
		return respond;
	}
	
	public CommonMsg handleFwWarPlayer(CommonMsg respond, CommonMsg receive) {
		HubGuildWarMsg msg = new HubGuildWarMsg();
		respond.body.hub_gw_msg = msg;
		
		String target_uid = receive.header.uid;
		
		//log.info("handleFwWarPlayer uid {}", target_uid);
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(target_uid);
		if (p_info == null) {
			return respond;
		}
		PlayerGuildDB pg_db = SpringContextUtil.getBean(PlayerGuildDB.class); 
		PlayerGuildInfo pg_info = pg_db.loadByUid(target_uid);
		if (pg_info == null) {
			return respond;
		}
		
		msg.p_list = new HashMap<String, HubGuildWarMsg.Player>(1);
		HubGuildWarMsg.Player player = msg.new Player();
		msg.p_list.put(target_uid, player);
		
		player.name = p_info.user_base.getNickname();
		player.vip = p_info.vip_level;
		player.team_lv = p_info.team_lv;
		player.fight = p_info.team_current_fighting;
		player.img = p_info.user_base.getuImg();
		player.raid = pg_info.raid_s;
		player.cnt = pg_info.daily_war_cnt;
		
		//log.info(JsonTransfer.getJson(respond));
		
		return respond;
	}
	
    private Map<Integer, RoleInfo> genMyRoles(String uid){
    	PlayerRolesInfoDB pr_db = SpringContextUtil.getBean(PlayerRolesInfoDB.class); 
    	PlayerRolesInfo pr_info = pr_db.load(uid);
    	if (pr_info == null) {
    		log.info("genMyRoles no PlayerRolesInfo {}", uid);
    		return null;
    	}
		
        Map<Integer, Integer> pve_team_ids = pr_info.pve_team;
        Map<Integer, RoleInfo> pve_team = new HashMap<>();

        // 找到详细阵容
        for(Map.Entry<Integer, Integer> e : pve_team_ids.entrySet()){
            int role_id = e.getValue();
            pr_info.roles.stream().filter(info -> info.role_id == role_id).forEach(info -> {
                RoleInfo put = pve_team.put(e.getKey(), info);
            });
        }

        return pve_team;
    }
	
    public CommonMsg handleG2HGWarAttack(CommonMsg respond, CommonMsg receive) {
    	
    	GuildWarMsg msg = new GuildWarMsg();
    	respond.body.guild_war = msg;
    	msg.success = false;
    	
    	HubGuildWarMsg attacker_msg = receive.body.hub_gw_msg;
    	String target_uid = attacker_msg.target_uid;
    	String attacker_gid = receive.header.uid;
    	String attacker_uid = attacker_msg.attacker_uid;
    	HubGuildWarMsg return_msg = new HubGuildWarMsg();
    	respond.body.hub_gw_msg = return_msg;
    	String rcd_id = attacker_uid + attacker_msg.one.cnt.toString();
    	return_msg.code = CODE_ERROR;
    	
		HubGuildDB g_db = SpringContextUtil.getBean(HubGuildDB.class);
		HubGuildInfo my_info = g_db.loadByGuildId(attacker_gid);
		if (my_info == null) {
			return respond;
		}
		HubGuildDB e_g_db = SpringContextUtil.getBean(HubGuildDB.class);
		HubGuildInfo e_g_info = e_g_db.loadByGuildId(my_info.enemy_id);
		if (e_g_info == null) {
			return respond;
		}
		HubGuildInfo.Player target_info = e_g_info.members.get(target_uid);
		if (target_info == null) {
			return respond;
		}
		
		//发送异地查询
		HubGuildWarMsg rec_msg = null;
		HubGuildWarMsg.Player rec_p = null;
		try {
			String enemy_url = null;
			Map<Integer, Hubmap> url_list = ConfigConstant.tHubmap;
			for (Entry<Integer, Hubmap> u : url_list.entrySet()) {
				if (u.getValue().getServerId() != e_g_info.zid) {
					continue;
				}
				enemy_url = u.getValue().getURL();
				break;
			}
			if (enemy_url == null) {
				return respond;
			}
			
			CommonMsg send = new CommonMsg(0, target_uid);
			//log.info("handleG2HGWarAttack zid " + e_g_info.zid + " r " + JsonTransfer.getJson(send));
    		String r_str = HttpRequest.PostFunction("http://"+ enemy_url + I_DefMoudle.HUB_H2G_FW_GW_ATTACK, JsonTransfer.getJson(send));
    		//log.info("zid " + e_g_info.zid +" get " + r_str);
    		CommonMsg result_obj = JsonTransfer._In(r_str, CommonMsg.class);
    		if (result_obj == null) {
    			return respond;
    		}
    		
    		rec_msg = result_obj.body.hub_gw_msg;
    		if (rec_msg.code == CODE_ERROR) {
    			return respond;
    		}
    		rec_p = rec_msg.p_list.get(target_uid);
    		if (rec_p == null) {
    			return respond;
    		}
		}
		catch (Exception e){
			e.printStackTrace();
			return respond;
		}
		//log.info("Fight");
		
        // 生成双方阵容
        Map<Integer, RoleInfo> my_roles = attacker_msg.roles;
        Map<Integer, RoleInfo> ene_roles = rec_msg.roles;

        // 模拟战斗
        BattleSimulator sim = new BattleSimulator();
        sim.InitAllActorLogic(my_roles, ene_roles, ConfigConstant.tConf.getArenaTime(), 0, BattleType.GUILD_WAR
        		, null, null, null, null);
        BattlePack battle_result = sim.Exe();
		
		lock.lock();
		try {
			HubGuildDB g_db_n = SpringContextUtil.getBean(HubGuildDB.class);
			HubGuildInfo my_info_n = g_db_n.loadByGuildId(attacker_gid);
			if (my_info_n == null) {
				log.info("handleG2HGWarAttack no HubGuildInfo {} ", attacker_gid);
				return respond;
			}
			HubGuildInfo.Player attack_info_n = my_info_n.members.get(attacker_uid);
			if (attack_info_n == null) {
				log.info("handleG2HGWarAttack no HubGuildInfo.Player {} ", attacker_gid);
				return respond;
			}
			if (attack_info_n.rcd.contains(rcd_id)) {
				log.info("handleG2HGWarAttack already rcd {} {}", attacker_gid, rcd_id);
				return respond;
			}
			HubGuildDB e_g_db_n = SpringContextUtil.getBean(HubGuildDB.class);
			HubGuildInfo e_g_info_n = e_g_db_n.loadByGuildId(my_info.enemy_id);
			if (e_g_info_n == null) {
				log.info("handleG2HGWarAttack no HubGuildInfo en {} ", my_info.enemy_id);
				return respond;
			}
			HubGuildInfo.Player target_info_n = e_g_info_n.members.get(target_uid);
			if (target_info_n == null) {
				log.info("handleG2HGWarAttack no HubGuildInfo.Player en {} ", target_uid);
				return respond;
			}
			if (target_info_n.rcd.contains(rcd_id)) {
				log.info("handleG2HGWarAttack already rcd en {} {}", target_uid, rcd_id);
				return respond;
			}
			if (target_info_n.daily_s >= DAILY_STARS) {
				log.info("handleG2HGWarAttack target_info daily {} {}", target_uid, target_info.daily_s);
				return_msg.code = CODE_DAILY_FULL;
				return respond;
			}
			//全部检测完毕。可以执行修改
			
			int get_max = 0;
			if (battle_result.m_result == BattleResult.LEFT_WIN) {
				get_max = WIN_STARS_MAX;
				get_max -= battle_result.l_dead;
				if (get_max < 1) {
					get_max = 1;
				}
				int left_star = DAILY_STARS - target_info.daily_s;
				if (get_max > left_star) {
					get_max = left_star;
				}
			}
				
			target_info_n.daily_s += get_max;
			target_info_n.touched = 1;
			my_info_n.weekly_s += get_max;
			my_info_n.b_t = Calendar.getInstance().getTimeInMillis();
			my_info_n.daily_s += get_max;
			target_info_n.rcd.add(rcd_id);
			attack_info_n.rcd.add(rcd_id);
			
			return_msg.contri_cnt = ConfigConstant.tConf.getBasicsPC();
			if (battle_result.m_result == BattleResult.LEFT_WIN) {
				return_msg.contri_cnt += ConfigConstant.tConf.getAddedPC() * get_max;
			} 
			
			HubWarRcdDB rcd_db = SpringContextUtil.getBean(HubWarRcdDB.class);
			HubWarRcdInfo rcd = rcd_db.loadById(rcd_id);
			if (rcd == null) {
				rcd = (HubWarRcdInfo)rcd_db.createDB(rcd_id);
			}
			rcd.l_id = attacker_uid;
			rcd.r_id = target_uid;
			rcd.l_img = attacker_msg.one.img;
			rcd.r_img = rec_p.img;
			rcd.l_name = attacker_msg.one.name;
			rcd.r_name = rec_p.name;
			rcd.l_lv = attacker_msg.one.team_lv;
			rcd.r_lv = rec_p.team_lv;
			rcd.result = battle_result.m_result;
			rcd.star = (byte) get_max;
			rcd.pack = JsonTransfer.getJson(battle_result);
			rcd_db.save();
			g_db_n.save();
			e_g_db_n.save();
			
			msg.success = true;
			return_msg.code = 0;
			GuildWarPlayerMsg mpmsg = new GuildWarPlayerMsg();
			msg.player = mpmsg;
			mpmsg.username = rec_p.name;
			mpmsg.avatar = rec_p.img;
			mpmsg.vip = rec_p.vip;
			mpmsg.team_lv = rec_p.team_lv;
			mpmsg.fighting = rec_p.fight;
			mpmsg.tries = (int)rec_p.cnt;
			mpmsg.star_lost = target_info.daily_s;
			mpmsg.star_gain = rec_p.raid;
			msg.battle_data = battle_result;
			msg.r_stars = get_max;
			//log.info("Finish Fight {}", JsonTransfer.getJson(respond));
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			return respond;
		} finally {
			lock.unlock();
		}
    }
    
    public CommonMsg handleH2GFWGWAttack(CommonMsg respond, CommonMsg receive) {
    	String uid = receive.header.uid;
    	HubGuildWarMsg msg = new HubGuildWarMsg();
    	respond.body.hub_gw_msg = msg;
    	msg.code = CODE_ERROR;
    	//log.info("handleH2GFWGWAttack uid {}", uid);
    	
    	msg.roles = genMyRoles(uid);
    	if (msg.roles == null) {
    		log.info("handleH2GFWGWAttack gen null {}", uid);
    		return respond;
    	}
 
		PlayerGuildDB g_db = SpringContextUtil.getBean(PlayerGuildDB.class);
		PlayerGuildInfo g_info = g_db.loadByUid(uid);
		if (g_info == null) {
			log.info("handleH2GFWGWAttack no PlayerGuildInfo {}", uid);
			return respond;
		}
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (p_info == null) {
			log.info("handleH2GFWGWAttack no PlayerInfo {}", uid);
			return respond;
		}

    	HubGuildWarMsg.Player p = msg.new Player();
    	msg.p_list = new HashMap<String, HubGuildWarMsg.Player>(1);
    	msg.p_list.put(uid, p);
    	p.name = p_info.user_base.getNickname();
    	p.img = p_info.user_base.getuImg();
    	p.vip = p_info.vip_level;
    	p.team_lv = p_info.team_lv;
    	p.fight = p_info.team_current_fighting;
    	p.cnt = g_info.daily_war_cnt;
    	p.raid = g_info.raid_s;
    	
    	msg.code = 0;
    	//log.info(JsonTransfer.getJson(respond));
    	return respond;
    }
    
    public CommonMsg handleG2HLogPlay(CommonMsg respond, CommonMsg receive) {
    	String r_id = receive.header.uid;
    	//log.info("handleG2HLogPlay {}", r_id);
		HubWarRcdDB rcd_db = SpringContextUtil.getBean(HubWarRcdDB.class);
		HubWarRcdInfo rcd = rcd_db.loadById(r_id);
		if (rcd == null) {
			return respond;
		}
		GuildWarMsg msg = new GuildWarMsg();
		respond.body.guild_war = msg;
		msg.success = true;
		GuildWarPlayerMsg p = new GuildWarPlayerMsg();
		msg.player = p;
		p.username = rcd.l_name;
		p.team_lv = rcd.l_lv;
		p.avatar = rcd.l_img;
		GuildWarPlayerMsg p2 = new GuildWarPlayerMsg();
		msg.player_2p = p2;
		p2.username = rcd.r_name;
		p2.team_lv = rcd.r_lv;
		p2.avatar = rcd.r_img;
		msg.battle_data = JsonTransfer._In(rcd.pack, BattlePack.class);
		msg.r_stars = (int)rcd.star;
		
		return respond;
    }
    
	public CommonMsg handleWarRank(CommonMsg respond) {	
		//log.info("Query handleWarRank ");
		try {
			String result = HttpRequest.PostFunction("http://"+ HUB_URL + I_DefMoudle.HUB_G2H_WARRANK, "");
			CommonMsg result_obj = JsonTransfer._In(result, CommonMsg.class);			
			if (result_obj == null) {
				return respond;
			}
			return result_obj;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		}
	}
    
	public CommonMsg handleG2HWarRank(CommonMsg respond, CommonMsg receive) {
		respond.body.rank = HubGuildWarManager.guild_war_msg;
		return respond;
	}
	
	public CommonMsg handleDaily(CommonMsg respond, HubGuildWarMsg daily_msg) {
		log.info("handleDaily {}", JsonTransfer.getJson(daily_msg));
		int mail_id = 1; //自定义物品邮件
		
		Mail victory_cfg = ConfigConstant.tMail.get(ConfigConstant.tConf.getWarMail1());
		String win_title = ConfigConstant.tWordconfig.get(victory_cfg.getMTitle()).getWord_cn();
		String win_word = ConfigConstant.tWordconfig.get(victory_cfg.getMTxt()).getWord_cn();
		Mail lose_cfg = ConfigConstant.tMail.get(ConfigConstant.tConf.getWarMail2());
		String lose_title = ConfigConstant.tWordconfig.get(lose_cfg.getMTitle()).getWord_cn();
		String lose_word = ConfigConstant.tWordconfig.get(lose_cfg.getMTxt()).getWord_cn();
		Mail draw_cfg = ConfigConstant.tMail.get(ConfigConstant.tConf.getWarMail3());
		String draw_title = ConfigConstant.tWordconfig.get(draw_cfg.getMTitle()).getWord_cn();
		String draw_word = ConfigConstant.tWordconfig.get(draw_cfg.getMTxt()).getWord_cn();
		
		int cid = ConfigConstant.tConf.getDonate1();
		lock.lock();
		try {
			for (Entry<String, HubGuildWarMsg.I> g : daily_msg.f_list.entrySet()) {
				String gid = g.getKey();
				HubGuildWarMsg.I mi = g.getValue();
				GuildDB gdb = SpringContextUtil.getBean(GuildDB.class);
				GuildInfo gi = gdb.loadByGuildId(gid);
				if (mi.win == BattleResult.LEFT_WIN) {
					gi.funds += ConfigConstant.tConf.getVictoryCapital();
				} else if (mi.win == BattleResult.LEFT_LOSE) {
					gi.funds += ConfigConstant.tConf.getFailCapital();
				} else if (mi.win == BattleResult.DRAW) {
					gi.funds += ConfigConstant.tConf.getDrawCapital();
				}
				gdb.save();
				int ms = gi.members.size();
				for (int i = 0; i < ms; ++i) {
					String uid = gi.members.get(i).id;
					PlayerMailDB mail_db = SpringContextUtil.getBean(PlayerMailDB.class);
					PlayerMailInfo mail_info = mail_db.loadByUid(uid);
					if (mail_info == null) {
						continue;
					}
					MailInfo custom = new MailInfo();
					custom.cnt = new ArrayList<Integer>();
					custom.cnt.add(mi.contri);
					custom.ids = new ArrayList<Integer>();
					custom.ids.add(cid);
					if (mi.win == BattleResult.LEFT_WIN) {
						custom.title = win_title;
						custom.word = win_word;
						if (uid.equals(gi.ceo_id)) {
							custom.dmd = (double)ConfigConstant.tConf.getCDRWin();
						}
					} else if (mi.win == BattleResult.LEFT_LOSE) {
						custom.title = lose_title;
						custom.word = lose_word;
						if (uid.equals(gi.ceo_id)) {
							custom.dmd = (double)ConfigConstant.tConf.getCDRFail();
						}
					} else if (mi.win == BattleResult.DRAW) {
						custom.title = draw_title;
						custom.word = draw_word;
						if (uid.equals(gi.ceo_id)) {
							custom.dmd = (double)ConfigConstant.tConf.getCDRDraw();
						}
					}
					MailImpl.AddMail(mail_info, mail_id, null, 1, custom);
					mail_db.save();
					//log.info("send guild war daily {} cti {}", mail_info.uid, mi.contri);
				}
			}
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}
 		
	}
	
	public CommonMsg handleWeekly(CommonMsg respond, HubGuildWarMsg weekly_msg) {
		log.info("handleWeekly {}", JsonTransfer.getJson(weekly_msg));
		int cfg_size = ConfigConstant.tWarrank.size();
		List<Integer> last_rank_list = new ArrayList<Integer>(cfg_size);
		List<Integer> mail_id_list = new ArrayList<Integer>(cfg_size);
		List<Integer> capital_list = new ArrayList<Integer>(cfg_size);
		for (int id = 1000; id < 1000 + cfg_size; ++id) {
			Warrank cfg = ConfigConstant.tWarrank.get(id);
			last_rank_list.add(cfg.getArenaNum()[1]);
			mail_id_list.add(cfg.getRankMail());
			capital_list.add(cfg.getCapital());
		}
		lock.lock();
		try {
			for (Entry<String, HubGuildWarMsg.I> g : weekly_msg.f_list.entrySet()) {
				String gid = g.getKey();
				HubGuildWarMsg.I mi = g.getValue();
				GuildDB gdb = SpringContextUtil.getBean(GuildDB.class);
				GuildInfo gi = gdb.loadByGuildId(gid);
				int ms = gi.members.size();
				int capital = 0;
				for (int i = 0; i < ms; ++i) {
					PlayerMailDB mail_db = SpringContextUtil.getBean(PlayerMailDB.class);
					PlayerMailInfo mail_info = mail_db.loadByUid(gi.members.get(i).id);
					if (mail_info == null) {
						continue;
					}
					int mail_id = 0;
					for (int j = 0; j < last_rank_list.size(); ++j) {
						if (mi.rank <= last_rank_list.get(j)) {
							mail_id = mail_id_list.get(j);
							capital = capital_list.get(j);
							break;
						}
					}
					if (mail_id == 0) {
						continue;
					}
					List<String> args = new ArrayList<String>();
					args.add(Integer.toString(mi.rank));
					MailImpl.AddMail(mail_info, mail_id, args, 1, null);
					mail_db.save();
					log.info("send guild war weekly {} rank {}", mail_info.uid, mi.rank);
				}
				gi.funds += capital;
				gdb.save();
			}
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}
	}
	/*================公会战相关 END============================*/
}

class HubGuildInfoFightSort implements Comparator<HubGuildInfo> {
	public int compare(HubGuildInfo arg0, HubGuildInfo arg1)
	{
		if (arg0 == null && arg1 != null)
			return -1;
		if (arg0 != null && arg1 == null)
			return 1;
		if (arg0 == null && arg1 == null)
			return 0;
		
		//按照fight大小，降序排
		if (arg0.fight < arg1.fight)
			return 1;
		if (arg0.fight > arg1.fight)
			return -1;

		return 0;
	}
}

class HubGuildInfoRandSort implements Comparator<HubGuildInfo> {
	public int compare(HubGuildInfo arg0, HubGuildInfo arg1)
	{
		if (Math.random() > 0.5) {
			return -1;
		}
		return 1;
	}
}

class PlayerFightSort implements Comparator<PlayerInfo> {
	public int compare(PlayerInfo arg0, PlayerInfo arg1)
	{
		if (arg0 == null && arg1 != null)
			return -1;
		if (arg0 != null && arg1 == null)
			return 1;
		if (arg0 == null && arg1 == null)
			return 0;
		
		//按照fight大小，降序排
		if (arg0.team_history_max_fighting < arg1.team_history_max_fighting)
			return 1;
		if (arg0.team_history_max_fighting > arg1.team_history_max_fighting)
			return -1;

		return 0;
	}
}

