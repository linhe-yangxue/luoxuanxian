package com.ssmGame.defdata.msg.duel;

import com.ssmData.dbase.RoleInfo;
import com.ssmGame.module.battle.BattlePack;

import java.util.List;

/**
 * 一骑当千战斗信息
 * Created by WYM on 2016/12/17.
 */
public class DuelBattleMsg {

    public int is_win;                  // 是否取得胜利
    public boolean e_is_player;             // 敌人是否玩家

    public String e_name;                   // 敌人名字（仅玩家）
    public String e_avatar;                 // 敌人头像URL（仅玩家）
    public int e_vip_lv;                   // 敌人VIP等级（仅玩家）
    public int e_robot_id;                  // 敌人机器人id（仅机器人）
    public Integer e_team_lv;

    public List<RoleInfo> e_team_leader;    // 敌人队伍队长信息 [1队队长, 2队队长, 3队队长]
    public List<Integer> e_team_fighting;   // 敌人队伍战力信息 [1队战力, 2队战力, 3队战力]

    public List<BattlePack> script;             // 战斗脚本内容

}
