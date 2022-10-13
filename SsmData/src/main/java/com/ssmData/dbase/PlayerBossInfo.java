package com.ssmData.dbase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;

public class PlayerBossInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	public String id;
	
    // 关卡奖励
    public String reward_hash = "";             // 可领取奖励的hash，用于验证奖励有效性，为空时无可领取奖励（上线时清空）
    public Integer reward_b_id = 0;			//奖励副本的副本ID，用于验证奖励有效性，为空时无可领取奖励（上线时清空）
    
    public Map<Integer, Long> fb_t = new HashMap<Integer, Long>();		//上一次攻击时间戳
}
