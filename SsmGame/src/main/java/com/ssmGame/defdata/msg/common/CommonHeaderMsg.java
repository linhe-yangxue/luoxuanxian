package com.ssmGame.defdata.msg.common;

/**
 * 通用消息头
 * Created by WYM on 2016/10/26.
 */
public class CommonHeaderMsg {

    public int rt; // 返回码
    public int rt_msg; // 通用消息码
    public int rt_sub; // 子系统消息码
    public String uid; // uid id TODO 暂时传的是用户uid
    public String mkey; //登录密钥
    public String gid;
    public Integer zid; //登录区号
}
