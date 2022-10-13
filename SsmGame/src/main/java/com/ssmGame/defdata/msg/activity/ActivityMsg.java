package com.ssmGame.defdata.msg.activity;

import java.util.List;
import com.ssmGame.defdata.msg.sync.SyncBagItem;
import com.ssmGame.module.activity.ActivityType;

/**
 * 活动消息体
 * Created by WYM on 2017/02/18.
 */
public class ActivityMsg {

    public boolean success = false;             // 是否成功

    // 上发数据
    public ActivityType type;                   // 活动类型
    public Integer reward_id;                       // 请求的奖励ID
    public Integer check_in_id;                     // 请求的签到ID
    public Integer gift_id;                         // 请求的礼包ID
    public Integer turn_id;                         // 转盘位置ID (0~7, 以配置表为准)

    // 累计充值活动
    public Integer acc_bill_rmb;			        // 累计充值的数值
    public List<Integer> acc_bill_award;        // 累计充值奖励

    // 7日登入活动
    public Integer seven_acc = 0;		        // 7日登陆活动的累计登陆日期
    public List<Integer> seven_award;           // 7日登陆活动已经领奖的列表

    // 限时7日登入活动
    public Integer limit_seven_acc = 0;		    // 7日登陆活动的累计登陆日期
    public List<Integer> limit_seven_award;     // 7日登陆活动已经领奖的列表

    // 等级成长活动
    public List<Integer> lvgrow_award;          // 等级成长已经领奖的列表

    // 关卡成长活动
    public List<Integer> gkgrow_award;          // 关卡成长已经领奖的列表

    // 成长基金
    public Boolean has_invest;  	            // 是否已经投资过
    public List<Integer> invest_award;          // 投资活动已经领奖的列表

    // 签到
    public List<Integer> check_in_date;         // 当月已签到日id
    public List<Integer> check_in_award;        // 当月已领取累计签到奖励id
    public Integer check_in_re;						// 当月已经补签次数

    // 超值礼包活动
    public List<Integer> gift_ids;              // 超值礼包已经购买的id
    public List<Integer> gift_cnts;             // 超值礼包已经购买的次数，跟id一一对应

    // 限时超值礼包活动
    public List<Integer> limit_gift_ids;        // 限时超值礼包已经购买的id
    public List<Integer> limit_gift_cnts;       // 限时超值礼包已经购买的次数，跟id一一对应

    // 累计消费活动
    public Double acc_spend;		            // 累计花费数量
    public List<Integer> acc_spend_award;       // 已经领取奖品id

    // 日充值活动
    public Integer day_pay_acc;		                // 日充值累计充值数量
    public List<Integer> day_pay_award;         // 日充值已经领取奖品id

    // 在线奖励活动
    public Long duration_acc;                   // 上次登录时的今日累计在线时间
    public List<Integer> duration_award;        // 在线奖励活动奖励领取情况

    // 钻石轮盘活动
    public Integer dmd_plate_cnt;                   // 钻石轮盘已转动次数

    // VIP每日礼包
    public Long vip_benefit_date;               // 上次领取VIP每日礼包时间

    // 获取的收益
    public double r_gold;                       // 获取的金币信息
    public double r_diamond;                    // 获取的钻石信息
    public List<SyncBagItem> r_items;           // 获取的背包物品信息

}
