package com.ssmGame.defdata.msg.arena;

import java.util.ArrayList;
import java.util.List;

/**
 * 竞技场敌人信息消息
 * Created by WYM on 2016/12/5.
 */
public class ArenaEnemyInfo {

    public int type = 0; // 类型，0为玩家，1为机器人
    public String uid = ""; // 玩家uid，如果是机器人，则为机器人id

    public int ranking; // 当前排名
    public int role_id; // 领队角色id
    public int vip_lv; // VIP等级
    public List<Integer> mc_ids = new ArrayList<Integer>(); // 玩家持有的月卡、终生卡ids
    public String avatar_img; // 头像URL
    public int team_lv; // 战队等级
    public int team_fighting; // 战队战斗力
    public String nickname; // 昵称

    public boolean challengable; // 能否挑战

}
