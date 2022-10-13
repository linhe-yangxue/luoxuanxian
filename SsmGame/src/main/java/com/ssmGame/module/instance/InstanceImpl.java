package com.ssmGame.module.instance;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Dailytask;
import com.ssmData.config.entity.Fbtype;
import com.ssmData.config.entity.Instance;
import com.ssmData.config.entity.Role;
import com.ssmData.config.entity.Scroll;
import com.ssmData.config.entity.Vip;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerInstanceDB;
import com.ssmData.dbase.PlayerInstanceInfo;
import com.ssmData.dbase.PlayerLevelDB;
import com.ssmData.dbase.PlayerLevelInfo;
import com.ssmData.dbase.PlayerRolesInfo;
import com.ssmData.dbase.PlayerRolesInfoDB;
import com.ssmData.dbase.PlayerScrollDB;
import com.ssmData.dbase.PlayerScrollInfo;
import com.ssmData.dbase.RoleInfo;
import com.ssmData.dbase.ScrollInfo;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.instance.InstanceMsg;
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
import com.ssmGame.module.player.PlayerImpl;
import com.ssmGame.module.task.TaskImpl;
import com.ssmGame.module.task.TaskType;
import com.ssmGame.util.AwardUtils;
import com.ssmGame.util.ItemCountPair;
import com.ssmGame.util.TimeUtils;

@Service
@Scope("prototype")
public class InstanceImpl {
	private static final Logger log = LoggerFactory.getLogger(InstanceImpl.class);
	
	public final static InstanceImpl getInstance(){
        return SpringContextUtil.getBean(InstanceImpl.class);
    }
	
	@Autowired
	PlayerInstanceDB m_instance_db;
	PlayerInstanceInfo m_instance_info = null;
	
    @Autowired
    PlayerRolesInfoDB m_roles_db;
    PlayerRolesInfo m_roles = null;

    @Autowired
    PlayerInfoDB m_player_db;
    PlayerInfo m_player = null;
    
    @Autowired
    PlayerScrollDB m_scroll_db;
    PlayerScrollInfo m_scroll = null;
    
    @Autowired
    PlayerBagDB m_bag_db;
    PlayerBagInfo m_bag = null;
    
    @Autowired
    PlayerLevelDB m_level_db;
    PlayerLevelInfo m_level = null;
    
	
	public InstanceImpl init(String player_id)
	{
		m_instance_info = m_instance_db.loadByUid(player_id);
		m_roles = m_roles_db.load(player_id);
		m_player = m_player_db.loadById(player_id);
		m_scroll = m_scroll_db.loadByUid(player_id);
		m_bag = m_bag_db.loadByUid(player_id);
		m_level = m_level_db.loadByUid(player_id);
		
		if (null == m_instance_info)
		{
			m_instance_info = (PlayerInstanceInfo)m_instance_db.createDB(player_id);
		}
		
		return this;
	}
	
	public CommonMsg handleLogin(CommonMsg respond)
	{
		RefreshCount(Calendar.getInstance(), TimeUtils.TodayStart());
		PlayerInstanceInfo msg = new PlayerInstanceInfo();
		msg.challenge_buy_cnt = m_instance_info.challenge_buy_cnt;
		UpdateNewOpen();
		msg.far_win_id = m_instance_info.far_win_id;
		msg.farest_id = m_instance_info.farest_id;
		msg.instance_count = m_instance_info.instance_count;
		msg.last_reset_time = m_instance_info.last_reset_time;
		respond.body.playerLogin.instance = msg;
		return respond;
	}
	
	private void UpdateNewOpen(){
		Map<Integer, Fbtype> fb = ConfigConstant.tFbtype;
		for (Entry<Integer, Fbtype> f : fb.entrySet()) {
			if (m_instance_info.far_win_id.containsKey(f.getKey())){
				int far_win = m_instance_info.far_win_id.get(f.getKey());
				int far_open = m_instance_info.farest_id.get(f.getKey());
				if (far_win == far_open){
					Instance i = ConfigConstant.tInstance.get(far_open);
					if (i != null){
						int next = i.getLowLevel();
						if (next != -1 && next != far_open) {
							m_instance_info.farest_id.put(f.getKey(), next);
						}
					}
				}
			}
		}
		m_instance_db.save();
	}
	
