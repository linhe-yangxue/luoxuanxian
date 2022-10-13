package com.ssmGame.defdata.msg.common;

import com.ssmGame.defdata.msg.sync.SyncBagItem;

import java.util.List;

/**
 * Created by WYM on 2017/6/1.
 */
public class RewardMsg {

    public static RewardMsg generate(double exp, double gold, double diamond, List<SyncBagItem> items) {
        RewardMsg result = new RewardMsg();
        result.exp = exp;
        result.gold = gold;
        result.diamond = diamond;
        result.items = items;
        return result;
    }

    public Double exp;                        // 获取的经验值信息
    public Double gold;                       // 获取的金币信息
    public Double diamond;                    // 获取的钻石信息
    public List<SyncBagItem> items;           // 获取的背包物品信息

}
