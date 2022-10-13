package com.ssmGame.module.equip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Config;
import com.ssmData.config.entity.Craft;
import com.ssmData.config.entity.Enchant;
import com.ssmData.config.entity.Equip;
import com.ssmData.config.entity.Equipup;
import com.ssmData.config.entity.Item;
import com.ssmData.config.entity.Jewelry;
import com.ssmData.config.entity.Jewelryup;
import com.ssmData.config.entity.Role;
import com.ssmData.dbase.EquipInfo;
import com.ssmData.dbase.JewelryInfo;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerRolesInfo;
import com.ssmData.dbase.PlayerRolesInfoDB;
import com.ssmData.dbase.RoleInfo;
import com.ssmData.dbase.enums.EquipPos;
import com.ssmData.dbase.enums.ItemType;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.common.RewardMsg;
import com.ssmGame.defdata.msg.role.RoleEquipMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncEquipBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
import com.ssmGame.module.player.PlayerImpl;
import com.ssmGame.module.role.RoleAttrCalc;
import com.ssmGame.module.task.TaskImpl;
import com.ssmGame.module.task.TaskType;
import com.ssmGame.util.AwardUtils;
import com.ssmGame.util.ItemCountPair;
import com.ssmGame.util.RandomMethod;

@Service
@Scope("prototype")
public class EquipImpl {
	private static final Logger log = LoggerFactory.getLogger(EquipImpl.class);
	
	private static final int ENCHANT_CNT = 2; //两条属性
	
	public final static EquipImpl getInstance(){
        return SpringContextUtil.getBean(EquipImpl.class);
    }
	
    @Autowired
    PlayerRolesInfoDB m_roles_db;
    PlayerRolesInfo m_roles = null;
    
    @Autowired
    PlayerInfoDB m_player_db;
    PlayerInfo m_player = null;
    
    @Autowired
    PlayerBagDB m_bag_db;
    PlayerBagInfo m_bag = null;
    
    public EquipImpl init(String player_id)
	{
		m_roles = m_roles_db.load(player_id);
		m_player = m_player_db.loadById(player_id);
		m_bag = m_bag_db.loadByUid(player_id);
		
		if (null == m_roles)
		{
			return null;
		}
		
		return this;
	}
    
    private void destory()
    {
    	m_roles = null;
    	m_roles_db = null;
    	m_player = null;
    	m_player_db = null;
    	m_bag = null;
    	m_bag_db = null;
    }
    
    //合成 （todo 如果是饰品的话，加一下参数来判断是否直接装备）
    public CommonMsg HandleAddNewItem(CommonMsg respond, int frag_id)
    {
    	RoleEquipMsg e_msg = new RoleEquipMsg();
    	respond.body.role_equip = e_msg;
    	e_msg.success = false;
    	
    	Item i_cfg = ConfigConstant.tItem.get(frag_id);
    	if (i_cfg == null)
    	{
    		log.info("HandleAddNewItem() No Item Id {}", frag_id);
    		return respond;
    	}
    	
    	Equip e_cfg = ConfigConstant.tEquip.get(i_cfg.getEquipId());
    	if (e_cfg == null)
    	{
    		log.info("HandleAddNewItem() No Equip Id {}, frag_id is {}", i_cfg.getEquipId(), frag_id);
    		return respond;
    	}
    	
    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
   
    	if (!m_bag.hasItemCount(frag_id, i_cfg.getSynthesis()))
    	{
    		log.info("HandleAddNewItem() No Enough Frag frag_id is {}", frag_id);
    		return respond;
    	}
    	m_bag.subItemCount(frag_id, i_cfg.getSynthesis());
    	m_bag.addItemCount(i_cfg.getEquipId(), 1);
    	m_bag_db.save();
    	respond.body.sync_bag = new SyncBagMsg();
		respond.body.sync_bag.items.put(frag_id, m_bag.getItemCount(frag_id));
		respond.body.sync_equip_bag = new SyncEquipBagMsg();
		respond.body.sync_equip_bag.add.add(i_cfg.getEquipId());
    	
		e_msg.success = true;
		e_msg.bag_items = new SyncBagItem[1];
		e_msg.bag_items[0] = new SyncBagItem();
		e_msg.bag_items[0].count = 1;
		e_msg.bag_items[0].id = i_cfg.getEquipId();
		
		//log.info("HandleAddNewItem() SUCCESS frag_id is {}", frag_id);
		destory();
    	return respond;
    }
    
    //穿和换装备
    public CommonMsg HandleChange(CommonMsg respond, int pos, int id, int role_id)
    {
    	RoleEquipMsg e_msg = new RoleEquipMsg();
    	respond.body.role_equip = e_msg;
    	e_msg.success = false;
    	
    	if (!m_bag.hasEquip(id))
		{
    		log.info("HandleChange() No Equip id is {}", id);
    		return respond;
		}
    	RoleInfo r_info = m_roles.GetRole(role_id);
    	if (null == r_info)
    	{
    		log.info("HandleChange() No RoleInfo id  {}", role_id);
    		return respond;
    	}
    	respond.body.sync_equip_bag = new SyncEquipBagMsg();
    	for (int j = 0; j < r_info.equips.size(); ++j)
		{
			EquipInfo e_info = r_info.equips.get(j);
			if (pos == e_info.pos)
			{
				m_bag.subItemCount(id, 1);
				respond.body.sync_equip_bag.sub.add(id);
				int return_id = e_info.equip_id;
				if (0 != return_id)
				{
					m_bag.addItemCount(return_id, 1);
					respond.body.sync_equip_bag.add.add(return_id);
				}
				e_info.equip_id = id;
			}
		}
    	m_bag_db.save(); 	
    	SyncRoles(respond, r_info);
    	
    	//主线
    	int t = 0;
		for (EquipInfo ei : r_info.equips)
		{
			if (null != ConfigConstant.tEquip.get(ei.equip_id))
				t++;
		}
		TaskImpl.doTask(respond.header.uid, TaskType.HAS_ROLE_EQUIP_CNT, t);
    	
    	e_msg.success = true;
    	//log.info("HandleChange() SUCCESS!");
    	destory();
    	return respond;
    }
    
    private void SyncRoles(CommonMsg respond, RoleInfo r_info)
    {
    	if (respond.body.sync_roles == null)
    		respond.body.sync_roles = new PlayerRolesInfo();
    	if (respond.body.sync_roles.roles == null)
    		respond.body.sync_roles.roles = new ArrayList<RoleInfo>();
    	RoleAttrCalc.RefreshRoleAttr(r_info.role_id, m_roles);
    	respond.body.sync_roles.roles.add(r_info.Clone());
    	if (m_roles.IsHero(r_info.role_id))
    	{
    		SyncPlayerFighting(respond);
    	}
    	else
    	{
        	int hero_id = m_roles.GetBackupsHeroId(r_info.role_id);
        	if (-1 != hero_id)
        	{
        		RoleInfo hero_info = m_roles.GetRole(hero_id);
        		if (hero_info != null)
        		{
        			RoleAttrCalc.RefreshRoleAttr(hero_id, m_roles);
        			SyncPlayerFighting(respond);
        			respond.body.sync_roles.roles.add(hero_info.Clone());
        		}
        	}
    	}
		m_roles_db.save();
    }
    
    private void SyncPlayerFighting(CommonMsg respond)
    {
		PlayerImpl.UpdateTeamFightingCheckMax(m_player, m_roles.CalcTeamFighting());
		m_player_db.save();	
		if (respond.body.sync_player_info == null)
			respond.body.sync_player_info = new SyncPlayerInfoMsg();
		respond.body.sync_player_info.is_refresh = false;
		respond.body.sync_player_info.team_current_fighting = m_player.team_current_fighting;
    }
    
