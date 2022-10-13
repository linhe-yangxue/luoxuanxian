package com.ssmGame.module.send;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.ssmData.config.entity.Hubmap;
import com.ssmData.config.entity.Send;
import com.ssmData.dbase.HubSendDB;
import com.ssmData.dbase.HubSendInfo;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerRolesInfo;
import com.ssmData.dbase.PlayerRolesInfoDB;
import com.ssmData.dbase.PlayerScrollDB;
import com.ssmData.dbase.PlayerScrollInfo;
import com.ssmData.dbase.PlayerSendDB;
import com.ssmData.dbase.PlayerSendInfo;
import com.ssmData.dbase.PlayerSendRcdDB;
import com.ssmData.dbase.PlayerSendRcdInfo;
import com.ssmData.dbase.RoleInfo;
import com.ssmData.dbase.ScrollInfo;
import com.ssmData.dbase.SendQuest;
import com.ssmGame.constants.I_DefMoudle;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.RewardMsg;
import com.ssmGame.defdata.msg.send.SendMsg;
import com.ssmGame.defdata.msg.send.SendQuestMsg;
import com.ssmGame.defdata.msg.send.SendRobLogMsg;
import com.ssmGame.defdata.msg.send.SendRobTeamMsg;
import com.ssmGame.defdata.msg.send.SendRobType;
import com.ssmGame.defdata.msg.send.SendRunType;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
import com.ssmGame.module.battle.ActorLogic;
import com.ssmGame.module.battle.BattlePack;
import com.ssmGame.module.battle.BattleResult;
import com.ssmGame.module.battle.BattleSide;
import com.ssmGame.module.battle.BattleSimulator;
import com.ssmGame.module.battle.BattleType;
import com.ssmGame.module.boss.BossImpl;
import com.ssmGame.module.player.PlayerImpl;
import com.ssmGame.module.role.RoleAttrCalc;
import com.ssmGame.util.RandomMethod;
import com.ssmGame.util.TimeUtils;

@Service
@Scope("prototype")
public class SendImpl {
	private static final Logger log = LoggerFactory.getLogger(SendImpl.class);
	private static final ReentrantLock lock = new ReentrantLock();
	
	private @Value("${HUB_URL}")String HUB_URL;
	private @Value("${IS_HUB}") int IS_HUB;
	
	private static int MAX_REFRESH = 5;  //掠夺推荐人数
	private static int MAX_ENEMY = 20;
	private static int MAX_RCD = 5;
	
	public final static SendImpl getInstance(){
        return SpringContextUtil.getBean(SendImpl.class);
	}
	
	private boolean isHub() {
		return IS_HUB == 1;
	}
	
	public CommonMsg handleRefreshQuest(CommonMsg respond, CommonMsg receive) {
		String uid = receive.header.uid;
		SendMsg msg = new SendMsg();
		respond.body.send = msg;
		msg.success = false;
		if (isHub()){
			log.error("is hub");
			return respond;
		}
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (p_info == null) {
			log.error("no p info {}", uid);
			return respond;
		}
		
		PlayerScrollDB sc_db = SpringContextUtil.getBean(PlayerScrollDB.class);
		PlayerScrollInfo sc_info = sc_db.loadByUid(uid);
		if (sc_info == null) {
			log.error("no scroll info {}", uid);
			return respond;
		}
		int sc_id = 1000;
		ScrollInfo sc = sc_info.Get(sc_id);
		if (sc == null) {
			log.error("no scroll cfg {}", sc_id);
			return respond;  
		}
		if (sc.count <= 0) {
			log.error("no scroll enough {}", uid);
			return respond;
		}
		
		lock.lock();
		try{
			PlayerSendDB s_db = SpringContextUtil.getBean(PlayerSendDB.class);
			PlayerSendInfo s_info = s_db.loadByUid(uid);
			if (s_info == null) {
				log.error("no send info {}", uid);
				return respond;
			}
			int r_ret = RandAllQuest(s_info, p_info.team_lv);
			if (r_ret != 0) {
				respond.header.rt_sub = r_ret;
				return respond;
			}
			--sc.count;
			sc_db.save();
			s_db.save();
			respond.body.sync_scroll = new PlayerScrollInfo();
			respond.body.sync_scroll.scroll_list = new ArrayList<ScrollInfo>();
			respond.body.sync_scroll.scroll_list.add(sc);
			InitQuestMsg(msg, s_info);
			
			msg.success = true;
			return respond;
		}catch (Exception e) {
			e.printStackTrace();
			return respond;
		}finally {
			lock.unlock();
		}
	}
	
	public CommonMsg handleReqQuestInfo(CommonMsg respond, CommonMsg receive) {
		String uid = receive.header.uid;
		SendMsg msg = new SendMsg();
		respond.body.send = msg;
		msg.success = false;
		if (isHub()){
			log.error("is hub");
			return respond;
		}
		
		PlayerSendDB s_db = SpringContextUtil.getBean(PlayerSendDB.class);
		PlayerSendInfo s_info = s_db.loadByUid(uid);
		if (s_info == null) {
			log.error("no send info {}", uid);
			return respond;
		}
		
		if (s_info.qs.size() != ConfigConstant.tConf.getSendIdNum()) {
			PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
			PlayerInfo p_info = p_db.loadById(uid);
			if (p_info == null) {
				log.error("no p info {}", uid);
				return respond;
			}
			int r_ret = RandAllQuest(s_info, p_info.team_lv);
			if (r_ret != 0) {
				respond.header.rt_sub = r_ret;
				return respond;
			}
			s_db.save();
		}
		InitQuestMsg(msg, s_info);
		
		msg.success = true;
		return respond;
	}
	
	private static void InitQuestMsg(SendMsg msg, PlayerSendInfo info) {
		msg.quests = new ArrayList<SendQuestMsg>();
		for (int i = 0; i < info.qs.size(); ++i){
			SendQuest q = info.qs.get(i);
			SendQuestMsg qm = new SendQuestMsg();
			msg.quests.add(qm);
			qm.send_id = q.send_id;
			qm.end_t = q.end_t;
			qm.roles = q.roles;
			qm.cnt_be_rob = q.cnt_be_rob;
			qm.cnt_rob = q.cnt_rob;
			qm.type = q.type;
		}
	}
	
	private static void InitPros(List<Send> all_ids, List<Integer> all_pros, int lv){
		Map<Integer, Send> all_qs = ConfigConstant.tSend;
		for (Entry<Integer, Send> q : all_qs.entrySet()) {
			Send cfg = q.getValue();
			if (lv < cfg.getLv()[0] || lv > cfg.getLv()[1]) {
				continue;
			}
			all_ids.add(cfg);
			all_pros.add(cfg.getSendPR());
		}
	}
	
	private static int RandOneQuest(SendQuest q, int lv){
		List<Send> all_ids = new ArrayList<Send>();
		List<Integer> all_pros = new ArrayList<Integer>();
		InitPros(all_ids, all_pros, lv);
		int hit = RandomMethod.CalcHitWhichIndex(all_pros);
		if (hit == -1) {
			log.error("RandOneQuest Error");
			return -1;
		}
		Send cfg = all_ids.get(hit);
		q.send_id = cfg.getSendId();
		q.cnt_be_rob = 0;
		q.cnt_rob = 0;
		q.end_t = 0L;
		q.r_ids.clear();
		q.roles.clear();
		q.roles.add(-1);q.roles.add(-1);q.roles.add(-1);q.roles.add(-1);q.roles.add(-1);
		q.type = SendRunType.Normal;
		return q.send_id;
	}
	
