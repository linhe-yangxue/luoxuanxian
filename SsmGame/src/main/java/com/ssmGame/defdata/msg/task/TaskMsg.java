package com.ssmGame.defdata.msg.task;

import com.ssmGame.defdata.msg.sync.SyncBagItem;

import java.util.List;

/**
 * 任务通用消息
 * Created by WYM on 2016/12/27.
 */
public class TaskMsg {

    public boolean success; // 是否成功

    public int cur_id;		//当前任务状态

    public boolean is_finish; //当前任务是否完成

    public int arg;			//当前进度参数

    // 收益信息
    public double r_gold;
    public double r_diamond;
    public List<SyncBagItem> r_items;

}

