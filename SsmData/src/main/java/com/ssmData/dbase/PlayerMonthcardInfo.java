package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class PlayerMonthcardInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	public String _id;
	
    @Indexed
    public String uid;                      // 玩家唯一ID
    
    public List<Integer> mc_ids; 			//拥有的卡ID
    
    public long	mc_end;					//月卡结束时间戳，毫秒
    
    public long last_mail_t;				//上一次发福利的时间戳，毫秒
    
    public int first_award_status;		//首充奖励状态值
    
    public long bill_t = 0L;			//充值时间戳
    public boolean has_bill = false;	//是否有充值数据    
    public double r_dmd = 0;			//显示用的钻石数据
    
    public List<Integer> finish_1st;   //已经充过首充的id
    
    public Long lvgift_t = 0L; //限时礼包过期时间戳
    public List<Integer> lvgift_id = new ArrayList<Integer>();  //已经激活的id列表
    public List<Integer> lvgift_st = new ArrayList<Integer>();  //已经激活的id的状态 
    
    public static int LVGIFT_ONSALE = 0;  //限时礼包出售中
    public static int LVGIFT_HOLDING = 1;	//等待领取
    public static int LVGIFT_OUT = 2;  //超时未购买
    public static int LVGIFT_FINISH = 3; //已经领取完成
    
    public List<Integer> spay_ids = new ArrayList<Integer>();  //已经激活的spay的id列表
    public List<Long> spay_t = new ArrayList<Long>();		//对应ID的激活时间戳
    public List<Integer> spay_r = new ArrayList<Integer>();  //已经领取的档位id
    
    public void Init(String uuid)
    {
    	uid = uuid;
    	mc_ids = new ArrayList<Integer>();
    	mc_end = 0;
    	last_mail_t = Calendar.getInstance().getTimeInMillis();
    	first_award_status = 0;
    	has_bill = false;
    	r_dmd = 0;
    	bill_t = 0L;
    }
    
    public boolean hasType(int id)
    {
    	return mc_ids.contains(id);
    }
    
    public void addCardId(int id)
    {
    	if (!mc_ids.contains(id))
    	{
    		mc_ids.add(id);
    	}
    }
    
    public void removeId(int id)
    {
    	for (int i = 0; i < mc_ids.size(); ++i) {
    		if (mc_ids.get(i) == id) {
    			mc_ids.remove(i);
    			break;
    		}
    	} 	
    }
}