	private static int RandAllQuest(PlayerSendInfo info, int lv){
		int t_cnt = ConfigConstant.tConf.getSendIdNum();
		while (info.qs.size() < t_cnt){
			SendQuest q = new SendQuest();
			info.qs.add(q);
		}
		while (info.qs.size() > t_cnt){
			info.qs.remove(info.qs.size() - 1);
		}
		
		List<Send> all_ids = new ArrayList<Send>();
		List<Integer> all_pros = new ArrayList<Integer>();
		InitPros(all_ids, all_pros, lv);
		
		int no_refresh = 1228;
		for (int i = 0; i < t_cnt; ++i){
			SendQuest q = info.qs.get(i);
			if (q.end_t != 0) {
				continue;
			}
			int hit = RandomMethod.CalcHitWhichIndex(all_pros);
			if (hit == -1) {
				no_refresh = 1228;
				return no_refresh;
			}
			Send cfg = all_ids.get(hit);
			q.send_id = cfg.getSendId();
			q.cnt_be_rob = 0;
			q.cnt_rob = 0;
			q.end_t = 0L;
			q.roles.clear();
			q.roles.add(-1);q.roles.add(-1);q.roles.add(-1);q.roles.add(-1);q.roles.add(-1);
			q.type = SendRunType.Normal;
			no_refresh = 0;
		}
		if (no_refresh != 0) {
			return no_refresh;
		}
		
		return 0;
	}
	
	public CommonMsg handleQuestExc(CommonMsg respond, CommonMsg receive) {
		String uid = receive.header.uid;
		SendMsg msg = new SendMsg();
		int quest_i = receive.body.send.quest_id;
		List<Integer> roles = receive.body.send.roles;
		int quest_type = receive.body.send.type;
		respond.body.send = msg;
		msg.success = false;
		if (isHub()){
			log.error("is hub");
			return respond;
		}
		
		PlayerScrollDB sc_db = SpringContextUtil.getBean(PlayerScrollDB.class);
		PlayerScrollInfo sc_info = sc_db.loadByUid(uid);
		if (sc_info == null) {
			log.error("no scroll info {}", uid);
			return respond;
		}
		int sc_id = ConfigConstant.tConf.getSend();
		ScrollInfo sc = sc_info.Get(sc_id);
		if (sc == null) {
			log.error("no scroll cfg {}", sc_id);
			return respond;  
		}
		if (sc.count <= 0) {
			respond.header.rt_sub = 1225;
			return respond;
		}
		
		int t_cnt = ConfigConstant.tConf.getSendIdNum();
		if (quest_i < 0 || quest_i >= t_cnt) {
			log.error("quest_i error {}", quest_i);
			return respond;
		}
		
		if (quest_type != SendRunType.Normal && quest_type != SendRunType.Extreme) {
			log.error("quest_type error {}", quest_type);
			return respond;
		}
		
		List<Integer> test_same = new ArrayList<Integer>();
		for (int i = 0; i < roles.size(); ++i){
			int id = roles.get(i);
			if (id == -1) {
				continue;
			}
			if (test_same.contains(id)) {
				log.error("role same {}", id);
				return respond;
			}
			test_same.add(id);
		}
		if (test_same.size() == 0) {
			log.error("no enough player");
			return respond;
		}
		
		PlayerRolesInfoDB r_db = SpringContextUtil.getBean(PlayerRolesInfoDB.class);
		PlayerRolesInfo r_info = r_db.load(uid);
		if (r_info == null) {
			log.error("no roles info {}", uid);
			return respond;
		}
		for (int i = 0; i < roles.size(); ++i){
			int id = roles.get(i);
			if (id == -1){
				continue;
			}
			if (r_info.GetRole(id) == null) {
				log.error("has no role {} p {}", id, uid);
				return respond;
			}
		}
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (p_info == null) {
			log.error("no p info {}", uid);
			return respond;
		}
		
		lock.lock();
		try {
			PlayerSendDB s_db = SpringContextUtil.getBean(PlayerSendDB.class);
			PlayerSendInfo s_info = s_db.loadByUid(uid);
			if (s_info == null) {
				log.error("no send info {}", uid);
				return respond;
			}
			if (quest_i >= s_info.qs.size()) {
				log.error("quest_i out of qs size q {} s {}", quest_i, s_info.qs.size());
				return respond;
			}
			int qs_sum = 0;
			for (int i = 0; i < s_info.qs.size(); ++i) {
				if (s_info.qs.get(i).end_t != 0) {
					++qs_sum;
				}
			}
			if (qs_sum >= ConfigConstant.tConf.getSendNum()) {
				respond.header.rt_sub = 1231;
				return respond;
			}
				
			SendQuest t_info = s_info.qs.get(quest_i);
			if (t_info.end_t != 0) {
				log.error("t_info.end_t  t {}, index {}", t_info.end_t, quest_i);
				return respond;
			}
			
			Send s_cfg = ConfigConstant.tSend.get(t_info.send_id);
			if (s_cfg == null) {
				log.error("send cfg error {}, p {}", t_info.send_id, uid);
				return respond;
			}
			
			if (quest_type == SendRunType.Extreme && !p_info.hasDiamond(s_cfg.getVipSend())) {
				log.error("Vip Task DMD not enough {}", uid);
				return respond;
			}
			
			if (test_same.size() < s_cfg.getRoleNum()) {
				respond.header.rt_sub = 1226;
				return respond;
			}
			
			for (int i = 0; i < s_info.qs.size(); ++i) {
				if (i == quest_i) {
					continue;
				}
				SendQuest t = s_info.qs.get(i);
				if (t.end_t == 0) {
					continue;
				}
				for (int j = 0; j < t.roles.size(); ++j) {
					if (test_same.contains(t.roles.get(j))) {
						log.error("has same role in another quest {}, role {}, qi {}", i, t.roles.get(j), quest_i);
						return respond;
					}
				}
			}
			
			///test pass
			if (quest_type == SendRunType.Extreme) {
				int dmd_cost = s_cfg.getVipSend();
				PlayerImpl.SubDiamond(p_info, dmd_cost);
				p_db.save();
				respond.body.sync_player_info = new SyncPlayerInfoMsg();
				respond.body.sync_player_info.diamond = -dmd_cost;
			}
			--sc.count;
			sc_db.save();
			respond.body.sync_scroll = new PlayerScrollInfo();
			respond.body.sync_scroll.scroll_list = new ArrayList<ScrollInfo>();
			respond.body.sync_scroll.scroll_list.add(sc);
			
			long now = Calendar.getInstance().getTimeInMillis();
			t_info.end_t = now + s_cfg.getTime();
			t_info.roles = roles;
			t_info.type = quest_type;
			
			PlayerRolesInfo infos = new PlayerRolesInfo();
    		infos.InitEmpty();
    		for (Integer id : roles)
    		{
    			if (id <= 0)
    				continue;
    			infos.roles.add(r_info.GetRole(id).Clone());
    		}
    		for (int pos = 1; pos <= roles.size(); ++pos)
    		{
    			infos.pve_team.put(pos, roles.get(pos - 1));
    		}
    		for (Integer id : roles)
    		{
    			if (id <= 0)
    				continue;
    			RoleAttrCalc.RefreshRoleAttr(id, infos);
    		}
    		t_info.fight = infos.CalcTeamFighting();
			
			s_db.save();
			msg.success = true;
			sendCheckInToHub(uid, quest_i, p_info, t_info);
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}
	}

