package com.ssmGame.module.daily;

import java.util.ArrayList;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Dailytask;
import com.ssmData.config.entity.Tasklv;
import com.ssmData.config.entity.Tasklvreward;
import com.ssmData.dbase.DailyTaskInfo;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerDailyTaskDB;
import com.ssmData.dbase.PlayerDailyTaskInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerRolesInfo;
import com.ssmData.dbase.PlayerRolesInfoDB;
import com.ssmData.dbase.RoleInfo;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.daily.DailyTaskMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
import com.ssmGame.module.role.RoleAttrCalc;
import com.ssmGame.module.task.TaskImpl;
import com.ssmGame.module.task.TaskType;
import com.ssmGame.util.TimeUtils;

@Service
@Scope("prototype")
public class DailyTaskImpl {
	private static final Logger log = LoggerFactory.getLogger(DailyTaskImpl.class);
	
	public final static DailyTaskImpl getInstance(){
        return SpringContextUtil.getBean(DailyTaskImpl.class);
	}
	
	public static Dailytask getCfg(int id)
	{
		Dailytask d_cfg = null;
		for (Dailytask d : ConfigConstant.tDailytask.values())
		{
			if (d.getID() == id)
			{
				d_cfg = d;
				break;
			}
		}
		return d_cfg;
	}
	
	public static int AddDaily(Dailytask d_cfg, int add_count, String uid)
	{
		
		if (d_cfg == null)
			return 0;
        PlayerDailyTaskDB daily_db = SpringContextUtil.getBean(PlayerDailyTaskDB.class);
        PlayerDailyTaskInfo daily_info = daily_db.loadByUid(uid);
        int result = 0;
        if (daily_info != null)
        {
        	daily_info.Refresh(Calendar.getInstance(), TimeUtils.TodayStart());
        	DailyTaskInfo i = daily_info.getTaskInfo(d_cfg.getID());
        	if (i == null)
        	{
        		i = new DailyTaskInfo();
        		i.cnt = 0;
        		i.id = d_cfg.getID();
        		daily_info.tasks.add(i);
        	}
        	if (null != i)
        	{
        		int old = i.cnt;
        		i.cnt += add_count;
        		if (i.cnt >= d_cfg.getMaxNumber())
        			i.cnt = d_cfg.getMaxNumber();
        		result = i.cnt - old;
        	}
        	daily_db.save();
        }
        daily_db = null;
        daily_info = null;
        return result;
	}

	@Autowired
	PlayerRolesInfoDB m_roles_db;
	PlayerRolesInfo m_roles;
	
	@Autowired
	PlayerBagDB m_bag_db;
	PlayerBagInfo m_bag;
	
    @Autowired
    PlayerInfoDB m_player_db;
    PlayerInfo m_player = null;
    
    @Autowired
    PlayerDailyTaskDB m_daily_db;
    PlayerDailyTaskInfo m_daily;
    
    public void Destroy()
    {
    	m_roles_db = null;
    	m_roles = null;
    	m_bag_db = null;
    	m_bag = null;
    	m_player_db = null;
    	m_player = null;
    	m_daily_db = null;
    	m_daily = null;
    }
	
	public CommonMsg handleInfo(CommonMsg respond)
	{
		String uid = respond.header.uid;
		DailyTaskMsg msg = InitMsg(respond);
		
		msg.my_info = m_daily_db.loadByUid(uid);
		if (msg.my_info == null)
		{
			msg.my_info = (PlayerDailyTaskInfo)m_daily_db.createDB(uid);
		}
		else
		{
			msg.my_info.Refresh(Calendar.getInstance(), TimeUtils.TodayStart());
		}
		msg.success = true;
		
		return respond;
	}
	
	private DailyTaskMsg InitMsg(CommonMsg respond)
	{
		DailyTaskMsg msg = new DailyTaskMsg();
		respond.body.daily_task = msg;
		msg.success = false;
		return msg;
	}
	
