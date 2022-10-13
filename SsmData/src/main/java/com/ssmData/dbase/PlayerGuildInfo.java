package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.data.annotation.Id;

public class PlayerGuildInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    public String id;		//玩家ID

    public String gd_id = "";	//当前工会id
    
    public Long	quit_t = 0L;		//上次退出公会的时间戳，用于冷却申请
    
    public Integer daily_gold_dnt_cnt = 0;		//当天金币已经捐献的次数
    
    public Long last_gold_dnt_t = Calendar.getInstance().getTimeInMillis();			//上一次捐献时间
    
    public Integer daily_dmd_dnt_cnt = 0;		//当天钻石已经捐献的次数
    
    public Long last_dmd_dnt_t = Calendar.getInstance().getTimeInMillis();			//上一次捐献时间
    
    public Long last_war_t = 0L;			//上一次工会战挑战的时间
    
    public Byte daily_war_cnt = 0; //每天已经工会战挑战的次数
    
    public Integer raid_s = 0;		//当日掠夺积分
    
    public Long in_t = 0L;	//加入公会的时间戳 
    
    public List<String> apply = new ArrayList<String>();	//当前申请中的工会列表
}
