package com.ssmGame.defdata.msg.sync;

import java.util.*;

/**
 * 背包道具消息
 * Created by WYM on 2016/11/7.
 */
public class SyncBagItem {

    public static SyncBagItem generate(Integer id, Integer count) {
        SyncBagItem result = new SyncBagItem();
        result.id = id;
        result.count = count;
        return result;
    }

    public static List<SyncBagItem> generateList(Integer[] ids, Integer[] counts) {
        if (ids == null || counts == null) {
            throw new NullPointerException("ids or counts is not defined.");
        }

        if (ids.length != counts.length) {
            throw new ArrayIndexOutOfBoundsException("the length of ids not equals to counts'");
        }

        List<SyncBagItem> result = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            result.add(SyncBagItem.generate(ids[i], counts[i]));
        }
        return result;
    }

    public int id;          // 道具id
    public int count;       // 道具数量

}
