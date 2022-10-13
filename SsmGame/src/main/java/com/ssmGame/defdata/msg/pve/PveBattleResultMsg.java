package com.ssmGame.defdata.msg.pve;

import com.ssmGame.defdata.msg.battle.BattleResultMsg;

/**
 * PVE普通战斗结果
 * Created by 协议生成器 on 2016/11/4.
 */
public class PveBattleResultMsg { 

    // 是否合法
    public boolean is_legal; 
    // 战斗脚本
    public BattleResultMsg script;
    // 奖励id
    public String reward_hash;

}