	private void sendCheckInToHub(String uid, int quest_i, PlayerInfo p_info, SendQuest t_info)
			throws Exception {
		CommonMsg send = new CommonMsg(0, uid);
		SendMsg sm = new SendMsg();
		send.body.send = sm;
		sm.quest_id = quest_i;
		sm.hub_quest = t_info;
		sm.name = p_info.user_base.getNickname();
		sm.vip = p_info.vip_level;
		send.header.zid = p_info.user_base.zid;
		sm.lv = p_info.team_lv;
		String result = HttpRequest.PostFunction("http://"+ HUB_URL + I_DefMoudle.HUB_SEND_CHECKIN, JsonTransfer.getJson(send));
		CommonMsg result_obj = JsonTransfer._In(result, CommonMsg.class);
		if (result_obj == null || result_obj.header.rt != 0) {
			log.error(result);
			log.error("Hub Error");
		}
	}

	public CommonMsg handleQuestOp(CommonMsg respond, CommonMsg receive) {
		String uid = receive.header.uid;
		SendMsg msg = new SendMsg();
		respond.body.send = msg;
		msg.success = false;
		if (isHub()){
			log.error("is hub");
			return respond;
		}
		int quest_i = receive.body.send.quest_id;
		List<Integer> roles = receive.body.send.roles;
		
		int t_cnt = ConfigConstant.tConf.getSendIdNum();
		if (quest_i < 0 || quest_i >= t_cnt) {
			log.error("quest_i error {}", quest_i);
			return respond;
		}
		
		List<Integer> test_same = new ArrayList<Integer>();
		for (int i = 0; i < roles.size(); ++i){
			int id = roles.get(i);
			if (id == -1) {
				continue;
			}
			if (test_same.contains(id)) {
				log.error("role same {}", id);
				return respond;
			}
			test_same.add(id);
		}
		if (test_same.size() == 0) {
			log.error("no enough player");
			return respond;
		}
		PlayerRolesInfoDB r_db = SpringContextUtil.getBean(PlayerRolesInfoDB.class);
		PlayerRolesInfo r_info = r_db.load(uid);
		if (r_info == null) {
			log.error("no roles info {}", uid);
			return respond;
		}
		for (int i = 0; i < roles.size(); ++i){
			int id = roles.get(i);
			if (id == -1){
				continue;
			}
			if (r_info.GetRole(id) == null) {
				log.error("has no role {} p {}", id, uid);
				return respond;
			}
		}
		
		lock.lock();
		try {
			PlayerSendDB s_db = SpringContextUtil.getBean(PlayerSendDB.class);
			PlayerSendInfo s_info = s_db.loadByUid(uid);
			if (s_info == null) {
				log.error("no send info {}", uid);
				return respond;
			}
			if (quest_i >= s_info.qs.size()) {
				log.error("quest_i out of qs size q {} s {}", quest_i, s_info.qs.size());
				return respond;
			}
				
			SendQuest t_info = s_info.qs.get(quest_i);
			if (t_info.end_t == 0) {
				log.error("t_info.end_t  t {}, index {}", t_info.end_t, quest_i);
				return respond;
			}
			
			int sum = 0;
			for (int i = 0; i < t_info.roles.size(); ++i) {
				if (test_same.contains(t_info.roles.get(i))) {
					++sum;
				}
			}
			if (test_same.size() != sum) {
				log.error("size not match  ori {} new {}", sum, test_same.size());
				return respond;
			}
			
			t_info.roles = roles;
			s_db.save();
			
			msg.success = true;
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}
	}
	
