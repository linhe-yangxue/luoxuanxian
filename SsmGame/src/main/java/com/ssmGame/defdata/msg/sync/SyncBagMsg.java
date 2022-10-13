package com.ssmGame.defdata.msg.sync;

import java.util.HashMap;
import java.util.Map;

/**
 * 背包同步消息
 * Created by WYM on 2016/11/7.
 */
public class SyncBagMsg {

    public boolean is_refresh;                                  // 是否刷新（false只下发有变化的道具，true则完全刷新。首次为true，一般为false。）
    public Map<Integer, Integer> items = new HashMap<Integer, Integer>();         // 道具内容

    public SyncBagMsg(boolean is_refresh){
        this.is_refresh = is_refresh;
    }

    public SyncBagMsg(){
        this.is_refresh = false;
    }

}