    //卸下
    public CommonMsg HandleTakeOff(CommonMsg respond, int pos, int role_id)
    {
    	RoleEquipMsg e_msg = new RoleEquipMsg();
    	respond.body.role_equip = e_msg;
    	e_msg.success = false;
    	
    	if (pos < EquipPos.Weapon || pos > EquipPos.Shoe)
    	{
    		log.info("HandleTakeOff() Equip Pos Error  {}, pos {}", role_id, pos);
    		return respond;
    	}
    	
    	RoleInfo r_info = m_roles.GetRole(role_id);
    	if (null == r_info)
    	{
    		log.info("HandleTakeOff() No RoleInfo id  {}", role_id);
    		return respond;
    	}
    	
    	if (m_bag.getEquipBagCapacity() < 1)
    	{
    		log.info("HandleTakeOff() No Enough Space role id  {}", role_id);
    		respond.header.rt_sub = 1125;
    		return respond;
    	}
    	respond.body.sync_equip_bag = new SyncEquipBagMsg();
    	for (int i = 0; i < r_info.equips.size(); ++i)
		{
			EquipInfo e_info = r_info.equips.get(i);
			if (pos == e_info.pos)
			{
				int return_id = e_info.equip_id;
				m_bag.addItemCount(return_id, 1);
				respond.body.sync_equip_bag.add.add(return_id);
				//脱下
				e_info.equip_id = 0;
			}
		}
    	m_bag_db.save();
    	
    	SyncRoles(respond, r_info);
    	e_msg.success = true;
    	//log.info("HandleTakeOff() SUCCESS!");
    	destory();
    	return respond;
    }
    
    //强化装备
    public CommonMsg HandleStrength(CommonMsg respond, int pos, int role_id)
    {
    	RoleEquipMsg e_msg = new RoleEquipMsg();
    	respond.body.role_equip = e_msg;
    	e_msg.success = false;
    	
    	//todo 饰品的话先不做
    	if (pos < EquipPos.Weapon || pos > EquipPos.Shoe)
    	{
    		log.info("HandleStrength() Equip Pos Error  {}, pos {}", role_id, pos);
    		return respond;
    	}
    	
    	RoleInfo r_info = m_roles.GetRole(role_id);
    	if (null == r_info)
    	{
    		log.info("HandleStrength() No RoleInfo id  {}", role_id);
    		return respond;
    	}
    	
    	EquipInfo e_info = r_info.GetEquipByPos(pos);
    	if (null == e_info)
    	{
    		log.info("HandleStrength() No Equip Pos  {}", pos);
    		return respond;
    	}
    	if (e_info.equip_id == 0)
    	{
    		log.info("HandleStrength() Equip Pos Empty {}", pos);
    		return respond;
    	}
    	
    	Equipup cur_up_cfg = ConfigConstant.tEquipup.get(e_info.stg_lv);
    	if (null == cur_up_cfg)
    	{
    		log.info("HandleStrength() No Equipup cfg  {}", e_info.stg_lv);
    		return respond;
    	}
    	
    	int team_lv = m_player.team_lv;
    	int target_lv = e_info.stg_lv + 1;
    	Equipup up_cfg = ConfigConstant.tEquipup.get(target_lv);
    	if (up_cfg == null || target_lv > team_lv * 2)
    	{
    		respond.header.rt_sub = 1111;
    		return respond;
    	}
    	
    	if (!m_player.hasGold(cur_up_cfg.getBasicsGold()))
    	{
    		respond.header.rt_sub = 1004;
    		return respond;
    	}
    	m_player.subGold(cur_up_cfg.getBasicsGold());
    	m_player_db.save();
		respond.body.sync_player_info = new SyncPlayerInfoMsg();
		respond.body.sync_player_info.gold = -cur_up_cfg.getBasicsGold();
		
		e_info.stg_lv += 1;
		SyncRoles(respond, r_info);
		
		//主线任务
        TaskImpl.doTask(m_roles.player_info_id, TaskType.EQUIP_STR, 1);
        TaskImpl.doTask(m_roles.player_info_id, TaskType.HAS_EQUIP_STR, e_info.stg_lv);
		
		e_msg.success = true;
		//log.info("HandleStrength() SUCCESS!");
		destory();
    	return respond;
    }
    
    //一键强化装备
    public CommonMsg HandleOneKeyStrength(CommonMsg respond, int role_id)
    {
    	RoleEquipMsg e_msg = new RoleEquipMsg();
    	respond.body.role_equip = e_msg;
    	e_msg.success = false;
    	
    	RoleInfo r_info = m_roles.GetRole(role_id);
    	if (null == r_info)
    	{
    		log.info("HandleOneKeyStrength() No RoleInfo id  {}", role_id);
    		return respond;
    	}
    	
    	boolean has_up = true;
    	double all_money = 0.0;
    	int team_lv = m_player.team_lv;
    	Map<Integer, Integer> add_lvs = new HashMap<Integer, Integer>();
    	for (int i = EquipPos.Weapon; i <= EquipPos.Shoe; ++i)
    		add_lvs.put(i, 0);
    	boolean has_level = false;
    	while (has_up)
    	{
    		has_up = false;
    		for (int i = EquipPos.Weapon; i <= EquipPos.Shoe; ++i)
        	{
        		EquipInfo e_info = r_info.GetEquipByPos(i);
            	if (null == e_info)
            		continue;
            	if (e_info.equip_id == 0)
            		continue;
            	int cur_lv = e_info.stg_lv + add_lvs.get(i);
            	Equipup cur_up_cfg = ConfigConstant.tEquipup.get(cur_lv);
            	if (null == cur_up_cfg)
            		continue;
            	int next_lv = cur_lv + 1;
            	Equipup up_cfg = ConfigConstant.tEquipup.get(next_lv);
            	if (up_cfg == null)
            		continue;
            	if (next_lv > team_lv * 2)
            		continue;
            	has_level = true;
            	Equipup t_cfg = ConfigConstant.tEquipup.get(cur_lv);
            	if (m_player.gold - all_money - t_cfg.getBasicsGold() < 0)
            		continue;
        		all_money += t_cfg.getBasicsGold();
        		add_lvs.put(i, add_lvs.get(i) + 1);
            	has_up = true;
        	}
    	}
    	int cnt = 0;
    	for (int i = EquipPos.Weapon; i <= EquipPos.Shoe; ++i)
    		cnt += add_lvs.get(i);
    	if (cnt == 0)
    	{
    		if (!has_level)
    			respond.header.rt_sub = 1111;
    		else
    			respond.header.rt_sub = 1004;
    		return respond;
    	}
    	
    	if (!m_player.hasGold(all_money))
    	{
    		respond.header.rt_sub = 1004;
    		return respond;
    	}
    	//System.out.println(all_money);
    	m_player.subGold(all_money);
    	m_player_db.save();
		respond.body.sync_player_info = new SyncPlayerInfoMsg();
		respond.body.sync_player_info.gold = -all_money;
		
		int max_lv = 0;
		e_msg.pos_ex = new ArrayList<Integer>();
		for (int i = EquipPos.Weapon; i <= EquipPos.Shoe; ++i)
    	{
    		EquipInfo e_info = r_info.GetEquipByPos(i);
        	if (null == e_info)
        		continue;
        	int target_lv = e_info.stg_lv + add_lvs.get(i);
        	if (e_info.stg_lv >= target_lv)
        		continue;
        	Equipup cur_up_cfg = ConfigConstant.tEquipup.get(e_info.stg_lv);
        	if (null == cur_up_cfg)
        		continue;
        	Equipup up_cfg = ConfigConstant.tEquipup.get(target_lv);
        	if (up_cfg == null)
        		continue;
        	if (e_info.equip_id == 0)
        		continue;
        	e_info.stg_lv = target_lv;
        	if (e_info.stg_lv > max_lv)
        		max_lv = e_info.stg_lv;
        	e_msg.pos_ex.add(i);
    	}
		SyncRoles(respond, r_info);
		
		//主线任务
        TaskImpl.doTask(m_roles.player_info_id, TaskType.EQUIP_STR, cnt);
        TaskImpl.doTask(m_roles.player_info_id, TaskType.HAS_EQUIP_STR, max_lv);
		
		e_msg.success = true;
		//log.info("HandleOneKeyStrength() SUCCESS!");
		destory();
    	return respond;
    }
    
