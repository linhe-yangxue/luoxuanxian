package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import com.ssmCore.tool.colligate.DateUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Dailytask;

public class PlayerDailyTaskInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	public String _id;
	
    @Indexed
    public String uid;                      // 玩家唯一ID
    
    public long last_reset_time;			//上一次重置时间戳
    
    public List<DailyTaskInfo> tasks;		//身上的每日任务进度
    
    public List<Integer> rewards;		//已经领取的等级奖励ID tasklvreward表
    
	public void Init(String uuid) {
		uid = uuid;
		last_reset_time = 0;
		rewards = new ArrayList<Integer>();
		tasks = new ArrayList<DailyTaskInfo>();
		for (Dailytask d_cfg : ConfigConstant.tDailytask.values())
		{
			if (getTaskInfo(d_cfg.getID()) != null)
			{
				DailyTaskInfo n = new DailyTaskInfo();
				n.cnt = 0;
				n.id = d_cfg.getID();
			}
		}
	}
	
	public DailyTaskInfo getTaskInfo(int id)
	{
		for (DailyTaskInfo d_info : tasks)
		{
			if (d_info.id == id)
				return d_info;
		}
		return null;
	}
	
	public void Refresh(Calendar now_cal, long today_start)
	{
		long now = now_cal.getTimeInMillis();	
    	int[] rt = new int[]{ConfigConstant.tConf.getRTime()};
    	if (DateUtil.checkPassTime(last_reset_time, now, rt))
    	{
    		last_reset_time = now;
			for (DailyTaskInfo d_info : tasks)
			{
				d_info.cnt = 0;
			}
    	}
	}
}
