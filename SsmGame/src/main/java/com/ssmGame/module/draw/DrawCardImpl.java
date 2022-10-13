package com.ssmGame.module.draw;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Card;
import com.ssmData.config.entity.Dailytask;
import com.ssmData.config.entity.Role;
import com.ssmData.config.entity.Vip;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerDrawCardDB;
import com.ssmData.dbase.PlayerDrawCardInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerRolesInfo;
import com.ssmData.dbase.PlayerRolesInfoDB;
import com.ssmData.dbase.PlayerScrollDB;
import com.ssmData.dbase.PlayerScrollInfo;
import com.ssmData.dbase.RoleInfo;
import com.ssmData.dbase.ScrollInfo;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.draw.DrawCardMsg;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
import com.ssmGame.module.broadcast.BroadcastImpl;
import com.ssmGame.module.daily.DailyTaskImpl;
import com.ssmGame.module.daily.DailyTaskType;
import com.ssmGame.module.equip.EquipImpl;
import com.ssmGame.module.player.PlayerImpl;
import com.ssmGame.module.role.RoleAttrCalc;
import com.ssmGame.module.task.TaskImpl;
import com.ssmGame.module.task.TaskType;
import com.ssmGame.util.RandomMethod;
import com.ssmGame.util.TimeUtils;

@Service
@Scope("prototype")
public class DrawCardImpl {
	private static final Logger log = LoggerFactory.getLogger(DrawCardImpl.class);
	
	private static final int FREE_ITEM_SCROLL = 601; //道具单抽免费门票id
	
	public final static DrawCardImpl getInstance(){
        return SpringContextUtil.getBean(DrawCardImpl.class);
	}
	
    @Autowired
    PlayerInfoDB m_player_db;
    PlayerInfo m_player = null;
    
    @Autowired
    PlayerBagDB m_bag_db;
    PlayerBagInfo m_bag = null;
    
    @Autowired
    PlayerRolesInfoDB m_roles_db;
    PlayerRolesInfo m_roles = null;
    
    @Autowired
    PlayerDrawCardDB m_draw_db;
    PlayerDrawCardInfo m_draw_info = null;
    
    @Autowired
    PlayerScrollDB m_scroll_db;
    PlayerScrollInfo m_scroll;
    
    public DrawCardImpl init(String player_id)
    {
    	m_draw_info = m_draw_db.loadByUid(player_id);
		m_roles = m_roles_db.load(player_id);
		m_player = m_player_db.loadById(player_id);
		m_bag = m_bag_db.loadByUid(player_id);
		m_scroll = m_scroll_db.loadByUid(player_id);
		
		if (null == m_draw_info)
		{
			return null;
		}
		return this;
    }
    
    public DrawCardImpl initInLogin(String player_id)
    {
    	m_draw_info = m_draw_db.loadByUid(player_id);
    	if (null == m_draw_info)
		{
    		m_draw_info = (PlayerDrawCardInfo)m_draw_db.createDB(player_id);
		}
    	if (null == m_draw_info.lmt_for_10_cnt){
    		m_draw_info.lmt_for_10_cnt = 0; 
    	}
		return this;
    }
    
    public void destroy()
	{
		m_roles = null;
		m_roles_db = null;
		m_player = null;
		m_player_db = null;
		m_draw_info = null;
		m_draw_db = null;
		m_bag_db = null;
		m_bag = null;
		m_scroll = null;
		m_scroll_db = null;
	}
    
    public CommonMsg handleLogin(CommonMsg respond)
    {
    	Refresh();
    	DrawCardMsg msg = new DrawCardMsg();
    	msg.current = m_draw_info;
    	/*msg.current = new PlayerDrawCardInfo();
    	msg.current.dmd_draw_cnt = m_draw_info.dmd_draw_cnt;
    	msg.current.dmd_is_free = m_draw_info.dmd_is_free;
    	msg.current.last_reset_lmt_time = m_draw_info.last_reset_lmt_time;
    	msg.current.last_free_time = m_draw_info.last_free_time;
    	msg.current.lmt_draw_cnt = m_draw_info.lmt_draw_cnt;*/
    	respond.body.draw_card = msg;
    	m_draw_db.save();
    	
    	return respond;
    }
    
