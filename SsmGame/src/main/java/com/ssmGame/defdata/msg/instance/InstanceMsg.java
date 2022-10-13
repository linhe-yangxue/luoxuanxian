package com.ssmGame.defdata.msg.instance;

import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.module.battle.BattlePack;

/**
 * 副本消息
 * Created by WYM on 2016/11/22.
 */
public class InstanceMsg {

    // 是否成功
    public boolean success;

    // 副本id
    public int instance_id;

    // 战斗数据包
    public BattlePack battle_data;

    // 奖励hash
    public String reward_hash;

    // 金币
    public int gold;
    // 经验
    public int exp;
    // 钻石
    public int diamond;
    // 背包物品
    public SyncBagItem[] bag_items;

}
