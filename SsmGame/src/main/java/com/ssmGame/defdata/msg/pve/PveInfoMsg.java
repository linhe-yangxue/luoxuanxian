package com.ssmGame.defdata.msg.pve;

/**
 * PVE基本信息消息
 * Created by WYM on 2016/11/5.
 */
public class PveInfoMsg {
    // 本关id
    public int cur_level;
    // 每小时金币收益
    public int gold_per_hour;
    // 每小时经验收益
    public int exp_per_hour;

    // 本关已击败敌人数
    public int enemy_killed;

    // 总快速战斗次数
    public int all_quick_count;
    // 今日快速战斗次数
    public int today_quick_count;
    // 今日最大快速战斗次数
    public int today_quick_limit;
    // 下次快速战斗价格
    public int quick_next_price;


    // 是否拥有auto权限
    public boolean auto_avaliable;

}
