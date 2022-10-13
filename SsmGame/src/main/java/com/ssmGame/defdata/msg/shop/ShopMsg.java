package com.ssmGame.defdata.msg.shop;

import com.ssmData.dbase.ShopInfo;
import com.ssmGame.defdata.msg.sync.SyncBagItem;

import java.util.List;

/**
 * 商店消息
 * Created by WYM on 2016/12/14.
 */
public class ShopMsg {

    public boolean success; // 是否成功

    public int is_id; // 店铺itemshop id
    public ShopInfo shop_info; // 店铺信息

    public int good_list_id; // 货品在list中的位置
    public int good_id; // 货品id
    public int good_price; // 货品价格

    // 获取的道具信息
    public double r_gold;
    public double r_diamond;
    public List<SyncBagItem> r_items;

}

