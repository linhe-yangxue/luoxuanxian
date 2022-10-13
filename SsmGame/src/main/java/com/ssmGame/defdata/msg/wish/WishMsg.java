package com.ssmGame.defdata.msg.wish;

import com.ssmGame.defdata.msg.sync.SyncBagItem;

import java.util.List;

/**
 * 许愿消息体
 * Created by WYM on 2017/3/21.
 */
public class WishMsg {

    public boolean success;                             // 是否成功

    public int lv;                                      // 当前许愿等级
    public int exp;                                     // 当前许愿经验
    public int stamina;                                 // 当前能量
    public long stamina_rec;                             // 上次能量恢复时间
    public int role_id;                                 // 当前选择的斗士id

    // 获取的收益信息
    public double r_gold;                               // 获取的金币
    public double r_diamond;                            // 获取的钻石
    public List<SyncBagItem> r_items;                   // 获取的背包物品

}
