package com.ssmGame.defdata.msg.rank;

import com.ssmData.dbase.PlayerRolesInfo;

/**
 * 排行榜玩家信息
 * Created by WYM on 2016/12/5.
 */
public class RankPlayerInfo {
    public String uid; // 玩家id
    public int type; // 玩家类型
    public String nickname; // 玩家昵称
    public int vip_lv; // VIP等级
    public int team_lv; // 队伍等级
    public String guard_name; // 公会名称
    public String avatar_img; // 头像URL
    public int ranking; // 排名
    public double fighting; // 战斗力（仅竞技场排行榜）
    public int level; // 关卡id （仅关卡排行榜）
    public int tower; // 爬塔进度 （仅试炼塔排行榜）
    public PlayerRolesInfo roles_info; // 角色信息（用于角色详情，查询榜单时不下发）
}

