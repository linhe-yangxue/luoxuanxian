package com.ssmGame.defdata.msg.guild;

import com.ssmData.dbase.GuildInfo;
import com.ssmData.dbase.PlayerGuildInfo;
import com.ssmGame.defdata.msg.sync.SyncBagItem;

import java.util.List;

/**
 * 公会消息体
 * Created by WYM on 2017/4/24.
 */
public class GuildMsg {

    public Boolean success;                     // 是否成功

    public Boolean result;                      // 处理结果
    public String uid;                          // 用户id
    public String gid;                          // 公会id
    public String name;                         // 公会名称
    public Integer tech_id;                     // 科技id
    public GuildInfo g_info;                    // 公会信息（不含申请队列和成员列表）
    public PlayerGuildInfo my_info;             // 玩家个人公会相关信息

    public List<GuildPlayerMsg> user_list;      // 用户列表
    public List<GuildSoloMsg> guild_list;       // 公会列表
    public List<GuildSoloMsg> apply_list;       // 申请的公会列表
    public List<String> uid_list;              // 用户uid列表


    public Integer lv;						    // 公会等级
    public Double exp;				            // 公会经验
    public String words;				        // 公告

    public Integer daily_gold_dnt_cnt;		    // 当天金币已经捐献的次数
    public Long last_gold_dnt_t;			    // 金币上一次捐献时间

    public Integer daily_dmd_dnt_cnt;		    // 当天钻石已经捐献的次数
    public Long last_dmd_dnt_t;			        // 钻石上一次捐献时间

    // 获取的收益
    public Double r_gold;                       // 获取的金币信息
    public Double r_diamond;                    // 获取的钻石信息
    public List<SyncBagItem> r_items;           // 获取的背包物品信息

}
