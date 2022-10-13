package com.ssmGame.defdata.msg.guildwar;

import com.ssmGame.defdata.msg.guild.GuildSoloMsg;
import com.ssmGame.module.battle.BattlePack;

import java.util.List;

/**
 * 公会战消息体
 * Created by WYM on 2017/5/8.
 */
public class GuildWarMsg {

    public Boolean success;                     // 是否成功

    public String uid;                          // 玩家uid
    public String guild_id;                     // 公会id

    public Integer status;                      // 公会战状态 [0正常, 1人数不足, 2轮空]
    public Integer match_type;                  // 匹配类型 [0本服, 1跨服]
    public GuildSoloMsg g_info;                 // 公会信息

    public Integer my_score;                    // 我方积分
    public Integer ene_score;                   // 敌方积分

    public Integer seed;                        // 随机数种子
    public List<GuildWarPlayerMsg> players;     // 玩家列表

    public BattlePack battle_data;              // 战斗数据

    public GuildWarPlayerMsg player;            // 玩家信息
    public GuildWarPlayerMsg player_2p;         // 玩家信息2p
    public Integer r_stars;                     // 获得星数
    public Integer r_dnt;                       // 获得贡献

    public Integer csd;                         // 本日已使用挑战机会（Chance Spend Daily）
    public Long last_csd;                       // 本日已使用挑战机会上次更新时间

    public String log_id;                       // 战报id
    public List<GuildWarLogMsg> logs;           // 战报列表

}