    //精炼装备
    public CommonMsg HandleRefine(CommonMsg respond, int pos, int role_id)
    {
    	RoleEquipMsg e_msg = new RoleEquipMsg();
    	respond.body.role_equip = e_msg;
    	e_msg.success = false;
    	
    	if (pos < EquipPos.Weapon || pos > EquipPos.Shoe)
    	{
    		log.info("HandleRefine() Equip Pos Error  {}, pos {}", role_id, pos);
    		return respond;
    	}
    	
    	RoleInfo r_info = m_roles.GetRole(role_id);
    	if (null == r_info)
    	{
    		log.info("HandleRefine() No RoleInfo id  {}", role_id);
    		return respond;
    	}
    	
    	EquipInfo e_info = r_info.GetEquipByPos(pos);
    	if (null == e_info)
    	{
    		log.info("HandleRefine() No Equip Pos  {}", pos);
    		return respond;
    	}
    	if (e_info.equip_id == 0)
    	{
    		log.info("HandleRefine() Equip Pos Empty {}", pos);
    		return respond;
    	}
    	
    	int target_lv = e_info.rfn_lv + 1;
    	Equipup up_cfg = ConfigConstant.tEquipup.get(target_lv);
    	if (up_cfg == null || ConfigConstant.tConf.getRefineMax() < target_lv)
    	{
    		respond.header.rt_sub = 1118;
    		return respond;
    	}
    	
    	Equipup cur_up_cfg = ConfigConstant.tEquipup.get(e_info.rfn_lv);
    	if (null == cur_up_cfg)
    	{
    		log.info("HandleRefine() No Equipup cfg  {}", e_info.rfn_lv);
    		return respond;
    	}
    	
    	boolean check_pass = true;
    	for (int i = 0; i < cur_up_cfg.getRefineItem().length; ++i)
    	{
    		int id = cur_up_cfg.getRefineItem()[i];
    		int count = cur_up_cfg.getRefineItemNum()[i];
    		if (!m_bag.hasItemCount(id, count))
    		{
    			check_pass = false;
    			break;
    		}
    	}
    	if (!check_pass)
    	{
    		respond.header.rt_sub = 1113;
    		return respond;
    	}
    	respond.body.sync_bag = new SyncBagMsg();
		respond.body.sync_bag.items = new HashMap<Integer, Integer>();
    	for (int i = 0; i < cur_up_cfg.getRefineItem().length; ++i)
    	{
    		int id = cur_up_cfg.getRefineItem()[i];
    		int count = cur_up_cfg.getRefineItemNum()[i];
    		m_bag.subItemCount(id, count);
    		respond.body.sync_bag.items.put(id, m_bag.getItemCount(id));
    	}
    	m_bag_db.save();
    	
    	e_info.rfn_lv += 1;
    	SyncRoles(respond, r_info);
    	
		//主线任务
        TaskImpl.doTask(m_roles.player_info_id, TaskType.EQUIP_REFINE, 1);
        TaskImpl.doTask(m_roles.player_info_id, TaskType.HAS_EQUIP_REFINE, e_info.rfn_lv);
    	
    	e_msg.success = true;
    	//log.info("HandleRefine() SUCCESS!");
    	destory();
    	return respond;
    }
    
    //一键装备
    public CommonMsg HandleOneKeyEquip(CommonMsg respond, int role_id)
    {
    	RoleEquipMsg e_msg = new RoleEquipMsg();
    	respond.body.role_equip = e_msg;
    	e_msg.success = false;
    	
    	RoleInfo r_info = m_roles.GetRole(role_id);
    	if (null == r_info)
    	{
    		log.info("HandleOneKeyEquip() No RoleInfo id  {}", role_id);
    		return respond;
    	}
    	
    	Map<Integer, Integer> cur_equip_star = new HashMap<Integer, Integer>();
    	Map<Integer, EquipInfo> cur_equio_info = new HashMap<Integer, EquipInfo>();
    	for (int i = 0; i < r_info.equips.size(); ++i)
    	{
    		Item i_cfg = ConfigConstant.tItem.get(r_info.equips.get(i).equip_id);
    		int star = 0;
    		if (i_cfg != null)
    			star = i_cfg.getIStar();
    		cur_equip_star.put(r_info.equips.get(i).pos, star);
    		cur_equio_info.put(r_info.equips.get(i).pos, r_info.equips.get(i));
    	}
    	
    	Map<Integer, Integer> max_stars = new HashMap<Integer, Integer>();
    	Map<Integer, Integer> max_ids = new HashMap<Integer, Integer>();
    	for (int i = EquipPos.Weapon; i <= EquipPos.Shoe; ++i)
    	{
    		max_stars.put(i, 0);
    		max_ids.put(i, 0);
    	}
    	boolean change = false;	
    	for (int i = 0; i < m_bag.equips.size(); ++i)
    	{
    		int id = m_bag.equips.get(i);
    		Item i_cfg = ConfigConstant.tItem.get(id);
    		Equip e_cfg = ConfigConstant.tEquip.get(id);
    		
    		if (i_cfg != null && e_cfg != null
    			&& max_stars.containsKey(e_cfg.getPosition())
    			&& checkStarAIsBiger(i_cfg.getIStar(), max_stars.get(e_cfg.getPosition())))
    		{
    			max_ids.put(e_cfg.getPosition(), id);
    			max_stars.put(e_cfg.getPosition(), i_cfg.getIStar());
    		}
    	}
    	for (int i = EquipPos.Weapon; i <= EquipPos.Shoe; ++i)
    	{
    		
    		if (checkStarAIsBiger(max_stars.get(i), cur_equip_star.get(i)) )
    		{
    			if (respond.body.sync_equip_bag == null)
    				respond.body.sync_equip_bag = new SyncEquipBagMsg();
    			
    			m_bag.subItemCount(max_ids.get(i), 1);
    			respond.body.sync_equip_bag.sub.add(max_ids.get(i));
    			int return_id = cur_equio_info.get(i).equip_id;
    			if (return_id > 0)
    			{
    				m_bag.addItemCount(return_id, 1);
    				respond.body.sync_equip_bag.add.add(return_id);
    			}
    			cur_equio_info.get(i).equip_id = max_ids.get(i);
    			change = true;
    		}
    	}
    	if (change)
    	{
    		m_bag_db.save();
    		SyncRoles(respond, r_info);
    	}
    	
    	//主线
    	int t = 0;
		for (EquipInfo ei : r_info.equips)
		{
			if (null != ConfigConstant.tEquip.get(ei.equip_id))
				t++;
		}
		TaskImpl.doTask(respond.header.uid, TaskType.HAS_ROLE_EQUIP_CNT, t);
    	
    	e_msg.success = true;
    	//log.info("HandleOneKeyEquip() SUCCESS!");
    	destory();
    	return respond;
    }

    //分解装备
    public CommonMsg HandleResolve(CommonMsg respond, List<Integer> equips)
    {
    	RoleEquipMsg e_msg = new RoleEquipMsg();
    	respond.body.role_equip = e_msg;
    	e_msg.success = false;
    	
    	boolean success = true;
    	int add_id = ConfigConstant.tConf.getResolveItem();
    	int add_count = 0;
    	for (int i : equips)
    	{
    		Equip e_cfg = ConfigConstant.tEquip.get(i);
    		if (e_cfg == null)
    		{
    			success = false;
    			break;
    		}
    		
    		if (m_bag.hasEquip(i))
    		{
    			m_bag.subItemCount(i, 1);
    			
    			m_bag.addItemCount(add_id, e_cfg.getResolve());
    			add_count += e_cfg.getResolve();
    		}
    		else
    		{
    			success = false;
    			break;
    		}
    	}
    	if (success)
    	{
    		e_msg.bag_items = new SyncBagItem[1];
    		e_msg.bag_items[0] = new SyncBagItem();
    		e_msg.bag_items[0].id = add_id;
    		e_msg.bag_items[0].count = add_count;
    		
    		respond.body.sync_bag = new SyncBagMsg();
    		respond.body.sync_bag.items.put(add_id, m_bag.getItemCount(add_id));
    		
    		m_bag_db.save();
    	}
    	
    	respond.body.role_equip.success = success;
    	destory();
    	return respond;
    }

    //给角色替换附魔ID
    public static void changeRoleEnchantId(RoleInfo r)
    {
    	if (r == null)
    		return;
    	for (EquipInfo e : r.equips)
    	{
    		changeEquipEnchantId(e);
    	}
    }

    //创建角色时，生成角色的附魔id
	private static void changeEquipEnchantId(EquipInfo e) {
		int[] new_ids = genEnchantId(e.pos);
		if (new_ids != null)
		{
			e.eht_ids = new ArrayList<Integer>();
			for (int i : new_ids)
			{
				e.eht_ids.add(i);
				if (e.eht_lvs.size() < new_ids.length)
					e.eht_lvs.add(0);
			}
		}
	}
    
