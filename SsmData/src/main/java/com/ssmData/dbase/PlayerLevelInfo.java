package com.ssmData.dbase;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 主线关卡表
 * Created by WYM on 2016/11/1.
 */
@Document
public class PlayerLevelInfo implements Serializable  {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    public String _id;

    @Indexed
    public String uid;                      // 玩家唯一ID

    @Indexed
    public int cur_level;                   // 当前关卡id
    
    public long time;						//首次通关时间

    // 快速战斗
    public int today_quick_count;           // 当日快速战斗次数（取值时如果非当日5时至次日5时清空）
    public long last_quick_time;            // 上次快速战斗刷新时间
    public int all_quick_count;             // 总快速战斗次数

    // 挑战BOSS
    public int enemy_killed;                // 本关已击杀怪物波数

    // 喜从天降
    public int treasure_count;              // 喜从天降计数器（上线时清空）
    public boolean treasure_avaliable;      // 是否可领取喜从天降奖励（上线时清空）

    // 关卡奖励
    public String reward_hash;              // 可领取奖励的hash，用于验证奖励有效性，为空时无可领取奖励（上线时清空）
    public boolean reward_is_boss;          // 待领取的奖励是否是Boss奖励

}