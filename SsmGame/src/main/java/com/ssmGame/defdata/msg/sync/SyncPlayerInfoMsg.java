package com.ssmGame.defdata.msg.sync;

import java.util.List;

/**
 * 玩家信息同步消息
 * Created by WYM on 2016/11/7.
 */
public class SyncPlayerInfoMsg {

    public SyncPlayerInfoMsg(){}

    public SyncPlayerInfoMsg(boolean is_refresh){
        this.is_refresh = is_refresh;
    }

    public boolean is_refresh;              // 是否刷新（false则下发差异值，true则完全刷新。首次为true，一般为false。）
    public int team_lv;                     // 战队等级
    public double gold;                     // 金币
    public double diamond;                  // 钻石
    public double exp;                      // 经验值
    public int team_current_fighting;       // 队伍战力
    public int team_history_max_fighting;   // 战队的历史最高战斗力
    public int vip_level;                   // VIP等级
    public int acc_rmb;                     // 累计冲的钱数（必定全值）
    public List<Integer> vip_award;         // vip奖励（必定全值）
    public long last_gold_time;				// 上一次回复金币购买次数的时间
    public long gold_buy_cnt;				// 当天金币已经购买次数
}
