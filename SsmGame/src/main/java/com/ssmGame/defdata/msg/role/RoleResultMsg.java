package com.ssmGame.defdata.msg.role;

import com.ssmGame.defdata.msg.sync.SyncBagItem;

import java.util.List;
import java.util.Map;

/**
 * 角色操作通用消息体
 * Created by WYM on 2016/11/15.
 */
public class RoleResultMsg {

    // 操作的角色id
    public int role_id;

    // 队伍阵形
    public Map<Integer, Integer> opinion;

    // 要上阵的队伍位置
    public int team_pos;

    // 要上阵的副将位置
    public int backup_pos;

    // 是否成功
    public boolean is_success;

    // 奖励领取情况
    public List<Integer> award;

    // 请求的奖励ID
    public int reward_id;

    // 获取的收益
    public double r_gold;                               // 获取的金币信息
    public double r_diamond;                            // 获取的钻石信息
    public List<SyncBagItem> r_items;                   // 获取的背包物品信息

}
