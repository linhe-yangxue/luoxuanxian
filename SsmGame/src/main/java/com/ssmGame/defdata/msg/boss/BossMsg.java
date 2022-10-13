package com.ssmGame.defdata.msg.boss;

import com.ssmGame.module.battle.BattlePack;

import java.util.List;

/**
 * Created by WYM on 2017/6/1.
 */
public class BossMsg {

    public boolean success;

    public Integer boss_id;             // BOSS ID
    public String reward_hash;          // 奖励标识符

    public BattlePack battle;           // 战斗数据
    public List<BossStatusMsg> status;  // BOSS 状态
    public List<BossPlayerMsg> players; // 玩家信息
    public Double damage;               // 造成伤害

}