    //生成附魔id
    private static int[] genEnchantId(int pos)
    {
    	int[] result = null;
    	
    	int[] field = getEnchantIdField(pos);
    	    	
    	if (null != field)
    	{
    		int[] probs = new int[field.length];
        	for (int i = 0; i < field.length; ++i)
        	{
        		Enchant e_cfg = ConfigConstant.tEnchant.get(field[i]);
        		if (e_cfg != null && e_cfg.getEnchantPR() > 0)
        		{
        			probs[i] = e_cfg.getEnchantPR();
        		}
        	}
        	
        	if (null == result)
        		result = new int[ENCHANT_CNT];
        	for (int i = 0; i < ENCHANT_CNT; ++i)
        	{
        		int hit = RandomMethod.CalcHitWhichIndex(probs);
        		result[i] = field[hit];
        	}
    	}
    	return result;
    }
    
    private static int[] getEnchantIdField(int pos)
    {
    	int[] result = null;
    	int[] temp = null;
    	Config cfg = ConfigConstant.tConf;
    	switch (pos)
    	{
    	case EquipPos.Weapon:
    		temp = cfg.getEnchantId1();
    		break;
    	case EquipPos.Chest:
    		temp = cfg.getEnchantId2();
    		break;
    	case EquipPos.Leg:
    		temp = cfg.getEnchantId3();
    		break;
    	case EquipPos.Shoe:
    		temp = cfg.getEnchantId4();
    		break;
    	}
    	if (temp != null)
    	{
    		result = new int[temp[1] - temp[0] + 1];
    		for (int i = temp[0]; i <= temp[1]; ++i)
    		{
    			result[i - temp[0]] = i;
    		}
    	}
    	return result;
    }
    
    //装备附魔升级
    public CommonMsg HandleEnchantLv(CommonMsg respond, int pos, int role_id)
    {
    	RoleEquipMsg msg = new RoleEquipMsg();
    	respond.body.role_equip = msg;
    	msg.success = false;
    	
    	RoleInfo r_info = m_roles.GetRole(role_id);
    	if (null == r_info)
    	{
    		log.warn("HandleEnchantLv() No RoleInfo id  {}", role_id);
    		return respond;
    	}
    	EquipInfo e_info = r_info.GetEquipByPos(pos);
    	if (null == e_info)
    	{
    		log.warn("HandleEnchantLv() No Equip Pos  {}", pos);
    		return respond;
    	}
    	
    	Config c_cfg = ConfigConstant.tConf;
    	if (e_info.eht_breach >= c_cfg.getEnchantLv().length)
    	{
    		log.warn("HandleEnchantLv() No EnchantLv eht_breach  {}", e_info.eht_breach);
    		return respond;
    	}
    	
    	int max_lv = c_cfg.getEnchantLv()[e_info.eht_breach];
    	int cost_cnt = -1;
    	int id = c_cfg.getEnchantItem();
    	for (int i = 0; i < e_info.eht_lvs.size(); ++i)
    	{
    		int cur_lv = e_info.eht_lvs.get(i);
    		if (cur_lv < max_lv)
    		{
    			Equipup up_cfg = ConfigConstant.tEquipup.get(cur_lv);
    			if (up_cfg == null)
    			{
    	    		log.warn("HandleEnchantLv() No tEquipup lv  {}", cur_lv);
    	    		return respond;
    			}
    			cost_cnt = up_cfg.getEnchantItemNum();
    			if (!m_bag.hasItemCount(id, cost_cnt))
    			{
    				respond.header.rt_sub = 1113; //道具不足id
    				return respond;
    			}
    			m_bag.subItemCount(id, cost_cnt);
    	    	respond.body.sync_bag = new SyncBagMsg();
    			respond.body.sync_bag.items = new HashMap<Integer, Integer>();
    			respond.body.sync_bag.items.put(id, m_bag.getItemCount(id));
    			m_bag_db.save();
    			    			
    			e_info.eht_lvs.set(i, cur_lv+1);
    			SyncRoles(respond, r_info);
    			break;
    		}
    	}
    	
    	//主线
    	int t = 0;
		for (EquipInfo ei : r_info.equips)
		{
			int et = 0;
			for (int lv : ei.eht_lvs)
				et += lv;
			t = t < et ? et : t;
		}
		TaskImpl.doTask(respond.header.uid, TaskType.HAS_ENCHANT_ROLE, t);
    	
    	msg.success = true;
    	return respond;
    }
    
    //装备附魔突破
    public CommonMsg HandleEnchantBreach(CommonMsg respond, int pos, int role_id)
    {
    	RoleEquipMsg msg = new RoleEquipMsg();
    	respond.body.role_equip = msg;
    	msg.success = false;
    	
    	RoleInfo r_info = m_roles.GetRole(role_id);
    	if (null == r_info)
    	{
    		log.warn("HandleEnchantBreach() No RoleInfo id  {}", role_id);
    		return respond;
    	}
    	EquipInfo e_info = r_info.GetEquipByPos(pos);
    	if (null == e_info)
    	{
    		log.warn("HandleEnchantBreach() No Equip Pos  {}", pos);
    		return respond;
    	}
    	
    	Config c_cfg = ConfigConstant.tConf;
    	if (e_info.eht_breach >= c_cfg.getEnchantLv().length - 1)
    	{
    		log.warn("HandleEnchantBreach() Breach MAX  {}", e_info.eht_breach);
    		return respond;
    	}
    	
    	int cur_max_lv = c_cfg.getEnchantLv()[e_info.eht_breach];
    	for (int i = 0; i < e_info.eht_lvs.size(); ++i)
    	{
    		if (e_info.eht_lvs.get(i) < cur_max_lv)
    		{
    			log.warn("HandleEnchantBreach() No enough LV to breach {}", e_info.eht_lvs.get(i));
    			return respond;
    		}
    	}
    	
    	if (m_player.team_lv < c_cfg.getEnchantTeamLv()[e_info.eht_breach]) {
    		respond.header.rt_sub = 1209;
    		return respond;
    	}
    	
    	int cost_cnt = c_cfg.getEnchantLvNum()[e_info.eht_breach];
    	int id = c_cfg.getEnchantBreach();
    	if (!m_bag.hasItemCount(id, cost_cnt))
		{
			respond.header.rt_sub = 1113; //道具不足id
			return respond;
		}
		m_bag.subItemCount(id, cost_cnt);
		respond.body.sync_bag = new SyncBagMsg();
		respond.body.sync_bag.items = new HashMap<Integer, Integer>();
		respond.body.sync_bag.items.put(id, m_bag.getItemCount(id));
		m_bag_db.save();
		
		e_info.eht_breach++;
		m_roles_db.save();
    	
		msg.success = true;
    	return respond;
    }
    
    //钻石附魔突破
    public CommonMsg HandleEnchantBreachDmd(CommonMsg respond, int pos, int role_id)
    {
    	RoleEquipMsg msg = new RoleEquipMsg();
    	respond.body.role_equip = msg;
    	msg.success = false;
    	
    	RoleInfo r_info = m_roles.GetRole(role_id);
    	if (null == r_info)
    	{
    		log.warn("HandleEnchantBreachDmd() No RoleInfo id  {}", role_id);
    		return respond;
    	}
    	EquipInfo e_info = r_info.GetEquipByPos(pos);
    	if (null == e_info)
    	{
    		log.warn("HandleEnchantBreachDmd() No Equip Pos  {}", pos);
    		return respond;
    	}
    	
    	Config c_cfg = ConfigConstant.tConf;
    	if (e_info.eht_breach >= c_cfg.getEnchantLv().length - 1)
    	{
    		log.warn("HandleEnchantBreach() Breach MAX  {}", e_info.eht_breach);
    		return respond;
    	}
    	
    	int cur_max_lv = c_cfg.getEnchantLv()[e_info.eht_breach];
    	for (int i = 0; i < e_info.eht_lvs.size(); ++i)
    	{
    		if (e_info.eht_lvs.get(i) < cur_max_lv)
    		{
    			log.warn("HandleEnchantBreachDmd() No enough LV to breach {}", e_info.eht_lvs.get(i));
    			return respond;
    		}
    	}
    	
    	if (m_player.team_lv < c_cfg.getEnchantTeamLv()[e_info.eht_breach]) {
    		respond.header.rt_sub = 1209;
    		return respond;
    	}
    	
    	int cost_cnt = c_cfg.getEnchantLvNum()[e_info.eht_breach];
    	int id = c_cfg.getEnchantBreach();
    	Item i_cfg = ConfigConstant.tItem.get(id);
    	if (i_cfg == null) {
    		log.error("no item cfg {}", id);
    		return respond;
    	}
    	int cost_dmd = cost_cnt * i_cfg.getCost();
    	if (!m_player.hasDiamond(cost_dmd)){
    		respond.header.rt_sub = 1005;
    		return respond;
    	}
    	m_player.subDiamond(cost_dmd);
    	m_player_db.save();
		respond.body.sync_player_info = new SyncPlayerInfoMsg();
		respond.body.sync_player_info.diamond = -cost_dmd;
		
		e_info.eht_breach++;
		m_roles_db.save();
    	
		msg.success = true;
    	return respond;
    }
    