	public void destroy()
	{
		m_instance_info = null;
		m_instance_db = null;
		m_roles = null;
		m_roles_db = null;
		m_player = null;
		m_player_db = null;
		m_scroll = null;
		m_scroll_db = null;
		m_bag_db = null;
		m_bag = null;
		m_level = null;
		m_level_db = null;
	}
	
	//购买挑战次数
	public CommonMsg buyChallenge(CommonMsg respond, int instance_id)
	{
		InstanceMsg i_msg = new InstanceMsg();
		respond.body.instance = i_msg;
		i_msg.success = false;
		RefreshAll();
		
		Vip vip = ConfigConstant.tVip.get(m_player.vip_level);
		if (null == vip)
		{
			log.info("buyChallenge() player {} vip error {}", m_player._id, m_player.vip_level);
			return respond;
		}
		
		/*if (vip.getFBCNext() <= m_instance_info.challenge_buy_cnt)
		{
			respond.header.rt_sub = 1115;
			return respond;
		}*/
		
		double money = ConfigConstant.tConf.getFBFprice() 
				+ ConfigConstant.tConf.getOverlayPrice() * (m_instance_info.challenge_buy_cnt + 1);
		if (!m_player.hasDiamond(money))
		{
			return respond;
		}
		
		if (!m_instance_info.instance_count.containsKey(instance_id))
		{
			log.info("buyChallenge() player {} no instance id {}", m_player._id, instance_id);
			return respond;
		}
		
		int count_left = m_instance_info.instance_count.get(instance_id);
		if (count_left > 0)
		{
			respond.header.rt_sub = 1108;
			return respond;
		}
		PlayerImpl.SubDiamond(m_player, money);
		m_player_db.save();
		respond.body.sync_player_info = new SyncPlayerInfoMsg();
		respond.body.sync_player_info.diamond = -money;
		
		m_instance_info.challenge_buy_cnt++;
		m_instance_info.instance_count.put(instance_id, ConfigConstant.tConf.getBuyQuantity());
		m_instance_db.save();
		
		respond.body.sync_instance = new PlayerInstanceInfo();
		respond.body.sync_instance.challenge_buy_cnt = m_instance_info.challenge_buy_cnt;
		respond.body.sync_instance.instance_count = new HashMap<Integer, Integer>();
		respond.body.sync_instance.instance_count.put(instance_id, ConfigConstant.tConf.getBuyQuantity());
		i_msg.success = true;
		return respond;
	}
	
