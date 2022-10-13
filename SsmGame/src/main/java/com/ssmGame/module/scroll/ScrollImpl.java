package com.ssmGame.module.scroll;

import java.util.ArrayList;
import java.util.Calendar;
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
import com.ssmData.config.entity.Scroll;
import com.ssmData.dbase.PlayerInfo;
import com.ssmData.dbase.PlayerInfoDB;
import com.ssmData.dbase.PlayerScrollDB;
import com.ssmData.dbase.PlayerScrollInfo;
import com.ssmData.dbase.ScrollInfo;
import com.ssmGame.defdata.msg.common.CommonMsg;
import com.ssmGame.defdata.msg.scroll.ScrollMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
import com.ssmGame.module.player.PlayerImpl;
import com.ssmGame.util.TimeUtils;

@Service
@Scope("prototype")
public class ScrollImpl {

	private static final Logger log = LoggerFactory.getLogger(ScrollImpl.class);
	
	public final static ScrollImpl getInstance(){
        return SpringContextUtil.getBean(ScrollImpl.class);
	}
	
    @Autowired
    PlayerInfoDB m_player_db;
    PlayerInfo m_player = null;
    
    @Autowired
    PlayerScrollDB m_scroll_db;
    PlayerScrollInfo m_scroll;
    
    public ScrollImpl init(String player_id)
	{
    	m_scroll = m_scroll_db.loadByUid(player_id);
		m_player = m_player_db.loadById(player_id);
		
		if (null == m_scroll)
		{
			return null;
		}
		
		return this;
	}
    
	public void destroy()
	{
		m_player = null;
		m_player_db = null;
		m_scroll = null;
		m_scroll_db = null;
	}
	
	public ScrollImpl initInLogin(String player_id)
	{
		m_scroll = m_scroll_db.loadByUid(player_id);
		
		if (null == m_scroll)
		{
			m_scroll = (PlayerScrollInfo)m_scroll_db.createDB(player_id);
		}
		
		return this;
	}
	
	public CommonMsg handleNewPlayer(CommonMsg respond)
	{
		Map<Integer, Scroll> cfg_list = ConfigConstant.tScroll;
		for (Entry<Integer, Scroll> cfg : cfg_list.entrySet())
		{
			ScrollInfo s = new ScrollInfo();
			s.InitByConfig(cfg.getKey());
			m_scroll.scroll_list.add(s);
		}
		m_scroll_db.save();
		PlayerScrollInfo msg = new PlayerScrollInfo();
		msg.scroll_list = m_scroll.scroll_list;
		respond.body.playerLogin.scroll = msg;
		return respond;
	}
	
	public CommonMsg handleLogin(CommonMsg respond)
	{
		long today_start = TimeUtils.TodayStart();
		List<Integer> has_id = new ArrayList<Integer>();
		for (ScrollInfo s : m_scroll.scroll_list)
		{
			has_id.add(s.id);
			s.Refresh(Calendar.getInstance(), today_start);
		}
		Map<Integer, Scroll> cfg_list = ConfigConstant.tScroll;
		for (Entry<Integer, Scroll> cfg : cfg_list.entrySet())
		{
			if (has_id.contains(cfg.getKey().intValue()))
				continue;
			ScrollInfo s = new ScrollInfo();
			s.InitByConfig(cfg.getKey());
			m_scroll.scroll_list.add(s);
		}
		m_scroll_db.save();
		PlayerScrollInfo msg = new PlayerScrollInfo();
		msg.scroll_list = m_scroll.scroll_list;
		respond.body.playerLogin.scroll = msg;
		return respond;
	}
	
	//购买卷
	public CommonMsg buyScroll(CommonMsg respond, int scroll_id)
	{
		ScrollMsg s_msg = new ScrollMsg();
		s_msg.success = false;
		respond.body.scroll = s_msg;
		
		Scroll config = ConfigConstant.tScroll.get(scroll_id);
		if (config == null)
		{
			log.info("buyScroll() scroll_id {}, NO SCROLL Config", scroll_id);
			return respond;
		}
		
		ScrollInfo s = m_scroll.Get(scroll_id);
		if (s == null)
		{
			s = new ScrollInfo();
			s.InitByConfig(config.getID());
			m_scroll.scroll_list.add(s);
			m_scroll_db.save();
		}
		
		s.Refresh(Calendar.getInstance(), TimeUtils.TodayStart());
	
		if (config.getBuyTime()[m_player.vip_level] <= s.buy_cnt)
		{
			respond.header.rt_sub = config.getInfo();
			return respond;
		}
		
		double money = config.getInitialP() + config.getPriceM() * s.buy_cnt;
		if (!m_player.hasDiamond(money))
		{
			respond.header.rt_sub = 1005;
			return respond;
		}
		
		PlayerImpl.SubDiamond(m_player, money);
		m_player_db.save();
		respond.body.sync_player_info = new SyncPlayerInfoMsg();
		respond.body.sync_player_info.diamond = -money;
		
		s.buy_cnt++;
		s.count += config.getASpurchase();
		m_scroll_db.save();
		respond.body.sync_scroll = new PlayerScrollInfo();
		respond.body.sync_scroll.scroll_list = new ArrayList<ScrollInfo>();
		respond.body.sync_scroll.scroll_list.add(s);
		
		s_msg.success = true;
		return respond;
	}
}