    //道具抽
    public CommonMsg ItemDraw(CommonMsg respond, int cnt)
    {
    	respond.body.draw_card = new DrawCardMsg();
    	respond.body.draw_card.is_success = false;
    	Refresh();
    	
    	if (cnt != 1 && cnt != 10)
    		return respond;
    	
    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
    	
    	ScrollInfo s_info = m_scroll.Get(FREE_ITEM_SCROLL);
    	if (null != s_info)
    		s_info.Refresh(Calendar.getInstance(), TimeUtils.TodayStart());
    	if (cnt == 1 && s_info != null && s_info.count > 0) {
			s_info.count -= 1;
			m_scroll_db.save();
			respond.body.sync_scroll = new PlayerScrollInfo();
			respond.body.sync_scroll.scroll_list = new ArrayList<ScrollInfo>();
			respond.body.sync_scroll.scroll_list.add(s_info);
    	}else {
        	int cost_count = cnt * ConfigConstant.tConf.getCardItem()[1];
        	int cost_id = ConfigConstant.tConf.getCardItem()[0];	
        	if (!m_bag.hasItemCount(cost_id, cost_count))
        	{
        		respond.header.rt_sub = 1119;
        		return respond;
        	}
    		respond.body.sync_bag = new SyncBagMsg();
        	respond.body.sync_bag.items = new HashMap<Integer, Integer>();
        	m_bag.subItemCount(cost_id, cost_count);
        	m_bag_db.save();
        	respond.body.sync_bag.items.put(cost_id, m_bag.getItemCount(cost_id));	
    	}
    	
    	List<Card> all_item_draw_cfg = new ArrayList<Card>();
    	List<Integer> all_item_draw_probs = new ArrayList<Integer>();
    	for (Card c : ConfigConstant.tCard.values())
    	{
    		if (c.getCardType() == DrawCardType.Item)
    		{
    			all_item_draw_cfg.add(c);
    			all_item_draw_probs.add(c.getCardPR());
    		}
    	}
    	
    	respond.body.sync_roles = new PlayerRolesInfo();
		respond.body.sync_roles.roles = new ArrayList<RoleInfo>();
		respond.body.draw_card.ids = new ArrayList<Integer>();
		respond.body.draw_card.types = new ArrayList<Integer>();
		respond.body.draw_card.counts = new ArrayList<Integer>();
		respond.body.draw_card.rare = new ArrayList<Integer>();
    	for (int i = 0; i < cnt; ++i)
    	{
    		int hit = RandomMethod.CalcHitWhichIndex(all_item_draw_probs);
    		if (hit == -1)
    		{
    			log.warn("ItemDraw() Random Error!");
    			return respond;
    		}
    		Card c = all_item_draw_cfg.get(hit);
    		if (cnt == 1 && m_draw_info.item_1st == true)
    		{
    			c = new Card();
    			c.setType(CardItemType.Role);
    			c.setCardPet(ConfigConstant.tConf.getFirst1());
    			c.setCardTen(0);
    		}
    		m_draw_info.item_1st = false;
    		if (!GetCard(c, false, respond, m_player))
    		{
    			return respond;
    		}
    	}
    	respond.body.draw_card.current = m_draw_info;
    	respond.body.draw_card.is_success = true;
    	
    	AddDaily(cnt, respond);
    	
    	//主线
    	TaskImpl.doTask(respond.header.uid, TaskType.ITEM_DRAW_CNT, cnt);
    	
    	m_bag_db.save();
    	m_draw_db.save();
    	m_roles_db.save();
    	m_player_db.save();
    	return respond;
    }
    
    private void AddDaily(int count, CommonMsg respond)
    {
    	//日常任务
        Dailytask d_cfg = DailyTaskImpl.getCfg(DailyTaskType.DRAW);
        int add_result = DailyTaskImpl.AddDaily(d_cfg, count, m_bag.uid);
        if (d_cfg != null && add_result != 0)
        {
        	int id = ConfigConstant.tConf.getCharmItem();
        	int cnt = d_cfg.getTaskReward() * add_result;
        	m_bag.addItemCount(id, cnt);
			if (respond.body.sync_bag == null) {
	    		respond.body.sync_bag = new SyncBagMsg();
	        	respond.body.sync_bag.items = new HashMap<Integer, Integer>();
			}
        	respond.body.sync_bag.items.put(id, m_bag.getItemCount(id));
        }
    }
    
