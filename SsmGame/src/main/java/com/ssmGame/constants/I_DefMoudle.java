package com.ssmGame.constants;

/**
 * 模块接口定义
 *
 */
public interface I_DefMoudle {
	
	/*客户端系统
	    */
	public final static String SYS_WINDOW_ERROR = "/sys/wnd_err";  //客户端报错
	
	
	/**
	 * 获取战斗信息
	 */
	public final static String BATTLE_REQ = "/battle/req";
	public final static String BATTLE_INFO = "/battle/info";  //ws的测试


	/**
	 * 用户登录
	 */
	public final static String PLAYER_LOGIN = "/player/login"; //用户登陆 （不存在则创建，存在则登陆）
	
	public final static String PLAYER_CREATE = "/player/create"; //用户创建，选头像和昵称
	
	public final static String PLAYER_PLATFORM = "/player/platlogin"; //用户创建，选头像和昵称

	/**
	 * 主动同步
	 */
	public final static String PLAYER_INFO = "/player/info"; // 请求同步用户基本信息

	/**
	 * 背包信息
	 */
	public final static String BAG_TEST = "/bag/test"; // 背包测试

	/**
	 * 关卡系统
	 */
	public final static String PVE_INFO = "/pve/info";
	public final static String PVE_REQ_BATTLE = "/pve/battle";
    public final static String PVE_REQ_BOSS = "/pve/boss";
    public final static String PVE_REQ_QUICK = "/pve/quick";
    public final static String PVE_TREASURE = "/pve/treasure";

	/**
	 * 斗士系统
	 */
	public final static String ROLE_HERO_CHANGE = "/role/hero/change";
	public final static String ROLE_COMPOSE = "/role/compose";
	public final static String ROLE_LEVELUP = "/role/levelup";
	public final static String ROLE_LEVELUP_5 = "/role/levelup5";
	public final static String ROLE_BREACH = "/role/breach";
	public final static String ROLE_TALENT = "/role/talent";
	public final static String ROLE_AWAKEN = "/role/awaken";
	public final static String ROLE_AWAKEN_RESET = "/role/awaken/reset"; // 觉醒重置
	public final static String ROLE_BACKUP = "/role/backup";
	public final static String ROLE_OPINION = "/role/opinion";
	public final static String ROLE_LINEUP_INFO = "/role/lineup/info";
	public final static String ROLE_LINEUP_REWARD = "/role/lineup/reward";

	/**
	 * 装备
	 */
	public final static String EQUIP_BAG_INFO = "/equip/bag/info"; // 请求同步背包基本信息
	public final static String EQUIP_BAG_GIFT_USE_SWITCH = "/equip/bag/gift/use/switch"; //使用选择型礼包
	public final static String EQUIP_BAG_GIFT_USE_NORMAL = "/equip/bag/gift/use/normal"; //使用非选择型礼包
	public final static String EQUIP_BAG_GIFT_USE_RANDOM = "/equip/bag/gift/use/random"; //使用随机型礼包
	public final static String EQUIP_BAG_ITEM_COMPOSE = "/equip/bag/item/compose"; //进行道具合成
	public final static String EQUIP_COMPOSE = "/equip/compose"; // 装备合成
	public final static String EQUIP_CHANGE = "/equip/change"; // 装备穿戴/更换
	public final static String EQUIP_CHANGE_AUTO = "/equip/change/auto"; // 一键装备
	public final static String EQUIP_UNLOAD = "/equip/unload"; // 装备卸下
	public final static String EQUIP_STRENGTH = "/equip/strength"; // 装备强化
	public final static String EQUIP_STRENGTH_AUTO = "/equip/strength/auto"; // 一键强化
	public final static String EQUIP_REFINE = "/equip/refine"; // 装备精炼
	public final static String EQUIP_RESOLVE = "/equip/resolve"; // 装备分解
	public final static String EQUIP_ENCHANT_LV = "/equip/enchant/lv"; //附魔升级
	public final static String EQUIP_ENCHANT_BREACH = "/equip/enchant/breach"; //附魔突破
	public final static String EQUIP_ENCHANT_BREACH_DMD = "/equip/enchant/breach/dmd"; //附魔钻石突破
	public final static String EQUIP_ENCHANT_WASH = "/equip/enchant/wash"; //附魔洗属性
	public final static String EQUIP_ENCHANT_CHANGE = "/equip/enchant/change"; //附魔换属性
	public final static String EQUIP_ENCHANT_CHANGE_CONST = "/equip/enchant/change/const"; //附魔定向换属性
	public final static String EQUIP_ENCHANT_ONE_KEY = "/equip/enchant/onekey"; //一键附魔
	public final static String EQUIP_EXCHANGE_LV = "/equip/exchange/lv"; // 交换装备成长

