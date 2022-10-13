package com.ssmGame.defdata.msg.sync;

import java.util.ArrayList;
import java.util.List;

/**
 * 背包同步消息
 * Created by WYM on 2016/11/7.
 */
public class SyncEquipBagMsg {

    public boolean is_refresh;                                  // 是否刷新（false只下发有变化的道具，true则完全刷新。首次为true，一般为false。）
    public List<Integer> add = new ArrayList<Integer>();        // 本次新增的装备内容（如果为全部下发，则将所有道具填写到add部分）
    public List<Integer> sub = new ArrayList<Integer>();        // 本次扣减的装备内容

    public SyncEquipBagMsg(boolean is_refresh){
        this.is_refresh = is_refresh;
    }

    public SyncEquipBagMsg(){
        this.is_refresh = false;
    }

}