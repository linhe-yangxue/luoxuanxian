package com.ssmGame.module.shop;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.colligate.DateUtil;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Item;
import com.ssmData.config.entity.Itemgood;
import com.ssmData.config.entity.Itemshop;
import com.ssmData.config.entity.Vip;
import com.ssmData.dbase.PlayerBagDB;
import com.ssmData.dbase.PlayerBagInfo;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerShopDB;
import com.ssmData.dbase.PlayerShopInfo;
import com.ssmData.dbase.ShopInfo;
import com.ssmData.dbase.enums.ItemType;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.shop.ShopMsg;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
import com.ssmGame.module.player.PlayerImpl;
import com.ssmGame.util.RandomMethod;
import com.ssmGame.util.TimeUtils;

@Service
@Scope("prototype")
public class ShopImpl {
	private static final Logger log = LoggerFactory.getLogger(ShopImpl.class);
	
	public final static ShopImpl getInstance(){
        return SpringContextUtil.getBean(ShopImpl.class);
	}
	
    @Autowired
    PlayerInfoDB m_player_db;
    PlayerInfo m_player = null;
    
    @Autowired
    PlayerShopDB m_shop_db;
    PlayerShopInfo m_shop_info;
    
    @Autowired
    PlayerBagDB m_bag_db;
    PlayerBagInfo m_bag = null;
    
    public ShopImpl init(String player_id)
	{
    	m_shop_info = m_shop_db.loadByUid(player_id);
		m_player = m_player_db.loadById(player_id);
		m_bag = m_bag_db.loadByUid(player_id);
		
		if (null == m_shop_info)
		{
			m_shop_info = (PlayerShopInfo)m_shop_db.createDB(player_id);
		}
		
		return this;
	}
    
	public void destroy()
	{
		m_player = null;
		m_player_db = null;
		m_shop_info = null;
		m_shop_db = null;
		m_bag_db = null;
		m_bag = null;
	}
	
	public CommonMsg handleShopInfo(CommonMsg respond, int item_shop_id)
	{
		ShopMsg msg = new ShopMsg();
		respond.body.shop = msg;
		msg.success = false;
		
		ShopInfo info = m_shop_info.Get(item_shop_id);
		if (info == null)
		{
			info = new ShopInfo();
			info.init(item_shop_id);
			m_shop_info.shop_list.add(info);
		}
		
		Refresh(Calendar.getInstance(), TimeUtils.TodayStart(), info);
		m_shop_db.save();
		msg.shop_info = info;
		msg.success = true;
		//log.info("handleShopInfo SUCCESS! ");
		return respond;
	}
	
	public CommonMsg handleReqBuyItem(CommonMsg respond, int item_shop_id, int item_good_id
			, int good_index, int price)
	{
		ShopMsg msg = new ShopMsg();
		respond.body.shop = msg;
		msg.success = false;
		
		Itemshop is_cfg = ConfigConstant.tItemshop.get(item_shop_id);
		if (is_cfg == null)
		{
			log.info("handleReqBuyItem() no itemshop config {}", item_shop_id);
			return respond;
		}
		
		Itemgood ig_cfg = ConfigConstant.tItemgood.get(item_good_id);
		if (ig_cfg == null)
		{
			log.info("handleReqBuyItem() no itemgood config {}", item_good_id);
			return respond;
		}
		if (m_player.vip_level < ig_cfg.getVip()) {
			log.error("handleReqBuyItem no vip enough {}", m_player._id);
			return respond;
		}
		
		SyncPlayerInfoMsg sync_player = new SyncPlayerInfoMsg(false);
		SyncBagMsg sync_bag = new SyncBagMsg();
		if (is_cfg.getCounts() == -1)//普通商店
		{
			for (int id = is_cfg.getItemGoodId()[0], index = 0; id <= is_cfg.getItemGoodId()[1]; id++, index++)
			{
				if (index == good_index
					&& id == item_good_id
					&& ig_cfg.getPrice() == price)
				{
					BuyProcess(respond, is_cfg, sync_player, sync_bag, ig_cfg.getPrice(), ig_cfg);
					//普通商店
					break;
				}
			}
		}
		else//随机商店
		{
			for (int i = 0; i < m_shop_info.shop_list.size(); ++i)
			{
				ShopInfo info = m_shop_info.shop_list.get(i);
				if (info.is_id != item_shop_id)
					continue;
				
				Refresh(Calendar.getInstance(), TimeUtils.TodayStart(), info);
				
				int found_idx = -1;
				for (int item_ix = 0; item_ix < info.gd_list.size(); ++item_ix)
				{
					if (item_ix == good_index 
						&& info.gd_list.get(item_ix) == item_good_id
						&& info.price_list.get(item_ix) == price)
					{
						found_idx = item_ix;
					}
				}
				if (found_idx == -1)
				{
					respond.header.rt_sub = 1131;
					break;
				}
				if (info.sold_out.get(found_idx))
				{
					respond.header.rt_sub = 1132;
					break;
				}
				boolean buy_success = BuyProcess(respond, is_cfg, sync_player, sync_bag
						, info.price_list.get(found_idx), ig_cfg);
				if (buy_success)
				{
					info.sold_out.set(found_idx, true);
					m_shop_db.save();
				}
			}
		}
		
		return respond;
	}
	
