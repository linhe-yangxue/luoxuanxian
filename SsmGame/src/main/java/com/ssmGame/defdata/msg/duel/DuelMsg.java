package com.ssmGame.defdata.msg.duel;

import com.ssmData.dbase.PlayerDuelInfo;
import com.ssmGame.defdata.msg.sync.SyncBagItem;

import java.util.List;

/**
 * 一骑当千系统通用消息
 * Created by WYM on 2016/12/17.
 */
public class DuelMsg {

    public boolean success;                 // 是否成功

    public PlayerDuelInfo my_info;        // 玩家一骑当千信息
    public List<List<Integer>> my_teams;    // 玩家一骑当千队伍，结构为[队伍1[角色1, 角色2,...], 队伍2[...], ...]

    public int team_id;                     // 当前操作的队伍id
    public int team_pos;                    // 当前操作的队伍位置
    public int role_id;                     // 当前操作的角色信息
    public List<Integer> cur_team;          // 当前操作的队伍信息

    public DuelBattleMsg battle;            // 战斗信息

    public int score_id;                    // 功勋id
    public int wins_id;                     // 胜场id

    // 获取的道具信息
    public double r_gold;
    public double r_diamond;
    public List<SyncBagItem> r_items;

}

