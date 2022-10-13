package com.ssmGame.module.wish;

import java.util.ArrayList;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.colligate.DateUtil;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Dailytask;
import com.ssmData.config.entity.Role;
import com.ssmData.config.entity.Wish;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerRolesInfo;
import com.ssmData.dbase.PlayerRolesInfoDB;
import com.ssmData.dbase.PlayerWishDB;
import com.ssmData.dbase.PlayerWishInfo;
import com.ssmData.dbase.RoleInfo;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
import com.ssmGame.defdata.msg.wish.WishMsg;
import com.ssmGame.module.daily.DailyTaskImpl;
import com.ssmGame.module.daily.DailyTaskType;
import com.ssmGame.module.player.PlayerImpl;
import com.ssmGame.module.task.TaskImpl;
import com.ssmGame.module.task.TaskType;

@Service
@Scope("prototype")
public class WishImpl {
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
    PlayerWishDB m_wish_db;
    PlayerWishInfo m_wish = null;
    
    public void destroy()
	{
		m_player = null;
		m_player_db = null;
		m_bag_db = null;
		m_bag = null;
		m_roles_db = null;
		m_roles = null;
		m_wish_db = null;
		m_wish = null;
	}
	
	private static final Logger log = LoggerFactory.getLogger(WishImpl.class);
	
	public final static WishImpl getInstance(){
        return SpringContextUtil.getBean(WishImpl.class);
	}

	public CommonMsg handleInfo(CommonMsg respond)
	{
    	String uid = respond.header.uid;
		WishMsg msg = new WishMsg();
		respond.body.wish = msg;
		msg.success = false;
		
		m_wish = m_wish_db.loadByUid(uid);
		if (m_wish == null) {
			m_wish = (PlayerWishInfo) m_wish_db.createDB(uid);
		}		
		reset(m_wish);
		m_wish_db.save();
		
		msg.lv = m_wish.lv;
		msg.exp = m_wish.exp;
		msg.stamina = m_wish.eng;
		msg.stamina_rec = m_wish.last;
		msg.role_id = m_wish.role;
		
		msg.success = true;
		return respond;
	}
	
	public CommonMsg handleRole(CommonMsg respond, int role_id)
	{
    	String uid = respond.header.uid;
		WishMsg msg = new WishMsg();
		respond.body.wish = msg;
		msg.success = false;
		
		m_wish = m_wish_db.loadByUid(uid);
		if (m_wish == null) {
			log.warn("WishImpl handleRole no DB {}", uid);
			return respond;
		}
		
		m_roles = m_roles_db.load(uid);
		if (m_roles == null) {
			log.warn("WishImpl handleRole no roles db {}", uid);
			return respond;
		}
		
		RoleInfo r = m_roles.GetRole(role_id);
		if (r == null) {
			log.warn("WishImpl handleRole no role id {} , uid {}", role_id, uid);
			return respond;
		}
		
		Role r_cfg = ConfigConstant.tRole.get(role_id);
		if (r_cfg == null) {
			log.warn("WishImpl handleRole no role_cfg id {} , uid {}", role_id, uid);
			return respond;
		}
		
		if (r_cfg.getWish() <= 0) {
			log.warn("WishImpl handleRole role_cfg not alow id {} , uid {}", role_id, uid);
			return respond;
		}
		
		reset(m_wish);
		if (m_wish.eng < r_cfg.getWishCost()) {
			log.warn("WishImpl handleRole role_id not enough energy {} , uid {}", role_id, uid);
			respond.header.rt_sub = 1189;
			return respond;
		}
		
		m_wish.role = role_id;
		m_wish_db.save();
		
		msg.success = true;
		return respond;
	}
	
