package com.ssmGame.defdata.msg.pve;

import com.ssmGame.defdata.msg.sync.SyncBagItem;

/**
 * PVE领奖结果
 * Created by 协议生成器 on 2016/11/4.
 */
public class PveRewardMsg { 

    // 奖励id
    public String reward_hash; 
    // 是否合法
    public boolean is_legal; 
    // 金币
    public int gold; 
    // 经验
    public int exp; 
    // 钻石
    public int diamond;
    // 背包物品
    public SyncBagItem[] bag_items;

    // 本关id
    public int cur_level;
    // 本关已击败敌人数
    public int enemy_killed;

    // 喜从天降是否可用
    public boolean fly_treasure;

}
