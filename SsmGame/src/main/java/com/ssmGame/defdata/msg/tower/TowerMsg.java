package com.ssmGame.defdata.msg.tower;

import com.ssmData.dbase.PlayerTowerInfo;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.module.battle.BattlePack;

import java.util.List;

/**
 * 试炼塔通用消息
 * Created by WYM on 2017/2/13.
 */
public class TowerMsg {

    // 是否成功
    public boolean success;

    // 玩家爬塔信息
    public PlayerTowerInfo info;

    // 要领取的首通奖励id
    public int reward_id;

    // 是否胜利
    public boolean is_win;

    // 战斗数据包
    public BattlePack battle_data;

    // 收益信息
    public double r_gold;
    public double r_diamond;
    public List<SyncBagItem> r_items;

}