	public CommonMsg handleWish(CommonMsg respond, int role_id){
		String uid = respond.header.uid;
		WishMsg msg = new WishMsg();
		respond.body.wish = msg;
		msg.success = false;
		
		m_wish = m_wish_db.loadByUid(uid);
		if (m_wish == null) {
			log.warn("WishImpl handleWish no DB {}", uid);
			return respond;
		}
		
		m_roles = m_roles_db.load(uid);
		if (m_roles == null) {
			log.warn("WishImpl handleWish no roles db {}", uid);
			return respond;
		}
		
		RoleInfo r = m_roles.GetRole(role_id);
		if (r == null) {
			log.warn("WishImpl handleWish no role id {} , uid {}", role_id, uid);
			return respond;
		}
		
		Role r_cfg = ConfigConstant.tRole.get(role_id);
		if (r_cfg == null) {
			log.warn("WishImpl handleWish no role_cfg id {} , uid {}", role_id, uid);
			return respond;
		}
		
		if (r_cfg.getWish() <= 0) {
			log.warn("WishImpl handleWish role_cfg not alow id {} , uid {}", role_id, uid);
			return respond;
		}
		
		if (m_wish.role != role_id) {
			log.warn("WishImpl handleWish role_id not the same id {} , uid {}", role_id, uid);
			return respond;
		}
		
		reset(m_wish);
		int cost = r_cfg.getWishCost();
		if (m_wish.eng >= cost) {
	    	m_bag = m_bag_db.loadByUid(uid);
	    	if (m_bag == null) {
	    		log.warn("WishImpl.handleWish() no bag id {}", uid);
	    		return respond;
	    	}
	    	
	    	m_wish.eng -= cost;
	    	msg.stamina = m_wish.eng;
	    	
	    	int id = r_cfg.getFragment();
	    	int cnt = ConfigConstant.tConf.getWishChip();
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
    		
    		//日常任务
            Dailytask d_cfg = DailyTaskImpl.getCfg(DailyTaskType.WISH);
            int add_result = DailyTaskImpl.AddDaily(d_cfg, 1, uid);
            if (d_cfg != null && add_result != 0)
            {
            	int wid = ConfigConstant.tConf.getCharmItem();
            	int wcnt = d_cfg.getTaskReward() * add_result;
            	m_bag.addItemCount(wid, wcnt);
            	respond.body.sync_bag.items.put(wid, m_bag.getItemCount(wid));
            }
            m_bag_db.save();
            
            //主线
            TaskImpl.doTask(uid, TaskType.WISH_CNT, 1);
    		
		} else {
			log.warn("WishImpl handleWish role_id not enough energy {} , uid {}", role_id, uid);
			respond.header.rt_sub = 1190;
			return respond;
		}
		m_wish_db.save();
		
		msg.success = true;
		return respond;
	}
	
	public CommonMsg handleOneKeyWish(CommonMsg respond, int role_id){
		String uid = respond.header.uid;
		WishMsg msg = new WishMsg();
		respond.body.wish = msg;
		msg.success = false;
		
		m_wish = m_wish_db.loadByUid(uid);
		if (m_wish == null) {
			log.warn("WishImpl handleOneKeyWish no DB {}", uid);
			return respond;
		}
		
		m_roles = m_roles_db.load(uid);
		if (m_roles == null) {
			log.warn("WishImpl handleOneKeyWish no roles db {}", uid);
			return respond;
		}
		
		RoleInfo r = m_roles.GetRole(role_id);
		if (r == null) {
			log.warn("WishImpl handleOneKeyWish no role id {} , uid {}", role_id, uid);
			return respond;
		}
		
		Role r_cfg = ConfigConstant.tRole.get(role_id);
		if (r_cfg == null) {
			log.warn("WishImpl handleOneKeyWish no role_cfg id {} , uid {}", role_id, uid);
			return respond;
		}
		
		if (r_cfg.getWish() <= 0) {
			log.warn("WishImpl handleOneKeyWish role_cfg not alow id {} , uid {}", role_id, uid);
			return respond;
		}
		
		if (m_wish.role != role_id) {
			log.warn("WishImpl handleOneKeyWish role_id not the same id {} , uid {}", role_id, uid);
			return respond;
		}
		
		reset(m_wish);
		int cost = r_cfg.getWishCost();
		if (m_wish.eng >= cost) {
	    	m_bag = m_bag_db.loadByUid(uid);
	    	if (m_bag == null) {
	    		log.warn("WishImpl.handleOneKeyWish() no bag id {}", uid);
	    		return respond;
	    	}
	    	
	    	int times = 0;
	    	while (m_wish.eng >= cost) {
	    		m_wish.eng -= cost;
	    		times++;
	    	}
	    	msg.stamina = m_wish.eng;
	    	
	    	int id = r_cfg.getFragment();
	    	int cnt = ConfigConstant.tConf.getWishChip() * times;
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
    		
    		//日常任务
            Dailytask d_cfg = DailyTaskImpl.getCfg(DailyTaskType.WISH);
            int add_result = DailyTaskImpl.AddDaily(d_cfg, times, uid);
            if (d_cfg != null && add_result != 0)
            {
            	int wid = ConfigConstant.tConf.getCharmItem();
            	int wcnt = d_cfg.getTaskReward() * add_result;
            	m_bag.addItemCount(wid, wcnt);
            	respond.body.sync_bag.items.put(wid, m_bag.getItemCount(wid));
            }
            m_bag_db.save();
            
            //主线
            TaskImpl.doTask(uid, TaskType.WISH_CNT, 1);
		} else {
			log.warn("WishImpl handleOneKeyWish role_id not enough energy {} , uid {}", role_id, uid);
			respond.header.rt_sub = 1190;
			return respond;
		}
		m_wish_db.save();
		
		msg.success = true;
		return respond;
	}
	
