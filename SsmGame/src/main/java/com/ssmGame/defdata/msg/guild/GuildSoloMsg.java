package com.ssmGame.defdata.msg.guild;

/**
 * 公会信息
 * Created by WYM on 2017/4/24.
 */
public class GuildSoloMsg {

    public Integer rank;            // 公会当前排名

    public String id;               // 公会id
    public String name;             // 公会名称
    public Integer guild_lv;        // 公会等级
    public Integer cnt_member;      // 公会人数
    public Integer zid;             // 所在区服id
    public Long fighting;           // 公会总战力
    public Long score;              // 公会总积分

    public String chairman_uid;     // 会长uid
    public String chairman_name;    // 会长昵称

}