    //钻石抽
    public CommonMsg DiamondDraw(CommonMsg respond, int cnt)
    {
    	respond.body.draw_card = new DrawCardMsg();
    	respond.body.draw_card.is_success = false;
    	Refresh();
    	
		if (m_draw_info.dmd_ten_cnt == null){
			m_draw_info.dmd_ten_cnt = ConfigConstant.tConf.getFirstCard();
		}
    	
    	if (cnt != 1 && cnt != 10)
    		return respond;
    	
    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
    	
    	int dmd_cost = 0;
    	int item_cost = 0;
    	int item_cost_id = ConfigConstant.tConf.getCardTenItem();
    	//boolean is_free = false;
    	int add_point_cnt = 0;
    	Vip vip_cfg = ConfigConstant.tVip.get(m_player.vip_level);
    	if (vip_cfg == null) {
    		return respond;
    	}
    	int ten_cnt_add = 0;
    	if (cnt == 1)
    	{
    		if (m_draw_info.dmd_is_free == 1)
    		{
    			m_draw_info.dmd_is_free = 0;
    			//is_free = true;
    		}
    		else
    		{
    			dmd_cost = ConfigConstant.tConf.getCardDiamonds();
    		}
    		if (m_player.dmd_draw == null) {
    			m_player.dmd_draw = 1;
    		} else {
    			m_player.dmd_draw++;
    		}
    		add_point_cnt = (int)Math.floor(ConfigConstant.tConf.getOnePoint() * vip_cfg.getVipPoint());
    	}
    	else if (cnt == 10)
    	{
    		if (m_bag.hasItemCount(item_cost_id, 1)) {
    			item_cost = 1;
    			dmd_cost = 0;
    		}
    		else {
    			dmd_cost = ConfigConstant.tConf.getCardDiamondsTen();
    		}
    		if (m_player.dmd_draw_ten == null) {
    			m_player.dmd_draw_ten = 1;
    		} else {
    			m_player.dmd_draw_ten++;
    		}
    		add_point_cnt = (int)Math.floor(ConfigConstant.tConf.getTenPoint() * vip_cfg.getVipPoint());
    		ten_cnt_add = m_draw_info.dmd_ten_cnt;
    	}
    	
    	if (dmd_cost > 0 && !m_player.hasDiamond(dmd_cost))
    	{
    		//log.info("DiamondDraw() Not Enough Diamond!");
    		respond.header.rt_sub = 1005;
    		return respond;
    	}
    	
    	respond.body.sync_player_info = new SyncPlayerInfoMsg();
    	if (dmd_cost > 0) {
    		PlayerImpl.SubDiamond(m_player, dmd_cost);
        	respond.body.sync_player_info.diamond = -dmd_cost;
    	}
    	respond.body.sync_bag = new SyncBagMsg();
    	respond.body.sync_bag.items = new HashMap<Integer, Integer>();
    	if (item_cost > 0){
    		m_bag.subItemCount(item_cost_id, item_cost);
    		respond.body.sync_bag.items.put(item_cost_id, m_bag.getItemCount(item_cost_id));
    	}
    	
    	List<Card> all_item_draw_cfg = new ArrayList<Card>();
    	List<Integer> all_item_draw_probs = new ArrayList<Integer>();
    	for (Card c : ConfigConstant.tCard.values())
    	{
    		if (c.getCardType() == DrawCardType.Dmd)
    		{
    			all_item_draw_cfg.add(c);
    			all_item_draw_probs.add(c.getCardPR());
    		}
    	}

    	respond.body.sync_roles = new PlayerRolesInfo();
		respond.body.sync_roles.roles = new ArrayList<RoleInfo>();
		respond.body.draw_card.ids = new ArrayList<Integer>();
		respond.body.draw_card.types = new ArrayList<Integer>();
		respond.body.draw_card.counts = new ArrayList<Integer>();
		respond.body.draw_card.rare = new ArrayList<Integer>();
		
		int point_id = ConfigConstant.tConf.getPointItem();
		m_bag.addItemCount(point_id, add_point_cnt);
		respond.body.sync_bag.items.put(point_id, m_bag.getItemCount(point_id));
		
    	for (int i = 0; i < cnt; ++i)
    	{
    		int hit = -1;
    		Card hit_c = null;
    		boolean is_ten = m_draw_info.dmd_draw_cnt == 9;
    		if (is_ten)
    		{
    			
    			m_draw_info.dmd_draw_cnt = 0;
    	    	List<Card> ten_cfg = new ArrayList<Card>();
    	    	List<Integer> ten_probs = new ArrayList<Integer>();
    	    	for (Card c : ConfigConstant.tCard.values())
    	    	{
    	    		if (c.getCardType() == DrawCardType.Dmd) 
    	    		{
    	    			ten_cfg.add(c);
    	    			ten_probs.add(c.getCardTen() + ten_cnt_add * c.getCardValue());
    	    		}
    	    	}
    	    	hit = RandomMethod.CalcHitWhichIndex(ten_probs);
    	    	if (hit != -1){
    	    		hit_c = ten_cfg.get(hit);
    	    	}
    	    	/*if (cnt == 10){
    	    		int all_tp = 0;
    	    		for (Integer tp : ten_probs){
    	    			all_tp += tp;
    	    		}
    	    		log.info("Ten pro: all {} hit {} cnt {}, uid {}", all_tp
    	    				, hit_c.getCardTen() + ten_cnt_add * hit_c.getCardValue(), m_draw_info.dmd_ten_cnt, respond.header.uid);
    	    	}*/
    	    	if (cnt == 10){
    	    		if (hit_c.getCardValue() > 0){
    	    			m_draw_info.dmd_ten_cnt = 0;
    	    		} else {
    	    			m_draw_info.dmd_ten_cnt++;
    	    		}
    	    	} 
    		}
    		else
    		{
    			m_draw_info.dmd_draw_cnt++;
    			hit = RandomMethod.CalcHitWhichIndex(all_item_draw_probs);
    	    	if (hit != -1)
    	    		hit_c = all_item_draw_cfg.get(hit);
    		}
    		if (cnt == 1 && m_draw_info.dmd_1st == true)
    		{
    			hit_c = new Card();
    			hit_c.setType(CardItemType.Role);
    			hit_c.setCardPet(ConfigConstant.tConf.getFirst2());
    			hit_c.setCardTen(0);
    		}
    		m_draw_info.dmd_1st = false;
    		if (!GetCard(hit_c, is_ten, respond, m_player))
    		{
    			return respond;
    		}
    	}
    	respond.body.draw_card.current = m_draw_info;
    	respond.body.draw_card.is_success = true;
    	
    	AddDaily(cnt, respond);
    	
    	m_bag_db.save();
    	m_draw_db.save();
    	m_roles_db.save();
    	m_player_db.save();
    	
    	//主线任务
    	TaskImpl.doTask(respond.header.uid, TaskType.HAS_FIRST_DMD_DRAW, 0);
    	
    	return respond;
    }
    