	public CommonMsg handleQuestReward(CommonMsg respond, CommonMsg receive) {
		String uid = receive.header.uid;
		SendMsg msg = new SendMsg();
		respond.body.send = msg;
		msg.success = false;
		if (isHub()){
			log.error("is hub");
			return respond;
		}
		int quest_i = receive.body.send.quest_id;	
		int t_cnt = ConfigConstant.tConf.getSendIdNum();
		if (quest_i < 0 || quest_i >= t_cnt) {
			log.error("quest_i error {}", quest_i);
			return respond;
		}
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (p_info == null) {
			log.error("no p info {}", uid);
			return respond;
		}
		
		PlayerBagDB bag_db = SpringContextUtil.getBean(PlayerBagDB.class);
		PlayerBagInfo bag = bag_db.loadByUid(uid);
		if (bag == null) {
			log.error("no bag info {}", uid);
			return respond;
		}
		if (bag.getEquipBagCapacity() - bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		bag_db = null;
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
		
		lock.lock();
		try {
			PlayerSendDB s_db = SpringContextUtil.getBean(PlayerSendDB.class);
			PlayerSendInfo s_info = s_db.loadByUid(uid);
			if (s_info == null) {
				log.error("no send info {}", uid);
				return respond;
			}
			if (quest_i >= s_info.qs.size()) {
				log.error("quest_i out of qs size q {} s {}", quest_i, s_info.qs.size());
				return respond;
			}
				
			SendQuest t_info = s_info.qs.get(quest_i);
			if (t_info.end_t == 0) {
				log.error("t_info.end_t  t {}, index {}", t_info.end_t, quest_i);
				return respond;
			}
			
			long now = Calendar.getInstance().getTimeInMillis();
			if (t_info.end_t > now) {
				log.error("time error now {} end {}", now, t_info.end_t);
				return respond;
			}
			
			Send s_cfg = ConfigConstant.tSend.get(t_info.send_id);
			if (s_cfg == null) {
				log.error("send cfg error {}, p {}", t_info.send_id, uid);
				return respond;
			}
			
			float extreme = 1.0f;
			if (t_info.type == SendRunType.Extreme) {
				extreme = 2.0f;
			}
			
			int add_dmd = (int)((s_cfg.getJewel()[0] - s_cfg.getJewel()[1] * t_info.cnt_be_rob) * extreme);
			if (add_dmd < 0) add_dmd = 0;
			int add_gold = (int)((s_cfg.getGold()[0] - s_cfg.getGold()[1] * t_info.cnt_be_rob) * extreme);
			if (add_gold < 0) add_gold = 0;
			int add_exp = (int)((s_cfg.getExp()[0] - s_cfg.getExp()[1] * t_info.cnt_be_rob) * extreme);
			if (add_exp < 0) add_exp = 0;
			
			if (add_exp > 0)
	    	{
	    		boolean is_levelup = PlayerImpl.addExp(p_info, add_exp);
	            if(is_levelup){
	            	respond.header.rt_msg = 10001;
	            	if (respond.body.sync_player_info == null)
	            		respond.body.sync_player_info = new SyncPlayerInfoMsg(false);
	            	respond.body.sync_player_info = BossImpl.getSyncPlayerInfoMsg(p_info);
	            }else{
	            	if (respond.body.sync_player_info == null)
	            		respond.body.sync_player_info = new SyncPlayerInfoMsg(false);
	            	respond.body.sync_player_info.exp = add_exp;
					if (respond.body.reward == null)
						respond.body.reward = new RewardMsg();
	            	respond.body.reward.exp = (double)add_exp;
	            }
	    	}
			if (add_dmd > 0){
				p_info.addDiamond(add_dmd);
	    		if (respond.body.sync_player_info == null)
	    			respond.body.sync_player_info = new SyncPlayerInfoMsg();
	    		respond.body.sync_player_info.diamond = add_dmd;
	    		if (respond.body.reward == null)
	    			respond.body.reward = new RewardMsg();
	    		respond.body.reward.diamond = (double)add_dmd;
			}
	    	if (add_gold > 0)
	    	{
	    		p_info.addGold(add_gold);
	    		if (respond.body.sync_player_info == null)
	    			respond.body.sync_player_info = new SyncPlayerInfoMsg();
	    		respond.body.sync_player_info.gold = add_gold;
	    		if (respond.body.reward == null)
	    			respond.body.reward = new RewardMsg();
	    		respond.body.reward.gold = (double)add_gold;
	    	}
	    	for (int i = 0; i < s_cfg.getItem().length; ++i) {
	    		int cnt = s_cfg.getQuantity()[i];
	    		int rob = s_cfg.getRobItem()[i];
	    		int add = (int)((cnt- rob * t_info.cnt_be_rob) * extreme);
	    		if (add <= 0) {
	    			continue;
	    		}
	    		int id = s_cfg.getItem()[i];
	    		bag.addItemCount(id, add);
    			if (respond.body.sync_bag == null)
    				respond.body.sync_bag = new SyncBagMsg();
    			respond.body.sync_bag.items.put(id, bag.getItemCount(id));
	    		if (respond.body.reward == null)
	    			respond.body.reward = new RewardMsg();
	    		if (respond.body.reward.items == null)
	    			respond.body.reward.items = new ArrayList<SyncBagItem>();
    			SyncBagItem a = new SyncBagItem();
    			a.id = id;
    			a.count = add;
    			respond.body.reward.items.add(a);
	    	}
	    	p_db.save();
	    	bag_db.save();
	    	msg.send_id = RandOneQuest(t_info, p_info.team_lv);
	    	s_db.save();
	    	msg.success = true;
	    	sendCheckInToHub(uid, quest_i, p_info, t_info);
			return respond;
		} catch(Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}
	}

	public CommonMsg handleQuestQuick(CommonMsg respond, CommonMsg receive) {
		String uid = receive.header.uid;
		SendMsg msg = new SendMsg();
		respond.body.send = msg;
		msg.success = false;
		if (isHub()){
			log.error("is hub");
			return respond;
		}
		int quest_i = receive.body.send.quest_id;	
		int t_cnt = ConfigConstant.tConf.getSendIdNum();
		if (quest_i < 0 || quest_i >= t_cnt) {
			log.error("quest_i error {}", quest_i);
			return respond;
		}
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (p_info == null) {
			log.error("no p info {}", uid);
			return respond;
		}
		
		lock.lock();
		try {
			PlayerSendDB s_db = SpringContextUtil.getBean(PlayerSendDB.class);
			PlayerSendInfo s_info = s_db.loadByUid(uid);
			if (s_info == null) {
				log.error("no send info {}", uid);
				return respond;
			}
			if (quest_i >= s_info.qs.size()) {
				log.error("quest_i out of qs size q {} s {}", quest_i, s_info.qs.size());
				return respond;
			}
			SendQuest t_info = s_info.qs.get(quest_i);
			if (t_info.end_t == 0) {
				log.error("t_info.end_t  t {}, index {}", t_info.end_t, quest_i);
				return respond;
			}			
			long now = Calendar.getInstance().getTimeInMillis();
			int cost_dmd = (int)(((t_info.end_t - now) / TimeUtils.ONE_MIN_TIME) * ConfigConstant.tConf.getSendMoney());
			if (cost_dmd < 1) cost_dmd = 1;
			
			if (!p_info.hasDiamond(cost_dmd)) {
				log.error("DMD not enough {}, cost {}", uid, cost_dmd);
				return respond;
			}
			
			PlayerImpl.SubDiamond(p_info, cost_dmd);
    		if (respond.body.sync_player_info == null)
    			respond.body.sync_player_info = new SyncPlayerInfoMsg();
    		respond.body.sync_player_info.diamond = -cost_dmd;
    		p_db.save();
    		
    		t_info.end_t = now;
    		s_db.save();
    		msg.success = true;
    		sendCheckInToHub(uid, quest_i, p_info, t_info);
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}
	}

	public CommonMsg handleRobTeams(CommonMsg respond, CommonMsg receive) {
		String uid = receive.header.uid;
		SendMsg msg = new SendMsg();
		respond.body.send = msg;
		msg.success = false;
		if (isHub()){
			log.error("is hub");
			return respond;
		}
		Calendar now = Calendar.getInstance();
		int n_day = now.get(Calendar.DAY_OF_YEAR);
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (p_info == null) {
			log.error("no p info {}", uid);
			return respond;
		}
		
		lock.lock();
		try {
			PlayerSendDB s_db = SpringContextUtil.getBean(PlayerSendDB.class);
			PlayerSendInfo s_info = s_db.loadByUid(uid);
			if (s_info == null) {
				log.error("no send info {}", uid);
				return respond;
			}
			
			Calendar test_cal = Calendar.getInstance();
			test_cal.setTimeInMillis(s_info.refresh_rob_t);
			if (test_cal.get(Calendar.DAY_OF_YEAR) != n_day) {
				CommonMsg send = new CommonMsg(0, uid);
				SendMsg sm = new SendMsg();
				send.body.send = sm;
				sm.lv = p_info.team_lv;
				String result = HttpRequest.PostFunction("http://"+ HUB_URL + I_DefMoudle.HUB_SEND_REFRESH, JsonTransfer.getJson(send));
				CommonMsg result_obj = JsonTransfer._In(result, CommonMsg.class);
				if (result_obj.header.rt != 0) {
					log.error("Hub Error");
				} else {
					SendMsg receive_refresh = result_obj.body.send;
					s_info.rob_teams = receive_refresh.hub_rob_teams;
				}
				s_info.refresh_rob_t = now.getTimeInMillis();
				s_db.save();
			}
			
			msg.teams = new ArrayList<SendRobTeamMsg>();
			InitTeams(msg.teams, s_info.rob_teams);
			
			msg.success = true;
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}
	}
	
	private static void InitTeams(List<SendRobTeamMsg> dst, List<PlayerSendInfo.Team> src) {
		dst.clear();
		for (int i = 0; i < src.size(); ++i) {
			PlayerSendInfo.Team s = src.get(i);
			SendRobTeamMsg d = new SendRobTeamMsg();
			dst.add(d);
			d.quest_id = s.q_idx;
			d.team_lv = s.lv;
			d.uid = s.uid;
			d.username = s.user_name;
			d.vip = s.vip;
			d.zid = s.zid;
			d.quest = new SendQuestMsg();
			InitQuestMsgInTeam(s.q, d);
		}
	}

	private static void InitQuestMsgInTeam(SendQuest q, SendRobTeamMsg d) {
		d.quest.cnt_be_rob = q.cnt_be_rob;
		d.quest.cnt_rob = q.cnt_rob;
		d.quest.end_t = q.end_t;
		d.quest.send_id = q.send_id;
		d.quest.type = q.type;
		d.quest.roles = q.roles;
		d.fighting = q.fight;
	}
	
	public CommonMsg handleHubCheckin(CommonMsg respond, CommonMsg receive) {
		String uid = receive.header.uid;
		respond.header.rt = 1;
		if (!isHub()){
			log.error("is not hub");
			return respond;
		}
		
		HubSendDB s_db = SpringContextUtil.getBean(HubSendDB.class);
		HubSendInfo s_info = s_db.loadById(uid);
		if (s_info == null) {
			s_info = (HubSendInfo)s_db.createDB(uid);
		}
		while (ConfigConstant.tConf.getSendIdNum() > s_info.qs.size()) {
			SendQuest q = new SendQuest();
			s_info.qs.add(q);
		}
		SendMsg rec_msg = receive.body.send;
		int quest_idx = rec_msg.quest_id;
		if (quest_idx >= s_info.qs.size()) {
			log.error("quest_idx error {}", quest_idx, uid);
			return respond;
		}
		s_info.zid = receive.header.zid;
		s_info.qs.set(quest_idx, rec_msg.hub_quest);
		s_info.lv = rec_msg.lv;
		s_info.name = rec_msg.name;
		s_info.vip = rec_msg.vip;
		s_db.save();
		
		respond.header.rt = 0;
		return respond;
	}

	public CommonMsg handleHubRefresh(CommonMsg respond, CommonMsg receive) {
		String uid = receive.header.uid;
		respond.header.rt = 1;
		if (!isHub()){
			log.error("is not hub");
			return respond;
		}
		HubSendDB s_db = SpringContextUtil.getBean(HubSendDB.class);
		HubSendInfo s_info = s_db.loadById(uid);
		if (s_info == null) {
			log.error("no player {} ", uid);
			return respond;
		}
		SendMsg rec_msg = receive.body.send;
		s_info.lv = rec_msg.lv;
		s_db.save();
		int min_lv = rec_msg.lv - ConfigConstant.tConf.getSendMate()[1];
		if (min_lv < 1) min_lv = 1;
		int max_lv = rec_msg.lv + ConfigConstant.tConf.getSendMate()[0];
		
		long now = Calendar.getInstance().getTimeInMillis();
		Query q = new Query(new Criteria().andOperator(Criteria.where("lv").gte(min_lv), Criteria.where("lv").lte(max_lv)));
		List<HubSendInfo> all_user = BaseDaoImpl.getInstance().findAll(q, HubSendInfo.class);
		List<HubSendInfo> pools = new ArrayList<HubSendInfo>();
		selectPools(uid, now, all_user, pools);
		if (pools.size() < MAX_REFRESH) {
			q = new Query(new Criteria().orOperator(Criteria.where("lv").lt(min_lv), Criteria.where("lv").gt(max_lv)));
			all_user = BaseDaoImpl.getInstance().findAll(q, HubSendInfo.class);
			selectPools(uid, now, all_user, pools);
		}
		List<HubSendInfo> rand_ret = pools;
		if (pools.size() > MAX_REFRESH) {
			rand_ret = new ArrayList<HubSendInfo>();
			int i = 0;
			int r = pools.size() - 1;
			while (i < MAX_REFRESH) {
				++i;
				int rd = CommUtil.getRandom(0, r);
				rand_ret.add(pools.get(rd));
				pools.set(rd, pools.get(r));
				--r;
			}
		}
		PlayerSendInfo pp = new PlayerSendInfo();
		List<PlayerSendInfo.Team> ret = new ArrayList<PlayerSendInfo.Team>();
		int max_rob = ConfigConstant.tConf.getPlundered();
		for (int i = 0; i < rand_ret.size(); ++i) {
			HubSendInfo h = rand_ret.get(i);
			List<SendQuest> qs = new ArrayList<SendQuest>();
			List<Integer> qs_idx = new ArrayList<Integer>();
			for (int j = 0; j < h.qs.size(); ++j) {
				SendQuest rand_qs = h.qs.get(j);
				if (rand_qs.end_t > now && rand_qs.cnt_be_rob < max_rob && !rand_qs.r_ids.contains(uid)) {
					qs.add(rand_qs);
					qs_idx.add(j);
				}
			}
			if (qs.size() <= 0) {
				continue;
			}
			PlayerSendInfo.Team p = pp.new Team();
			ret.add(p);
			int rand_i = CommUtil.getRandom(0, qs.size() - 1);
			p.q_idx = qs_idx.get(rand_i);
			p.q = qs.get(rand_i);
			p.lv = h.lv;
			p.uid = h.id;
			p.user_name = h.name;
			p.vip = h.vip;
			p.zid = h.zid;
		}
		respond.body.send = new SendMsg();
		respond.body.send.hub_rob_teams = ret;
		
		respond.header.rt = 0;
		return respond;
	}

	private void selectPools(String uid, long now, List<HubSendInfo> all_user, List<HubSendInfo> pools) {
		int max_rob = ConfigConstant.tConf.getPlundered();
		for (int i = 0; i < all_user.size(); ++i) {
			HubSendInfo h = all_user.get(i);
			if (h.id.equals(uid))
				continue;
			for (int j = 0; j < h.qs.size(); ++j) {
				SendQuest quest = h.qs.get(j);
				if (quest.end_t > now && quest.cnt_be_rob < max_rob && !quest.r_ids.contains(uid)) {
					pools.add(h);
					break;
				}
			}
		}
	}

	//掠夺刷新
	public CommonMsg handleRobRefresh(CommonMsg respond, CommonMsg receive) {
		String uid = receive.header.uid;
		SendMsg msg = new SendMsg();
		respond.body.send = msg;
		msg.success = false;
		if (isHub()){
			log.error("is hub");
			return respond;
		}
		
		PlayerScrollDB sc_db = SpringContextUtil.getBean(PlayerScrollDB.class);
		PlayerScrollInfo sc_info = sc_db.loadByUid(uid);
		if (sc_info == null) {
			log.error("no scroll info {}", uid);
			return respond;
		}
		int sc_id = 1001;
		ScrollInfo sc = sc_info.Get(sc_id);
		if (sc == null) {
			log.error("no scroll cfg {}", sc_id);
			return respond;  
		}
		if (sc.count <= 0) {
			log.error("no scroll enough {}", uid);
			return respond;
		}
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (p_info == null) {
			log.error("no p info {}", uid);
			return respond;
		}
		
		lock.lock();
		try {
			CommonMsg send = new CommonMsg(0, uid);
			SendMsg sm = new SendMsg();
			send.body.send = sm;
			sm.lv = p_info.team_lv;
			String result = HttpRequest.PostFunction("http://"+ HUB_URL + I_DefMoudle.HUB_SEND_REFRESH, JsonTransfer.getJson(send));
			CommonMsg result_obj = JsonTransfer._In(result, CommonMsg.class);
			if (result_obj.header.rt != 0) {
				log.error("Hub Error");
				return respond;
			} else {
				PlayerSendDB s_db = SpringContextUtil.getBean(PlayerSendDB.class);
				PlayerSendInfo s_info = s_db.loadByUid(uid);
				if (s_info == null) {
					log.error("no send info {}", uid);
					return respond;
				}
				SendMsg receive_refresh = result_obj.body.send;
				s_info.rob_teams = receive_refresh.hub_rob_teams;
				s_info.refresh_rob_t = Calendar.getInstance().getTimeInMillis();
				s_db.save();
				msg.teams = new ArrayList<SendRobTeamMsg>();
				InitTeams(msg.teams, s_info.rob_teams);
				
				--sc.count;
				sc_db.save();
				respond.body.sync_scroll = new PlayerScrollInfo();
				respond.body.sync_scroll.scroll_list = new ArrayList<ScrollInfo>();
				respond.body.sync_scroll.scroll_list.add(sc);
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
	
	//仇人列表
	public CommonMsg handleRobRevengers(CommonMsg respond, CommonMsg receive) {
		String uid = receive.header.uid;
		SendMsg msg = new SendMsg();
		respond.body.send = msg;
		msg.success = false;
		if (isHub()){
			log.error("is hub");
			return respond;
		}
		PlayerSendDB s_db = SpringContextUtil.getBean(PlayerSendDB.class);
		PlayerSendInfo s_info = s_db.loadByUid(uid);
		if (s_info == null) {
			log.error("no send info {}", uid);
			return respond;
		}
		
		CommonMsg send = new CommonMsg(0, uid);
		SendMsg sm = new SendMsg();
		send.body.send = sm;
		sm.hub_enemys = s_info.enemys;
		try {
			String result = HttpRequest.PostFunction("http://"+ HUB_URL + I_DefMoudle.HUB_SEND_FIND_ENEMY, JsonTransfer.getJson(send));
			CommonMsg result_obj = JsonTransfer._In(result, CommonMsg.class);
			if (result_obj.header.rt != 0) {
				log.error("Hub Error");
				return respond;
			} else {
				SendMsg receive_refresh = result_obj.body.send;
				msg.teams = receive_refresh.teams;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		}
		
		msg.success = true;
		return respond;
	}
	
	//仇人列表 hub
	public CommonMsg handleHubFindEnemy(CommonMsg respond, CommonMsg receive) {
		String uid = receive.header.uid;
		respond.header.rt = 1;
		if (!isHub()){
			log.error("is not hub");
			return respond;
		}
		SendMsg rec_msg = receive.body.send;
		SendMsg result_msg = new SendMsg();
		respond.body.send = result_msg;
		result_msg.teams = new ArrayList<SendRobTeamMsg>();
		long now = Calendar.getInstance().getTimeInMillis();
		int max_rob = ConfigConstant.tConf.getPlundered();
		for (int i = 0; i < rec_msg.hub_enemys.size(); ++i) {
			HubSendDB s_db = SpringContextUtil.getBean(HubSendDB.class);
			HubSendInfo s_info = s_db.loadById(rec_msg.hub_enemys.get(i));
			if (s_info == null) {
				continue;
			}
			for (int j = 0; j < s_info.qs.size(); ++j) {
				SendQuest q = s_info.qs.get(j);
				if (!QuestCanBeRob(q, now, uid, max_rob))
					continue;
				SendRobTeamMsg at = new SendRobTeamMsg();
				result_msg.teams.add(at);
				at.quest = new SendQuestMsg();
				InitQuestMsgInTeam(q, at);
				at.quest_id = j;
				at.team_lv = s_info.lv;
				at.uid = s_info.id;
				at.username = s_info.name;
				at.vip = s_info.vip;
				at.zid = s_info.zid;
			}
		}
		respond.header.rt = 0;
		return respond;
	}
	
	private static boolean QuestCanBeRob(SendQuest q, long now, String uid, int max_rob) {
		return q.end_t > now && !q.r_ids.contains(uid) && q.cnt_be_rob < max_rob;
	}
	
	public CommonMsg handleRobExec(CommonMsg respond, CommonMsg receive) {
		String uid = receive.header.uid;
		SendMsg msg = new SendMsg();
		msg.success = false;
		respond.body.send = msg;
		if (isHub()){
			log.error("is hub");
			return respond;
		}
		
		int quest_id = receive.body.send.quest_id;
		int t_cnt = ConfigConstant.tConf.getSendIdNum();
		if (quest_id < 0 || quest_id >= t_cnt) {
			log.error("quest_id error {}", quest_id);
			return respond;
		}
		int rob_type = receive.body.send.rob_type;
		int ene_quest_id = receive.body.send.ene_quest_id;
		if (ene_quest_id < 0 || ene_quest_id >= t_cnt) {
			log.error("ene_quest_id error {}", ene_quest_id);
			return respond;
		}
		String ene_uid = receive.body.send.uid;
		if (ene_uid.equals(uid)) {
			log.error("uid the same ene {} uid {}", ene_uid, uid);
			return respond;
		}
		int ene_zid = receive.body.send.zid;
		Hubmap h_cfg = ConfigConstant.tHubmap.get(ene_zid);
		if (h_cfg == null) {
			log.error("no hub zid {}", ene_zid);
			return respond;
		}
		
		PlayerRolesInfoDB r_db = SpringContextUtil.getBean(PlayerRolesInfoDB.class);
		PlayerRolesInfo r_info = r_db.load(uid);
		if (r_info == null) {
			log.error("no roles info {}", uid);
			return respond;
		}
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (p_info == null) {
			log.error("no p info {}", uid);
			return respond;
		}
		
		PlayerBagDB bag_db = SpringContextUtil.getBean(PlayerBagDB.class);
		PlayerBagInfo bag = bag_db.loadByUid(uid);
		if (bag == null) {
			log.error("no bag info {}", uid);
			return respond;
		}
		if (bag.getEquipBagCapacity() - bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		bag_db = null;
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
		
		CommonMsg result_obj = null;
		Send rob_cfg = null;
		int rob_times = 0;
		try {
			CommonMsg send = new CommonMsg(0, ene_uid);
			send.body.send = new SendMsg();
			send.body.send.quest_id = ene_quest_id;
			send.body.send.uid = uid;
			String result = HttpRequest.PostFunction("http://"+ h_cfg.getURL() + I_DefMoudle.HUB_G2G_SEND_QUEST_ROLES, JsonTransfer.getJson(send));
			result_obj = JsonTransfer._In(result, CommonMsg.class);
			if (result_obj.header.rt != 0) {
				respond.header.rt_sub = result_obj.header.rt;
				return respond;
			}
			rob_cfg = ConfigConstant.tSend.get(result_obj.body.send.send_id);
			rob_times = result_obj.body.send.rob_times;
		} catch (Exception e){
			e.printStackTrace();
			return respond;
		}
		BattlePack battle_result = null;

		lock.lock();
		try {
			PlayerSendDB s_db = SpringContextUtil.getBean(PlayerSendDB.class);
			PlayerSendInfo s_info = s_db.loadByUid(uid);
			if (s_info == null) {
				log.error("no send info {}", uid);
				return respond;
			}
			SendQuest my_quest = s_info.qs.get(quest_id);
			if (my_quest == null) {
				log.error("no quest_info  ply {} idx {}", uid, quest_id);
				return respond;
			}
			if (my_quest.cnt_rob >= ConfigConstant.tConf.getRob())
			{
				respond.header.rt_sub = 1227;
				return respond;
			}
			PlayerSendInfo.Team rob_team = null;
			if (rob_type == SendRobType.Normal) {
				for (int i = 0; i < s_info.rob_teams.size(); ++i) {
					rob_team = s_info.rob_teams.get(i);
					if (rob_team.uid.equals(ene_uid)) { 
						ene_zid = rob_team.zid;
						break;
					}
					rob_team = null;
				}
				if (rob_team == null) {
					log.error("no this rob team ene {} player {}", ene_uid, uid);
					return respond;
				}
			} else if (rob_type == SendRobType.Revenge) {
				if (!s_info.enemys.contains(ene_uid)) {
					log.error("no this enemy id ene {} player {}", ene_uid, uid);
					return respond;
				}
			} else {
				log.error("rob type error {}", rob_type);
				return respond;
			}
			
			Map<Integer, RoleInfo> my_roles = new HashMap<Integer, RoleInfo>();
			if (!GenRoles(my_roles, my_quest.roles, r_info)) {
				log.error("quest role error no r_id player {}", uid);
				return respond;
			}
			
			++my_quest.cnt_rob;
			
			Map<Integer, RoleInfo> ene_roles = result_obj.body.send.battle_roles;
	        // 模拟战斗
	        BattleSimulator sim = new BattleSimulator();
	        sim.InitAllActorLogic(my_roles, ene_roles, ConfigConstant.tConf.getArenaTime(), 0, BattleType.SEND_QUEST
	        		, null, null, null, null);
	        battle_result = sim.Exe();
	        
	        if (battle_result.m_result == BattleResult.LEFT_WIN) {
	        	int add_exp = rob_cfg.getExp()[1] * rob_times;
	        	int add_gold = rob_cfg.getGold()[1] * rob_times;
	        	int add_dmd = rob_cfg.getJewel()[1] * rob_times;
	        	if (add_exp > 0) {
	        		boolean is_levelup = PlayerImpl.addExp(p_info, add_exp);
		            if(is_levelup){
		            	respond.header.rt_msg = 10001;
		            	if (respond.body.sync_player_info == null)
		            		respond.body.sync_player_info = new SyncPlayerInfoMsg(false);
		            	respond.body.sync_player_info = BossImpl.getSyncPlayerInfoMsg(p_info);
		            }else{
		            	if (respond.body.sync_player_info == null)
		            		respond.body.sync_player_info = new SyncPlayerInfoMsg(false);
		            	respond.body.sync_player_info.exp = add_exp;
						if (respond.body.reward == null)
							respond.body.reward = new RewardMsg();
		            	respond.body.reward.exp = (double)add_exp;
		            }
	        	}
	        	if (add_dmd > 0){
					p_info.addDiamond(add_dmd);
		    		if (respond.body.sync_player_info == null)
		    			respond.body.sync_player_info = new SyncPlayerInfoMsg();
		    		respond.body.sync_player_info.diamond = add_dmd;
		    		if (respond.body.reward == null)
		    			respond.body.reward = new RewardMsg();
		    		respond.body.reward.diamond = (double)add_dmd;
				}
		    	if (add_gold > 0)
		    	{
		    		p_info.addGold(add_gold);
		    		if (respond.body.sync_player_info == null)
		    			respond.body.sync_player_info = new SyncPlayerInfoMsg();
		    		respond.body.sync_player_info.gold = add_gold;
		    		if (respond.body.reward == null)
		    			respond.body.reward = new RewardMsg();
		    		respond.body.reward.gold = (double)add_gold;
		    	}
		    	for (int i = 0; i < rob_cfg.getItem().length; ++i) {
		    		int rob = rob_cfg.getRobItem()[i];
		    		int add = rob * rob_times;
		    		if (add <= 0) {
		    			continue;
		    		}
		    		int id = rob_cfg.getItem()[i];
		    		bag.addItemCount(id, add);
	    			if (respond.body.sync_bag == null)
	    				respond.body.sync_bag = new SyncBagMsg();
	    			respond.body.sync_bag.items.put(id, bag.getItemCount(id));
		    		if (respond.body.reward == null)
		    			respond.body.reward = new RewardMsg();
		    		if (respond.body.reward.items == null)
		    			respond.body.reward.items = new ArrayList<SyncBagItem>();
	    			SyncBagItem a = new SyncBagItem();
	    			a.id = id;
	    			a.count = add;
	    			respond.body.reward.items.add(a);
		    	}
		    	p_db.save();
		    	bag_db.save();
		    	
		    	CommonMsg send = new CommonMsg(0, uid);
				SendMsg sm = new SendMsg();
				send.body.send = sm;
				sm.lv = p_info.team_lv;
				String result = HttpRequest.PostFunction("http://"+ HUB_URL + I_DefMoudle.HUB_SEND_REFRESH, JsonTransfer.getJson(send));
				CommonMsg refresh_obj = JsonTransfer._In(result, CommonMsg.class);
				if (result_obj.header.rt == 0) {
					s_info.rob_teams = refresh_obj.body.send.hub_rob_teams;
					s_info.refresh_rob_t = Calendar.getInstance().getTimeInMillis();
					msg.teams = new ArrayList<SendRobTeamMsg>();
					int s = s_info.rob_teams.size();
					for (int i = 0; i < s; ++i){
						PlayerSendInfo.Team t = s_info.rob_teams.get(i);
						if (t.uid.equals(ene_uid) && t.q_idx == ene_quest_id) {
							s_info.rob_teams.remove(i);
							break;
						}
					}
					InitTeams(msg.teams, s_info.rob_teams);
				}
	        }
	        //todo 存日志 先不做了，因为是自己打别人，只存别人打自己的 
	        s_db.save();			
	        msg.battle = battle_result;
			msg.success = true;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}
		
		try {
			CommonMsg send = new CommonMsg(0, ene_uid);
			send.body.send = new SendMsg();
			send.body.send.quest_id = ene_quest_id;
			send.body.send.uid = uid;
			send.body.send.name = p_info.user_base.getNickname();
			send.body.send.zid = p_info.user_base.zid;
			send.body.send.battle = battle_result;
			HttpRequest.PostFunction("http://"+ h_cfg.getURL() + I_DefMoudle.HUB_G2G_SEND_BATTLE_RET, JsonTransfer.getJson(send));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return respond;
	}

	public CommonMsg handleHubGGQuestAndRoles(CommonMsg respond, CommonMsg receive) {
		String uid = receive.header.uid;
		respond.header.rt = 1;
		SendMsg msg = new SendMsg();
		respond.body.send = msg;
		if (isHub()){
			log.error("is hub");
			return respond;
		}
		int quest_id = receive.body.send.quest_id;
		int t_cnt = ConfigConstant.tConf.getSendIdNum();
		if (quest_id < 0 || quest_id >= t_cnt) {
			log.error("quest_id error {}", quest_id);
			return respond;
		}
		PlayerSendDB s_db = SpringContextUtil.getBean(PlayerSendDB.class);
		PlayerSendInfo s_info = s_db.loadByUid(uid);
		if (s_info == null) {
			log.error("no send info {}", uid);
			return respond;
		}
		SendQuest my_quest = s_info.qs.get(quest_id);
		if (my_quest == null) {
			log.error("no quest_info  ply {} idx {}", uid, quest_id);
			return respond;
		}
		if (my_quest.cnt_be_rob >= ConfigConstant.tConf.getPlundered()) {
			respond.header.rt = 1229;
			return respond;
		}
		long now = Calendar.getInstance().getTimeInMillis();
		if (my_quest.end_t < now) {
			respond.header.rt = 1229;
			return respond;
		}
		if (my_quest.r_ids.contains(receive.body.send.uid)) {
			respond.header.rt = 1230;
			return respond;
		}
		PlayerRolesInfoDB r_db = SpringContextUtil.getBean(PlayerRolesInfoDB.class);
		PlayerRolesInfo r_info = r_db.load(uid);
		if (r_info == null) {
			log.error("no roles info {}", uid);
			return respond;
		}
		Map<Integer, RoleInfo> my_roles = new HashMap<Integer, RoleInfo>();
		if (!GenRoles(my_roles, my_quest.roles, r_info)) {
			log.error("quest role error no r_id player {}", uid);
			return respond;
		}
		msg.battle_roles = my_roles;
		msg.send_id = my_quest.send_id;
		msg.rob_times = my_quest.type == SendRunType.Extreme ? 2 : 1;
		
		respond.header.rt = 0;
		return respond;
	}
		
	private static boolean GenRoles(Map<Integer, RoleInfo> teams, List<Integer> quest, PlayerRolesInfo roles) {
		for (int i = 0; i < quest.size(); ++i){
			int r_id = quest.get(i);
			if (r_id <= 0) continue;
			RoleInfo r = roles.GetRole(r_id);
			if (r == null) {
				log.error("quest role error no r_id {}", r_id);
				return false;
			}
			teams.put(i + 1, r);
		}
		return true;
	}
	
	public CommonMsg handleHubGGBattleRet(CommonMsg respond, CommonMsg receive) {
		String uid = receive.header.uid;
		respond.header.rt = 1;
		if (isHub()){
			log.error("is hub");
			return respond;
		}
		int quest_id = receive.body.send.quest_id;
		int t_cnt = ConfigConstant.tConf.getSendIdNum();
		if (quest_id < 0 || quest_id >= t_cnt) {
			log.error("quest_id error {}", quest_id);
			return respond;
		}
		String ene_id = receive.body.send.uid;
		BattlePack battle_result = receive.body.send.battle;
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (p_info == null) {
			log.error("no p info {}", uid);
			return respond;
		}
		
		lock.lock();
		try {
			PlayerSendDB s_db = SpringContextUtil.getBean(PlayerSendDB.class);
			PlayerSendInfo s_info = s_db.loadByUid(uid);
			if (s_info == null) {
				log.error("no send info {}", uid);
				return respond;
			}
			SendQuest my_quest = s_info.qs.get(quest_id);
			if (my_quest == null) {
				log.error("no quest_info  ply {} idx {}", uid, quest_id);
				return respond;
			}
			if (battle_result.m_result == BattleResult.LEFT_WIN && my_quest.cnt_be_rob < ConfigConstant.tConf.getPlundered()) {
				++my_quest.cnt_be_rob;
				my_quest.r_ids.add(ene_id);
				
				for (int i = 0; i < s_info.enemys.size(); ++i) {
					String id = s_info.enemys.get(i);
					if (id.equals(ene_id)){
						for (int j = i + 1; j < s_info.enemys.size(); ++j, ++i) {
							s_info.enemys.set(i, s_info.enemys.get(j));
						}
						s_info.enemys.remove(i);
						break;
					}
				}
				s_info.enemys.add(ene_id);
				if (s_info.enemys.size() >= MAX_ENEMY) {
					s_info.enemys.remove(0);
				}
				s_db.save();
			}
			
			PlayerSendRcdDB rcd_db = SpringContextUtil.getBean(PlayerSendRcdDB.class);
			PlayerSendRcdInfo rcd_info = rcd_db.loadByUid(uid);
			if (rcd_info == null) {
				log.error("no send rcd info {}", uid);
				return respond;
			}
			if (rcd_info.rcds.size() >= MAX_RCD) {
				rcd_info.rcds.remove(0);
			}
			PlayerSendRcdInfo.Rcd rcd = rcd_info.new Rcd();
			rcd_info.rcds.add(rcd);
			rcd.send_id = my_quest.send_id;
			rcd.result = battle_result.m_result;
			rcd.type = my_quest.type;
			rcd.atk_uid = ene_id;
			rcd.atk_name = receive.body.send.name;
			rcd.atk_zid = receive.body.send.zid;
			rcd.atk_roles = new ArrayList<Integer>();
			rcd.def_roles = new ArrayList<Integer>();
			for (int i = 0; i < battle_result.m_actors_data.size(); ++i){
				ActorLogic a = battle_result.m_actors_data.get(i);
				if (a.m_side == BattleSide.Left) {
					rcd.atk_roles.add(a.m_r_inf.role_id);
				} else {
					rcd.def_roles.add(a.m_r_inf.role_id);
				}
			}
			rcd.def_uid = uid;
			rcd.def_name = p_info.user_base.getNickname();
			rcd.def_zid = p_info.user_base.zid;
			rcd.pk = JsonTransfer.getJson(battle_result);
			rcd_db.save();
			respond.header.rt = 0;
			sendCheckInToHub(uid, quest_id, p_info, my_quest);	
			return respond;
		} catch (Exception e) {
			e.printStackTrace();
			return respond;
		} finally {
			lock.unlock();
		}
	}

	public CommonMsg handleLogList(CommonMsg respond, CommonMsg receive) {
		String uid = receive.header.uid;
		SendMsg msg = new SendMsg();
		msg.success = false;
		respond.body.send = msg;
		if (isHub()){
			log.error("is hub");
			return respond;
		}
		
		PlayerSendRcdDB rcd_db = SpringContextUtil.getBean(PlayerSendRcdDB.class);
		PlayerSendRcdInfo rcd_info = rcd_db.loadByUid(uid);
		if (rcd_info == null) {
			log.error("no send rcd info {}", uid);
			return respond;
		}
		
		msg.logs = new ArrayList<SendRobLogMsg>();
		for (int i = rcd_info.rcds.size() - 1; i >= 0; --i) {
			SendRobLogMsg m = new SendRobLogMsg();
			msg.logs.add(m);
			PlayerSendRcdInfo.Rcd r = rcd_info.rcds.get(i);
			m.atk_name = r.atk_name;
			m.atk_roles = r.atk_roles;
			m.atk_uid = r.atk_uid;
			m.atk_zid = r.atk_zid;
			m.def_name = r.def_name;
			m.def_roles = r.def_roles;
			m.def_uid = r.def_uid;
			m.def_zid = r.def_zid;
			m.result = r.result;
			m.send_id = r.send_id;
			m.type = r.type;
		}
		msg.success = true;
		return respond;
	}

	public CommonMsg handleLogReplay(CommonMsg respond, CommonMsg receive) {
		String uid = receive.header.uid;
		SendMsg msg = new SendMsg();
		msg.success = false;
		respond.body.send = msg;
		if (isHub()){
			log.error("is hub");
			return respond;
		}
		int log_id = receive.body.send.log_id; //注意，是反向的。因为下发的时候根据时间倒转了
		PlayerSendRcdDB rcd_db = SpringContextUtil.getBean(PlayerSendRcdDB.class);
		PlayerSendRcdInfo rcd_info = rcd_db.loadByUid(uid);
		if (rcd_info == null) {
			log.error("no send rcd info {}", uid);
			return respond;
		}
		int get_id = rcd_info.rcds.size() - log_id - 1;
		if (get_id < 0 || get_id >= rcd_info.rcds.size()) {
			log.error("log id error {}", log_id, uid);
			return respond;
		}
		PlayerSendRcdInfo.Rcd r = rcd_info.rcds.get(get_id);
		if (r == null) {
			log.error("no rcd {} player {}", get_id, uid);
			return respond;
		}
		msg.battle = JsonTransfer._In(r.pk, BattlePack.class);		
		msg.success = true;
		return respond;
	}

	public CommonMsg handleAvatar(CommonMsg respond, CommonMsg receive) {
		SendMsg msg = new SendMsg();
		msg.success = false;
		respond.body.send = msg;
		if (isHub()){
			log.error("is hub");
			return respond;
		}
		int ene_zid = receive.body.send.zid;
		Hubmap h_cfg = ConfigConstant.tHubmap.get(ene_zid);
		if (h_cfg == null) {
			log.error("no hub zid {}", ene_zid);
			return respond;
		}
		String ene_uid = receive.body.send.uid;
		CommonMsg result_obj = null;
		try {
			CommonMsg send = new CommonMsg(0, ene_uid);
			String result = HttpRequest.PostFunction("http://"+ h_cfg.getURL() + I_DefMoudle.HUB_G2G_SEND_AVATAR, JsonTransfer.getJson(send));
			result_obj = JsonTransfer._In(result, CommonMsg.class);
			if (result_obj.header.rt != 0) {
				log.error("avatar error");
				return respond;
			}
			msg.avatar = result_obj.body.send.avatar;
			msg.success = true;
			return respond;
		} catch (Exception e){
			e.printStackTrace();
			return respond;
		}
	}

	public CommonMsg handleHubGGAvatar(CommonMsg respond, CommonMsg receive) {
		String uid = receive.header.uid;
		SendMsg msg = new SendMsg();
		respond.header.rt = 1;
		respond.body.send = msg;
		if (isHub()){
			log.error("is hub");
			return respond;
		}
		
		PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
		PlayerInfo p_info = p_db.loadById(uid);
		if (p_info == null) {
			log.error("no p info {}", uid);
			return respond;
		}
		respond.body.send.avatar = p_info.user_base.getuImg();
		respond.header.rt = 0;
		return respond;
	}
}

