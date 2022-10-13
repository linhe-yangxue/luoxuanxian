package com.ssmGame.defdata.msg.pve;

/**
 * PVE快速战斗消息
 * Created by WYM on 2016/11/4.
 */
public class PveQuickMsg {

    public boolean success; // 是否成功

    public int time; // 快速战斗时间
    public int gold; // 获取的金币
    public int exp;  // 获取的经验

    // 总快速战斗次数
    public int all_quick_count;
    // 今日快速战斗次数
    public int today_quick_count;
    // 下次快速战斗价格
    public int quick_next_price;

}