	public CommonMsg handleReqRefreshGood(CommonMsg respond, int item_shop_id)
	{
		ShopMsg msg = new ShopMsg();
		respond.body.shop = msg;
		msg.success = false;
		
		ShopInfo info = m_shop_info.Get(item_shop_id);
		if (info == null)
		{
			log.info("handleReqRefreshGood No item shop ID {} ", item_shop_id);
			return respond;
		}
		
		Itemshop is_cfg = ConfigConstant.tItemshop.get(item_shop_id);
		if (is_cfg == null)
		{
			log.info("handleReqRefreshGood() no itemshop config {}", item_shop_id);
			return respond;
		}
		
		Vip vip_cfg = ConfigConstant.tVip.get(m_player.vip_level);
		if (vip_cfg == null)
		{
			log.info("handleReqRefreshGood() no vip_cfg config {}", m_player.vip_level);
			return respond;
		}	
		
		Refresh(Calendar.getInstance(), TimeUtils.TodayStart(), info);
		if (info.refresh_cnt >= vip_cfg.getRefreshMax())
		{
			m_shop_db.save();
			respond.header.rt_sub = 1128;
			return respond;
		}
		
		int cost = ConfigConstant.tConf.getShopRefresh();
		if (!m_player.hasDiamond(cost))
		{
			m_shop_db.save();
			respond.header.rt_sub = 1005;
			return respond;
		}
		PlayerImpl.SubDiamond(m_player, cost);
		m_player_db.save();
		respond.body.sync_player_info = new SyncPlayerInfoMsg();
		respond.body.sync_player_info.diamond = -cost;
		
		info.refresh_cnt++;
		RefreshGood(info);
		m_shop_db.save();
		msg.shop_info = info;
		msg.success = true;
		//log.info("handleReqRefreshGood SUCCESS! ");
		return respond;
	}

	private boolean BuyProcess(CommonMsg respond, Itemshop is_cfg, SyncPlayerInfoMsg sync_player, SyncBagMsg sync_bag,
			int price, Itemgood ig_cfg) {
		boolean buy_success = false;
		Item item_cfg = ConfigConstant.tItem.get(ig_cfg.getItem());
		if (item_cfg.getIType() == ItemType.Equip)
		{
	    	if (m_bag.getEquipBagCapacity() - m_bag.equips.size() < PlayerBagInfo.MIN_SPACE)
	    	{
	    		respond.header.rt_sub = 1168;
	    		return buy_success;
	    	}
		}
		if (is_cfg.getType()[0] == PayType.Gold)
		{
			if (m_player.hasGold(price))
			{
				m_player.subGold(price);
				m_player_db.save();
				sync_player.gold = -price;						
				respond.body.sync_player_info = sync_player;
				buy_success = true;
			}
			else
			{
				respond.header.rt_sub = 1004;
			}
		}
		else if (is_cfg.getType()[0] == PayType.Diamond)
		{
			if (m_player.hasDiamond(price))
			{
				PlayerImpl.SubDiamond(m_player, price);
				m_player_db.save();
				sync_player.diamond = -price;
				respond.body.sync_player_info = sync_player;
				buy_success = true;
			}
			else
			{
				respond.header.rt_sub = 1005;
			}
		}
		else if (is_cfg.getType()[0] == PayType.Item)
		{
			int it_id = is_cfg.getType()[1];
			if (m_bag.hasItemCount(it_id, price))
			{
				m_bag.subItemCount(it_id, price);
				m_bag_db.save();
				sync_bag.items.put(it_id, m_bag.getItemCount(it_id));
				respond.body.sync_bag = sync_bag; 
				buy_success = true;
			}
			else
			{
				
			}
		}
		if (buy_success)
		{
			m_bag.addItemCount(ig_cfg.getItem(), ig_cfg.getCounts());
			m_bag_db.save();
			sync_bag.items.put(ig_cfg.getItem(), m_bag.getItemCount(ig_cfg.getItem()));
			respond.body.sync_bag = sync_bag;
			
			respond.body.shop.r_items = new ArrayList<SyncBagItem>();
			SyncBagItem ri = new SyncBagItem();
			ri.id = ig_cfg.getItem();
			ri.count = ig_cfg.getCounts();
			respond.body.shop.r_items.add(ri);
			respond.body.shop.success = true;
		}
		return buy_success;
	}
	
