package com.ssmGame.module.boss;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Fullboss;
import com.ssmData.config.entity.Role;
import com.ssmData.config.entity.Singleboss;
import com.ssmData.dbase.FullBossDB;
import com.ssmData.dbase.FullBossInfo;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerBossDB;
import com.ssmData.dbase.PlayerBossInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerMailDB;
import com.ssmData.dbase.PlayerMailInfo;
import com.ssmData.dbase.PlayerRolesInfo;
import com.ssmData.dbase.PlayerRolesInfoDB;
import com.ssmData.dbase.PlayerScrollDB;
import com.ssmData.dbase.PlayerScrollInfo;
import com.ssmData.dbase.RoleInfo;
import com.ssmData.dbase.ScrollInfo;
import com.ssmData.dbase.FullBossInfo.BossInfo;
import com.ssmGame.defdata.msg.boss.BossMsg;
import com.ssmGame.defdata.msg.boss.BossPlayerMsg;
import com.ssmGame.defdata.msg.boss.BossStatusMsg;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.RewardMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
import com.ssmGame.module.battle.ActorLogic;
import com.ssmGame.module.battle.BattlePack;
import com.ssmGame.module.battle.BattleResult;
import com.ssmGame.module.battle.BattleSide;
import com.ssmGame.module.battle.BattleSimulator;
import com.ssmGame.module.battle.BattleType;
import com.ssmGame.module.broadcast.BroadcastImpl;
import com.ssmGame.module.dmgreward.DmgRewardImpl;
import com.ssmGame.module.mail.MailImpl;
import com.ssmGame.module.player.PlayerImpl;
import com.ssmGame.util.AwardUtils;
import com.ssmGame.util.ItemCountPair;
import com.ssmGame.util.TimeUtils;

@Service
@Scope("prototype")
public class BossImpl {
	private static final Logger log = LoggerFactory.getLogger(BossImpl.class);
	private static final ReentrantLock LOCK = new ReentrantLock();
	private static final int NOT_ENOUGH_SCROLL = 1109;
	private static final int FULL_BOSS_SCROLL_ID = 801;
	private static final int TOP_KILLERS_SIZE = 5;
	
	public final static BossImpl getInstance(){
        return SpringContextUtil.getBean(BossImpl.class);
    }
	
	public static void handleLogin(String uid)
	{
		PlayerBossDB b_db = SpringContextUtil.getBean(PlayerBossDB.class);
		PlayerBossInfo act = b_db.loadByUid(uid);
		if (act == null){
			b_db.createDB(uid);
		}
	}
	