	public CommonMsg handleLevelup(CommonMsg respond)
	{
		String uid = respond.header.uid;
		DailyTaskMsg msg = InitMsg(respond);
		
		m_roles = m_roles_db.load(uid);
		Tasklv lv_cfg = ConfigConstant.tTaskLv.get(m_roles.m_mei_li_lv);
		if (lv_cfg == null)
		{
			log.info("Daily task NO tasklv_cfg id {}", m_roles.m_mei_li_lv);
			return respond;
		}
		
		Tasklv next_lv_cfg = ConfigConstant.tTaskLv.get(m_roles.m_mei_li_lv + 1);
		if (next_lv_cfg == null)
		{
			log.info("Daily task meili lv Max Lv {}", m_roles.m_mei_li_lv);
			respond.header.rt_sub = 1139;
			return respond;
		}
		
		m_bag = m_bag_db.loadByUid(uid);
		int charm_id = ConfigConstant.tConf.getCharmItem();
		if (!m_bag.hasItemCount(charm_id, lv_cfg.getExp()))
		{
			log.info("Daily task NO Enough CharmItem {}", m_bag.getItemCount(charm_id));
			return respond;
		}
		
		m_bag.subItemCount(charm_id, lv_cfg.getExp());
		respond.body.sync_bag = new SyncBagMsg();
		respond.body.sync_bag.items.put(charm_id, m_bag.getItemCount(charm_id));
		
		for (int i = 0; i < lv_cfg.getItemId().length; ++i)
		{
			int id = lv_cfg.getItemId()[i];
			int cnt = lv_cfg.getItemNum()[i];
			if (m_bag.addItemCount(id, cnt))
			{
				respond.body.sync_bag.items.put(id, m_bag.getItemCount(id));
				if (msg.r_items == null)
					msg.r_items = new ArrayList<SyncBagItem>(); 
				SyncBagItem sb = new SyncBagItem();
				msg.r_items.add(sb);
				sb.id = id;
				sb.count = cnt;
			}
		}
		m_bag_db.save();
		
		m_player = m_player_db.loadById(uid);
		respond.body.sync_player_info = new SyncPlayerInfoMsg();
    	if (lv_cfg.getMoney() > 0)
    	{
    		m_player.addGold(lv_cfg.getMoney());
    		msg.r_gold = lv_cfg.getMoney();
    		respond.body.sync_player_info.gold = lv_cfg.getMoney();
    	}
    	if (lv_cfg.getDiamonds() > 0)
    	{
    		m_player.addDiamond(lv_cfg.getDiamonds());
    		msg.r_diamond = lv_cfg.getDiamonds();
    		respond.body.sync_player_info.diamond = lv_cfg.getDiamonds();
    	}
    	
    	m_roles.m_mei_li_lv++;
    	msg.charm_lv = m_roles.m_mei_li_lv;
    	RoleAttrCalc.RecalcAllInfo(m_roles, m_player);
    	respond.body.sync_player_info.team_current_fighting = m_player.team_current_fighting;
    	respond.body.sync_player_info.team_history_max_fighting = m_player.team_history_max_fighting;
    	respond.body.sync_roles = new PlayerRolesInfo();
    	respond.body.sync_roles.roles = new ArrayList<RoleInfo>();
    	for (RoleInfo i : m_roles.roles)
    	{
    		respond.body.sync_roles.roles.add(i.Clone());
    	}
    	
    	//主线
        TaskImpl.doTask(uid, TaskType.HAS_CHARM_LV, m_roles.m_mei_li_lv);
    	
    	m_player_db.save();
    	m_roles_db.save();
    	msg.success = true;
		return respond;
	}
	
	public CommonMsg handleGetReward(CommonMsg respond, int level_id)
	{
		String uid = respond.header.uid;
		DailyTaskMsg msg = InitMsg(respond);
		
    	m_bag = m_bag_db.loadByUid(uid);
    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
		
		Tasklvreward t_cfg = ConfigConstant.tTaskLvReward.get(level_id);
		if (t_cfg == null)
		{
			log.info("daily handleGetReward no cfg id {}", level_id);
			return respond;
		}
		
		m_roles = m_roles_db.load(uid);
		if (t_cfg.getLv() > m_roles.m_mei_li_lv)
		{
			log.info("daily handleGetReward lv NotEnough");
			return respond;
		}
		
		m_daily = m_daily_db.loadByUid(uid);
		if (m_daily.rewards.contains(level_id))
		{
			log.info("daily handleGetReward Already GetReward");
			return respond;
		}
		m_daily.rewards.add(level_id);
		m_daily_db.save();
		
		for (int i = 0; i < t_cfg.getItemId().length; ++i)
		{
			int id = t_cfg.getItemId()[i];
			int cnt = t_cfg.getItemNum()[i];
			if (m_bag.addItemCount(id, cnt))
			{
				if (respond.body.sync_bag == null)
					respond.body.sync_bag = new SyncBagMsg();
				respond.body.sync_bag.items.put(id, m_bag.getItemCount(id));
				if (msg.r_items == null)
					msg.r_items = new ArrayList<SyncBagItem>(); 
				SyncBagItem sb = new SyncBagItem();
				msg.r_items.add(sb);
				sb.id = id;
				sb.count = cnt;
			}
		}
		m_bag_db.save();
		
		m_player = m_player_db.loadById(uid);
		respond.body.sync_player_info = new SyncPlayerInfoMsg();
    	if (t_cfg.getMoney() > 0)
    	{
    		m_player.addGold(t_cfg.getMoney());
    		msg.r_gold = t_cfg.getMoney();
    		respond.body.sync_player_info.gold = t_cfg.getMoney();
    	}
    	if (t_cfg.getDiamonds() > 0)
    	{
    		m_player.addDiamond(t_cfg.getDiamonds());
    		msg.r_diamond = t_cfg.getDiamonds();
    		respond.body.sync_player_info.diamond = t_cfg.getDiamonds();
    	}
    	m_player_db.save();
    	
		//主线任务
		TaskImpl.doTask(respond.header.uid, TaskType.HAS_GET_CHARM_LV_AWARD_ID, level_id);
		
    	msg.success = true;
		return respond;
	}
}