	private static void Refresh(Calendar now_cal, long today_start, ShopInfo info)
	{
		Itemshop is_cfg = ConfigConstant.tItemshop.get(info.is_id);
		if (null == is_cfg)
		{
			return;
		}
		
		long now = now_cal.getTimeInMillis();
    	int[] rt = new int[]{ConfigConstant.tConf.getRTime()};
    	if (DateUtil.checkPassTime(info.last_reset, now, rt))
    	{
    		info.refresh_cnt = 0;
			info.last_reset = now;
    	}
				
		long[] refresh_time = new long[is_cfg.getTime().length];
		for(int i = 0; i < refresh_time.length; ++i)
		{
			if (is_cfg.getTime()[i] < 0)
				break;
			int[] refresh_t = new int[]{is_cfg.getTime()[i]};
	    	if (DateUtil.checkPassTime(info.last_refresh, now, refresh_t))
	    	{
	    		RefreshGood(info);
				info.last_refresh = now;
	    	}
		}
	}
	
	private static void RefreshGood(ShopInfo info)
	{
		Itemshop is_cfg = ConfigConstant.tItemshop.get(info.is_id);
		if (null == is_cfg)
		{
			return;
		}
		
		if (is_cfg.getTime()[0] < 0 || is_cfg.getCounts() == 0)
		{
			return;
		}
		
		List<Itemgood> must_list = new ArrayList<Itemgood>();
		List<Itemgood> rand_list = new ArrayList<Itemgood>();
		List<Integer> rand_pro_list = new ArrayList<Integer>();
		for (int i = is_cfg.getItemGoodId()[0]; i <= is_cfg.getItemGoodId()[1]; ++i)
		{
			Itemgood ig_cfg = ConfigConstant.tItemgood.get(i);
			if (ig_cfg == null)
				continue;
			if (ig_cfg.getRandom() == -1)
				must_list.add(ig_cfg);
			else
			{
				rand_list.add(ig_cfg);
				rand_pro_list.add(ig_cfg.getRandom());
			}
		}
		
		info.gd_list.clear();
		info.sold_out.clear();
		info.price_list.clear();
		for (int i = 0; i < is_cfg.getCounts(); ++i)
		{
			Itemgood ig_cfg = null;
			if (i < must_list.size())
			{
				ig_cfg = must_list.get(i);
			}
			else
			{
				int hit = RandomMethod.CalcHitWhichIndex(rand_pro_list);
	    		if (hit == -1)
	    		{
	    			log.info("ShopImpl RefreshGood() Random Error!");
	    			continue;
	    		}
				ig_cfg = rand_list.get(hit);
			}
			info.gd_list.add(ig_cfg.getId());
			info.sold_out.add(false);
			info.price_list.add(ShopDiscount(ig_cfg));
		}
	}
	
	private static int ShopDiscount(Itemgood ig_cfg)
	{
		int origin = ig_cfg.getPrice();
		int off = 1000;
		if (ig_cfg.getOff() >= 1 && ig_cfg.getOff() <= 9)
		{
			double rate = (double)ig_cfg.getRate() / 1000;
			if (Math.random() <= rate)
				off = ig_cfg.getOff()  * 100;
		}
		if (off != 1000)
		{
			return (int)Math.floor(origin * ((float)off / 1000));
		}
		return origin;
	}
}

class PayType
{
	public static final int Gold = 1;
	public static final int Diamond = 2;
	public static final int Item = 3;
}