	//挑战副本
	public CommonMsg challenge(CommonMsg respond, int instance_id)
	{
		InstanceMsg i_msg = new InstanceMsg();
		respond.body.instance = i_msg;
		i_msg.success = false;
		RefreshAll();
		
    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
		
		Instance instance_cfg = ConfigConstant.tInstance.get(instance_id);
		if (null == instance_cfg)
		{
			log.info("challenge() player {} no instance_cfg id {}", m_player._id, instance_id);
			return respond;
		}
		
		Fbtype fbt_cfg = ConfigConstant.tFbtype.get(instance_cfg.getType());
		if (null == fbt_cfg)
		{
			log.info("challenge() player {} no fbt_cfg id {}", m_player._id, instance_cfg.getType());
			return respond;
		}
		
		Scroll s_cfg = ConfigConstant.tScroll.get(fbt_cfg.getConsume());
		if (null == s_cfg)
		{
			log.info("challenge() player {} no s_cfg id {}", m_player._id, fbt_cfg.getConsume());
			return respond;
		}
		
		ScrollInfo s_info = m_scroll.Get(s_cfg.getID());
		if (null == s_info)
		{
			s_info = new ScrollInfo();
			s_info.InitByConfig(s_cfg.getID());
			m_scroll.scroll_list.add(s_info);
			m_scroll_db.save();
		}
		
		if (s_info.count < instance_cfg.getCScroll())
		{
			if (!m_bag.hasItemCount(s_cfg.getItem(), instance_cfg.getCScroll())) {
				respond.header.rt_sub = 1109;
				return respond;
			}
		}
		
		//开新关卡
		if (!m_instance_info.instance_count.containsKey(instance_id))
		{
			//等级判断
			if (fbt_cfg.getOpenLv() > m_player.team_lv || instance_cfg.getOpenLevel() >= m_level.cur_level)
			{
				log.info("challenge() player {} inst id {} lv low", m_player._id, instance_cfg.getID());
				respond.header.rt_sub = 20001;
				return respond;
			}
			
			//进度判断
			Instance found_far_cfg = null;
			if (m_instance_info.farest_id.containsKey(fbt_cfg.getID()))
			{
				Instance far_cfg = ConfigConstant
						.tInstance.get(m_instance_info.farest_id.get(fbt_cfg.getID()));
				if (far_cfg == null)
				{
					log.info("challenge() player {} checek far inst id  ERROR", m_player._id);
					return respond;
				}
				found_far_cfg = far_cfg;
			}
			else
			{
				found_far_cfg = ConfigConstant.tInstance.get(fbt_cfg.getFbinitial());
				if (null == found_far_cfg)
				{
					log.info("challenge() player {} checek 1st inst id {} ERROR", m_player._id, fbt_cfg.getFbinitial());
					return respond;
				}
				m_instance_info.farest_id.put(fbt_cfg.getID(), found_far_cfg.getID());
				m_instance_info.instance_count.put(found_far_cfg.getID(), found_far_cfg.getFBchallenge());
				m_instance_db.save();
			}
			if (found_far_cfg.getID() < instance_id)
			{
				log.error("far is {} req is {}", found_far_cfg.getID(), instance_id);
				return respond;
			}
		}
		/*if (m_instance_info.instance_count.get(instance_id) <= 0)
		{
			respond.header.rt_sub = 1108;
			return respond;
		}*/
		
		// 生成双方阵容
        Map<Integer, RoleInfo> my_roles = m_roles.genRoles();
        Map<Integer, RoleInfo> enemy_roles = genEnemyRoles(instance_cfg);
        
        BattleSimulator sim = new BattleSimulator();
        sim.InitAllActorLogic(my_roles, enemy_roles, instance_cfg.getTime(), ConfigConstant.tConf.getBattletime(), BattleType.LEVEL_BOSS
        		, null, null, null, null);
        BattlePack battle_result = sim.Exe();
        DmgRewardImpl.RefreshMax(m_player._id, battle_result);
        
        if (battle_result.m_result == BattleResult.LEFT_WIN)
        {
        	m_instance_info.reward_hash = RandomStringUtils.randomAlphabetic(16);
        	m_instance_info.reward_instance_id = instance_id;
        	i_msg.reward_hash = m_instance_info.reward_hash;
        }
        
        i_msg.battle_data = battle_result;
        
        m_instance_db.save();
        i_msg.success = true;
        
        //日常任务
        int type = 0;
        //主线任务
        int main_task_type = 0;
        int main_task_has_type = 0;
        if (fbt_cfg.getType() == InstanceType.Equip)
        {
        	type = DailyTaskType.INSTANCE_Equip;
        	main_task_type = TaskType.INSTANCE_EQUIP;
        	main_task_has_type = TaskType.HAS_INSTANCE_EQUIP;
        }
        else if (fbt_cfg.getType() == InstanceType.Gold)
        {
        	type = DailyTaskType.INSTANCE_Gold;
        	main_task_type = TaskType.INSTANCE_GOLD;
        	main_task_has_type = TaskType.HAS_INSTANCE_GOLD;
        }
        else if (fbt_cfg.getType() == InstanceType.Hero)
        {
        	type = DailyTaskType.INSTANCE_Hero;
        	main_task_type = TaskType.INSTANCE_HERO;
        	main_task_has_type = TaskType.HAS_INSTANCE_HERO;
        }
        else if (fbt_cfg.getType() == InstanceType.Matiral)
        {
        	type = DailyTaskType.INSTANCE_Mat;
        	main_task_type = TaskType.INSTANCE_MAT;
        	main_task_has_type = TaskType.HAS_INSTANCE_MAT;
        }
        //日常
        Dailytask d_cfg = DailyTaskImpl.getCfg(type);
        int add_result = DailyTaskImpl.AddDaily(d_cfg, 1, respond.header.uid);
        if (d_cfg != null && add_result != 0)
        {
        	int id = ConfigConstant.tConf.getCharmItem();
        	int cnt = d_cfg.getTaskReward() * add_result;
        	m_bag.addItemCount(id, cnt);
        	m_bag_db.save();
        	respond.body.sync_bag = new SyncBagMsg();
        	respond.body.sync_bag.items.put(id, m_bag.getItemCount(id));
        }
        //主线
        TaskImpl.doTask(m_roles.player_info_id, main_task_type, 1);
        TaskImpl.doTask(m_roles.player_info_id, main_task_has_type, instance_id);
        
        // 记录最后战斗时间
        m_player.last_active_time = Calendar.getInstance().getTimeInMillis();
        m_player_db.save();
        
        return respond;
	}
	