    //装备附魔洗练
    public CommonMsg HandleEnchantWash(CommonMsg respond, int pos, int role_id)
    {
    	RoleEquipMsg msg = new RoleEquipMsg();
    	respond.body.role_equip = msg;
    	msg.success = false;
    	
    	RoleInfo r_info = m_roles.GetRole(role_id);
    	if (null == r_info)
    	{
    		log.warn("HandleEnchantWash() No RoleInfo id  {}", role_id);
    		return respond;
    	}
    	EquipInfo e_info = r_info.GetEquipByPos(pos);
    	if (null == e_info)
    	{
    		log.warn("HandleEnchantWash() No Equip Pos  {}", pos);
    		return respond;
    	}
    	    	
    	Config c_cfg = ConfigConstant.tConf;
		int id = c_cfg.getEnchantChange();
    	int cost_cnt = c_cfg.getEnchantChangeNum();
    	if (m_bag.hasItemCount(id, cost_cnt)) {
    		m_bag.subItemCount(id, cost_cnt);
    		respond.body.sync_bag = new SyncBagMsg();
    		respond.body.sync_bag.items = new HashMap<Integer, Integer>();
    		respond.body.sync_bag.items.put(id, m_bag.getItemCount(id));
    		m_bag_db.save();
    	} else if (ConfigConstant.tConf.getEnchantVip() <= m_player.vip_level) {
    		int dmd_cost = ConfigConstant.tConf.getEnchantVipNum();
    		if (m_player.hasDiamond(dmd_cost)) {
    			PlayerImpl.SubDiamond(m_player, dmd_cost);
    			respond.body.sync_player_info = new SyncPlayerInfoMsg();
    			respond.body.sync_player_info.diamond = -dmd_cost;
    			m_player_db.save();
    		}
    		else {
    			respond.header.rt_sub = 1005;
    			return respond;
    		}
    	} else {
    		respond.header.rt_sub = 1113; //道具不足id
    		return respond;
    	}
		
		int[] new_ids = genEnchantId(e_info.pos);
		if (new_ids != null) {
			e_info.eht_temp = new ArrayList<Integer>();
			for (int i : new_ids) {
				e_info.eht_temp.add(i);
			}
			m_roles_db.save();
		}
		else {
			log.warn("HandleEnchantWash() genEquip Error  {}", pos);
			return respond;
		}
		msg.eht_temp = e_info.eht_temp;
		
		msg.success = true;
    	return respond;
    }
    
    public CommonMsg HandleEnchantChange(CommonMsg respond, int pos, int role_id){
    	RoleEquipMsg msg = new RoleEquipMsg();
    	respond.body.role_equip = msg;
    	msg.success = false;
    	
    	RoleInfo r_info = m_roles.GetRole(role_id);
    	if (null == r_info)
    	{
    		log.warn("HandleEnchantChange() No RoleInfo id  {}", role_id);
    		return respond;
    	}
    	EquipInfo e_info = r_info.GetEquipByPos(pos);
    	if (null == e_info)
    	{
    		log.warn("HandleEnchantChange() No Equip Pos  {}", pos);
    		return respond;
    	}
    	
    	if (e_info.eht_temp != null && e_info.eht_temp.size() > 0) {
    		e_info.eht_ids = e_info.eht_temp;
    		SyncRoles(respond, r_info);
    	}
    	else {
    		//log.warn("HandleEnchantChange() No Temp Ids Pos  {}", pos);
    		return respond;
    	}
    	
		msg.success = true;
    	return respond;
    }

    //星级品质排序规则， 1 < 101 < 2 < 102
    private boolean checkStarAIsBiger(int star_a, int star_b) {
    	int mod_a = star_a % 100;
    	int mod_b = star_b % 100;
    	if (mod_a == mod_b) {
    		return star_a > star_b;
    	}
    	return mod_a > mod_b;
    }
    
    //装备一键附魔
    public CommonMsg HandleEnchantOneKey(CommonMsg respond, int pos, int role_id)
    {
    	RoleEquipMsg msg = new RoleEquipMsg();
    	respond.body.role_equip = msg;
    	msg.success = false;
    	
    	RoleInfo r_info = m_roles.GetRole(role_id);
    	if (null == r_info)
    	{
    		log.warn("HandleEnchantOneKey() No RoleInfo id  {}", role_id);
    		return respond;
    	}
    	EquipInfo e_info = r_info.GetEquipByPos(pos);
    	if (null == e_info)
    	{
    		log.warn("HandleEnchantOneKey() No Equip Pos  {}", pos);
    		return respond;
    	}
    	
    	Config c_cfg = ConfigConstant.tConf;
    	if (e_info.eht_breach >= c_cfg.getEnchantLv().length)
    	{
    		log.warn("HandleEnchantOneKey() No EnchantLv eht_breach  {}", e_info.eht_breach);
    		return respond;
    	}
    	
    	int max_lv = c_cfg.getEnchantLv()[e_info.eht_breach];
    	int cost_cnt = 0;
    	int id = c_cfg.getEnchantItem();
    	boolean all_max = true;
    	for (int i = 0; i < e_info.eht_lvs.size(); ++i)
    	{
    		int cur_lv = e_info.eht_lvs.get(i);
    		while (cur_lv < max_lv)
    		{
    			Equipup up_cfg = ConfigConstant.tEquipup.get(cur_lv);
    			if (up_cfg == null)
    			{
    	    		log.warn("HandleEnchantLv() No tEquipup lv  {}", cur_lv);
    	    		return respond;
    			}
    			cost_cnt += up_cfg.getEnchantItemNum();
    			if (!m_bag.hasItemCount(id, cost_cnt))
    			{
    				cost_cnt -= up_cfg.getEnchantItemNum();
    				break;
    			}
    			cur_lv++;	
    		}
    		if (cost_cnt > 0)
    		{
    			e_info.eht_lvs.set(i, cur_lv);
    		}
    		if (cur_lv < max_lv)
    		{
    			all_max = false;
    			break;
    		}
    	}
    	
    	if (cost_cnt > 0)
    	{
    		m_bag.subItemCount(id, cost_cnt);
    		respond.body.sync_bag = new SyncBagMsg();
    		respond.body.sync_bag.items = new HashMap<Integer, Integer>();
    		respond.body.sync_bag.items.put(id, m_bag.getItemCount(id));
    		m_bag_db.save();
    		    			
    		SyncRoles(respond, r_info);    	
    	    msg.success = true;
    	    
        	//主线
        	int t = 0;
    		for (EquipInfo ei : r_info.equips)
    		{
    			int et = 0;
    			for (int lv : ei.eht_lvs)
    				et += lv;
    			t = t < et ? et : t;
    		}
    		TaskImpl.doTask(respond.header.uid, TaskType.HAS_ENCHANT_ROLE, t);
    	}
    	else if (all_max != true)
    	{
    		respond.header.rt_sub = 1113; //道具不足id
    	}
    
    	return respond;
    }
    
    public CommonMsg HandleBagInfo(CommonMsg respond) {
		RoleEquipMsg msg = new RoleEquipMsg();
		respond.body.role_equip = msg;
		msg.success = false;
		
		respond.body.sync_bag = new SyncBagMsg();
		respond.body.sync_bag.is_refresh = true;
		for (Entry<Integer, Integer> kv : m_bag.items.entrySet()) {
			int id = kv.getKey();
			int v = kv.getValue();
			respond.body.sync_bag.items.put(id, v);
		}
		
		msg.success = true;
		return respond;
    }
    
