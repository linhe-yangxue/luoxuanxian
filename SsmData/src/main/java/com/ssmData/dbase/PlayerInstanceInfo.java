package com.ssmData.dbase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class PlayerInstanceInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	public String _id;
	
    @Indexed
    public String uid;                      // 玩家唯一ID
    
    public int challenge_buy_cnt;		//当天已经购买过挑战次数的次数
    
    public long last_reset_time;				//上一次重置次数的时间
    
    // 关卡奖励
    public String reward_hash;             // 可领取奖励的hash，用于验证奖励有效性，为空时无可领取奖励（上线时清空）
    public int reward_instance_id;			//奖励副本的副本ID，用于验证奖励有效性，为空时无可领取奖励（上线时清空）
    
    public Map<Integer, Integer> instance_count;		//当前已经开启的关卡id，当天剩余的挑战次数
    
    public Map<Integer, Integer> farest_id;					//当前各类关卡的最远关
    
    public Map<Integer, Integer> far_win_id;				//当前各类关卡的最远已胜利关 
    
    public void Init(String player_id)
    {
    	uid = player_id;
    	challenge_buy_cnt = 0;
    	instance_count = new HashMap<Integer, Integer>();
    	reward_hash = "";
    	farest_id = new HashMap<Integer, Integer>();
    	far_win_id = new HashMap<Integer, Integer>();
    	reward_instance_id = -1;
    }
    
    
    //todo 根据等级开启新的副本
    /*public void RefreshFarestId(int current_lv)
    {
    	for (int i = 0; i < 1; ++i)
    	{
    		int test_id = 0;
    		Instance ins_config = ConfigConstant.tInstance.get(test_id);
    		if (ins_config == null)
    			continue;
    		if (farest_id.contains(ins_config.getID()))
    			continue;
    		farest_id.add(ins_config.getID());
    		instance_count.put(ins_config.getID(), ins_config.getFBchallenge());
    	}
    	
    	for (int far_id : farest_id)
    	{
    		int test_id = far_id;
    		while (true)
    		{
    			Instance ins_config = ConfigConstant.tInstance.get(test_id);
        		if (ins_config == null)
        			break;
        		Instance low_config = ConfigConstant.tInstance.get(ins_config.getLowLevel());
        		if (low_config == null)
        			break;
        		farest_id.add(low_config.getID());
        		instance_count.put(low_config.getID(), low_config.getFBchallenge());
        		test_id = low_config.getID();
    		}
    	}
    }*/
}