	//请求奖励
	public CommonMsg reqReward(CommonMsg respond, String reward_hash)
	{
		InstanceMsg i_msg = new InstanceMsg();
		respond.body.instance = i_msg;
		i_msg.success = false;
		RefreshAll();
		
		if (!m_instance_info.reward_hash.trim().equals(reward_hash))
		{
			log.info("reqReward() reward hash not equal player{}", m_player._id);
            log.info("receive hash：" + reward_hash);
            log.info("db hash：" + m_instance_info.reward_hash);
			return respond;
		}
		
		int instance_id = m_instance_info.reward_instance_id;
		
		Instance instance_cfg = ConfigConstant.tInstance.get(instance_id);
		if (null == instance_cfg)
		{
			log.info("reqReward() player {} no instance_cfg id {}", m_player._id, instance_id);
			return respond;
		}
		
		Fbtype fbt_cfg = ConfigConstant.tFbtype.get(instance_cfg.getType());
		if (null == fbt_cfg)
		{
			log.info("reqReward() player {} no fbt_cfg id {}", m_player._id, instance_cfg.getType());
			return respond;
		}
		
		Scroll s_cfg = ConfigConstant.tScroll.get(fbt_cfg.getConsume());
		if (null == s_cfg)
		{
			log.info("reqReward() player {} no s_cfg id {}", m_player._id, fbt_cfg.getConsume());
			return respond;
		}
		
		ScrollInfo s_info = m_scroll.Get(s_cfg.getID());
		if (null == s_info)
		{
			log.info("reqReward() player {} no s_info id {}", m_player._id, s_cfg.getID());
			return respond;
		}
				
		boolean use_scroll = true;
		if (s_info.count < instance_cfg.getCScroll())
		{
			use_scroll = false;
			if (!m_bag.hasItemCount(s_cfg.getItem(), instance_cfg.getCScroll())) {
				respond.header.rt_sub = 1109;
				return respond;
			}
		}
			
		
		/*if (m_instance_info.instance_count.get(instance_id) <= 0)
		{
			respond.header.rt_sub = 1108;
			return respond;
		}*/
		
		int type_id = fbt_cfg.getID();
		
		if (!m_instance_info.farest_id.containsKey(type_id))
			return respond;
		int far_id = m_instance_info.farest_id.get(type_id);
		if (far_id < instance_id)
			return respond;
		
		respond.body.sync_instance = new PlayerInstanceInfo();
		respond.body.sync_instance.instance_count = new HashMap<Integer, Integer>();
		if (far_id == instance_id && instance_cfg.getLowLevel() != -1)
		{
			Instance next_cfg = ConfigConstant.tInstance.get(instance_cfg.getLowLevel());
			if (next_cfg == null)
				return respond;
			m_instance_info.farest_id.put(type_id, next_cfg.getID());
			m_instance_info.instance_count.put(next_cfg.getID(), next_cfg.getFBchallenge());
			
			respond.body.sync_instance.farest_id = new HashMap<Integer, Integer>();
			respond.body.sync_instance.farest_id.put(type_id, next_cfg.getID());
			respond.body.sync_instance.instance_count.put(next_cfg.getID(), next_cfg.getFBchallenge());
		}
				
		if (!m_instance_info.far_win_id.containsKey(type_id))
		{
			m_instance_info.far_win_id.put(type_id, instance_id);
			
			respond.body.sync_instance.far_win_id = new HashMap<Integer, Integer>(); 
			respond.body.sync_instance.far_win_id.put(type_id, instance_id);
		}
		else if (m_instance_info.far_win_id.get(type_id) < instance_id)
		{
			m_instance_info.far_win_id.put(type_id, instance_id);
			
			respond.body.sync_instance.far_win_id = new HashMap<Integer, Integer>(); 
			respond.body.sync_instance.far_win_id.put(type_id, instance_id);
		}
		
		//m_instance_info.instance_count.put(instance_id, m_instance_info.instance_count.get(instance_id) - 1);
		m_instance_db.save();
		respond.body.sync_instance.instance_count.put(instance_id, m_instance_info.instance_count.get(instance_id));
		
		Map<Integer, Integer> sync_bags = new HashMap<Integer, Integer>();
		if (use_scroll) {
			s_info.count -= instance_cfg.getCScroll();
			m_scroll_db.save();
			respond.body.sync_scroll = new PlayerScrollInfo();
			respond.body.sync_scroll.scroll_list = new ArrayList<ScrollInfo>();
			respond.body.sync_scroll.scroll_list.add(s_info);
		}
		else {
			m_bag.subItemCount(s_cfg.getItem(), instance_cfg.getCScroll());
			sync_bags.put(s_cfg.getItem(), m_bag.getItemCount(s_cfg.getItem()));
		}
		
		List<ItemCountPair> r_bag = AwardUtils.GetAward(instance_cfg.getAward());
		SyncBagItem[] r_bag_items = new SyncBagItem[r_bag.size()];
		int j = 0;
		for (ItemCountPair i : r_bag)
		{
			if (m_bag.addItemCount(i.m_item_id, i.m_count))
			{
				sync_bags.put(i.m_item_id, m_bag.getItemCount(i.m_item_id));
	            SyncBagItem item = new SyncBagItem();
	            item.id = i.m_item_id;
	            item.count = i.m_count;
	            r_bag_items[j++] = item;
			}
		}
		m_bag_db.save();
		respond.body.sync_bag = new SyncBagMsg();
		respond.body.sync_bag.items = sync_bags;
		
        // 记录最后战斗时间
        m_player.last_active_time = Calendar.getInstance().getTimeInMillis();
		
		int r_gold = instance_cfg.getGold();
		m_player.addGold(r_gold);
		
		int r_exp = instance_cfg.getExp();
		boolean is_levelup = PlayerImpl.addExp(m_player, r_exp);
		m_player_db.save();
		
        if(is_levelup){
        	respond.header.rt_msg = 10001;
        	respond.body.sync_player_info = this.getSyncPlayerInfoMsg();
        }else{
        	respond.body.sync_player_info = new SyncPlayerInfoMsg(false);
        	respond.body.sync_player_info.exp = r_exp;
        	respond.body.sync_player_info.gold = r_gold;
        }
        
        //公告
        if (instance_cfg.getNotice() == 1)
        {
        	String context = ConfigConstant.tWordconfig.get(BroadcastImpl.INSTANCE_PASS).getWord_cn();
        	context = context.replace("$1", m_player.user_base.getNickname());
        	context = context.replace("$2", ConfigConstant.tWordconfig.get(instance_cfg.getName()).getWord_cn());
        	BroadcastImpl bi = BroadcastImpl.getInstance();
        	bi.SendBrocast(context, m_player.user_base.gid, m_player.user_base.zid);
        }
		
		i_msg.success = true;
		i_msg.gold = r_gold;
		i_msg.exp = r_exp;
		i_msg.bag_items = r_bag_items;
		return respond;
	}
	