    public CommonMsg handleSelectPack(CommonMsg respond, int item_id, int select_id) {
    	RoleEquipMsg msg = new RoleEquipMsg();
		respond.body.role_equip = msg;
		msg.success = false;
    	
		if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
		
		Item i_cfg = ConfigConstant.tItem.get(item_id);
		if (null == i_cfg) {
			return respond;
		}
		if (i_cfg.getIType() != ItemType.SelectPack) {
			return respond;
		}
		
		if (m_bag.getItemCount(item_id) < 1) {
			return respond;
		}
		
		int cnt = 0;
		for (int i = 0; i < i_cfg.getBonusItem().length; ++i) {
			if (i_cfg.getBonusItem()[i] == select_id) {
				cnt = i_cfg.getBonusCounts()[i];
				break;
			}
		}
		if (cnt == 0) {
			return respond;
		}
		
		respond.body.sync_bag = new SyncBagMsg();
		
		if (!m_bag.addItemCount(select_id, cnt)) {
			respond.header.rt_sub = 1168;
    		return respond;
		}
		respond.body.sync_bag.items.put(select_id, m_bag.getItemCount(select_id));

		m_bag.subItemCount(item_id, 1);
		respond.body.sync_bag.items.put(item_id, m_bag.getItemCount(item_id));
		
		m_bag_db.save();
		
		SyncBagItem a = new SyncBagItem();
		a.id = select_id;
		a.count = cnt;
		msg.r_items = new ArrayList<SyncBagItem>();
		msg.r_items.add(a);

    	msg.success = true;
		return respond;
    }
    
    public CommonMsg handleItemPack(CommonMsg respond, int item_id, int use_cnt) {
    	RoleEquipMsg msg = new RoleEquipMsg();
		respond.body.role_equip = msg;
		msg.success = false;
    	
		if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
		
		Item i_cfg = ConfigConstant.tItem.get(item_id);
		if (null == i_cfg) {
			return respond;
		}
		if (i_cfg.getIType() != ItemType.ItemPack) {
			return respond;
		}
		
		if (m_bag.getItemCount(item_id) < use_cnt) {
			return respond;
		}
		
		respond.body.sync_bag = new SyncBagMsg();
		
		for (int i = 0; i < i_cfg.getBonusItem().length; ++i)
    	{
    		int id = i_cfg.getBonusItem()[i];
    		int cnt = i_cfg.getBonusCounts()[i] * use_cnt;
    		if (cnt <= 0)
    			continue;
    		if (!m_bag.addItemCount(id, cnt)) {
    			respond.header.rt_sub = 1168;
        		return respond;
    		}
    		
    		SyncBagItem a = new SyncBagItem();
    		a.id = id;
    		a.count = cnt;
    		if (msg.r_items == null)
    			msg.r_items = new ArrayList<SyncBagItem>();
    		msg.r_items.add(a);
    		respond.body.sync_bag.items.put(id, m_bag.getItemCount(id));
    	}
		
		m_bag.subItemCount(item_id, use_cnt);
		respond.body.sync_bag.items.put(item_id, m_bag.getItemCount(item_id));
		
		m_bag_db.save();
		
    	msg.success = true;
		return respond;
    }
    
    public CommonMsg handleRandomPack(CommonMsg respond, int item_id, int use_cnt) {
    	RoleEquipMsg msg = new RoleEquipMsg();
		respond.body.role_equip = msg;
		msg.success = false;
    	
		if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
		
		Item i_cfg = ConfigConstant.tItem.get(item_id);
		if (null == i_cfg) {
			return respond;
		}
		if (i_cfg.getIType() != ItemType.RandomPack) {
			return respond;
		}
		
		if (m_bag.getItemCount(item_id) < use_cnt) {
			return respond;
		}
		
		respond.body.sync_bag = new SyncBagMsg();
		
		for (int i = 0; i < i_cfg.getBonusItem().length; ++i)
    	{		
    		int award_id = i_cfg.getBonusItem()[i];
    		for (int j = 0; j < use_cnt; ++j)
    		{
    			List<ItemCountPair> awards = AwardUtils.GetAward(award_id);
    			for (int k = 0; k < awards.size(); ++k)
    			{
    				int id = awards.get(k).m_item_id;
    				int cnt = awards.get(k).m_count;
    	    		if (cnt <= 0)
    	    			continue;
    	    		if (!m_bag.addItemCount(id, cnt)) {
    	    			respond.header.rt_sub = 1168;
    	    			respond.body.sync_bag = null;
    	        		return respond;
    	    		}
    	    		SyncBagItem a = new SyncBagItem();
    	    		a.id = id;
    	    		a.count = cnt;
    	    		if (msg.r_items == null)
    	    			msg.r_items = new ArrayList<SyncBagItem>();
    	    		msg.r_items.add(a);
    	    		respond.body.sync_bag.items.put(id, m_bag.getItemCount(id));
    			}
    		} 		
    	}
		
		m_bag.subItemCount(item_id, use_cnt);
		respond.body.sync_bag.items.put(item_id, m_bag.getItemCount(item_id));
		
		m_bag_db.save();
		
    	msg.success = true;
    	return respond;
    }
    
    //饰品强化一次
    public CommonMsg handleJewelyStr(CommonMsg respond, CommonMsg receive)
    {
    	String uid = respond.header.uid;
    	Integer role_id = receive.body.role_equip.role_id;
    	int idx = receive.body.role_equip.sel_id;
    	RoleEquipMsg msg = new RoleEquipMsg();
    	respond.body.role_equip = msg;
    	msg.success = false;
    	
    	RoleInfo role_info = m_roles.GetRole(role_id);
    	if (role_info == null) {
    		log.error("No role id {} player {}", role_id, uid);
    		return respond;
    	}
    	JewelryInfo j_info = null;
    	for (int i = 0; i < role_info.jewelries.size(); ++i) {
    		JewelryInfo temp = role_info.jewelries.get(i);
    		if (temp.idx == idx) {
    			j_info = temp;
    			break;
    		}
    	}
    	if (j_info == null) {
    		log.error("No jewelry idx {} role id {} player {}", idx, role_id, uid);
    		return respond;
    	}
    	int max_lv = m_player.team_lv;
    	if (j_info.stg_lv >= max_lv) {
    		respond.header.rt_sub = 1210;
    		return respond;
    	}
    	int cost_id = ConfigConstant.tConf.getOrnamentsItem();
    	Equipup up_cfg = ConfigConstant.tEquipup.get(j_info.stg_lv);
    	if (up_cfg == null) {
    		log.error("No jewelry lv cfg  {} idx {} role id {} player {}", j_info.stg_lv, idx, role_id, uid);
    		return respond;
    	}
    	int cost_cnt = up_cfg.getOrnaments();
    	if (!m_bag.hasItemCount(cost_id, cost_cnt)) {
    		log.error("No cost item enough player {}", uid);
    		return respond;
    	}
    	
    	m_bag.subItemCount(cost_id, cost_cnt);
    	j_info.stg_lv++;
    	m_bag_db.save();
    	SyncRoles(respond, role_info);
    	
    	respond.body.sync_bag = new SyncBagMsg();
		respond.body.sync_bag.items.put(cost_id, m_bag.getItemCount(cost_id));
    	msg.success = true;
    	return respond;
    }
    
    //饰品强化一键
    public CommonMsg handleJewelyStrAuto(CommonMsg respond, CommonMsg receive) {
    	String uid = respond.header.uid;
    	Integer role_id = receive.body.role_equip.role_id;
    	int idx = receive.body.role_equip.sel_id;
    	RoleEquipMsg msg = new RoleEquipMsg();
    	respond.body.role_equip = msg;
    	msg.success = false;
    	
    	RoleInfo role_info = m_roles.GetRole(role_id);
    	if (role_info == null) {
    		log.error("No role id {} player {}", role_id, uid);
    		return respond;
    	}
    	JewelryInfo j_info = null;
    	for (int i = 0; i < role_info.jewelries.size(); ++i) {
    		JewelryInfo temp = role_info.jewelries.get(i);
    		if (temp.idx == idx) {
    			j_info = temp;
    			break;
    		}
    	}
    	if (j_info == null) {
    		log.error("No jewelry idx {} role id {} player {}", idx, role_id, uid);
    		return respond;
    	}
    	int max_lv = m_player.team_lv;
    	if (j_info.stg_lv >= max_lv) {
    		respond.header.rt_sub = 1210;
    		return respond;
    	}
    	int cost_id = ConfigConstant.tConf.getOrnamentsItem();
    	int cost_cnt = 0;
    	int final_lv = j_info.stg_lv;
    	while (final_lv < max_lv) {
    		Equipup up_cfg = ConfigConstant.tEquipup.get(final_lv);
        	if (up_cfg == null) {
        		log.error("No jewelry lv cfg  {} idx {} role id {} player {}", final_lv, idx, role_id, uid);
        		return respond;
        	}
        	cost_cnt += up_cfg.getOrnaments();
        	if (!m_bag.hasItemCount(cost_id, cost_cnt)) {
        		cost_cnt -= up_cfg.getOrnaments();
        		break;
        	}
        	final_lv++;
    	}
    	if (final_lv == j_info.stg_lv) {
    		log.error("No cost item enough player {}", uid);
    		return respond;
    	}
    	
    	m_bag.subItemCount(cost_id, cost_cnt);
    	j_info.stg_lv = final_lv;
    	m_bag_db.save();
    	SyncRoles(respond, role_info);
    	
    	respond.body.sync_bag = new SyncBagMsg();
		respond.body.sync_bag.items.put(cost_id, m_bag.getItemCount(cost_id));
    	msg.success = true;
    	return respond;
    }
    
