package com.ssmGame.defdata.msg.send;


/**
 * 斗士外派掠夺目标信息
 * Created by WYM on 2017/7/26.
 */
public class SendRobTeamMsg {

    public String uid;          // 玩家id
    public Integer zid;         // 区服id
    public String username;     // 玩家名字
    public Integer vip;          // 玩家VIP等级
    public Integer team_lv;      // 玩家战队等级

    public Integer quest_id;    // 队伍位id
    public Integer fighting;    // 队伍战斗力
    public SendQuestMsg quest;  // 任务信息

}