	/**
	 * 饰品
	 */
	public final static String JEWELRY_STR = "/jewelry/str"; // 饰品强化
	public final static String JEWELRY_STR_AUTO = "/jewelry/str/auto"; // 饰品一键强化
	public final static String JEWELRY_EVO = "/jewelry/evo"; // 饰品进阶（提升经验）
	public final static String JEWELRY_EVO_AUTO = "/jewelry/evo/auto"; // 饰品一键进阶

	/**
     * 抽卡
     */
    public final static String DRAW_CARD_ITEM_ONE = "/draw/item1"; //物品一抽
    public final static String DRAW_CARD_ITEM_TEN = "/draw/item10"; //物品十抽
    public final static String DRAW_CARD_DIAMOND_ONE = "/draw/diamond1"; //钻石一抽
    public final static String DRAW_CARD_DIAMOND_TEN = "/draw/diamond10"; //钻石十抽
    public final static String DRAW_CARD_LIMIT_ONE = "/draw/limit1"; //限制一抽
    public final static String DRAW_CARD_LIMIT_TEN = "/draw/limit10"; //限制十抽

	/**
	 * 副本
	 */
	public final static String INSTANCE_INFO = "/instance/info"; // 获取副本基本信息
	public final static String INSTANCE_BUY_CHANCE = "/instance/buy"; // 购买副本挑战次数
	public final static String INSTANCE_BATTLE = "/instance/battle"; // 请求战斗
	public final static String INSTANCE_RESULT = "/instance/result"; // 请求获取结果
	public final static String INSTANCE_RAID = "/instance/raid"; // 进行扫荡

	/**
	 * 挑战券
	 */
	public final static String SCROLL_INFO = "/scroll/info"; // 获取挑战券信息
	public final static String SCROLL_BUY = "/scroll/buy"; // 购买挑战券


	/**
	 * 排行榜
	 */
	public final static String RANK_LIST_ARENA = "/rank/list/arena"; // 获取竞技场排行榜
	public final static String RANK_LIST_PVE = "/rank/list/pve"; // 获取关卡排行榜
	public final static String RANK_LIST_TOWER = "/rank/list/tower"; // 获取爬塔排行榜
	public final static String RANK_LIST_GUILD = "/rank/list/guild"; // 获取公会排行榜
	public final static String RANK_LIST_GUILD_WAR = "/rank/list/guild/war"; // 获取公会战排行榜
	public final static String RANK_LIST_TEAM_LV = "/rank/list/team_lv"; // 获取队伍等级排行榜
	public final static String RANK_ME = "/rank/me"; // 获取个人排名
	public final static String RANK_INFO = "/rank/info"; // 排行榜角色信息

	/**
	 * 竞技场
	 */
	public final static String ARENA_INIT = "/arena/init"; // 初始化竞技场排行榜
	public final static String ARENA_RANK = "/arena/rank"; // 获取玩家当前竞技场排名
	public final static String ARENA_ENEMY = "/arena/enemy"; // 获取一批对手信息（包括前三）
	public final static String ARENA_CHALLENGE = "/arena/challenge"; // 挑战竞技场对手一次
	public final static String ARENA_RAID = "/arena/raid"; // 扫荡竞技场
	public final static String ARENA_RANK_REWARD = "/arena/rank/reward"; // 领取排行奖励
	public final static String ARENA_REWARD_INFO = "/arena/reward/info"; // 初始化竞技场排行榜

