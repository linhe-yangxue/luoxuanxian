package com.ssmData.dbase;

import java.io.Serializable;
import java.util.Calendar;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import com.ssmCore.tool.colligate.DateUtil;
import com.ssmData.config.constant.ConfigConstant;

public class PlayerDrawCardInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	public String _id;
	
    @Indexed
    public String uid;                      // 玩家唯一ID
    
    public long last_reset_lmt_time;			//上次重置抽的时间
    
    public long last_free_time;					//上一次免费的时间
    
    public int dmd_draw_cnt;		//钻石已抽取的次数 0-9循环
    
    public int dmd_is_free;			//当前单抽钻石是否免费 0-否 1-是
    
    public int lmt_draw_cnt;		//限量抽今日已抽取次数
    
    public boolean item_1st;		//是否是第一次道具抽
    
    public boolean dmd_1st;			//是否是第一次钻石抽
    
    public Integer dmd_ten_cnt;
    public Integer lmt_for_10_cnt;		//钻石已抽取的次数 0-9循环
    
    public void Init(String player_id)
    {
    	long now = Calendar.getInstance().getTimeInMillis();
    	uid = player_id;
    	last_reset_lmt_time = now;
    	last_free_time = now;
    	dmd_draw_cnt = 0;
    	dmd_is_free = 1;
    	lmt_draw_cnt = 0;
    	item_1st = true;
    	dmd_1st = true;
    	dmd_ten_cnt = 2;
    }
    
    public void Refresh(Calendar now_cal, long today_start)
    {
    	long now = now_cal.getTime().getTime();
    	
    	int[] dmd_free_reset_times = ConfigConstant.tConf.getCardTime();  	
    	if (DateUtil.checkPassTime(last_free_time, now, dmd_free_reset_times))
    	{
			dmd_is_free = 1;
			last_free_time = now;
    	}
    	
    	int[] rt = new int[]{ConfigConstant.tConf.getRTime()};
    	if (DateUtil.checkPassTime(last_reset_lmt_time, now, rt))
    	{
    		lmt_draw_cnt = 0;
    		last_reset_lmt_time = now;
    	}
    	return;
    }
}