	//刷新数据
	public CommonMsg info(CommonMsg respond)
	{
		RefreshAll();
		PlayerInstanceInfo i = new PlayerInstanceInfo();
		i.challenge_buy_cnt = m_instance_info.challenge_buy_cnt;
		i.far_win_id = m_instance_info.far_win_id;
		i.farest_id = m_instance_info.farest_id;
		i.instance_count = m_instance_info.instance_count;
		i.last_reset_time = m_instance_info.last_reset_time;
		return respond;
	}
	
	//扫荡
	public CommonMsg raid(CommonMsg respond, int instance_id)
	{
		InstanceMsg i_msg = new InstanceMsg();
		respond.body.instance = i_msg;
		i_msg.success = false;
		RefreshAll();
		
    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
		
		Instance instance_cfg = ConfigConstant.tInstance.get(instance_id);
		if (null == instance_cfg)
		{
			log.info("raid() player {} no instance_cfg id {}", m_player._id, instance_id);
			return respond;
		}
		
		Fbtype fbt_cfg = ConfigConstant.tFbtype.get(instance_cfg.getType());
		if (null == fbt_cfg)
		{
			log.info("raid() player {} no fbt_cfg id {}", m_player._id, instance_cfg.getType());
			return respond;
		}
		
		Scroll s_cfg = ConfigConstant.tScroll.get(fbt_cfg.getConsume());
		if (null == s_cfg)
		{
			log.info("raid() player {} no s_cfg id {}", m_player._id, fbt_cfg.getConsume());
			return respond;
		}
		
		ScrollInfo s_info = m_scroll.Get(s_cfg.getID());
		if (null == s_info)
		{
			log.info("raid() player {} no s_info id {}", m_player._id, s_cfg.getID());
			return respond;
		}
		
		boolean use_scroll = true;
		if (s_info.count < instance_cfg.getCScroll())
		{
			use_scroll = false;
			if (!m_bag.hasItemCount(s_cfg.getItem(), instance_cfg.getCScroll())) {
				respond.header.rt_sub = 1109;
				return respond;
			}
		}
		
		/*if (m_instance_info.instance_count.get(instance_id) <= 0)
		{
			respond.header.rt_sub = 1108;
			return respond;
		}*/	
		
		int type_id = fbt_cfg.getID();
		if (!m_instance_info.far_win_id.containsKey(type_id))
		{
			respond.header.rt_sub = 20001;
			return respond;
		}
		
		if (m_instance_info.far_win_id.get(type_id) < instance_id)
		{
			respond.header.rt_sub = 20001;
			return respond;
		}
		
		/*Vip vip = ConfigConstant.tVip.get(m_player.vip_level);
		if (null == vip)
		{
			log.info("raid() player {} vip error {}", m_player._id, m_player.vip_level);
			return respond;
		}
		
		if (vip.getID() < ConfigConstant.tConf.getMUPVip())
		{
			respond.header.rt_sub = 20002;
			return respond;
		}*/
		
		//m_instance_info.instance_count.put(instance_id, m_instance_info.instance_count.get(instance_id) - 1);
		m_instance_db.save();
		respond.body.sync_instance = new PlayerInstanceInfo();
		respond.body.sync_instance.instance_count = new HashMap<Integer, Integer>();
		respond.body.sync_instance.instance_count.put(instance_id, m_instance_info.instance_count.get(instance_id));
		
		Map<Integer, Integer> sync_bags = new HashMap<Integer, Integer>();
		if (use_scroll) {
			s_info.count -= instance_cfg.getCScroll();
			m_scroll_db.save();
			respond.body.sync_scroll = new PlayerScrollInfo();
			respond.body.sync_scroll.scroll_list = new ArrayList<ScrollInfo>();
			respond.body.sync_scroll.scroll_list.add(s_info);
		}
		else {
			m_bag.subItemCount(s_cfg.getItem(), instance_cfg.getCScroll());
			sync_bags.put(s_cfg.getItem(), m_bag.getItemCount(s_cfg.getItem()));
		}
		
		List<ItemCountPair> r_bag = AwardUtils.GetAward(instance_cfg.getAward());
		SyncBagItem[] r_bag_items = new SyncBagItem[r_bag.size()];
		int j = 0;
		for (ItemCountPair i : r_bag)
		{
			if (m_bag.addItemCount(i.m_item_id, i.m_count))
			{
				sync_bags.put(i.m_item_id, m_bag.getItemCount(i.m_item_id));
	            SyncBagItem item = new SyncBagItem();
	            item.id = i.m_item_id;
	            item.count = i.m_count;
	            r_bag_items[j++] = item;
			}
		}
		respond.body.sync_bag = new SyncBagMsg();
		respond.body.sync_bag.items = sync_bags;
		
		//日常任务
        int type = 0;
        //主线任务
        int main_task_type = 0;
        int main_task_has_type = 0;
        if (fbt_cfg.getType() == InstanceType.Equip)
        {
        	type = DailyTaskType.INSTANCE_Equip;
        	main_task_type = TaskType.INSTANCE_EQUIP;
        	main_task_has_type = TaskType.HAS_INSTANCE_EQUIP;
        }
        else if (fbt_cfg.getType() == InstanceType.Gold)
        {
        	type = DailyTaskType.INSTANCE_Gold;
        	main_task_type = TaskType.INSTANCE_GOLD;
        	main_task_has_type = TaskType.HAS_INSTANCE_GOLD;
        }
        else if (fbt_cfg.getType() == InstanceType.Hero)
        {
        	type = DailyTaskType.INSTANCE_Hero;
        	main_task_type = TaskType.INSTANCE_HERO;
        	main_task_has_type = TaskType.HAS_INSTANCE_HERO;
        }
        else if (fbt_cfg.getType() == InstanceType.Matiral)
        {
        	type = DailyTaskType.INSTANCE_Mat;
        	main_task_type = TaskType.INSTANCE_MAT;
        	main_task_has_type = TaskType.HAS_INSTANCE_MAT;
        }
        Dailytask d_cfg = DailyTaskImpl.getCfg(type);
        int add_result = DailyTaskImpl.AddDaily(d_cfg, 1, respond.header.uid);
        if (d_cfg != null && add_result != 0)
        {
        	int id = ConfigConstant.tConf.getCharmItem();
        	int cnt = d_cfg.getTaskReward() * add_result;
        	m_bag.addItemCount(id, cnt);
        	respond.body.sync_bag.items.put(id, m_bag.getItemCount(id));
        }
        m_bag_db.save();
        //主线
        TaskImpl.doTask(m_roles.player_info_id, main_task_type, 1);
        TaskImpl.doTask(m_roles.player_info_id, main_task_has_type, instance_id);
		
		int r_gold = instance_cfg.getGold();
		m_player.addGold(r_gold);
		
		int r_exp = instance_cfg.getExp();
		boolean is_levelup = PlayerImpl.addExp(m_player, r_exp);
		m_player_db.save();
		
        if(is_levelup){
        	respond.header.rt_msg = 10001;
        	respond.body.sync_player_info = this.getSyncPlayerInfoMsg();
        }else{
        	respond.body.sync_player_info = new SyncPlayerInfoMsg(false);
        	respond.body.sync_player_info.exp = r_exp;
        	respond.body.sync_player_info.gold = r_gold;
        }
		
		i_msg.success = true;
		i_msg.gold = r_gold;
		i_msg.exp = r_exp;
		i_msg.bag_items = r_bag_items;
		return respond;
	}
	
