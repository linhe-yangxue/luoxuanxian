package com.ssmGame.defdata.msg.guildwar;

/**
 * 公会战玩家个人信息
 * Created by WYM on 2017/5/8.
 */
public class GuildWarPlayerMsg {

    public String uid;
    public Integer vip;
    public String avatar;
    public String username;
    public Integer team_lv;
    public Integer fighting;

    public Integer star_lost; // 失去的星数
    public Integer star_gain; // 获得的星数
    public Integer tries;     // 已挑战次数

}
