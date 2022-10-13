package com.ssmGame.defdata.msg.guildwar;

/**
 * 公会战战报消息体
 * Created by WYM on 2017/5/8.
 */
public class GuildWarLogMsg {

    public String id;           // 战报id
    public Integer result;      // 战斗结果 [负0, 胜1, 平2]
    public Integer star;        // 战斗取得/失去的星数
    public Boolean is_atk;      // 是否我方进攻[我方进攻T, 我方防守F]
    public String atk_uid;      // 进攻方uid
    public String atk_name;     // 进攻方名字
    public String def_uid;      // 防守方uid
    public String def_name;     // 防守方名字

}
