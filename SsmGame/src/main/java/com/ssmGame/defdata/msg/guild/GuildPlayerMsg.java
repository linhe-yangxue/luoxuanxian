package com.ssmGame.defdata.msg.guild;

/**
 * 公会玩家信息
 * Created by WYM on 2017/4/24.
 */
public class GuildPlayerMsg {
    public String uid;
    public Integer vip;
    public String avatar;
    public String username;
    public Integer team_lv;
    public Integer fighting;

    public Double dnt;         // 累计贡献
    public Double dnt_kyo;     // 今日贡献
    public Long last_btt;    // 最后一次关卡战斗时间
}
