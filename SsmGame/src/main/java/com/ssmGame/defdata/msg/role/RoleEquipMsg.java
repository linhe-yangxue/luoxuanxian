package com.ssmGame.defdata.msg.role;

import java.util.List;

import com.ssmGame.defdata.msg.sync.SyncBagItem;

/**
 * 角色装备操作消息体
 * Created by WYM on 2016/11/30.
 */
public class RoleEquipMsg {

    // 是否成功
    public boolean success;

    // 操作的角色id
    public Integer role_id;

    // 目标id
    public Integer target_id;

    // 操作的道具id
    public Integer item_id;

    // 选择的id
    public Integer sel_id;

    // 合成id
    public Integer comp_id;

    // 操作的数量
    public Integer count;

    // 操作的部位
    public Integer pos;

    // 操作的装备id
    public Integer equip_id;

    // 临时洗练列表
    public List<Integer> eht_temp;

    // 操作的部位List
    public List<Integer> pos_ex;

    // 操作的装备列表
    public List<Integer> equips;

    // 背包物品
    public SyncBagItem[] bag_items;

    // 获取的收益
    public double r_gold;                       // 获取的金币信息
    public double r_diamond;                    // 获取的钻石信息
    public List<SyncBagItem> r_items;           // 获取的背包物品信息

}
