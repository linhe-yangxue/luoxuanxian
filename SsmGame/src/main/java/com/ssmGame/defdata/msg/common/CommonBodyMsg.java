package com.ssmGame.defdata.msg.common;
import com.ssmCore.constants.ReInfo;
import com.ssmData.dbase.PlayerInstanceInfo;
import com.ssmData.dbase.PlayerRolesInfo;
import com.ssmData.dbase.PlayerScrollInfo;
import com.ssmGame.defdata.msg.activity.ActivityMsg;
import com.ssmGame.defdata.msg.arena.ArenaMsg;
import com.ssmGame.defdata.msg.battle.BattleResultMsg;
import com.ssmGame.defdata.msg.battle.DmgMsg;
import com.ssmGame.defdata.msg.boss.BossMsg;
import com.ssmGame.defdata.msg.daily.DailyTaskMsg;
import com.ssmGame.defdata.msg.drama.DramaMsg;
import com.ssmGame.defdata.msg.draw.DrawCardMsg;
import com.ssmGame.defdata.msg.duel.DuelMsg;
import com.ssmGame.defdata.msg.guild.GuildMsg;
import com.ssmGame.defdata.msg.guildwar.GuildWarMsg;
import com.ssmGame.defdata.msg.hub.HubGuildWarMsg;
import com.ssmGame.defdata.msg.instance.InstanceMsg;
import com.ssmGame.defdata.msg.mail.MailMsg;
import com.ssmGame.defdata.msg.player.PlayerLoginMsg;
import com.ssmGame.defdata.msg.priv.PrivMsg;
import com.ssmGame.defdata.msg.pve.PveBattleResultMsg;
import com.ssmGame.defdata.msg.pve.PveBossResultMsg;
import com.ssmGame.defdata.msg.pve.PveInfoMsg;
import com.ssmGame.defdata.msg.pve.PveQuickMsg;
import com.ssmGame.defdata.msg.pve.PveRewardMsg;
import com.ssmGame.defdata.msg.pve.PveTreasureMsg;
import com.ssmGame.defdata.msg.rank.RankMsg;
import com.ssmGame.defdata.msg.role.RoleChangeHeroMsg;
import com.ssmGame.defdata.msg.role.RoleEquipMsg;
import com.ssmGame.defdata.msg.role.RoleResultMsg;
import com.ssmGame.defdata.msg.scroll.ScrollMsg;
import com.ssmGame.defdata.msg.send.SendMsg;
import com.ssmGame.defdata.msg.shop.ShopMsg;
import com.ssmGame.defdata.msg.sync.SyncBagMsg;
import com.ssmGame.defdata.msg.sync.SyncEquipBagMsg;
import com.ssmGame.defdata.msg.sync.SyncPlayerInfoMsg;
import com.ssmGame.defdata.msg.sys.SysMsg;
import com.ssmGame.defdata.msg.task.TaskMsg;
import com.ssmGame.defdata.msg.tower.TowerMsg;
import com.ssmGame.defdata.msg.wish.WishMsg;
import com.ssmShare.order.GiftItem;
import com.ssmShare.order.GmItem;

/**
 * 通用消息体
 * Created by WYM on 2016/10/26.
 */
public class CommonBodyMsg {
	//客户端系统错误
	public SysMsg systemMsg;
	
    // 用户登录信息
    public PlayerLoginMsg playerLogin;

    // 战斗结果消息
    public BattleResultMsg battleResult;

    // PVE类结果
    public PveInfoMsg pveInfo; // PVE基本信息
    public PveBattleResultMsg pveBattleResult; // 普通战斗结果
    public PveBossResultMsg pveBossResult; // BOSS战斗结果
    public PveRewardMsg pveReward; // 战斗奖励信息
    public PveQuickMsg pveQuick; // 快速战斗信息
    public PveTreasureMsg pveTreasure; // 喜从天降信息

    // 角色相关消息
    public RoleChangeHeroMsg role_change_hero; // 上阵请求
    public RoleResultMsg role_result; // 角色通用消息
    public RoleEquipMsg role_equip; // 角色装备消息
    
    // 抽卡
    public DrawCardMsg draw_card; //抽卡信息

    // 挑战券
    public ScrollMsg scroll; // 挑战券通用信息

    // 副本
    public InstanceMsg instance; // 副本通用信息

    // 竞技场
    public ArenaMsg arena; // 竞技场通用信息

    // 排行榜
    public RankMsg rank; // 排行榜通用信息

    // 邮件
    public MailMsg mail; // 邮件通用消息

    // 商店
    public ShopMsg shop; // 商店通用消息

    // 一骑当千
    public DuelMsg duel; // 一骑当千通用消息

    // 每日任务（历练）
    public DailyTaskMsg daily_task; // 历练通用消息

    // 任务
    public TaskMsg task; // 任务通用消息

    // 特权
    public PrivMsg priv; // 特权消息

    // 试炼塔
    public TowerMsg tower; // 试炼塔通用消息

    // 活动通用消息
    public ActivityMsg activity; // 活动通用消息

    // 秒伤奖励
    public DmgMsg dmg; // 秒伤奖励

    // 许愿
    public WishMsg wish; // 许愿通用消息

    // 剧情
    public DramaMsg drama; // 剧情通用消息

    // 世界BOSS
    public BossMsg boss; // 世界BOSS通用消息

    // 公会消息
    public GuildMsg guild; // 公会通用消息
    public GuildWarMsg guild_war; // 公会战通用消息

    // 斗士外派
    public SendMsg send; // 斗士外派消息

    // 同步消息
    public SyncPlayerInfoMsg sync_player_info; // 玩家信息同步
    public SyncBagMsg sync_bag; // 背包同步
    public SyncEquipBagMsg sync_equip_bag; // 装备背包同步
    public PlayerRolesInfo sync_roles; // 角色信息同步
    public PlayerScrollInfo sync_scroll; // 挑战券信息同步
    public PlayerInstanceInfo sync_instance; // 副本同步消息

    // 奖励
    public RewardMsg reward; // 通用奖励消息

    //充值消息
    public GmItem billing;
    //GM发送物品消息
    public GiftItem gift;
    //用来返回给其他服务器的通用信息
    public ReInfo return_msg;
    
    //工会战游戏服和HUB之间的信息
    public HubGuildWarMsg hub_gw_msg;

    public Boolean isRepair;
}
