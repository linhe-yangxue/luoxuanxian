package com.ssmGame.module.task;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Instance;
import com.ssmData.config.entity.Task;
import com.ssmData.dbase.EquipInfo;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerDailyTaskDB;
import com.ssmData.dbase.PlayerDailyTaskInfo;
import com.ssmData.dbase.PlayerDmgRewardDB;
import com.ssmData.dbase.PlayerDmgRewardInfo;
import com.ssmData.dbase.PlayerDrawCardDB;
import com.ssmData.dbase.PlayerDrawCardInfo;
import com.ssmData.dbase.PlayerDuelDB;
import com.ssmData.dbase.PlayerDuelInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerInstanceDB;
import com.ssmData.dbase.PlayerInstanceInfo;
import com.ssmData.dbase.PlayerLevelDB;
import com.ssmData.dbase.PlayerLevelInfo;
import com.ssmData.dbase.PlayerLineupDB;
import com.ssmData.dbase.PlayerLineupInfo;
import com.ssmData.dbase.PlayerRolesInfo;
import com.ssmData.dbase.PlayerRolesInfoDB;
import com.ssmData.dbase.PlayerTaskDB;
import com.ssmData.dbase.PlayerTaskInfo;
import com.ssmData.dbase.PlayerTowerDB;
import com.ssmData.dbase.PlayerTowerInfo;
import com.ssmData.dbase.PlayerWishDB;
import com.ssmData.dbase.PlayerWishInfo;
import com.ssmData.dbase.RoleInfo;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
import com.ssmGame.defdata.msg.task.TaskMsg;


@Service
@Scope("prototype")
public class TaskImpl {
	private static final Logger log = LoggerFactory.getLogger(TaskImpl.class);
	
	public final static TaskImpl getInstance(){
        return SpringContextUtil.getBean(TaskImpl.class);
	}
	
	@Autowired
	PlayerTaskDB m_task_db;
	PlayerTaskInfo m_task;
	
    @Autowired
    PlayerInfoDB m_player_db;
    PlayerInfo m_player = null;
    
    @Autowired
    PlayerBagDB m_bag_db;
    PlayerBagInfo m_bag = null;
	
	public void destroy()
	{
		m_task_db = null;
		m_task = null;
		m_player = null;
		m_player_db = null;
		m_bag_db = null;
		m_bag = null;
	}
	
	private TaskMsg initMsg(CommonMsg respond)
	{
		respond.body.task = new TaskMsg();
		respond.body.task.success = false;
		return respond.body.task;
	}
	
	public CommonMsg handleInfo(CommonMsg respond)
	{
		String uid = respond.header.uid;
		TaskMsg msg = initMsg(respond);
		
		m_task = m_task_db.loadByUid(uid);
		if (m_task == null)
		{
			m_task = (PlayerTaskInfo)m_task_db.createDB(uid);
		}
		if (m_task.cur_id == -1)
		{
			m_task.cur_id = 1;
			m_task.arg = 0;
			m_task.is_finish = false;
			checkTaskWhenActive(m_task);
			m_task_db.save();
		}
		
		msg.cur_id = m_task.cur_id;
		msg.is_finish = m_task.is_finish;
		msg.arg = m_task.arg;
		
		msg.success = true;
		return respond;
	}
	
