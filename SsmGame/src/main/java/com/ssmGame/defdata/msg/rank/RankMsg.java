package com.ssmGame.defdata.msg.rank;

import java.util.List;

import com.ssmData.dbase.PlayerRolesInfo;
import com.ssmGame.defdata.msg.guild.GuildSoloMsg;

/**
 * 排行榜消息
 * Created by WYM on 2016/12/5.
 */
public class RankMsg {

    public boolean success = false; // 是否成功/有效

    public List<RankPlayerInfo> players; // 排行榜列表
    public List<GuildSoloMsg> guilds; // 公会排行榜列表

    public int rank_type; // 排行类型
    public int my_rank; // 玩家当前排行

    public String uid; // 请求的玩家id
    public PlayerRolesInfo roles; // 玩家角色数据

}