    //限量抽
    public CommonMsg LimitDraw(CommonMsg respond, int cnt)
    {
    	respond.body.draw_card = new DrawCardMsg();
    	respond.body.draw_card.is_success = false;
    	Refresh();
    	
    	if (cnt != 1 && cnt != 10)
    		return respond;
    	
		Vip vip = ConfigConstant.tVip.get(m_player.vip_level);
		if (null == vip)
		{
			log.warn("LimitDraw() player {} vip error {}", m_player._id, m_player.vip_level);
			return respond;
		}
		
		if (vip.getLimited() < m_draw_info.lmt_draw_cnt + cnt)
		{
			respond.header.rt_sub = 1120;
			return respond;
		}
		
    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
    	{
    		respond.header.rt_sub = 1168;
    		return respond;
    	}
    	
    	int add_point_cnt = 0;
    	Vip vip_cfg = ConfigConstant.tVip.get(m_player.vip_level);
    	if (vip_cfg == null) {
    		return respond;
    	}
    	add_point_cnt = (int)Math.floor(ConfigConstant.tConf.getLimitedPoint() * vip_cfg.getVipPoint());
    	if (cnt == 10){
    		add_point_cnt = (int)(ConfigConstant.tConf.getLimitedTenPoint() * vip_cfg.getVipPoint());
    	}
    	
		if (m_player.lmt_draw == null) {
			m_player.lmt_draw = cnt;
		} else {
			m_player.lmt_draw += cnt;
		}
		
    	int cost = ConfigConstant.tConf.getCardLimited();
    	if (cnt == 10){
    		cost = ConfigConstant.tConf.getCardLimitedTen();
    	}
    	if (!m_player.hasDiamond(cost))
    	{
    		respond.header.rt_sub = 1005;
    		return respond;
    	}
    	log.info("Lmt draw player {} cost {}", m_player._id, cost);
    	PlayerImpl.SubDiamond(m_player, cost);
    	respond.body.sync_player_info = new SyncPlayerInfoMsg();
    	respond.body.sync_player_info.diamond = -cost;
    	
    	respond.body.sync_bag = new SyncBagMsg();
    	respond.body.sync_bag.items = new HashMap<Integer, Integer>();
    	respond.body.sync_roles = new PlayerRolesInfo();
		respond.body.sync_roles.roles = new ArrayList<RoleInfo>();
		respond.body.draw_card.ids = new ArrayList<Integer>();
		respond.body.draw_card.types = new ArrayList<Integer>();
		respond.body.draw_card.counts = new ArrayList<Integer>();
		respond.body.draw_card.rare = new ArrayList<Integer>();
		
		int point_id = ConfigConstant.tConf.getPointItem();
		m_bag.addItemCount(point_id, add_point_cnt);
		respond.body.sync_bag.items.put(point_id, m_bag.getItemCount(point_id));
    	
    	m_draw_info.lmt_draw_cnt += cnt;
    	
    	List<Card> all_item_draw_cfg = new ArrayList<Card>();
    	List<Integer> all_item_draw_probs = new ArrayList<Integer>();
    	for (Card c : ConfigConstant.tCard.values())
    	{
    		if (c.getCardType() == DrawCardType.Lmt)
    		{
    			all_item_draw_cfg.add(c);
    			all_item_draw_probs.add(c.getCardPR());
    		}
    	}
    	for (int i = 0; i < cnt; ++i)
    	{
    		int hit = -1;
    		Card hit_c = null;
    		boolean is_ten = m_draw_info.lmt_for_10_cnt == 9;
    		if (is_ten)
    		{
    			
    			m_draw_info.lmt_for_10_cnt = 0;
    	    	List<Card> ten_cfg = new ArrayList<Card>();
    	    	List<Integer> ten_probs = new ArrayList<Integer>();
    	    	for (Card c : ConfigConstant.tCard.values())
    	    	{
    	    		if (c.getCardType() == DrawCardType.Lmt) 
    	    		{
    	    			ten_cfg.add(c);
    	    			ten_probs.add(c.getCardTen());
    	    		}
    	    	}
    	    	hit = RandomMethod.CalcHitWhichIndex(ten_probs);
    	    	if (hit != -1){
    	    		hit_c = ten_cfg.get(hit);
    	    	}
    		}
    		else
    		{
    			m_draw_info.lmt_for_10_cnt++;
    			hit = RandomMethod.CalcHitWhichIndex(all_item_draw_probs);
    	    	if (hit != -1)
    	    		hit_c = all_item_draw_cfg.get(hit);
    		}
    		if (!GetCard(hit_c, is_ten, respond, m_player))
    		{
    			return respond;
    		}
    	}
		
		respond.body.draw_card.current = m_draw_info;
		respond.body.draw_card.is_success = true;
		
		AddDaily(cnt, respond);
		
    	m_bag_db.save();
    	m_draw_db.save();
    	m_roles_db.save();
    	m_player_db.save();
    	return respond;
    }
    