	public CommonMsg handleReward(CommonMsg respond, int task_id)
	{
		String uid = respond.header.uid;
		TaskMsg msg = initMsg(respond);
		
		Task t_cfg = ConfigConstant.tTask.get(task_id);
		if (null == t_cfg)
		{
			log.info("Task handleReward No TaskCfg ID {}", task_id);
			return respond;
		}
		
		m_task = m_task_db.loadByUid(uid);
		if (m_task.cur_id != task_id)
		{
			log.info("Task handleReward task_id error {}", task_id);
			return respond;
		}
		
		if (m_task.is_finish == false)
		{
			log.info("Task handleReward is Not Finish {}", task_id);
			return respond;
		}
		
		m_task.cur_id = t_cfg.getLast();
		m_task.arg = 0;
		m_task.is_finish = false;
		checkTaskWhenActive(m_task);
		m_task_db.save();
		msg.cur_id = m_task.cur_id;
		msg.is_finish = m_task.is_finish;
		msg.arg = m_task.arg;
		
		m_player = m_player_db.loadById(uid);
    	if (t_cfg.getGold() > 0)
    	{
    		m_player.addGold(t_cfg.getGold());
    		msg.r_gold = t_cfg.getGold();
    		if (respond.body.sync_player_info == null)
    			respond.body.sync_player_info  = new SyncPlayerInfoMsg();
    		respond.body.sync_player_info.gold = t_cfg.getGold();
    	}
    	if (t_cfg.getJewel() > 0)
    	{
    		m_player.addDiamond(t_cfg.getJewel());
    		msg.r_diamond = t_cfg.getJewel();
    		if (respond.body.sync_player_info == null)
    			respond.body.sync_player_info  = new SyncPlayerInfoMsg();
    		respond.body.sync_player_info.diamond = t_cfg.getJewel();
    	}
    	m_player_db.save();
    	
    	m_bag = m_bag_db.loadByUid(uid);
    	for (int i = 0; i < t_cfg.getItem().length; ++i)
    	{
    		int id = t_cfg.getItem()[i];
    		int cnt = t_cfg.getItemN()[i];
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
		return respond;
	}

	public static void doTask(String uid, int type, int check_value)
	{
		PlayerTaskDB t_db = SpringContextUtil.getBean(PlayerTaskDB.class);
		PlayerTaskInfo info = t_db.loadByUid(uid);
		if (info == null)
		{
			info = (PlayerTaskInfo)t_db.createDB(uid);
		}
		if (info.is_finish)
		{
			info = null;
			t_db = null;
			return;
		}
		
		Task t_cfg = ConfigConstant.tTask.get(info.cur_id);
		if (null == t_cfg)
		{
			info = null;
			t_db = null;
			return;
		}
		
		if (t_cfg.getType() != type)
		{
			info = null;
			t_db = null;
			return;
		}
		
		switch (t_cfg.getType())
		{
		case TaskType.HAS_FIRST_DMD_DRAW:
		case TaskType.HAS_ONE_BACKUP:
			info.is_finish = true;
			break;
		case TaskType.HAS_PVE:
		case TaskType.HAS_EQUIP_STR:
		case TaskType.HAS_ROLE_LV:
		case TaskType.HAS_ROLE_LV_ALL_ROLE:
		case TaskType.HAS_ROLE_BREACH:
		case TaskType.HAS_EQUIP_REFINE:
		case TaskType.HAS_ROLE_PVE_CNT:
		case TaskType.HAS_CHARM_LV:
		case TaskType.HAS_PLAYER_LEVEL:
		case TaskType.HAS_HISTORY_FIGHT:
		case TaskType.HAS_ROLE_EQUIP_CNT:
		case TaskType.HAS_ENCHANT_ROLE:
		case TaskType.HAS_TOWER_LV:
		case TaskType.HAS_WISH_LV:
			if (check_value >= t_cfg.getCondition())
				info.is_finish = true;
			break;
		case TaskType.HAS_INSTANCE_MAT:
		case TaskType.HAS_INSTANCE_GOLD:
		case TaskType.HAS_INSTANCE_HERO:
		case TaskType.HAS_INSTANCE_EQUIP:
			Instance check_cfg = ConfigConstant.tInstance.get(check_value);
			Instance con_cfg = ConfigConstant.tInstance.get(t_cfg.getCondition());
			if (check_cfg.getType() == con_cfg.getType())
			{
				if (check_value >= t_cfg.getCondition())
					info.is_finish = true;
			}
			break;
		case TaskType.HAS_ROLE_ID:
		case TaskType.HAS_GET_ARENA_AWARD_ID:
		case TaskType.HAS_GET_CHARM_LV_AWARD_ID:
		case TaskType.HAS_GET_DUEL_AWARD_ID:
		case TaskType.HAS_GET_TOWER_AWARD_ID:
		case TaskType.HAS_GET_DMG_AWARD_ID:
		case TaskType.HAS_GET_LINEUP_AWARD_ID:
			if (check_value == t_cfg.getCondition())
				info.is_finish = true;
			break;
		case TaskType.EQUIP_STR:
		case TaskType.EQUIP_REFINE:
		case TaskType.ROLE_LV_UP:
		case TaskType.ROLE_TALENT:
		case TaskType.ROLE_BREACH:
		case TaskType.ROLE_AWAKEN:
		case TaskType.INSTANCE_GOLD:
		case TaskType.INSTANCE_HERO:
		case TaskType.INSTANCE_EQUIP:
		case TaskType.INSTANCE_MAT:
		case TaskType.DUEL:
		case TaskType.ARENA_CNT:
		case TaskType.ITEM_DRAW_CNT:
		case TaskType.QUICK_BATTLE_CNT:
		case TaskType.GOLD_BUY_CNT:
		case TaskType.WISH_CNT:
			info.arg += check_value;
			if (info.arg >= t_cfg.getCondition())
			{
				info.arg = t_cfg.getCondition();
				info.is_finish = true;
			}
			break;
		}
		t_db.save();
		info = null;
		t_db = null;
		return;
	}
	
	private void checkTaskWhenActive(PlayerTaskInfo info)
	{		
		Task t_cfg = ConfigConstant.tTask.get(info.cur_id);
		if (null == t_cfg)
		{
			return;
		}
		
		String uid = info.uid;
		switch (t_cfg.getType())
		{
		case TaskType.HAS_PVE:
			PlayerLevelDB pve_db = SpringContextUtil.getBean(PlayerLevelDB.class);
			PlayerLevelInfo pve = pve_db.loadByUid(uid);
			if (pve.cur_level > t_cfg.getCondition())
				info.is_finish = true;
			pve_db = null;
			pve = null;
			break;
			
		case TaskType.HAS_EQUIP_STR:
			PlayerRolesInfoDB e_lv_db = SpringContextUtil.getBean(PlayerRolesInfoDB.class);
			PlayerRolesInfo e_lv = e_lv_db.load(uid);
			for (RoleInfo r : e_lv.roles)
			{
				for (EquipInfo e : r.equips)
				{
					if (e.stg_lv >= t_cfg.getCondition())
					{
						info.is_finish = true;
						break;
					}
				}
				if (info.is_finish)
					break;
			}
			break;
			
		case TaskType.HAS_ROLE_LV:
			PlayerRolesInfoDB r_lv_db = SpringContextUtil.getBean(PlayerRolesInfoDB.class);
			PlayerRolesInfo r_lv = r_lv_db.load(uid);
			for (RoleInfo r : r_lv.roles)
			{
				if (r.lv >= t_cfg.getCondition())
				{
					info.is_finish = true;
					break;
				}
			}
			break;
		case TaskType.HAS_ROLE_LV_ALL_ROLE:
			PlayerRolesInfoDB all_r_lv_db = SpringContextUtil.getBean(PlayerRolesInfoDB.class);
			PlayerRolesInfo all_r_lv = all_r_lv_db.load(uid);
			int all = 0;
			for (RoleInfo r : all_r_lv.roles)
			{
				all += r.lv;
				if (all >= t_cfg.getCondition())
				{
					info.is_finish = true;
					break;
				}
			}
			break;
		case TaskType.HAS_INSTANCE_MAT:
		case TaskType.HAS_INSTANCE_GOLD:
		case TaskType.HAS_INSTANCE_HERO:
		case TaskType.HAS_INSTANCE_EQUIP:  //副本检查是一样的算法
			PlayerInstanceDB i_pr_db = SpringContextUtil.getBean(PlayerInstanceDB.class);
			PlayerInstanceInfo i_pr = i_pr_db.loadByUid(uid);
			Instance target_cfg = ConfigConstant.tInstance.get(t_cfg.getCondition());
			if (target_cfg != null && i_pr.far_win_id.containsKey(target_cfg.getType())
					&& i_pr.far_win_id.get(target_cfg.getType()) >= target_cfg.getID())
			{
				info.is_finish = true;
			}
			break;
			
		case TaskType.HAS_ROLE_BREACH:
			PlayerRolesInfoDB r_br_db = SpringContextUtil.getBean(PlayerRolesInfoDB.class);
			PlayerRolesInfo r_br = r_br_db.load(uid);
			for (RoleInfo r : r_br.roles)
			{
				if (r.breach >= t_cfg.getCondition())
				{
					info.is_finish = true;
					break;
				}
			}
			break;
			
		case TaskType.HAS_EQUIP_REFINE:
			PlayerRolesInfoDB e_rf_db = SpringContextUtil.getBean(PlayerRolesInfoDB.class);
			PlayerRolesInfo e_rf = e_rf_db.load(uid);
			for (RoleInfo r : e_rf.roles)
			{
				for (EquipInfo e : r.equips)
				{
					if (e.rfn_lv >= t_cfg.getCondition())
					{
						info.is_finish = true;
						break;
					}
				}
				if (info.is_finish)
					break;
			}
			break;
		case TaskType.HAS_ROLE_ID:
			PlayerRolesInfoDB r_id_db = SpringContextUtil.getBean(PlayerRolesInfoDB.class);
			PlayerRolesInfo r_id = r_id_db.load(uid);
			if (r_id.GetRole(t_cfg.getCondition()) != null)
			{
				info.is_finish = true;
			}
			break;
		case TaskType.HAS_ROLE_PVE_CNT:
			PlayerRolesInfoDB team_db = SpringContextUtil.getBean(PlayerRolesInfoDB.class);
			PlayerRolesInfo team = team_db.load(uid);
			int role_cnt = 0;
			for (Entry<Integer, Integer> m : team.pve_team.entrySet())
			{
				if (team.GetRole(m.getValue()) != null)
				{
					role_cnt++;
				}
			}
			if (role_cnt >= t_cfg.getCondition())
			{
				info.is_finish = true;
			}
			break;
		case TaskType.HAS_CHARM_LV:
			PlayerRolesInfoDB charm_db = SpringContextUtil.getBean(PlayerRolesInfoDB.class);
			PlayerRolesInfo charm = charm_db.load(uid);
			if (charm.m_mei_li_lv >= t_cfg.getCondition())
				info.is_finish = true;
			break;
		case TaskType.HAS_ROLE_EQUIP_CNT:
			PlayerRolesInfoDB r_e_c_db = SpringContextUtil.getBean(PlayerRolesInfoDB.class);
			PlayerRolesInfo r_e_c = r_e_c_db.load(uid);
			for (RoleInfo r : r_e_c.roles)
			{
				int t = 0;
				for (EquipInfo ei : r.equips)
				{
					if (null != ConfigConstant.tEquip.get(ei.equip_id))
						t++;
				}
				if (t >= t_cfg.getCondition())
				{
					info.is_finish = true;
					break;
				}
			}
			break;
		case TaskType.HAS_PLAYER_LEVEL:
			PlayerInfoDB p_db = SpringContextUtil.getBean(PlayerInfoDB.class);
			PlayerInfo lv_info = p_db.loadById(uid);
			if (lv_info.team_lv >= t_cfg.getCondition())
				info.is_finish = true;
			break;
		case TaskType.HAS_HISTORY_FIGHT:
			PlayerInfoDB f_db = SpringContextUtil.getBean(PlayerInfoDB.class);
			PlayerInfo f_info = f_db.loadById(uid);
			if (f_info.team_history_max_fighting >= t_cfg.getCondition())
				info.is_finish = true;
			break;
		case TaskType.HAS_GET_ARENA_AWARD_ID:
			PlayerInfoDB a_db = SpringContextUtil.getBean(PlayerInfoDB.class);
			PlayerInfo a_info = a_db.loadById(uid);
			if (a_info.arena_rank_award.contains(t_cfg.getCondition()))
				info.is_finish = true;
			break;
		case TaskType.HAS_GET_CHARM_LV_AWARD_ID:
			PlayerDailyTaskDB cha_db = SpringContextUtil.getBean(PlayerDailyTaskDB.class);
			PlayerDailyTaskInfo cha = cha_db.loadByUid(uid);
			if (cha.rewards.contains(t_cfg.getCondition()))
				info.is_finish = true;
			break;
		case TaskType.HAS_GET_DUEL_AWARD_ID:
			PlayerDuelDB d_db = SpringContextUtil.getBean(PlayerDuelDB.class);
			PlayerDuelInfo d = d_db.loadByUid(uid);
			if (d.score_awards.contains(t_cfg.getCondition()))
				info.is_finish = true;
			break;
		case TaskType.HAS_ENCHANT_ROLE:
			PlayerRolesInfoDB r_eh_db = SpringContextUtil.getBean(PlayerRolesInfoDB.class);
			PlayerRolesInfo r_eh = r_eh_db.load(uid);
			for (RoleInfo r : r_eh.roles){
				int t = 0;
				for (EquipInfo ei : r.equips){
					int et = 0;
					for (int lv : ei.eht_lvs)
						et += lv;
					t = t < et ? et : t;
				}
				if (t >= t_cfg.getCondition())
				{
					info.is_finish = true;
					break;
				}
			}
			break;
		case TaskType.HAS_TOWER_LV:
			PlayerTowerDB t_db = SpringContextUtil.getBean(PlayerTowerDB.class);
			PlayerTowerInfo t_i = t_db.loadByUid(uid);
			if (t_i.history_box >= t_cfg.getCondition())
				info.is_finish = true;
			break;
		case TaskType.HAS_FIRST_DMD_DRAW:
			PlayerDrawCardDB fdd_db = SpringContextUtil.getBean(PlayerDrawCardDB.class);
			PlayerDrawCardInfo fdd_i = fdd_db.loadByUid(uid);
			if (fdd_i.dmd_1st == false)
				info.is_finish = true;
			break;
		case TaskType.HAS_ONE_BACKUP:
			PlayerRolesInfoDB bac_db = SpringContextUtil.getBean(PlayerRolesInfoDB.class);
			PlayerRolesInfo bac_eh = bac_db.load(uid);
			for (Map<Integer, Integer> r : bac_eh.backup_info.values()){
				for (int b : r.values()){
					if (b != 0) {
						info.is_finish = true;
						break;
					}
				}
				if (info.is_finish)
					break;
			}
			break;
		case TaskType.HAS_GET_TOWER_AWARD_ID:
			PlayerTowerDB ta_db = SpringContextUtil.getBean(PlayerTowerDB.class);
			PlayerTowerInfo ta_i = ta_db.loadByUid(uid);
			if (ta_i.first_award.contains(t_cfg.getCondition()))
				info.is_finish = true;
			break;
		case TaskType.HAS_GET_DMG_AWARD_ID:
			PlayerDmgRewardDB dmg_db = SpringContextUtil.getBean(PlayerDmgRewardDB.class);
			PlayerDmgRewardInfo dmg_i = dmg_db.loadByUid(uid);
			if (dmg_i.award.contains(t_cfg.getCondition()))
				info.is_finish = true;
			break;
		case TaskType.HAS_GET_LINEUP_AWARD_ID:
			PlayerLineupDB lu_db = SpringContextUtil.getBean(PlayerLineupDB.class);
			PlayerLineupInfo lu_i = lu_db.loadByUid(uid);
			if (lu_i.award.contains(t_cfg.getCondition()))
				info.is_finish = true;
			break;
		case TaskType.HAS_WISH_LV:
			PlayerWishDB w_db = SpringContextUtil.getBean(PlayerWishDB.class);
			PlayerWishInfo w_i = w_db.loadByUid(uid);
			if (w_i.lv >= t_cfg.getCondition())
				info.is_finish = true;
			break;
		}
		return;
	}
}
