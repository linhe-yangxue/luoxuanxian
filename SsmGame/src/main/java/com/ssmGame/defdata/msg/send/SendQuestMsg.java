package com.ssmGame.defdata.msg.send;

import java.util.*;

/**
 * 斗士外派任务信息
 * Created by WYM on 2017/7/26.
 */
public class SendQuestMsg {

    public Integer send_id;                     // 任务id（send表）
    public Long end_t;                          // 任务结束时间戳 未开始的任务默认为0
    public List<Integer> roles;                 // 参与角色id列表 按角色阵位顺序排序 无角色位置以-1填充 可为空或空列表

    public Integer cnt_rob = 0;                 // 掠夺次数
    public Integer cnt_be_rob = 0;              // 被掠夺次数

    public Integer type = SendRunType.Normal;   // 外派类型(是否至尊) 默认为0

}
