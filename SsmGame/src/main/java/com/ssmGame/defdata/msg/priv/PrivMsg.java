package com.ssmGame.defdata.msg.priv;

import com.ssmData.dbase.PlayerMonthcardInfo;
import com.ssmGame.defdata.msg.sync.SyncBagItem;

import java.util.List;

/**
 * 特权消息
 * Created by WYM on 2017/1/16.
 */
public class PrivMsg {

    public boolean success = false;         // 是否成功

    public int vip_id;                      // VIP等级礼包id
    public int reward_id;                   // 奖励id

    public PlayerMonthcardInfo card;

    public Double p_diamond;                // 钻石全值
    public Double off_diamond;              // 钻石差值
    public int acc_rmb;                     // 累计冲的钱数
    public List<Integer> vip_award;         // vip奖励

    public Long lvgift_t;                   // 限时礼包过期时间戳
    public List<Integer> lvgift_id;         // 已经激活的限时礼包id列表
    public List<Integer> lvgift_st;         // 已经激活的限时礼包id的状态

    public double r_gold;                   // 获取的金币信息
    public double r_diamond;                // 获取的钻石信息
    public List<SyncBagItem> r_items;       // 获取的背包物品信息

}

