package com.ssmGame.defdata.msg.arena;

import java.util.List;

import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.module.battle.BattlePack;

/**
 * 竞技场Msg
 * Created by WYM on 2016/12/5.
 */
public class ArenaMsg {

    public boolean success = false; // 是否成功

    public int enemy_rank_id; // 要挑战的对手排行
    public int reward_id; // 请求领取的奖励id
    public List<Integer> rank_reward_info; // 竞技场排名奖励状态

    public int my_cur_rank; // 玩家当前排名
    public int my_history_rank; // 玩家历史最高排名

    public List<ArenaEnemyInfo> enemies; // 对手列表

    public BattlePack battle_data; // 战斗数据

    public double r_gold; // 获取的金币信息
    public double r_diamond; // 获取的钻石信息
    public List<SyncBagItem> r_items; // 获取的背包物品信息

}
