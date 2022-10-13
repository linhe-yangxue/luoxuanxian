package com.ssmGame.defdata.msg.daily;

import com.ssmData.dbase.PlayerDailyTaskInfo;
import com.ssmGame.defdata.msg.sync.SyncBagItem;

import java.util.List;

/**
 * 每日任务（历练）通用消息
 * Created by WYM on 2016/12/22.
 */
public class DailyTaskMsg {

    public boolean success;                // 是否成功

    public PlayerDailyTaskInfo my_info;    // 玩家历练信息

    public int charm_lv;                   // 魅力等级

    public int reward_id;                  // 奖励id

    public double r_gold;
    public double r_diamond;
    public List<SyncBagItem> r_items;

}

