package com.ssmGame.defdata.msg.common;

/**
 * 通用消息体
 * Created by WYM on 2016/10/26.
 */
public class CommonMsg {

    public CommonHeaderMsg header;
    public CommonBodyMsg body;

    /**
     * 构造函数
     * @param rtCode
     * @param uid
     */
    public CommonMsg(int rtCode, String uid){
        // 初始化子消息体
        this.header = new CommonHeaderMsg();
        this.body = new CommonBodyMsg();

        // 初始化返回信息
        this.header.rt = rtCode;
        this.header.uid = uid;
    }

    /**
     * 构造错误消息体(不带用户信息)
     * @param rtCode
     * @return
     */
    public static CommonMsg err(int rtCode){
        return CommonMsg.err(rtCode, "");
    }

    /**
     * 构造错误消息体
     * @param rtCode
     * @param uid
     * @return
     */
    public static CommonMsg err(int rtCode, String uid){
        return new CommonMsg(rtCode, uid);
    }

}
