package com.ssmGame.defdata.msg.pve;

/**
 * PVE离线收益消息
 * Created by WYM on 2016/11/5.
 */
public class PveOfflineMsg {

    public long offline_time;       // 离线时间（秒）
    public int cur_level;           // 挂机关卡id

    public int win_count;           // 胜利场次数
    public int lose_count;          // 失败场次数

    public int base_exp;            // 基础经验收益
    public int sp_exp;              // 实发经验收益

    public int base_gold;           // 基础金币收益
    public int sp_gold;             // 实发金币收益

    public int sp_month_gold;       // 月卡加成经验收益
    public int sp_life_gold;        // 终生卡加成金币收益

    public int sp_month_exp;        // 月卡加成经验收益
    public int sp_life_exp;         // 终生卡加成经验收益

}