	public static CommonMsg handleSingleFight(CommonMsg respond, CommonMsg receive) {
		String uid = respond.header.uid;
		BossMsg msg = new BossMsg();
		respond.body.boss = msg;
		msg.success = false;
		
		int boss_id = receive.body.boss.boss_id;
		Singleboss b_cfg = ConfigConstant.tSingleboss.get(boss_id);
		if (null == b_cfg) {
			log.error("no single boss id {} player {}", boss_id, uid);
			return respond;
		}
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (p_info == null) {
			log.error("no player db player {}", uid);
			return respond;
		}
		if (b_cfg.getOpenLv() > p_info.team_lv) {
			return respond;
		}
		PlayerScrollDB s_db = SpringContextUtil.getBean(PlayerScrollDB.class);
		PlayerScrollInfo s_info = s_db.loadByUid(uid);
		if (s_info == null) {
			log.error("no scroll db player {}", uid);
			return respond;
		}
		int scroll_id = b_cfg.getFBchallenge();
		ScrollInfo sc_info = s_info.Get(scroll_id);
		if (sc_info == null) {
			log.error("no scroll id {} player {}", scroll_id, uid);
			return respond;
		}
		Calendar now_cal = Calendar.getInstance();
    	long today_start = TimeUtils.TodayStart();
    	sc_info.Refresh(now_cal, today_start);
    	if (sc_info.count <= 0) {
    		respond.header.rt_sub = NOT_ENOUGH_SCROLL;
    		return respond;
    	}
		PlayerBossDB b_db = SpringContextUtil.getBean(PlayerBossDB.class);
		PlayerBossInfo b_info = b_db.loadByUid(uid);
		if (b_info == null) {
			log.error("no boss db player {}", uid);
			return respond;
		}
		PlayerBagDB bag_db = SpringContextUtil.getBean(PlayerBagDB.class);
    	PlayerBagInfo bag_info = bag_db.loadByUid(uid);
    	if (bag_info.getEquipBagCapacity() - bag_info.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
    	PlayerRolesInfoDB r_db = SpringContextUtil.getBean(PlayerRolesInfoDB.class);
    	PlayerRolesInfo r_info = r_db.load(uid);
    	if (r_info == null) {
    		log.error("no roles db player {}", uid);
    		return respond;
    	}
    	
    	// 生成双方阵容
        Map<Integer, RoleInfo> my_roles = r_info.genRoles();
        Map<Integer, RoleInfo> enemy_roles = genSingleEnemyRoles(b_cfg);
        BattleSimulator sim = new BattleSimulator();
        sim.InitAllActorLogic(my_roles, enemy_roles, b_cfg.getTime(), ConfigConstant.tConf.getBattletime(), BattleType.LEVEL_BOSS
        		, null, null, null, null);
        BattlePack battle_result = sim.Exe();
        
        if (battle_result.m_result == BattleResult.LEFT_WIN)
        {
        	b_info.reward_hash = RandomStringUtils.randomAlphabetic(16);
        	b_info.reward_b_id = b_cfg.getID();
        	msg.reward_hash = b_info.reward_hash;
        }
        msg.battle = battle_result;
        
        // 记录最后战斗时间
        p_info.last_active_time = now_cal.getTimeInMillis();
        p_db.save();
        b_db.save();
		
        msg.success = true;
		return respond;
	}
	
	public static CommonMsg handleSingleReward(CommonMsg respond, CommonMsg receive) {
		String uid = respond.header.uid;
		BossMsg msg = new BossMsg();
		respond.body.boss = msg;
		msg.success = false;
		
		int boss_id = receive.body.boss.boss_id;
		Singleboss b_cfg = ConfigConstant.tSingleboss.get(boss_id);
		if (null == b_cfg) {
			log.error("no single boss id {} player {}", boss_id, uid);
			return respond;
		}
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (p_info == null) {
			log.error("no player db player {}", uid);
			return respond;
		}
		if (b_cfg.getOpenLv() > p_info.team_lv) {
			return respond;
		}
		PlayerScrollDB s_db = SpringContextUtil.getBean(PlayerScrollDB.class);
		PlayerScrollInfo s_info = s_db.loadByUid(uid);
		if (s_info == null) {
			log.error("no scroll db player {}", uid);
			return respond;
		}
		int scroll_id = b_cfg.getFBchallenge();
		ScrollInfo sc_info = s_info.Get(scroll_id);
		if (sc_info == null) {
			log.error("no scroll id {} player {}", scroll_id, uid);
			return respond;
		}
		Calendar now_cal = Calendar.getInstance();
    	long today_start = TimeUtils.TodayStart();
    	sc_info.Refresh(now_cal, today_start);
    	if (sc_info.count <= 0) {
    		respond.header.rt_sub = NOT_ENOUGH_SCROLL;
    		return respond;
    	}
		PlayerBossDB b_db = SpringContextUtil.getBean(PlayerBossDB.class);
		PlayerBossInfo b_info = b_db.loadByUid(uid);
		if (b_info == null) {
			log.error("no boss db player {}", uid);
			return respond;
		}
		if (boss_id != b_info.reward_b_id) {
			log.error("boss id not equal db {}, get {} player {}", b_info.reward_b_id, boss_id, uid);
			return respond;
		}
		if (!receive.body.boss.reward_hash.equals(b_info.reward_hash)) {
			log.error("boss hash not equal db {}, get {} player {}", b_info.reward_hash, receive.body.boss.reward_hash, uid);
			return respond;
		}
		PlayerBagDB bag_db = SpringContextUtil.getBean(PlayerBagDB.class);
    	PlayerBagInfo bag_info = bag_db.loadByUid(uid);
    	if (bag_info.getEquipBagCapacity() - bag_info.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
		sc_info.count--;
		b_info.reward_hash = "";
		b_info.reward_b_id = 0;
		b_db.save();
		s_db.save();
		respond.body.sync_scroll = new PlayerScrollInfo();
		respond.body.sync_scroll.scroll_list = new ArrayList<ScrollInfo>();
		respond.body.sync_scroll.scroll_list.add(sc_info);
			
		Map<Integer, Integer> sync_bags = new HashMap<Integer, Integer>();
		List<ItemCountPair> r_bag = AwardUtils.GetAward(b_cfg.getAward());
		List<SyncBagItem> r_bag_items = new ArrayList<SyncBagItem>(r_bag.size());
		for (ItemCountPair i : r_bag)
		{
			if (bag_info.addItemCount(i.m_item_id, i.m_count))
			{
				sync_bags.put(i.m_item_id, bag_info.getItemCount(i.m_item_id));
	            SyncBagItem item = new SyncBagItem();
	            item.id = i.m_item_id;
	            item.count = i.m_count;
	            r_bag_items.add(item);
			}
		}
		bag_db.save();
		respond.body.sync_bag = new SyncBagMsg();
		respond.body.sync_bag.items = sync_bags;
		
        // 记录最后战斗时间
        p_info.last_active_time = now_cal.getTimeInMillis();
		boolean is_levelup = PlayerImpl.addExp(p_info, b_cfg.getExp());

        if(is_levelup){
        	respond.header.rt_msg = 10001;
        	respond.body.sync_player_info = getSyncPlayerInfoMsg(p_info);
        }else{
        	respond.body.sync_player_info = new SyncPlayerInfoMsg(false);
        	respond.body.sync_player_info.exp = b_cfg.getExp();
        	respond.body.sync_player_info.gold = b_cfg.getGold();
        }
        p_info.addGold(b_cfg.getGold());
        p_db.save();
        
        RewardMsg reward = RewardMsg.generate((double)b_cfg.getExp(), (double)b_cfg.getGold(), 0.0, r_bag_items);
		respond.body.reward = reward;
		
		msg.success = true;
		return respond;
	}
	
	private static Map<Integer, RoleInfo> genSingleEnemyRoles(Singleboss cfg)
	{
		if (null == cfg)
			return null;
		// 构造返回内容
        Map<Integer, RoleInfo> enemy_team = new HashMap<Integer, RoleInfo>();
        for (int i = 0; i < cfg.getMon().length; ++i)
        {
			Role role_cfg = ConfigConstant.tRole.get(cfg.getMon()[i]);
			if(role_cfg == null) {
				continue;
			}

        	RoleInfo r = new RoleInfo();
        	r.InitByRoleConfigIdAndLv(cfg.getMon()[i], cfg.getMonLv()[i]);
        	r.skill_lv = role_cfg.getSkillLv(); // 特殊技能等级
        	enemy_team.put(i+1, r);
        }

        return enemy_team;
	}
	
    public static SyncPlayerInfoMsg getSyncPlayerInfoMsg(PlayerInfo p_info){
        SyncPlayerInfoMsg sync_player_info = new SyncPlayerInfoMsg(true);
        sync_player_info.gold = p_info.gold;
        sync_player_info.diamond = p_info.diamond;
        sync_player_info.exp = p_info.team_exp;
        sync_player_info.team_lv = p_info.team_lv;
        sync_player_info.team_current_fighting = p_info.team_current_fighting;
		sync_player_info.vip_level = p_info.vip_level;
		sync_player_info.last_gold_time = p_info.last_gold_time;
		sync_player_info.gold_buy_cnt = p_info.gold_buy_cnt;

        return sync_player_info;
    }
    
    //战斗、奖励和邮件全更新
    public static CommonMsg handleFullBossFight(CommonMsg respond, CommonMsg receive) {
		String uid = respond.header.uid;
		log.info("wb fight begin {}", uid);
		BossMsg msg = new BossMsg();
		respond.body.boss = msg;
		msg.success = false;
		
		int boss_id = receive.body.boss.boss_id;
		Fullboss b_cfg = ConfigConstant.tFullboss.get(boss_id);
		if (null == b_cfg) {
			log.error("no full boss id {} player {}", boss_id, uid);
			return respond;
		}
		PlayerScrollDB s_db = SpringContextUtil.getBean(PlayerScrollDB.class);
		PlayerScrollInfo s_info = s_db.loadByUid(uid);
		if (s_info == null) {
			log.error("no scroll db player {}", uid);
			return respond;
		}
		int scroll_id = FULL_BOSS_SCROLL_ID;
		ScrollInfo sc_info = s_info.Get(scroll_id);
		if (sc_info == null) {
			log.error("no scroll id {} player {}", scroll_id, uid);
			return respond;
		}
		Calendar now_cal = Calendar.getInstance();
    	long today_start = TimeUtils.TodayStart();
    	sc_info.Refresh(now_cal, today_start);
    	if (sc_info.count <= 0) {
    		respond.header.rt_sub = NOT_ENOUGH_SCROLL;
    		return respond;
    	}
		PlayerBossDB b_db = SpringContextUtil.getBean(PlayerBossDB.class);
		PlayerBossInfo b_info = b_db.loadByUid(uid);
		if (b_info == null) {
			log.error("no boss db player {}", uid);
			return respond;
		}
		Long last_t = b_info.fb_t.get(boss_id);
		if (last_t == null) {
			b_info.fb_t.put(boss_id, 0L);
			last_t = 0L;
		}
		long now = now_cal.getTimeInMillis();
		if (last_t > 0L && now - last_t < ConfigConstant.tConf.getBossCd() * 1000) {	
			return respond;
		}
    	PlayerRolesInfoDB r_db = SpringContextUtil.getBean(PlayerRolesInfoDB.class);
    	PlayerRolesInfo r_info = r_db.load(uid);
    	if (r_info == null) {
    		log.error("no roles db player {}", uid);
    		return respond;
    	}
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (p_info == null) {
			log.error("no player db player {}", uid);
			return respond;
		}
		
    	LOCK.lock();
    	try {
    		FullBossDB fb_db = SpringContextUtil.getBean(FullBossDB.class);
    		FullBossInfo fb_info = fb_db.load();
    		if (null == fb_info) {
    			log.error("no fullboss db");
    			return respond;
    		}
    		FullBossInfo.BossInfo bi = null;
    		for (int i = 0; i < fb_info.all.size(); ++i) {
    			FullBossInfo.BossInfo test = fb_info.all.get(i);
    			if (test.id == boss_id) {
    				bi = test;
    				break;
    			}
    		}
    		if (bi == null) {
    			bi = fb_info.new BossInfo();
    			InitOneBoss(bi, b_cfg);
    			fb_info.all.add(bi);
    		}
    		if (bi.dead_t > 0L) {
    			if (now - bi.dead_t < b_cfg.getRevive() * 1000) {
    				return respond;
    			}
    			InitOneBoss(bi, b_cfg);
    		}
    		FullBossInfo.BossInfo.Hitman hi = null;
    		for (int i = 0; i < bi.hit_list.size(); ++i) {
    			FullBossInfo.BossInfo.Hitman test = bi.hit_list.get(i);
    			if (test.uid.equals(uid)) {
    				hi = test;
    				break;
    			}
    		}
    		if (hi == null) {
    			hi = bi.new Hitman();
    			bi.hit_list.add(hi);
    			hi.uid = uid;
    		}
    		
        	// 生成双方阵容
    		Map<Integer, Integer> modify_right_hp = new HashMap<Integer, Integer>();
    		for (int i = 0; i < bi.mon_ids.size(); ++i) {
    			modify_right_hp.put(bi.mon_ids.get(i), bi.hps.get(i));
    		}
            Map<Integer, RoleInfo> my_roles = r_info.genRoles();
            Map<Integer, RoleInfo> enemy_roles = genFullEnemyRoles(bi.mon_ids, b_cfg.getMonLv());
            BattleSimulator sim = new BattleSimulator();
            sim.InitAllActorLogic(my_roles, enemy_roles, b_cfg.getTime(), ConfigConstant.tConf.getBattletime(), BattleType.LEVEL_BOSS
            		, null, null, modify_right_hp, null);
            BattlePack battle_result = sim.Exe();
            DmgRewardImpl.RefreshMax(uid, battle_result);
            sc_info.count--;
            for (ActorLogic a : sim.m_all_actors_logic.values()) {
            	if (a.m_side != BattleSide.Left) {
					continue;
				}
            	hi.acc_dmg += (long)a.t_dmg;
            }
            b_info.fb_t.put(boss_id, now);
            hi.a_t = now;
            boolean not_in_tops = true;
        	for (int i = 0; i < bi.tops.size(); ++i) {
        		BossInfo.Hitman h = bi.tops.get(i);
        		if (h.uid.equals(hi.uid)) {
        			h.acc_dmg = hi.acc_dmg;
        			h.a_t = now;
        			not_in_tops = false;
        			bi.tops.sort(new SortHitman());
        			break;
        		}
        	}
        	if (not_in_tops) {
        		if (bi.tops.size() >= TOP_KILLERS_SIZE) {
        			long min = Long.MAX_VALUE;
                	for (int i = 0; i < bi.tops.size(); ++i) {
                		long test = bi.tops.get(i).acc_dmg;
                		if (min > test) {
                			min = test;
                		}
                	}
                	if (hi.acc_dmg > min) {
                		bi.tops.remove(bi.tops.size() - 1);
                		bi.tops.add(hi);
                		bi.tops.sort(new SortHitman());
                	}
        		} else {
        			bi.tops.add(hi);
        			bi.tops.sort(new SortHitman());
        		}
        	}
            
            if (battle_result.m_result == BattleResult.LEFT_WIN){
            	bi.dead_t = now;
            	FullBossInfo.BossInfo.Killer killer = bi.new Killer();
            	killer.uid = uid;
            	killer.k_t = now;
            	killer.f = p_info.team_current_fighting;
            	bi.killers.add(killer);
            	if (bi.killers.size() > TOP_KILLERS_SIZE) {
            		bi.killers.remove(0);
            	}
            	
            	//发公告
            	String context = ConfigConstant.tWordconfig.get(BroadcastImpl.FULLBOSS_KILL).getWord_cn();
            	context = context.replace("$1", p_info.user_base.getNickname());
            	context = context.replace("$2", ConfigConstant.tWordconfig.get(b_cfg.getName()).getWord_cn());
            	BroadcastImpl bc = BroadcastImpl.getInstance();
            	bc.SendBrocast(context, p_info.user_base.gid, p_info.user_base.zid);
            	
            	//发邮件
            	PlayerMailDB my_mail_db = SpringContextUtil.getBean(PlayerMailDB.class);
				PlayerMailInfo my_mail_info = my_mail_db.loadByUid(uid);
				if (my_mail_info != null) {
					MailImpl.AddMail(my_mail_info, b_cfg.getKillAward(), null, 1, null);
					my_mail_db.save();
				}				
            	bi.hit_list.sort(new SortHitman());
            	for (int i = 0; i < bi.hit_list.size(); ++i) {
            		FullBossInfo.BossInfo.Hitman h = bi.hit_list.get(i);
            		int mail_id = 0;
            		if (i == 0) {
            			mail_id = b_cfg.getFirstAward();
            		} else if (i == 1) {
            			mail_id = b_cfg.getSecondAward();
            		} else if (i == 2) {
            			mail_id = b_cfg.getThirdAward();
            		} else {
            			mail_id = b_cfg.getOtherAward();
            		}
            		PlayerMailDB mail_db = SpringContextUtil.getBean(PlayerMailDB.class);
    				PlayerMailInfo mail_info = mail_db.loadByUid(h.uid);
    				if (mail_info == null)
    					continue;
    				MailImpl.AddMail(mail_info, mail_id, null, 1, null);
    				mail_db.save();
    				//log.info("Fullboss mail player {} rank {} boss {}", h.uid, i + 1, boss_id);
            	}
            }
            
            for (int i = 0; i < bi.mon_ids.size(); ++i) {
    			for (ActorLogic a : sim.m_all_actors_logic.values()) {
    				if (a.m_r_inf.role_id != bi.mon_ids.get(i)) {
    					continue;
    				}
    				if (!a.m_is_alive) {
    					bi.hps.set(i, 0);
    				} else {
    					bi.hps.set(i, a.m_current_hp);
    				}
    			}
    		}
            
            s_db.save();
            b_db.save();
            fb_db.save();
    		respond.body.sync_scroll = new PlayerScrollInfo();
    		respond.body.sync_scroll.scroll_list = new ArrayList<ScrollInfo>();
    		respond.body.sync_scroll.scroll_list.add(sc_info);
            
            msg.battle = battle_result;           
            msg.success = true;
            log.info("wb fight end {}", uid);
    		return respond;
    	} catch (Exception e) {
    		e.printStackTrace();
    		log.error(e.getMessage());
    		log.info("wb fight exp {}", uid);
    		return respond;
    	} finally {
    		LOCK.unlock();
    		log.info("wb fight unlock {}", uid);
    	}
    }
    
    public static void InitFullBossDB(FullBossInfo info) {
    	if (info == null) {
    		return;
    	}
    	List<FullBossInfo.BossInfo> old_all = info.all;
    	Map<Integer, Fullboss> all_cfg = ConfigConstant.tFullboss;
    	List<FullBossInfo.BossInfo> new_all = new ArrayList<FullBossInfo.BossInfo>(all_cfg.size());
    	info.all = new_all;
    	for (Fullboss cfg : all_cfg.values()) {
    		FullBossInfo.BossInfo bi = null;
    		for (int i = 0; i < old_all.size(); ++i) {
    			FullBossInfo.BossInfo oi = old_all.get(i);
    			if (oi.id == cfg.getID()) {
    				bi = oi;
    				break;
    			}
    		}
    		if (bi == null) {
    			bi = info.new BossInfo();
    			InitOneBoss(bi, cfg);
    		}
    		new_all.add(bi);
    	}
    }
    
    public static void InitOneBoss(FullBossInfo.BossInfo info, Fullboss cfg) {
    	info.id = cfg.getID();
    	info.dead_t = 0L;
    	info.maxhp = 0L;
    	info.hit_list.clear();
    	info.tops.clear();
    	info.mon_ids.clear();
    	info.hps.clear();
		for (int i = 0; i < cfg.getMon().length; ++i) {
			int role_id = cfg.getMon()[i];
			Role r_cfg = ConfigConstant.tRole.get(role_id);
			if (r_cfg == null) {
				log.error("no role id {}", role_id);
				continue;
			}
			info.mon_ids.add(role_id);
			RoleInfo ri = new RoleInfo();
			ri.InitByRoleConfigIdAndLv(role_id, cfg.getMonLv()[i]);
			info.hps.add(ri.max_hp);
			info.maxhp += (long)ri.max_hp;
		}
    }
    
    private static Map<Integer, RoleInfo> genFullEnemyRoles(List<Integer> ids, int[] lvs)
	{
		// 构造返回内容
        Map<Integer, RoleInfo> enemy_team = new HashMap<Integer, RoleInfo>();
        for (int i = 0; i < ids.size(); ++i)
        {
			Role role_cfg = ConfigConstant.tRole.get(ids.get(i));
			if(role_cfg == null) {
				continue;
			}

        	RoleInfo r = new RoleInfo();
        	r.InitByRoleConfigIdAndLv(role_cfg.getID(), lvs[i]);
        	r.skill_lv = role_cfg.getSkillLv(); // 特殊技能等级
        	enemy_team.put(i+1, r);
        }

        return enemy_team;
	}

    public static CommonMsg handleFbInfo(CommonMsg respond, CommonMsg receive){
		String uid = respond.header.uid;
		BossMsg msg = new BossMsg();
		respond.body.boss = msg;
		msg.success = false;
		
		List<BossStatusMsg> status = new ArrayList<BossStatusMsg>();
		msg.status = status;
		PlayerBossDB b_db = SpringContextUtil.getBean(PlayerBossDB.class);
		PlayerBossInfo b_info = b_db.loadByUid(uid);
		if (b_info == null) {
			log.error("no boss db player {}", uid);
			return respond;
		}
		FullBossDB fb_db = SpringContextUtil.getBean(FullBossDB.class);
		FullBossInfo fb_info = fb_db.load();
		if (null == fb_info) {
			log.error("no fullboss db");
			return respond;
		}
		for (int i = 0; i < fb_info.all.size(); ++i) {
			FullBossInfo.BossInfo bi = fb_info.all.get(i);
			BossStatusMsg m = new BossStatusMsg();
			status.add(m);
			m.id = bi.id;
			m.dead_t = bi.dead_t;
			m.maxhp = bi.maxhp;
			m.curhp = 0L;
			for (int j = 0; j < bi.hps.size(); ++j) {
				m.curhp += (long)bi.hps.get(j);
			}
			m.atk_t = b_info.fb_t.get(bi.id);
			if (m.atk_t == null) {
				m.atk_t = 0L;
			}
			m.participant = bi.hit_list.size();
		}
		msg.success = true;
    	return respond;
    }

    public static CommonMsg handleFBKiller(CommonMsg respond, CommonMsg receive){
		BossMsg msg = new BossMsg();
		respond.body.boss = msg;
		msg.success = false;
		
		int boss_id = receive.body.boss.boss_id;
		
		List<BossPlayerMsg> players = new ArrayList<BossPlayerMsg>();
		msg.players = players;
		
		FullBossDB fb_db = SpringContextUtil.getBean(FullBossDB.class);
		FullBossInfo fb_info = fb_db.load();
		if (null == fb_info) {
			log.error("no fullboss db");
			return respond;
		}
		FullBossInfo.BossInfo bi = null;
		for (int i = 0; i < fb_info.all.size(); ++i) {
			FullBossInfo.BossInfo test = fb_info.all.get(i);
			if (test.id == boss_id) {
				bi = test;
				break;
			}
		}
		if (bi == null) {
			return respond;
		}
		for (int i = 0; i < bi.killers.size() && i < TOP_KILLERS_SIZE; ++i) {
			FullBossInfo.BossInfo.Killer k = bi.killers.get(bi.killers.size() - i - 1);
			PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
			PlayerInfo p_info = p_db.loadById(k.uid);
			if (p_info == null) {
				continue;
			}
			BossPlayerMsg m = new BossPlayerMsg();
			players.add(m);
			m.uid = k.uid;
			m.nickname = p_info.user_base.getNickname();
			m.fighting = k.f;
			m.timeline = k.k_t;
		}
		msg.success = true;
    	return respond;
    }

    public static CommonMsg handleFBDamage(CommonMsg respond, CommonMsg receive) {
		String uid = respond.header.uid;
		BossMsg msg = new BossMsg();
		respond.body.boss = msg;
		msg.success = false;
		
		int boss_id = receive.body.boss.boss_id;
		Fullboss b_cfg = ConfigConstant.tFullboss.get(boss_id);
		if (null == b_cfg) {
			log.error("no full boss id {} player {}", boss_id, uid);
			return respond;
		}
		
		FullBossDB fb_db = SpringContextUtil.getBean(FullBossDB.class);
		FullBossInfo fb_info = fb_db.load();
		if (null == fb_info) {
			log.error("no fullboss db");
			return respond;
		}
		FullBossInfo.BossInfo bi = null;
		for (int i = 0; i < fb_info.all.size(); ++i) {
			FullBossInfo.BossInfo test = fb_info.all.get(i);
			if (test.id == boss_id) {
				bi = test;
				break;
			}
		}
		if (bi == null) {
			return respond;
		}
		
		boolean revived_but_not_hit = false;
		long now = Calendar.getInstance().getTimeInMillis();
		if (bi.dead_t > 0L) {
			if (now - bi.dead_t > b_cfg.getRevive() * 1000) {
				revived_but_not_hit = true;
			}
		}
		
		msg.damage = 0.0;
		if (!revived_but_not_hit) {
			for (int i = 0; i < bi.hit_list.size(); ++i) {
				FullBossInfo.BossInfo.Hitman h = bi.hit_list.get(i);
				if (h.uid.equals(uid)) {
					msg.damage = (double)h.acc_dmg;
				}
			}
		}
		
		List<BossPlayerMsg> players = new ArrayList<BossPlayerMsg>();
		msg.players = players;
		
		if (!revived_but_not_hit) {
			for (int i = 0; i < bi.tops.size(); ++i) {
				FullBossInfo.BossInfo.Hitman h = bi.tops.get(i);
				PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
				PlayerInfo p_info = p_db.loadById(h.uid);
				if (p_info == null) {
					continue;
				}
				BossPlayerMsg m = new BossPlayerMsg();
				players.add(m);
				m.uid = h.uid;
				m.nickname = p_info.user_base.getNickname();
				m.damage = (double)h.acc_dmg;
			}
		}
		
    	msg.success = true;
    	return respond;
    }
}

class SortHitman implements Comparator<FullBossInfo.BossInfo.Hitman>
{
	public int compare(FullBossInfo.BossInfo.Hitman arg0, FullBossInfo.BossInfo.Hitman arg1)
	{
		if (arg0 == null && arg1 != null)
			return -1;
		if (arg0 != null && arg1 == null)
			return 1;
		if (arg0 == null && arg1 == null)
			return 0;
		
		//按照dmg大小，降序排
		if (arg0.acc_dmg < arg1.acc_dmg)
			return 1;
		if (arg0.acc_dmg > arg1.acc_dmg)
			return -1;
		
		//按照时间，升序
		if (arg0.a_t < arg1.a_t)
			return -1;
		if (arg0.a_t > arg1.a_t)
			return 1;
		
		return 0;
	}
}
