package com.ssmGame.module.goldbuy;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.colligate.DateUtil;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Dailytask;
import com.ssmData.config.entity.Vip;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.shop.ShopMsg;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
import com.ssmGame.module.daily.DailyTaskImpl;
import com.ssmGame.module.daily.DailyTaskType;
import com.ssmGame.module.player.PlayerImpl;
import com.ssmGame.module.task.TaskImpl;
import com.ssmGame.module.task.TaskType;

@Service
@Scope("prototype")
public class GoldBuyImpl {
	//private static final Logger log = LoggerFactory.getLogger(GoldBuyImpl.class);
	
    @Autowired
    PlayerInfoDB m_player_db;
    PlayerInfo m_player = null;
    
    @Autowired
    private PlayerBagDB m_bag_db;
    private PlayerBagInfo m_bag = null;
    
	public void destroy()
	{		
		m_player_db = null;
		m_player = null;
		m_bag_db = null;
		m_bag = null;
	}
	
	public final static GoldBuyImpl getInstance(){
        return SpringContextUtil.getBean(GoldBuyImpl.class);
	}
	
	public CommonMsg handleInfo(CommonMsg respond)
	{
		String uid = respond.header.uid;    
    	
    	m_player = m_player_db.loadById(uid);
    	Refresh(m_player);
    	m_player_db.save();
    	
    	respond.body.sync_player_info = new SyncPlayerInfoMsg();
    	respond.body.sync_player_info.last_gold_time = m_player.last_gold_time;
    	respond.body.sync_player_info.gold_buy_cnt = m_player.gold_buy_cnt;
    	
    	return respond;
	}
	
	public CommonMsg handleBuy(CommonMsg respond)
	{
		String uid = respond.header.uid;
		ShopMsg msg = new ShopMsg();
		respond.body.shop = msg;
		msg.success = false;
    	
    	m_player = m_player_db.loadById(uid);
    	Refresh(m_player);
    	
        int my_vip_lv = m_player.vip_level;
        Vip v_cfg = ConfigConstant.tVip.get(my_vip_lv);
        
        if (m_player.gold_buy_cnt >= v_cfg.getGoldBuy())
        {
        	return respond;
        }
        
        int cost_diamond = ConfigConstant.tConf.getGoldBuy();
        if (!m_player.hasDiamond(cost_diamond))
        {
    		respond.header.rt_sub = 1005;
    		return respond;
        }
        PlayerImpl.SubDiamond(m_player, cost_diamond);
    	respond.body.sync_player_info = new SyncPlayerInfoMsg();
    	respond.body.sync_player_info.diamond = -cost_diamond;
        
        m_player.gold_buy_cnt++;
        double add_gold = m_player.team_lv * m_player.team_lv * ConfigConstant.tConf.getBuyGoldC();
        m_player.addGold(add_gold);
        m_player_db.save();
        msg.r_gold = add_gold;
        respond.body.sync_player_info.gold = add_gold;
		respond.body.sync_player_info.last_gold_time = m_player.last_gold_time;
		respond.body.sync_player_info.gold_buy_cnt = m_player.gold_buy_cnt;
		
		//主线
        TaskImpl.doTask(uid, TaskType.GOLD_BUY_CNT, 1);
        
        //日常任务
        Dailytask d_cfg = DailyTaskImpl.getCfg(DailyTaskType.BUY_GOLD);
        int add_result = DailyTaskImpl.AddDaily(d_cfg, 1, uid);
        if (d_cfg != null && add_result != 0)
        {
        	int id = ConfigConstant.tConf.getCharmItem();
        	int cnt = d_cfg.getTaskReward() * add_result;
        	m_bag = m_bag_db.loadByUid(uid);
        	m_bag.addItemCount(id, cnt);
        	m_bag_db.save();
        	respond.body.sync_bag = new SyncBagMsg();
        	respond.body.sync_bag.items.put(id, m_bag.getItemCount(id));
        }
		
        msg.success = true;
		return respond;
	}
	
	public static void Refresh(PlayerInfo info)
	{
		long now = Calendar.getInstance().getTimeInMillis();    	
    	int[] rt = new int[]{ConfigConstant.tConf.getRTime()};
    	if (DateUtil.checkPassTime(info.last_gold_time, now, rt))
    	{
    		info.last_gold_time = now;
    		info.gold_buy_cnt = 0;
    	}
	}
}