    //饰品进阶
    public CommonMsg handleJewelyEvo(CommonMsg respond, CommonMsg receive) {
    	String uid = respond.header.uid;
    	Integer role_id = receive.body.role_equip.role_id;
    	int idx = receive.body.role_equip.sel_id;
    	RoleEquipMsg msg = new RoleEquipMsg();
    	respond.body.role_equip = msg;
    	msg.success = false;
    	
    	RoleInfo role_info = m_roles.GetRole(role_id);
    	if (role_info == null) {
    		log.error("No role id {} player {}", role_id, uid);
    		return respond;
    	}
    	Role r_config = ConfigConstant.tRole.get(role_id);
    	if (r_config == null) {
    		log.error("No role cfg id {} player {}", role_id, uid);
    		return respond;
    	}
    	JewelryInfo j_info = null;
    	for (int i = 0; i < role_info.jewelries.size(); ++i) {
    		JewelryInfo temp = role_info.jewelries.get(i);
    		if (temp.idx == idx) {
    			j_info = temp;
    			break;
    		}
    	}
    	if (j_info == null) {
    		log.error("No jewelry idx {} role id {} player {}", idx, role_id, uid);
    		return respond;
    	}
    	Equipup cur_up_cfg = ConfigConstant.tEquipup.get(j_info.evo_lv);
    	if (m_player.team_lv < cur_up_cfg.getJewelryNeed()) {
    		respond.header.rt_sub = 1209;
    		return respond;
    	}
		Jewelry j_cfg = ConfigConstant.tJewelry.get(r_config.getJewelryId()[idx]);
		if (j_cfg == null) {
			log.error("No jewlry cfg player {} idx {}", uid, idx);
			return respond;
		}
    	Jewelryup j_up_cfg = ConfigConstant.tJewelryup.get(j_cfg.getAdvanced()[j_info.evo_lv]);
    	if (j_up_cfg == null)
    	{
    		log.error("No jewlryup cfg player {} lv {}", uid, j_info.evo_lv);
    		return respond;
    	}
    	int cost_cnt = 1;
    	int cost_id = j_up_cfg.getItem();//ConfigConstant.tConf.getJewelryItem();
    	if (m_bag.hasItemCount(cost_id, cost_cnt)) {
    		m_bag.subItemCount(cost_id, cost_cnt);
        	respond.body.sync_bag = new SyncBagMsg();
    		respond.body.sync_bag.items.put(cost_id, m_bag.getItemCount(cost_id));
    		m_bag_db.save();
    	} else{
    		/*Item c_cfg = ConfigConstant.tItem.get(cost_id);
    		if (c_cfg == null) {
    			log.error("No item cfg {} ", cost_id);
    			return respond;
    		}
    		int cost_dmd = c_cfg.getCost();
    		if (!m_player.hasDiamond(cost_dmd)) {
    			respond.header.rt_sub = 1005;
    			return respond;
    		}
    		PlayerImpl.SubDiamond(m_player, cost_dmd);
    		respond.body.sync_player_info = new SyncPlayerInfoMsg();
    		respond.body.sync_player_info.diamond = -cost_dmd;
    		m_player_db.save();*/
    		respond.header.rt_sub = 1113; //道具不足id
    		return respond;
    	}
    	int add_exp = ConfigConstant.tConf.getJewelryItemExp();
    	j_info.evo_exp += add_exp;
    	if (j_info.evo_exp >= cur_up_cfg.getJewelryExp()) {
    		j_info.evo_exp = 0;
    		j_info.evo_lv++;
    	}
    	SyncRoles(respond, role_info);
    	msg.success = true;
    	return respond;
    }
    
    //饰品进阶一建
    public CommonMsg handleJewelyEvoAuto(CommonMsg respond, CommonMsg receive) {
    	String uid = respond.header.uid;
    	Integer role_id = receive.body.role_equip.role_id;
    	int idx = receive.body.role_equip.sel_id;
    	RoleEquipMsg msg = new RoleEquipMsg();
    	respond.body.role_equip = msg;
    	msg.success = false;
    	
    	RoleInfo role_info = m_roles.GetRole(role_id);
    	if (role_info == null) {
    		log.error("No role id {} player {}", role_id, uid);
    		return respond;
    	}
    	Role r_config = ConfigConstant.tRole.get(role_id);
    	if (r_config == null) {
    		log.error("No role cfg id {} player {}", role_id, uid);
    		return respond;
    	}
    	JewelryInfo j_info = null;
    	for (int i = 0; i < role_info.jewelries.size(); ++i) {
    		JewelryInfo temp = role_info.jewelries.get(i);
    		if (temp.idx == idx) {
    			j_info = temp;
    			break;
    		}
    	}
    	if (j_info == null) {
    		log.error("No jewelry idx {} role id {} player {}", idx, role_id, uid);
    		return respond;
    	}
    	Equipup cur_up_cfg = ConfigConstant.tEquipup.get(j_info.evo_lv);
    	if (m_player.team_lv < cur_up_cfg.getJewelryNeed()) {
    		respond.header.rt_sub = 1209;
    		return respond;
    	}
		Jewelry j_cfg = ConfigConstant.tJewelry.get(r_config.getJewelryId()[idx]);
		if (j_cfg == null) {
			log.error("No jewlry cfg player {} idx {}", uid, idx);
			return respond;
		}
    	Jewelryup j_up_cfg = ConfigConstant.tJewelryup.get(j_cfg.getAdvanced()[j_info.evo_lv]);
    	if (j_up_cfg == null)
    	{
    		log.error("No jewlryup cfg player {} lv {}", uid, j_info.evo_lv);
    		return respond;
    	}
    	int add_exp = ConfigConstant.tConf.getJewelryItemExp();
    	int cost_cnt = (cur_up_cfg.getJewelryExp() - j_info.evo_exp) / add_exp;
    	int cost_id = j_up_cfg.getItem();//ConfigConstant.tConf.getJewelryItem();
    	if (m_bag.hasItemCount(cost_id, cost_cnt)) {
    		m_bag.subItemCount(cost_id, cost_cnt);
        	respond.body.sync_bag = new SyncBagMsg();
    		respond.body.sync_bag.items.put(cost_id, m_bag.getItemCount(cost_id));
    		m_bag_db.save();
    	} else{
    		Item c_cfg = ConfigConstant.tItem.get(cost_id);
    		if (c_cfg == null) {
    			log.error("No item cfg {} ", cost_id);
    			return respond;
    		}
    		int cost_dmd = c_cfg.getCost() * cost_cnt;
    		if (!m_player.hasDiamond(cost_dmd)) {
    			respond.header.rt_sub = 1005;
    			return respond;
    		}
    		PlayerImpl.SubDiamond(m_player, cost_dmd);
    		respond.body.sync_player_info = new SyncPlayerInfoMsg();
    		respond.body.sync_player_info.diamond = -cost_dmd;
    		m_player_db.save();
    	}
    	j_info.evo_exp = 0;
		j_info.evo_lv++;
    	SyncRoles(respond, role_info);
    	msg.success = true;
    	return respond;
    }
    