	/**
	 * 邮件
	 */
	public final static String MAIL_CHECK = "/mail/check"; // 检查是否有新邮件
	public final static String MAIL_LIST = "/mail/list"; // 获取所有邮件列表
	public final static String MAIL_READ = "/mail/read"; // 阅读邮件
	public final static String MAIL_REWARD = "/mail/reward"; // 领取邮件物品
	public final static String MAIL_REWARD_ONEKEY = "/mail/reward/onekey"; // 一键领取邮件物品


	/**
	 * 商店
	 */
	public final static String SHOP_INFO = "/shop/info"; // 获取商店信息与物品
	public final static String SHOP_REFRESH = "/shop/refresh"; // 手动刷新商店物品
	public final static String SHOP_BUY = "/shop/buy"; // 购买商店物品
	public final static String SHOP_GOLD_BUY_INFO = "/shop/goldbuy/info"; //金币兑换信息
	public final static String SHOP_GOLD_BUY = "/shop/goldbuy"; //金币兑换

	/**
	 * 一骑当千
	 */
	public final static String DUEL_INFO = "/duel/info"; // 获取一骑当千基本信息
	public final static String DUEL_TEAM_CHANGE = "/duel/team/change"; // 请求更换上阵斗士
	public final static String DUEL_TEAM_CHANGE_ALL = "/duel/team/change/all"; // 请求一键上阵所有斗士
	public final static String DUEL_TEAM_OPINION = "/duel/team/opinion"; // 请求调整队伍阵形
	public final static String DUEL_CHALLENGE = "/duel/challenge"; // 请求进行一骑当千战斗
	public final static String DUEL_SCORE_REWARD = "/duel/score/reward"; // 请求领取一骑当千功勋奖励
	public final static String DUEL_WINS_REWARD = "/duel/wins/reward"; // 请求领取一骑当千每日胜场奖励
	public final static String DUEL_TEAM_DROP = "/duel/team/drop"; // 下阵一骑当千队伍

	/**
	 * 历练
	 */
	public final static String DAILY_TASK_INFO = "/daily/task/info"; // 获取历练基本信息
	public final static String DAILY_TASK_UPGRADE = "/daily/task/upgrade"; // 请求提升魅力等级
	public final static String DAILY_TASK_REWARD = "/daily/task/reward"; // 请求领取魅力奖励

	/**
	 * 任务
	 */
	public final static String TASK_INFO = "/task/info"; // 获取任务基本信息
	public final static String TASK_REWARD = "/task/reward"; // 请求领取任务奖励

	/**
	 * 特权
	 */
	public final static String PRIV_PAYBACK = "/priv/payback"; // 获取充值改变的信息
	public final static String PRIV_INFO = "/priv/info"; // 获取特权信息
	public final static String PRIV_FPAY = "/priv/fpay"; // 领取首冲奖励
	public final static String PRIV_CARD_MAIL = "/priv/card/mail"; // 请求卡邮件
	public final static String PRIV_VIP_GIFT = "/priv/vip/gift"; // 购买VIP礼包
	public final static String PRIV_LVGIFT_INFO = "/priv/lvgift/info"; // 请求限时礼包基本信息
	public final static String PRIV_LVGIFT_REWARD = "/priv/lvgift/reward"; // 请求领取限时礼包奖励
	public final static String PRIV_SPAY_REWARD = "/priv/spay/reward"; // 新首充领取奖励

	/**
	 * 试炼塔
	 */
	public final static String TOWER_INFO = "/tower/info"; // 试炼塔基本信息
	public final static String TOWER_BAT = "/tower/bat"; // 请求小怪战斗
	public final static String TOWER_BAT_BOSS = "/tower/bat/boss"; // 爬塔BOSS战斗
	public final static String TOWER_UPSTAIRS = "/tower/upstairs"; // 领取当前层奖励
	public final static String TOWER_RESET = "/tower/reset"; // 重置爬塔进度
	public final static String TOWER_RAID = "/tower/raid"; // 扫荡试炼塔
	public final static String TOWER_REWARD = "/tower/reward"; // 领取首通奖励

