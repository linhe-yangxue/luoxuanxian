package com.ssmData.dbase;

import java.io.Serializable;
import java.util.Calendar;

import com.ssmCore.tool.colligate.DateUtil;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Scroll;

public class ScrollInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public int id;							//scroll 配置表id
	public int count;				//当前剩余数量
	public int buy_cnt;			//当天已经购买过的次数
	public long last_time;				//上一次恢复的时间
	public long last_reset_time;     //上一次重置次数的时间
	
	
	public void InitByConfig(int s_id)
	{
		Scroll config = ConfigConstant.tScroll.get(s_id);
		if (null == config)
		{
			return;
		}
		
		id = config.getID();
		count = config.getUpperL();
		buy_cnt = 0;
		last_time = Calendar.getInstance().getTimeInMillis();
		last_reset_time = Calendar.getInstance().getTimeInMillis();
	}
	
	public Scroll Refresh(Calendar now_cal, long today_start)
	{
		Scroll config = ConfigConstant.tScroll.get(id);
		if (null == config)
		{
			return config;
		}
		
		long now = now_cal.getTimeInMillis();
		int max = config.getUpperL();
    	int[] rt = new int[]{ConfigConstant.tConf.getRTime()};
    	if (DateUtil.checkPassTime(last_reset_time, now, rt))
    	{
    		buy_cnt = 0;
			last_reset_time = now;
			if (config.getType() == ScrollRecoverType.Reset && count < max)
			{
				count = max;
				last_time = now;
			}
    	}
		
		if (count == max)
		{
			last_time = now;
		}
		else if (count < max && now > last_time && config.getType() == ScrollRecoverType.Recover)
		{	
			long delta = now - last_time;
			int add = (int)Math.floor((float)delta / config.getCSRTime());
			if (add < 0)
				add = max;
			count += add;
			count = Math.min(count, max);
			
			long add_time = add * config.getCSRTime();
			last_time += add_time;
			if (last_time < 0 || last_time > now)
				last_time = now;
		}
		
		return config;
	}
}

class ScrollRecoverType
{
	public static final int Recover = 1;
	public static final int Reset = 2;
}
