package com.ssmGame.module.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.ssmCore.tool.colligate.DateUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Basicactivity;
import com.ssmData.config.entity.Beginactivity;
import com.ssmData.dbase.PlayerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivityType {

	private static Logger logger = LoggerFactory.getLogger(ActivityType.class);
	public static final int Seven = 1;	//7日登入活动ID1
	public static final int LvRank = 2;  //等级排名奖励活动ID2
	public static final int PveRank = 3;  //关卡排名奖励活动ID3
	public static final int AccRmb = 4; //累计充值活动ID4
	public static final int LimitSeven = 5;	//限时7日登入活动ID5
	public static final int CheckIn = 6;	//签到活动ID6
	public static final int LvGrow = 7; //等级成长活动ID7
	public static final int GkGrow = 8; //关卡成长活动ID8
	public static final int Invest = 9; //投资计划ID9
	public static final int Turnplate = 10; //幸运转盘
	public static final int Gift = 11; //超值礼包
	public static final int AccSpend = 12; //累计花费
	public static final int Daypay = 13; //每日充值
	public static final int LimitGift = 14; //限时超值礼包
	public static final int CDkey = 15; //激活码
	public static final int Duration = 16; //在线活动
	public static final int Dmdplate = 17; //钻石转盘
	public static final int Vipbonus = 18; //vip 福利活动
	public static final int Vipgift = 19; //vip 周礼包活动
	public static final int RoleInvest = 20; //斗士投资计划
	public static final int EnchantInvest = 21; //附魔投资计划
	public static final int JewelryInvest = 22; //饰品投资计划
	public static final int ExRole = 23; //EX角色礼包
	
	private static final int Always = 0; //永久活动
	
	public static final int GiftTypeBegin = 1; //开服超级礼包
	public static final int GiftTypeBasic = 2; //限时超级礼包
	
	public static final int BeginTypeSrv = 0; //开服活动以服务器开服时间计算
	public static final int BeginTypeSelf = 1; //开服活动以自己的创建时间计算
	
	public static boolean checkActivityOpen(int act_type, long test_time, PlayerInfo p_info)
	{

		if (p_info == null) {
		    logger.info("oynghu为空");
			return false;
		}
		Basicactivity ba = ConfigConstant.tBasicactivity.get(act_type);
		logger.info(new Gson().toJson(ConfigConstant.tBasicactivity));
		logger.info(new Gson().toJson(ba));

		boolean in_p = false;
		if (ba != null)
		{
			for (String s : ba.getPlatformID())
			{
				logger.info("pid循环" + s == null?"kong" : s);
				if (s.equals(p_info.user_base.pid))
				{
					in_p = true;
					break;
				}
			}
			if (in_p && ba.getType() == BasicactivityType.Always)
				return true;
		}
		
		Beginactivity be = ConfigConstant.tBeginactivity.get(act_type);
		logger.info(new Gson().toJson(ConfigConstant.tBeginactivity));
		logger.info(new Gson().toJson(be));
		if (be != null)
		{
			boolean be_in_p = false;
			for (String s : be.getPlatformID())
			{
				logger.info("pid循环" + s == null?"kong" : s);
				if (s.equals(p_info.user_base.pid))
				if (s.equals(p_info.user_base.pid))
				{
					be_in_p = true;
					break;
				}
			}
			if (be_in_p)
			{
				long start = -1;
				long end = -1;
				if (be.getType() == ActivityType.BeginTypeSrv) {
					start = getBeginOpen(act_type);
					end = getBeginEnd(act_type);
				} else if (be.getType() == ActivityType.BeginTypeSelf) {
					start = p_info.creat_t;
					end = start + (long)be.getTime();
				}
				if (start > -1 && end > -1 && test_time >= start && test_time <= end)
					return true;
			}
		}
		
		if (ba != null && in_p)
		{
			long start = getBasicOpen(act_type);
			long end = getBasicEnd(act_type);
			if (start > -1 && end > -1 && test_time >= start && test_time <= end)
				return true;
		}
		
		return false;
	}
	
	private static Map<Integer, Long> BeginOpen = new HashMap<Integer, Long>();
	private static Map<Integer, Long> BeginFinish = new HashMap<Integer, Long>();
	
	public static void InitBeginTimeTable(long server_start) {
		BeginOpen.clear();
		BeginFinish.clear();
		
		for (Entry<Integer, Beginactivity> a : ConfigConstant.tBeginactivity.entrySet()) {
			Basicactivity ba_cfg = ConfigConstant.tBasicactivity.get(a.getKey());
			if (ba_cfg != null && ba_cfg.getType() == ActivityType.Always) {
				continue;
			}
			Calendar s = Calendar.getInstance();
			s.setTimeInMillis(server_start);
			s.set(Calendar.HOUR_OF_DAY, 0);
			s.set(Calendar.MINUTE, 0);
			s.set(Calendar.SECOND, 0);
			s.set(Calendar.MILLISECOND, 0);
			long start = s.getTimeInMillis() + a.getValue().getDelayT() * DateUtil.ONE_DAY_TIME;
			long end = start + (long)a.getValue().getTime();
			BeginOpen.put(a.getKey(), start);
			BeginFinish.put(a.getKey(), end);
		}
	}
	
	public static long getBeginOpen(int type)
	{
		if (BeginOpen.containsKey(type))
			return BeginOpen.get(type);
		return -1;
	}
	
	public static long getBeginEnd(int type)
	{
		if (BeginFinish.containsKey(type))
			return BeginFinish.get(type);
		return -1;
	}
	
	private static Map<Integer, Long> BasicOpen = new HashMap<Integer, Long>();
	private static Map<Integer, Long> BasicFinish = new HashMap<Integer, Long>();
	
	public static long getBasicOpen(int type)
	{
		if (BasicOpen.containsKey(type))
			return BasicOpen.get(type);
		return -1;
	}
	
	public static long getBasicEnd(int type)
	{
		if (BasicFinish.containsKey(type))
			return BasicFinish.get(type);
		return -1;
	}
	
	public static void InitBasicTimeTable(long server_start)
	{
		try
		{
			BasicOpen.clear();
			BasicFinish.clear();

			SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");//HHmmss
			Date d = null;
			for (Entry<Integer, Basicactivity> a : ConfigConstant.tBasicactivity.entrySet())
			{
				if (a.getValue().getType() == BasicactivityType.Always || a.getValue().getOpenT().length() < 3) //检测一下时间字符串是不是太短
					continue;
				d = f.parse(a.getValue().getOpenT());
				long start = d.getTime();
				Calendar s = Calendar.getInstance();
				s.setTime(d);
				long s_start = s.getTimeInMillis() + a.getValue().getClose() * DateUtil.ONE_DAY_TIME;
				if (s_start > start)
					start = s_start;
				BasicOpen.put(a.getKey(), start);
				
				d = f.parse(a.getValue().getFinishT());
				Calendar dc = Calendar.getInstance();
				dc.setTime(d);
				long end = dc.getTimeInMillis();
				BasicFinish.put(a.getKey(), end);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

class BasicactivityType
{
	public static final int Normal = 1; //限时类型
	public static final int Always = 2; //永久类型
}