	/**
	 * 活动
	 */
	public final static String ACT_ACC_RMB_INFO = "/act/acc_rmb/info"; // 累计充值活动基本信息
	public final static String ACT_ACC_RMB_REWARD = "/act/acc_rmb/reward"; // 累计充值活动领取奖励
	public final static String ACT_SEVEN_INFO = "/act/seven/info"; // 7日登入活动基本信息
	public final static String ACT_SEVEN_REWARD = "/act/seven/reward"; // 7日登入活动领取奖励
	public final static String ACT_LIMIT_SEVEN_INFO = "/act/limit_seven/info"; // 限时7日登入活动基本信息
	public final static String ACT_LIMIT_SEVEN_REWARD = "/act/limit_seven/reward"; // 限时7日登入活动领取奖励
	public final static String ACT_LV_GROW_INFO = "/act/lv_grow/info"; // 等级成长活动基本信息
	public final static String ACT_LV_GROW_REWARD = "/act/lv_grow/reward"; // 等级成长活动领取奖励
	public final static String ACT_GK_GROW_INFO = "/act/gk_grow/info"; // 关卡成长活动基本信息
	public final static String ACT_GK_GROW_REWARD = "/act/gk_grow/reward"; // 关卡成长活动领取奖励
	public final static String ACT_INVEST_INFO = "/act/invest/info"; // 成长基金活动基本信息
	public final static String ACT_INVEST_REWARD = "/act/invest/reward"; // 成长基金活动领取奖励
	public final static String ACT_CHECK_IN_INFO = "/act/check_in/info"; // 签到活动基本信息
	public final static String ACT_CHECK_IN_EXEC = "/act/check_in/exec"; // 签到活动进行签到或补签
	public final static String ACT_CHECK_IN_REWARD = "/act/check_in/reward"; // 签到活动领取累计奖励
	public final static String ACT_GIFT_INFO = "/act/gift/info"; // 超值礼包活动基本信息
	public final static String ACT_GIFT_BUY = "/act/gift/buy"; // 超值礼包活动领取奖励
	public final static String ACT_LIMIT_GIFT_INFO = "/act/limit_gift/info"; // 限时超值礼包活动基本信息
	public final static String ACT_LIMIT_GIFT_BUY = "/act/limit_gift/buy"; // 限时超值礼包活动领取奖励
	public final static String ACT_ACC_SPEND_INFO = "/act/acc_spend/info"; // 累计消费活动基本信息
	public final static String ACT_ACC_SPEND_REWARD = "/act/acc_spend/reward"; // 累计消费活动领取奖励
	public final static String ACT_DAY_PAY_INFO = "/act/day_pay/info"; // 日充值活动基本信息
	public final static String ACT_DAY_PAY_REWARD = "/act/day_pay/reward"; // 日充值活动领取奖励
	public final static String ACT_TURN_PLATE_EXEC = "/act/turn_plate/exec"; // 幸运转盘活动转动一次
	public final static String ACT_DURATION_INFO = "/act/duration/info"; // 在线奖励活动基本信息
	public final static String ACT_DURATION_REWARD = "/act/duration/reward"; // 在线奖励活动领取奖励
	public final static String ACT_DMD_PLATE_INFO = "/act/dmd_plate/info"; // 钻石转盘活动基本信息
	public final static String ACT_DMD_PLATE_EXEC = "/act/dmd_plate/exec"; // 钻石转盘活动转动一次
	public final static String ACT_VIP_BENEFIT_INFO = "/act/vip_benefit/info"; // VIP每日礼包基本信息
	public final static String ACT_VIP_BENEFIT_EXEC = "/act/vip_benefit/exec"; // VIP每日礼包领取
	public final static String ACT_VIP_WEEKLY_INFO = "/act/vip_weekly/info"; // VIP周礼包基本信息
	public final static String ACT_VIP_WEEKLY_BUY = "/act/vip_weekly/buy"; // VIP周礼包购买
	public final static String ACT_INVEST_ROLE_INFO = "/act/invest/role/info"; // 斗士投资活动基本信息
	public final static String ACT_INVEST_ROLE_BUY = "/act/invest/role/buy"; // 斗士投资活动购买
	public final static String ACT_INVEST_ROLE_REWARD = "/act/invest/role/reward"; // 斗士投资活动领取奖励
	public final static String ACT_INVEST_ENCHANT_INFO = "/act/invest/enchant/info"; // 附魔投资活动基本信息
	public final static String ACT_INVEST_ENCHANT_BUY = "/act/invest/enchant/buy"; // 附魔投资活动购买
	public final static String ACT_INVEST_ENCHANT_REWARD = "/act/invest/enchant/reward"; // 附魔投资活动领取奖励
	public final static String ACT_INVEST_JEWELRY_INFO = "/act/invest/jewelry/info"; // 饰品投资活动基本信息
	public final static String ACT_INVEST_JEWELRY_BUY = "/act/invest/jewelry/buy"; // 饰品投资活动购买
	public final static String ACT_INVEST_JEWELRY_REWARD = "/act/invest/jewelry/reward"; // 饰品投资活动领取奖励
	public final static String ACT_EX_ROLE_INFO = "/act/ex_role/info"; // EX角色礼包基本信息
	public final static String ACT_EX_ROLE_BUY = "/act/ex_role/buy"; // EX角色礼包礼包购买