	public CommonMsg handleItemLvUp(CommonMsg respond){
		String uid = respond.header.uid;
		WishMsg msg = new WishMsg();
		respond.body.wish = msg;
		msg.success = false;
		
    	m_bag = m_bag_db.loadByUid(uid);
    	if (m_bag == null) {
    		log.warn("WishImpl.handleItemLvUp() no bag id {}", uid);
    		return respond;
    	}
    	
    	int cost_id = ConfigConstant.tConf.getWishItem();
    	int cost_cnt = 1;
    	if (!m_bag.hasItemCount(cost_id, cost_cnt)) {
    		log.warn("WishImpl.handleItemLvUp() no item enough uid {}", uid);
    		return respond;
    	}
    	
		m_wish = m_wish_db.loadByUid(uid);
		if (m_wish == null) {
			log.warn("WishImpl handleItemLvUp no DB {}", uid);
			return respond;
		}
		Wish next_cfg = ConfigConstant.tWish.get(m_wish.lv + 1); 
		if (next_cfg == null) {
			log.warn("WishImpl handleItemLvUp no next lv {} {}", m_wish.lv + 1 , uid);
			return respond;
		}
		Wish w_cfg = ConfigConstant.tWish.get(m_wish.lv);
		if (w_cfg == null) {
			log.warn("WishImpl handleItemLvUp no lv {} {}", m_wish.lv, uid);
			return respond;
		}
		
		m_bag.subItemCount(cost_id, cost_cnt);
		m_bag_db.save();
		if (respond.body.sync_bag == null)
			respond.body.sync_bag = new SyncBagMsg();
		respond.body.sync_bag.items.put(cost_id, m_bag.getItemCount(cost_id));
		
		reset(m_wish);
		m_wish.exp += ConfigConstant.tConf.getWishUpExp();
		if (m_wish.exp >= w_cfg.getExp()) {
			m_wish.exp = 0;
			m_wish.lv += 1;
			m_wish.eng += w_cfg.getWishRe();
			
	    	//主线任务
	    	TaskImpl.doTask(uid, TaskType.HAS_WISH_LV, m_wish.lv);
		}
		m_wish_db.save();
		
		msg.lv = m_wish.lv;
		msg.exp = m_wish.exp;
		msg.stamina = m_wish.eng;
		
		msg.success = true;
		return respond;
	}
	
	public CommonMsg handleDmdLvUp(CommonMsg respond){
		String uid = respond.header.uid;
		WishMsg msg = new WishMsg();
		respond.body.wish = msg;
		msg.success = false;
		
    	m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.warn("WishImpl.handleDmdLvUp() no player id {}", uid);
    		return respond;
    	}
    	
    	if (m_player.vip_level < ConfigConstant.tConf.getWishVip()) {
    		log.warn("WishImpl.handleDmdLvUp() no vip enough id {}", uid);
    		return respond;
    	}
    	
    	int cost_cnt = ConfigConstant.tConf.getWishUpNum();
    	if (!m_player.hasDiamond(cost_cnt)) {
    		log.warn("WishImpl.handleDmdLvUp() no dmd enough uid {}", uid);
    		return respond;
    	}
    	
		m_wish = m_wish_db.loadByUid(uid);
		if (m_wish == null) {
			log.warn("WishImpl handleDmdLvUp no DB {}", uid);
			return respond;
		}
		Wish next_cfg = ConfigConstant.tWish.get(m_wish.lv + 1); 
		if (next_cfg == null) {
			log.warn("WishImpl handleDmdLvUp no next lv {} {}", m_wish.lv + 1 , uid);
			return respond;
		}
		Wish w_cfg = ConfigConstant.tWish.get(m_wish.lv);
		if (w_cfg == null) {
			log.warn("WishImpl handleDmdLvUp no lv {} {}", m_wish.lv, uid);
			return respond;
		}
		