    private void Refresh()
    {
    	m_draw_info.Refresh(Calendar.getInstance(), TimeUtils.TodayStart());
    }
    
    private boolean GetCard(Card c, boolean dmd_ten, CommonMsg respond, PlayerInfo p)
    {
    	if (c.getType() == CardItemType.Item)
		{
			m_bag.addItemCount(c.getCardPet(), c.getCardNum());
			if (respond.body.sync_bag == null) {
	    		respond.body.sync_bag = new SyncBagMsg();
	        	respond.body.sync_bag.items = new HashMap<Integer, Integer>();
			}
			respond.body.sync_bag.items.put(c.getCardPet(), m_bag.getItemCount(c.getCardPet()));
			respond.body.draw_card.types.add(DrawCardMsgType.Item);
			respond.body.draw_card.ids.add(c.getCardPet());
			respond.body.draw_card.counts.add(c.getCardNum());
		}
		else if (c.getType() == CardItemType.Role)
		{
			Role r_cfg = ConfigConstant.tRole.get(c.getCardPet());
			if (r_cfg == null)
			{
    			log.warn("ItemDraw() Role_cfg Error! id{}", c.getCardPet());
    			return false;
			}
			if (m_roles.GetRole(r_cfg.getID()) != null)
			{
				int f_before = m_bag.getItemCount(r_cfg.getFragment());
				m_bag.addItemCount(r_cfg.getFragment(), r_cfg.getFragmentNum());
				if (respond.body.sync_bag == null) {
		    		respond.body.sync_bag = new SyncBagMsg();
		        	respond.body.sync_bag.items = new HashMap<Integer, Integer>();
				}
				respond.body.sync_bag
				.items.put(r_cfg.getFragment(), m_bag.getItemCount(r_cfg.getFragment()));
				respond.body.draw_card.types.add(DrawCardMsgType.RoleFrag);
				respond.body.draw_card.ids.add(r_cfg.getID());
				respond.body.draw_card.counts.add(r_cfg.getFragmentNum());
				log.info("Get Frags id {} before {} now {} player {}", r_cfg.getFragment()
						, f_before, m_bag.getItemCount(r_cfg.getFragment()), m_bag.uid);
			}
			else
			{
				RoleInfo role = new RoleInfo();
				role.InitByRoleConfigIdAndLv(r_cfg.getID(), 1);
				EquipImpl.changeRoleEnchantId(role);
				/*if (dmd_ten)
				{
					role.awaken = ConfigConstant.tConf.getCardAwaken();
				}*/
				
				m_roles.roles.add(role);
				RoleAttrCalc.RefreshRoleAttr(r_cfg.getID(), m_roles);
				if (RoleAttrCalc.RecalcPveTeamInfo(m_roles, p)) {
					if (respond.body.sync_player_info == null)
						respond.body.sync_player_info = new SyncPlayerInfoMsg();
					respond.body.sync_player_info.is_refresh = false;
					respond.body.sync_player_info.team_current_fighting = m_player.team_current_fighting;
				}
				respond.body.draw_card.types.add(DrawCardMsgType.Role);
				respond.body.draw_card.ids.add(r_cfg.getID());
				respond.body.draw_card.counts.add(0);
				respond.body.sync_roles.roles.add(role);
				log.info("Get Role id {} player {}", role.role_id, m_player._id);
				
				//主线任务
				TaskImpl.doTask(respond.header.uid, TaskType.HAS_ROLE_ID, r_cfg.getID());				
			}
			//公告
	        if (r_cfg.getStar() >= 3)
	        {
	        	String context = ConfigConstant.tWordconfig.get(BroadcastImpl.GET_ROLE_4_5_STAR).getWord_cn();
	        	context = context.replace("$1", p.user_base.getNickname());
	        	context = context.replace("$2", ConfigConstant.tWordconfig.get(r_cfg.getName()).getWord_cn());
	        	BroadcastImpl bi = BroadcastImpl.getInstance();
	        	bi.SendBrocast(context, p.user_base.gid, p.user_base.zid);
	        }
		}
    	respond.body.draw_card.rare.add(c.getCardTen() > 0 ? 1 : 0);
    	return true;
    }
}

class DrawCardType
{
	public static final int Item = 0; //道具抽
	public static final int Dmd = 1; //钻石抽
	public static final int Lmt = 2; //限量抽
}

class CardItemType
{
	public static final int Item = 0; //奖励道具
	public static final int Role = 1; //奖励人物
}

class DrawCardMsgType
{
	public static final int Item = 0; //道具
	public static final int Role = 1; //角色
	public static final int RoleFrag = 2; //角色转碎片
}