	/**
	 * 关注分享
	 */
	public final static String SNS_FOLLOW_REWARD = "/sns/follow/reward"; // 领取关注奖励
	public final static String SNS_INVITE_ONCE = "/sns/invite/once"; // 进行一次分享
	public final static String SNS_INVITE_REWARD = "/sns/invite/reward"; // 领取分享奖励
	public final static String SNS_QQ_SHORTCUT_ACCOMPLISH = "/sns/qq/shortcut/accomplish"; // 上报完成图标创建
	public final static String SNS_QQ_SHORTCUT_REWARD = "/sns/qq/shortcut/reward"; // 领取创建奖励

	/**
	 * 秒伤
	 */
	public final static String DMG_INFO = "/dmg/info"; // 秒伤基本信息
	public final static String DMG_REWARD = "/dmg/reward"; // 领取秒伤奖励

	/**
	 * 许愿
	 */
	public final static String WISH_INFO = "/wish/info"; // 许愿基本信息
	public final static String WISH_SEL = "/wish/sel"; // 选择许愿的斗士
	public final static String WISH_EXEC = "/wish/exec"; // 许愿一次
	public final static String WISH_EXEC_ONEKEY = "/wish/exec/onekey"; // 一键许愿
	public final static String WISH_LVUP_ITEM = "/wish/lvup/item"; // 道具提升许愿经验
	public final static String WISH_LVUP_DIAMOND = "/wish/lvup/diamond"; // 钻石提升许愿经验
	public final static String WISH_LVUP_ONEKEY = "/wish/lvup/onekey"; // 请求一键提升许愿等级

	/**
	 * 剧情
	 */
	public final static String DRAMA_BATTLE = "/drama/battle"; // 请求战斗脚本
	public final static String DRAMA_FIN_DIALOG = "/drama/fin/dialog"; // 标记剧情对话完成
	public final static String DRAMA_FIN_BATTLE = "/drama/fin/battle"; // 标记剧情战斗完成

	/**
	 * 世界BOSS
	 */
	public final static String BOSS_SB_BATTLE = "/boss/sb/battle"; // 挑战个人BOSS
	public final static String BOSS_SB_REWARD = "/boss/sb/reward"; // 个人BOSS挑战完成
	public final static String BOSS_FB_INFO = "/boss/fb/info"; // 获取全服BOSS信息
	public final static String BOSS_FB_BATTLE = "/boss/fb/battle"; // 挑战全服BOSS
	public final static String BOSS_FB_DAMAGE = "/boss/fb/damage"; // 获取全服BOSS伤害排行
	public final static String BOSS_FB_KILLER = "/boss/fb/killer"; // 获取全服BOSS击杀记录

