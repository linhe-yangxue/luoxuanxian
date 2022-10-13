package com.ssmGame.defdata.msg.common;

/**
 * 通信消息码定义
 * Created by WYM on 2016/10/26.
 */
public class MsgCode {

    // 正常情况
    public static final int SUCCESS = 0;
    public static final int FAILED = 1;

    // 策划通用错误
    public static final int DESIGN_ERR_PVE      = 101; // 关卡配表错误
    public static final int DESIGN_ERR_ROLE     = 102; // 角色配表错误
    public static final int DESIGN_ERR_DRAW     = 103; // 抽卡配表错误
    public static final int DESIGN_ERR_SCROLL   = 104; // 门票配表错误
    public static final int DESIGN_ERR_INSTANCE = 105; // 副本配表错误
    public static final int DESIGN_ERR_EQUIP 	= 106; // 装备配表错误
    public static final int DESIGN_ERR_ARENA 	= 107; // 竞技场配表错误
    public static final int DESIGN_ERR_RANK		= 108;	//排行榜错误
    public static final int DESIGN_ERR_SHOP		= 109;	//商店错误
    public static final int DESIGN_ERR_MAIL		= 110;	//邮件错误
    public static final int DESIGN_ERR_DUEL		= 111; //一骑当千错误
    public static final int DESIGN_ERR_DAILY_TASK = 112; //历练错误
    public static final int DESIGN_ERR_TASK 	= 113; //主线任务错误
    public static final int DESIGN_ERR_BILLING 	= 114; //支付错误
    public static final int DESIGN_ERR_PRIV		= 115; //月卡错误
    public static final int DESIGN_ERR_GIFT 	= 116; //GM发放错误
    public static final int DESIGN_ERR_TOWER 	= 117; //爬塔
    public static final int DESIGN_ERR_ACTIVITY = 118; //活动
    public static final int DESIGN_ERR_DMG 		= 119; //伤害统计
    public static final int DESIGN_ERR_LINEUP 		= 120; //阵容推荐
    public static final int DESIGN_ERR_WISH 		= 121; //许愿
    public static final int DESIGN_ERR_DRAMA 		= 122; //剧情
    public static final int DESIGN_ERR_GUILD 		= 123; //公会
    public static final int DESIGN_ERR_JEWELRY 		= 124; //饰品
    public static final int DESIGN_ERR_SNS 		= 125; //SNS
    public static final int DESIGN_ERR_BOSS 		= 126; //世界、个人BOSS
    public static final int DESIGN_ERR_VIPBENEFIT 		= 127; //VIP福利
    public static final int DESIGN_ERR_VIPGIFT 		= 128; //VIP周礼包
    public static final int DESIGN_ERR_EXROLE_GIFT 		= 129; //ExRole礼包
    public static final int DESIGN_ERR_SEND 		= 130; //斗士外派

    // ------ 网络错误(10001 - 10999)
    public static final int HTTP_INCORRECT_PARAM = 10001; // HTTP参数不正确
    public static final int HTTP_NO_RESPONSE = 10002; // HTTP无响应
    public static final int HTTP_MKEY_ERROR = 10003; //客户端的Mkey值异常

    // ------ 消息错误(11001 - 11999)
    public static final int MSG_INCORRECT_PARAM = 11001; // 消息内参数不正确

    // ------ 通用错误 (20001 - 29999)
    public static final int BASE_LOGIN_ERROR = 20001; // 登录错误

    // ------ 游戏模块错误 (30001 - 99999)
    public static final int PVE_ILLEGAL         = 30101; // 关卡请求不合法

}