	//上线
	public void OnLogin()
	{
		RefreshAll();
		m_instance_info.reward_hash = "";
		m_instance_info.reward_instance_id = -1;
		m_instance_db.save();
	}
	
	private void RefreshAll()
	{
		Calendar cal = Calendar.getInstance();
		long today_start = TimeUtils.TodayStart();
		List<Integer> scl = new ArrayList<Integer>();
		for (Fbtype f : ConfigConstant.tFbtype.values())
		{
			if (scl.contains(f.getConsume()))
				continue;
			scl.add(f.getConsume());
		}
		for (int i : scl)
		{
			ScrollInfo s = m_scroll.Get(i);
			if (s != null)
				s.Refresh(cal, today_start);
		}
		
		RefreshCount(cal, today_start);
	}
	
	private void RefreshCount(Calendar now_cal, long today_start)
	{
		long now = now_cal.getTimeInMillis();
		int now_day = now_cal.get(Calendar.DAY_OF_MONTH);
		now_cal.setTimeInMillis(m_instance_info.last_reset_time);
		int last_day = now_cal.get(Calendar.DAY_OF_MONTH);
		long reset_time = ConfigConstant.tConf.getRTime() * 60 * 60 * 1000 + today_start;
		
		if (reset_time > m_instance_info.last_reset_time && now_day != last_day)
		{
			for (Entry<Integer, Integer> i : m_instance_info.instance_count.entrySet())
			{
				Instance config = ConfigConstant.tInstance.get(i.getKey());
				if (null != config && i.getValue() < config.getFBchallenge())
				{
					i.setValue(config.getFBchallenge());
				}
			}
			
			//顺便清空购买次数
			m_instance_info.challenge_buy_cnt = 0;
			m_instance_info.last_reset_time = now;
		}
	}
	
	private static Map<Integer, RoleInfo> genEnemyRoles(Instance cfg)
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
        	r.skill_lv = ConfigConstant.tRole.get(cfg.getMon()[i]).getSkillLv(); // 特殊技能等级
        	enemy_team.put(i+1, r);
        }

        return enemy_team;
	}
	
    private SyncPlayerInfoMsg getSyncPlayerInfoMsg(){
        SyncPlayerInfoMsg sync_player_info = new SyncPlayerInfoMsg(true);
        sync_player_info.gold = m_player.gold;
        sync_player_info.diamond = m_player.diamond;
        sync_player_info.exp = m_player.team_exp;
        sync_player_info.team_lv = m_player.team_lv;
        sync_player_info.team_current_fighting = m_player.team_current_fighting;
		sync_player_info.vip_level = m_player.vip_level;
		sync_player_info.last_gold_time = m_player.last_gold_time;
		sync_player_info.gold_buy_cnt = m_player.gold_buy_cnt;

        return sync_player_info;
    }
}
