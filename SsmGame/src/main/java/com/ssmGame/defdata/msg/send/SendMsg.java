package com.ssmGame.defdata.msg.send;

import com.ssmData.dbase.PlayerSendInfo;
import com.ssmData.dbase.RoleInfo;
import com.ssmData.dbase.SendQuest;
import com.ssmGame.module.battle.BattlePack;

import java.util.List;
import java.util.Map;

/**
 * 斗士外派消息
 * Created by WYM on 2017/07/25.
 */
public class SendMsg {

    public Boolean success; // 是否成功

    public String uid; // 玩家id
    public Integer zid; // 区服id
    public Integer quest_id; // 任务位id
    public Integer ene_quest_id; // 敌方任务位id
    public Integer send_id; // 任务位id
    public Integer log_id; // 战斗日志id

    public List<Integer> roles; // 斗士列表
    public Integer type; // 外派类型（是否至尊）
    public Integer rob_type; // 掠夺类型（是否仇人）
    public BattlePack battle;           // 战斗数据
    public String avatar;       // 玩家头像
  
    public List<SendQuestMsg> quests; // 外派任务列表
    public List<SendRobLogMsg> logs; // 外派掠夺战斗日志列表
    public List<SendRobTeamMsg> teams; // 外派掠夺队伍列表

    
    //hub用
    public Integer lv;  		//等级 hub用
	public String name;
	public Integer vip;
	public SendQuest hub_quest;    //报名数据
	public List<PlayerSendInfo.Team> hub_rob_teams; //匹配结果
	public List<String> hub_enemys;  //查询仇人
	public Map<Integer, RoleInfo> battle_roles; //战斗队伍
	public Integer rob_times; //被掠夺时，奖励倍数
}