	/**
	 * 公会
	 */
	public final static String GUILD_INFO = "/guild/info"; // 请求公会基本信息
	public final static String GUILD_INFO_MEMBERS = "/guild/info/members"; // 获取公会成员列表
	public final static String GUILD_INFO_LIST_INT = "/guild/info/list/int"; // 请求可加入的公会列表
	public final static String GUILD_CREATE = "/guild/create"; // 创建新公会
	public final static String GUILD_HR_LIST = "/guild/hr/list"; // 获取入会申请名单
	public final static String GUILD_HR_APPLY = "/guild/hr/apply"; // 申请加入公会
	public final static String GUILD_HR_REVOKE = "/guild/hr/revoke"; // 撤销加公会申请
	public final static String GUILD_HR_HANDLE = "/guild/hr/handle"; // 批准入会申请
	public final static String GUILD_HR_REJECT = "/guild/hr/reject"; // 拒绝入会申请
	public final static String GUILD_HR_EXIT = "/guild/hr/exit"; // 退出公会
	public final static String GUILD_DNT_DMD = "/guild/dnt/dmd"; // 钻石捐赠
	public final static String GUILD_DNT_GOLD = "/guild/dnt/gold"; // 金币捐赠
	public final static String GUILD_HR_RULER_SET = "/guild/hr/ruler/set"; // 设为副会长
	public final static String GUILD_HR_RULER_RELIEVE = "/guild/hr/ruler/relieve"; // 撤销副会长
	public final static String GUILD_HR_LEADER_SET = "/guild/leader/set"; // 转让会长
	public final static String GUILD_HR_DISMISS = "/guild/hr/dismiss"; // 开除公会成员
	public final static String GUILD_DISSOLVE = "/guild/dissolve"; // 解散公会
	public final static String GUILD_ANNOUNCE = "/guild/announce"; // 编辑公会公告
	public final static String GUILD_SEARCH = "/guild/search"; // 搜索公会
	public final static String GUILD_HR_HANDLE_ALL = "/guild/hr/handle/all"; // 批准所有入会申请
	public final static String GUILD_HR_REJECT_ALL = "/guild/hr/reject/all"; // 拒绝所有入会申请
	public final static String GUILD_TECH_UPGRADE = "/guild/tech/upgrade"; // 提升公会科技等级上限
	public final static String GUILD_TECH_LEVELUP = "/guild/tech/levelup"; // 提升个人公会科技等级

	/**
	 * 公会战
	 */
	public final static String GUILD_WAR_INFO = "/guild/war/info"; // 请求公会战基本信息
	public final static String GUILD_WAR_SCORE = "/guild/war/score"; // 请求公会战积分信息
	public final static String GUILD_WAR_LIST_ENEMY = "/guild/war/list/enemy"; // 请求公会战敌人列表
	public final static String GUILD_WAR_LIST_MY = "/guild/war/list/my"; // 请求公会战玩家列表
	public final static String GUILD_WAR_ATTACK = "/guild/war/attack"; // 请求进攻公会战对手
	public final static String GUILD_WAR_PLAYER = "/guild/war/player"; // 请求公会战玩家信息
	public final static String GUILD_WAR_LOG_LIST = "/guild/war/log/list"; // 请求获取公会战战报列表
	public final static String GUILD_WAR_LOG_PLAY = "/guild/war/log/play"; // 请求播放战报录像
	public final static String GUILD_WAR_MYSELF = "/guild/war/myself"; // 请求公会战玩家个人信息