    //装备属性互换
    public CommonMsg handleExchange(CommonMsg respond, CommonMsg receive) {
    	String uid = respond.header.uid;
    	Integer my_id = receive.body.role_equip.role_id;
    	Integer ex_id = receive.body.role_equip.target_id;
    	RoleEquipMsg msg = new RoleEquipMsg();
    	respond.body.role_equip = msg;
    	msg.success = false;
    	
    	int cost_dmd = ConfigConstant.tConf.getExchange();
    	if (!m_player.hasDiamond(cost_dmd)) {
    		return respond;
    	}
    	
    	RoleInfo my_info = m_roles.GetRole(my_id);
    	if (my_info == null) {
    		log.error("No role id {} player {}", my_id, uid);
    		return respond;
    	}
    	
    	RoleInfo ex_info = m_roles.GetRole(ex_id);
    	if (ex_info == null) {
    		log.error("No role id {} player {}", ex_id, uid);
    		return respond;
    	}
    	
    	PlayerImpl.SubDiamond(m_player, cost_dmd);
    	for (int i = EquipPos.Weapon; i <= EquipPos.Shoe; ++i) {
    		EquipInfo my_e_info = my_info.GetEquipByPos(i);
    		EquipInfo ex_e_info = ex_info.GetEquipByPos(i);
    		
    		//强化
    		int temp = my_e_info.stg_lv;
    		my_e_info.stg_lv = ex_e_info.stg_lv;
    		ex_e_info.stg_lv = temp;
    		
    		//精炼
    		temp = my_e_info.rfn_lv;
    		my_e_info.rfn_lv = ex_e_info.rfn_lv;
    		ex_e_info.rfn_lv = temp;
    		
    		//附魔
    		temp = my_e_info.eht_breach;
    		my_e_info.eht_breach = ex_e_info.eht_breach;
    		ex_e_info.eht_breach = temp;
    		List<Integer> temp_eh = my_e_info.eht_lvs;
    		my_e_info.eht_lvs = ex_e_info.eht_lvs;
    		ex_e_info.eht_lvs = temp_eh;
    	}
    	
    	for (int i = 0; i < my_info.jewelries.size(); ++i) {
    		JewelryInfo my = my_info.jewelries.get(i);
    		JewelryInfo ex = ex_info.jewelries.get(i);
    		
    		//强化等级
    		int temp = my.stg_lv;
    		my.stg_lv = ex.stg_lv;
    		ex.stg_lv = temp;
    		
    		//进阶等级
    		temp = my.evo_lv;
    		my.evo_lv = ex.evo_lv;
    		ex.evo_lv = temp;
    		
    		//进阶经验
    		temp = my.evo_exp;
    		my.evo_exp = ex.evo_exp;
    		ex.evo_exp = temp;
    	}
    	
    	SyncRoles(respond, my_info);
    	SyncRoles(respond, ex_info);
		if (respond.body.sync_player_info == null)
			respond.body.sync_player_info = new SyncPlayerInfoMsg();
		respond.body.sync_player_info.is_refresh = false;
		respond.body.sync_player_info.diamond = -cost_dmd;
    	m_player_db.save();
    	msg.success = true;
    	return respond;
    }
    
  //附魔属性更换
    public CommonMsg handleChangeConst(CommonMsg respond, CommonMsg receive) {
    	String uid = respond.header.uid;
    	int my_id = receive.body.role_equip.role_id;
    	int pos = receive.body.role_equip.pos;
    	int item_id = receive.body.role_equip.item_id;
    	int cost = 1;
    	int eht_pos = receive.body.role_equip.target_id;
    	RoleEquipMsg msg = new RoleEquipMsg();
    	respond.body.role_equip = msg;
    	msg.success = false;
    	
    	if (!m_bag.hasItemCount(item_id, cost)) {
    		return respond;
    	}
    	
    	Item i_cfg = ConfigConstant.tItem.get(item_id);
    	if (null == i_cfg) {
    		log.error("no item cfg id {} player {}", item_id, uid);
    		return respond;
    	}
    	int eht_id = i_cfg.getEnchantId();
    	Enchant e_cfg = ConfigConstant.tEnchant.get(eht_id);
    	if (e_cfg == null) {
    		log.error("no eht id {} item cfg id {} player {}",eht_id, item_id, uid);
    		return respond;
    	}
    	int[] field = getEnchantIdField(pos);
    	boolean not_found = true;
    	for (int i = 0; i < field.length; ++i) {
    		if (field[i] == eht_id) {
    			not_found = false;
    			break;
    		}
    	}
    	if (not_found) {
    		log.error("No eht_id in pos field ehid {} player {} pos {}",eht_id, uid, pos);
    		return respond;
    	}
    	
    	RoleInfo my_info = m_roles.GetRole(my_id);
    	if (my_info == null) {
    		log.error("No role id {} player {}", my_id, uid);
    		return respond;
    	}
    	EquipInfo my_e_info = my_info.GetEquipByPos(pos);
    	if (my_e_info == null) {
    		log.error("No equip pos {} role id {} player {}",pos, my_id, uid);
    		return respond;
    	}
    	if (eht_pos >= my_e_info.eht_ids.size()) {
    		log.error("eht_pos error eht_pos {} player {}", eht_pos, uid);
    		return respond;
    	}
    	
    	m_bag.subItemCount(item_id, cost);
    	respond.body.sync_bag = new SyncBagMsg();
		respond.body.sync_bag.items.put(item_id, m_bag.getItemCount(item_id));
		m_bag_db.save();
		
    	my_e_info.eht_ids.set(eht_pos, eht_id);
    	SyncRoles(respond, my_info);
    	
    	msg.success = true;
    	return respond;
    }

    //物品合成
    public CommonMsg handleItemCompose(CommonMsg respond, CommonMsg receive) {
    	String uid = respond.header.uid;
    	int comp_id = receive.body.role_equip.comp_id;
    	int cnt = receive.body.role_equip.count;
    	RoleEquipMsg msg = new RoleEquipMsg();
    	msg.success = false;
    	respond.body.role_equip = msg;
    	
    	if (cnt != 1 && cnt != 10){
    		log.error("cnt error {}, player {}", cnt, uid);
    		return respond;
    	}
    	
    	Craft c_cfg = ConfigConstant.tCraft.get(comp_id);
    	if (c_cfg == null) {
    		log.error("no cfg {} player {}", comp_id, uid);
    		return respond;
    	}
    	
		if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
    	
		int gold_cost = c_cfg.getGold() * cnt;
		if (!m_player.hasGold(gold_cost)){
			respond.header.rt_sub = 1004;
			return respond;
		}
		m_player.subGold(gold_cost);
    	
		List<Integer> sync_ids = new ArrayList<Integer>();
		for (int i = 0; i < c_cfg.getRequire().length; ++i){
			int id = c_cfg.getRequire()[i];
			int i_cnt = c_cfg.getCounts()[i] * cnt;
			if (!m_bag.hasItemCount(id, i_cnt)){
				respond.header.rt_sub = 1113; //道具不足id
				return respond;
			}
			m_bag.subItemCount(id, i_cnt);
			sync_ids.add(id);
		}
		
		List<Integer> probs = new ArrayList<Integer>();
		for (int i = 0; i < c_cfg.getItemvalue().length; ++i){
			probs.add(c_cfg.getItem()[i]);
		}
		respond.body.reward = new RewardMsg();
		respond.body.reward.items = new ArrayList<SyncBagItem>();
		for (int i = 0; i < cnt; ++i){
			int hit = RandomMethod.CalcHitWhichIndex(probs);
			if (hit == -1){
				respond.body.reward = null;
				log.error("rand error hit {} player {}", hit, uid);
				return respond;
			}
			int id = c_cfg.getItem()[hit];
			m_bag.addItemCount(id, 1);
			if (!sync_ids.contains(id)) {
				sync_ids.add(id);
			}
			SyncBagItem si = new SyncBagItem();
			si.id = id;
			si.count = 1;
			respond.body.reward.items.add(si);
		}
		m_bag_db.save();
		m_player_db.save();
		
		respond.body.sync_bag = new SyncBagMsg();
		for (int i = 0; i < sync_ids.size(); ++i){
			int id = sync_ids.get(i);
			respond.body.sync_bag.items.put(id, m_bag.getItemCount(id));
		}
		
		respond.body.sync_player_info = new SyncPlayerInfoMsg();
		respond.body.sync_player_info.gold = -gold_cost;
    	
    	msg.success = true;
    	return respond;
    }
}