		PlayerImpl.SubDiamond(m_player, cost_cnt);
		m_player_db.save();
		if (respond.body.sync_player_info == null)
			respond.body.sync_player_info = new SyncPlayerInfoMsg();
		respond.body.sync_player_info.diamond = -cost_cnt;
		
		reset(m_wish);
		m_wish.exp += ConfigConstant.tConf.getWishUpExp();
		if (m_wish.exp >= w_cfg.getExp()) {
			m_wish.exp = 0;
			m_wish.lv += 1;
			m_wish.eng += w_cfg.getWishRe();
			
	    	//主线任务
	    	TaskImpl.doTask(uid, TaskType.HAS_WISH_LV, m_wish.lv);
		}
		m_wish_db.save();
		
		msg.lv = m_wish.lv;
		msg.exp = m_wish.exp;
		msg.stamina = m_wish.eng;
		
		msg.success = true;
		return respond;
	}
	
	public CommonMsg handleOneLevelLvUp(CommonMsg respond){
		String uid = respond.header.uid;
		WishMsg msg = new WishMsg();
		respond.body.wish = msg;
		msg.success = false;
		
    	m_player = m_player_db.loadById(uid);
    	if (m_player == null) {
    		log.warn("WishImpl.handleDmdLvUp() no player id {}", uid);
    		return respond;
    	}
    	
    	if (m_player.vip_level < ConfigConstant.tConf.getWishVip()) {
    		log.warn("WishImpl.handleDmdLvUp() no vip enough id {}", uid);
    		return respond;
    	}
    	
		m_wish = m_wish_db.loadByUid(uid);
		if (m_wish == null) {
			log.warn("WishImpl handleDmdLvUp no DB {}", uid);
			return respond;
		}
		Wish next_cfg = ConfigConstant.tWish.get(m_wish.lv + 1); 
		if (next_cfg == null) {
			log.warn("WishImpl handleDmdLvUp no next lv {} {}", m_wish.lv + 1 , uid);
			return respond;
		}
		Wish w_cfg = ConfigConstant.tWish.get(m_wish.lv);
		if (w_cfg == null) {
			log.warn("WishImpl handleDmdLvUp no lv {} {}", m_wish.lv, uid);
			return respond;
		}
		
		//计算公式：（升级所需经验-当前经验）/单次提升经验*单次提升钻石价格
    	int cost_cnt = (int)Math.ceil((float)(w_cfg.getExp() - m_wish.exp) / ConfigConstant.tConf.getWishUpExp() * ConfigConstant.tConf.getWishUpNum());
    	if (!m_player.hasDiamond(cost_cnt)) {
    		log.warn("WishImpl.handleDmdLvUp() no dmd enough uid {}", uid);
    		return respond;
    	}
		
		PlayerImpl.SubDiamond(m_player, cost_cnt);
		m_player_db.save();
		if (respond.body.sync_player_info == null)
			respond.body.sync_player_info = new SyncPlayerInfoMsg();
		respond.body.sync_player_info.diamond = -cost_cnt;
		
		reset(m_wish);
		m_wish.exp = 0;
		m_wish.lv += 1;
		m_wish.eng += w_cfg.getWishRe();
		m_wish_db.save();
		
    	//主线任务
    	TaskImpl.doTask(uid, TaskType.HAS_WISH_LV, m_wish.lv);
		
		msg.lv = m_wish.lv;
		msg.exp = m_wish.exp;
		msg.stamina = m_wish.eng;
		
		msg.success = true;
		return respond;
	}
	
	private void reset(PlayerWishInfo wish){
		long now = Calendar.getInstance().getTimeInMillis();
    	int[] rt = new int[]{ConfigConstant.tConf.getRTime()};
    	if (DateUtil.checkPassTime(wish.last, now, rt))
    	{
    		wish.last = now;
    		Wish w_cfg = ConfigConstant.tWish.get(wish.lv);
    		if (w_cfg != null && wish.eng < w_cfg.getWishMax()) {
    			wish.eng = w_cfg.getWishMax();
    		}
    	}
	}
}