	/**
	 * 斗士外派
	 */
	public final static String SEND_QUEST_INFO = "/send/quest/info"; // 请求外派任务基本信息
	public final static String SEND_QUEST_REFRESH = "/send/quest/refresh"; // 手动刷新外派任务
	public final static String SEND_QUEST_EXEC = "/send/quest/exec"; // 进行外派任务
	public final static String SEND_QUEST_OPINION = "/send/quest/opinion"; // 更改外派任务布阵
	public final static String SEND_QUEST_REWARD = "/send/quest/reward"; // 领取外派任务奖励
	public final static String SEND_QUEST_QUICK = "/send/quest/quick"; // 消耗钻石立即完成外派任务
	public final static String SEND_ROB_TEAMS = "/send/rob/teams"; // 获取掠夺目标列表
	public final static String SEND_ROB_REFRESH = "/send/rob/refresh"; // 手动刷新掠夺目标列表
	public final static String SEND_ROB_REVENGERS = "/send/rob/revengers"; // 获取仇人列表
	public final static String SEND_ROB_EXEC = "/send/rob/exec"; // 掠夺一个正在任务中的队伍
	public final static String SEND_LOG_LIST = "/send/log/list"; // 获取外派战斗日志列表
	public final static String SEND_LOG_REPLAY = "/send/log/replay"; // 重播战斗日志
	public final static String SEND_UTIL_AVATAR = "/send/util/avatar"; // 跨服获取玩家头像
	
	/*
	 *  支付
	 */
	public final static String BILL_BUY = "/bill/buy";  //充值购买
	public final static String BILL_GIFT = "/bill/gift"; //GM工具发送物品
	
	/*
	 *  Hub相关请求
	 */
	public final static String HUB_PREFIX = "/hub/";
	public final static String HUB_H2G_REQ_MATCH_INFO = "/hub/h2g/rmi";   //hub向游服请求匹配数据
	public final static String HUB_H2G_MATCH_RESULT = "/hub/h2g/smr"; //hub向游戏服下发匹配结果
	public final static String HUB_G2H_WAR_SCORE = "/hub/g2h/warscore"; //游戏服向hub请求今日分数
	public final static String HUB_G2H_ENEMY_LIST = "/hub/g2h/enemylist"; //游戏服向hub请求对手列表
	public final static String HUB_H2G_FW_ENEMY_LIST = "/hub/h2g/fwenemylist"; //hub转发查询对手列表给游服
	public final static String HUB_G2H_MY_LIST = "/hub/g2h/mylist"; //游戏服向hub请求对手列表
	public final static String HUB_G2H_WAR_PLAYER = "/hub/g2h/warplayer"; //游戏服向hub请求一个玩家
	public final static String HUB_H2G_FW_WAR_PLAYER = "/hub/h2g/fwwarplayer"; //hub转发查询玩家给游服
	public final static String HUB_G2H_GWAR_ATTACK = "/hub/g2h/gwarattack"; //工会发起攻击
	public final static String HUB_H2G_FW_GW_ATTACK = "/hub/h2g/fwgwattack"; //工会发起攻击
	public final static String HUB_G2H_LOGPLAY = "/hub/g2h/logplay"; //游戏服向hub请求录像数据
	public final static String HUB_G2H_WARRANK = "/hub/g2h/warrank"; //游戏服向hub请求排行榜
	public final static String HUB_H2G_GW_DAILY = "/hub/h2g/gwdaily"; //hub向游戏服下发每日奖励
	public final static String HUB_H2G_GW_WEEKLY = "/hub/h2g/gwweekly"; //hub向游戏服下发每周奖励
	/**
	 * 斗士外派 hub相关
	 */
	public final static String HUB_SEND_CHECKIN = "/hub/send/checkin";  //接受任务，报名
	public final static String HUB_SEND_REFRESH = "/hub/send/refresh";   //刷新掠夺列表
	public final static String HUB_SEND_FIND_ENEMY = "/hub/send/findenemy";   //仇人列表
	public final static String HUB_G2G_SEND_QUEST_ROLES = "/hub/g2g/send/quest_roles";   //查询掠夺对象的信息
	public final static String HUB_G2G_SEND_BATTLE_RET = "/hub/g2g/send/battle_ret";   //通知被掠夺战果
	public final static String HUB_G2G_SEND_AVATAR = "/hub/g2g/send/avatar";   //取头像

	/**
	 * 轮训，查询服务器是否维护
	 */
	public final static String POLLING_URL = "/client/polling"; // 客户端轮训是否维护
}