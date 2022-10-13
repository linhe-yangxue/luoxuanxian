package com.ssmGame.defdata.msg.battle;

import com.ssmGame.defdata.msg.sync.SyncBagItem;
import java.util.List;

/**
 * 秒伤消息体
 */
public class DmgMsg {

    public int reward_id;                               // 请求的奖励ID

    public boolean success;                             // 是否成功
    public int max = 0;			                        // 历史最高秒伤
    public List<Integer> award;                         // 已经领取过的奖励

    // 获取的收益
    public double r_gold;                               // 获取的金币信息
    public double r_diamond;                            // 获取的钻石信息
    public List<SyncBagItem> r_items;                   // 获取的背包物品信息

}
