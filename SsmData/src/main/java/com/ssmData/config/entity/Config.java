package com.ssmData.config.entity;

public class Config {
    /** 初始金币*/
    private int iniGold;
    /** 初始钻石*/
    private int iniDiamond;
    /** 初始角色*/
    private int[] iniPet;
    /** 初始队伍*/
    private int[] iniPart;
    /** 挂机搜索时间*/
    private int battletime;
    /** 基本战斗时间*/
    private int BCTime;
    /** 基本战斗时间后*/
    private int BCTimelast;
    /** 关卡区分*/
    private int BCTimelv;
    /** 索敌时间*/
    private int addspeed;
    /** 战斗上限时间*/
    private int battlelimit;
    /** 追击触发*/
    private int[] comboRate;
    /** 离线战斗时间（秒）*/
    private int OffCTime;
    /** 离线收益上限时间（秒）*/
    private int OffULTime;
    /** 战斗场景大小*/
    private int[] battleBackGroundSize;
    /** 战斗场景移动区域*/
    private int[] battleWalkSize;
    /** 战斗场景站位X坐标*/
    private int[] battlePosX;
    /** 战斗场景站位Y坐标*/
    private int[] battlePosY;
    /** 移动区域的原点Y坐标偏移*/
    private int battleWalkZoneY;
    /** 1号位锁敌优先级*/
    private int[] pos_1_targets;
    /** 2号4号位索敌优先级*/
    private int[] pos_2_4_targets;
    /** 3号5号位索敌优先级*/
    private int[] pos_3_5_targets;
    /** 投掷物速度*/
    private int throwSpeed;
    /** 战斗全屏技能坐标*/
    private int[] effectPos;
    /** 怒值上限*/
    private int rageMax;
    /** 攻击者加的怒值*/
    private int rageAtk;
    /** 被击中加的怒值*/
    private int rageBehit;
    /** 初始关卡*/
    private int InitialLevel;
    /** 快速战斗初始时间（秒）*/
    private int QCITime;
    /** 快速战斗最大时间（秒）*/
    private int QCULTime;
    /** 每日重置时间*/
    private int RTime;
    /** 周礼包重置时间*/
    private int[] WeekTime;
    /** 喜从天降倍数*/
    private int PSGold;
    /** 喜从天降间隔战斗次数*/
    private int[] PSTime;
    /** 离线收益倍数*/
    private int Multiple;
    /** 首充开启自动挑战*/
    private int SMCVIPlv;
    /** 关卡开启自动挑战*/
    private int SMCLevel;
    /** 队伍等级上限*/
    private int LevelCap;
    /** 装备数量上限*/
    private int EquipNum;
    /** 创建角色头像*/
    private String[] Head;
    /** 队伍格子开启条件*/
    private int[] Join;
    /** 副将格子解锁等级*/
    private int[] Backup;
    /** 快速战斗价格叠加数*/
    private int PriceM;
    /** 快速战斗初始价格*/
    private int QCPrice;
    /** 扫荡所需VIP等级*/
    private int MUPVip;
    /** 挑战次数初始价格*/
    private int FBFprice;
    /** 挑战次数叠加价格*/
    private int OverlayPrice;
    /** 挑战次数购买1次所得量*/
    private int BuyQuantity;
    /** 道具单抽费用*/
    private int[] CardItem;
    /** 钻石单抽费用*/
    private int CardDiamonds;
    /** 钻石十连抽费用*/
    private int CardDiamondsTen;
    /** 钻石十连抽道具*/
    private int CardTenItem;
    /** 钻石免费次数重置时间*/
    private int[] CardTime;
    /** 限量抽费用*/
    private int CardLimited;
    /** 限量十连抽费用*/
    private int CardLimitedTen;
    /** 抽十次必得该觉醒等级以上*/
    private int CardAwaken;
    /** 觉醒等级上限*/
    private int AwakenMax;
    /** 装备精炼上限*/
    private int RefineMax;
    /** 排行榜刷新间隔时间（毫秒）*/
    private int RankingTime;
    /** 挑战次数购买费用*/
    private int ArenaCost;
    /** 挑战次数最大值*/
    private int ArenaMax;
    /** 胜利奖励竞技点*/
    private int ArenaVictory;
    /** 失败奖励竞技点*/
    private int ArenaFailure;
    /** 竞技挑战秒数*/
    private int ArenaTime;
    /** 竞技点对应道具ID*/
    private int ArenaItem;
    /** 竞技场对应门票ID*/
    private int ArenaScroll;
    /** 竞技场跳过战斗所需VIP*/
    private int ArenaVip;
    /** 竞技场停战时间*/
    private int[] ArenaEnd;
    /** 竞技场场景*/
    private int ArenaScene;
    /** 一骑当千跳过战斗开启所需等级*/
    private int duelLV;
    /** 一骑当千跳过战斗开启所需VIP*/
    private int duelVIP;
    /** 一骑当千匹配*/
    private int[] duelMatch;
    /** 商店刷新费用*/
    private int ShopRefresh;
    /** 聊天所需战队等级*/
    private int Chat;
    /** 魅力点对应道具*/
    private int CharmItem;
    /** 分解对应道具ID*/
    private int ResolveItem;
    /** 道具招募第一次*/
    private int First1;
    /** 钻石招募第一次*/
    private int First2;
    /** 装备商店ID*/
    private int EquipShop;
    /** 普通捐献获得公会经验*/
    private int DonateExp1;
    /** 钻石捐献获得公会经验*/
    private int DonateExp2;
    /** 普通捐献获得道具ID*/
    private int Donate1;
    /** 普通捐献获得道具数量*/
    private int DonateNum1;
    /** 钻石捐献获得道具ID*/
    private int Donate2;
    /** 钻石捐献获得道具数量*/
    private int DonateNum2;
    /** 普通捐献价格*/
    private int DonateMoney;
    /** 钻石捐献价格*/
    private int DonateDiamonds;
    /** 公会最大申请人数*/
    private int GuildApply;
    /** 公会创建所需钻石*/
    private int GuildCreate;
    /** 公会初始资金*/
    private int GuildCapital;
    /** 公会创建所需VIP*/
    private int GuildVip;
    /** 入会冷却*/
    private int GuildCd;
    /** 首充开关*/
    private int FPaySwitch;
    /** 首充奖励*/
    private int[] FPay;
    /** 首充奖励数量*/
    private int[] FPayQ;
    /** 月卡时效*/
    private String Month;
    /** 附魔所需道具*/
    private int EnchantItem;
    /** 附魔更换属性所需道具*/
    private int EnchantChange;
    /** 附魔更换属性所需道具数量*/
    private int EnchantChangeNum;
    /** 附魔突破所需道具*/
    private int EnchantBreach;
    /** 附魔突破等级段*/
    private int[] EnchantLv;
    /** 附魔突破所需战队等级*/
    private int[] EnchantTeamLv;
    /** 附魔突破所需道具数量*/
    private int[] EnchantLvNum;
    /** 武器部位的附魔ID区间*/
    private int[] EnchantId1;
    /** 衣服部位的附魔ID区间*/
    private int[] EnchantId2;
    /** 裤子部位的附魔ID区间*/
    private int[] EnchantId3;
    /** 鞋子部位的附魔ID区间*/
    private int[] EnchantId4;
    /** 排名奖励时间区间*/
    private int[] ranktime;
    /** 金币兑换所要花费的钻石数*/
    private int GoldBuy;
    /** 金币兑换系数*/
    private int BuyGoldC;
    /** 爬塔BOSS挑战秒数*/
    private int TowerTime;
    /** 试练币ID*/
    private int Train;
    /** 补签所需花费钻石*/
    private int Retroactive;
    /** 钻石转盘消耗钻石*/
    private int DialDiamond;
    /** 幸运转盘启动所需道具*/
    private int DialItem;
    /** 幸运转盘权重*/
    private int DialValue;
    /** 直接挑战BOSS*/
    private int RushBoss;
    /** 许愿碎片数量*/
    private int WishChip;
    /** 许愿升级经验*/
    private int WishUpExp;
    /** 许愿升级钻石价格*/
    private int WishUpNum;
    /** 许愿升级道具id*/
    private int WishItem;
    /** 钻石提升和直升一级所需VIP*/
    private int WishVip;
    /** 钻石单抽积分*/
    private int OnePoint;
    /** 钻石十连抽积分*/
    private int TenPoint;
    /** 限量抽单抽积分*/
    private int LimitedPoint;
    /** 限量十连抽积分*/
    private int LimitedTenPoint;
    /** 积分道具*/
    private int PointItem;
    /** 钻石洗练所需VIP等级*/
    private int EnchantVip;
    /** 钻石洗练所需钻石数量*/
    private int EnchantVipNum;
    /** 挑战全服BOSS冷却*/
    private int BossCd;
    /** 公会战匹配时间*/
    private int GuildMate;
    /** 公会战开战时间*/
    private int[] GuildWar;
    /** 公会战匹配开服时间*/
    private int OpenDay;
    /** 公会战分组数量*/
    private int Grouping;
    /** 挑战次数*/
    private int GuildDekaron;
    /** 个人基础贡献*/
    private int BasicsPC;
    /** 个人额外贡献*/
    private int AddedPC;
    /** 胜利获得贡献*/
    private int GuildVictory;
    /** 失败获得贡献*/
    private int GuildFail;
    /** 平局获得贡献*/
    private int GuildDraw;
    /** 额外公会贡献*/
    private int AddedGuild;
    /** 会长胜利钻石*/
    private int CDRWin;
    /** 会长失败钻石*/
    private int CDRFail;
    /** 会长平局钻石*/
    private int CDRDraw;
    /** 公会战胜负结算时间*/
    private int[] Cutoff;
    /** 公会战排行结算时间*/
    private int[] WarRankTime;
    /** 公会战胜利邮件*/
    private int WarMail1;
    /** 公会战失败邮件*/
    private int WarMail2;
    /** 公会战平局邮件*/
    private int WarMail3;
    /** 饰品强化所需道具*/
    private int OrnamentsItem;
    /** 饰品进阶所需道具ID*/
    private int JewelryItem;
    /** 每次进阶获得经验*/
    private int JewelryItemExp;
    /** 关注奖励钻石*/
    private int Follow;
    /** 邀请次数*/
    private int InviteNum;
    /** 邀请给与的钻石数量*/
    private int InviteReward;
    /** 邀请冷却时间*/
    private int InviteTime;
    /** 觉醒重置所需钻石*/
    private int AwakenReset;
    /** 第一行小图标排序*/
    private int[] SubmenuSort1;
    /** 第二行小图标排序*/
    private int[] SubmenuSort2;
    /** 第三行小图标排序*/
    private int[] SubmenuSort3;
    /** 世界boss和一骑当千跳过倒计时*/
    private int SkipCd;
    /** 互换所需钻石*/
    private int Exchange;
    /** 玩吧桌面*/
    private int WanBa;
    /** 斗士投资计划*/
    private int[] InvestRole;
    /** 斗士投资计划*/
    private int[] InvestRoleID;
    /** 附魔投资计划*/
    private int[] InvestEnchant;
    /** 附魔投资计划*/
    private int[] InvestEnchantID;
    /** 饰品投资计划*/
    private int[] InvestJewelry;
    /** 饰品投资计划*/
    private int[] InvestJewelryID;
    /** 金币捐献获得资金*/
    private int MoneyCapital;
    /** 钻石捐献获得资金*/
    private int DiamondsCapital;
    /** 胜利者资金*/
    private int VictoryCapital;
    /** 失败者资金*/
    private int FailCapital;
    /** 平局资金*/
    private int DrawCapital;
    /** EX活动角色资源*/
    private String ExRole;
    /** EX活动角色ID*/
    private int[] ExRoleID;
    /** 首次十连权重*/
    private int FirstCard;
    /** 显示的外派任务数量*/
    private int SendIdNum;
    /** 外派任务每日接取数量对应门票表ID*/
    private int Send;
    /** 外派任务同时进行数量*/
    private int SendNum;
    /** 直接完成每分钟所需钻石*/
    private int SendMoney;
    /** 掠夺次数*/
    private int Rob;
    /** 被掠夺次数*/
    private int Plundered;
    /** 斗士外派匹配区间*/
    private int[] SendMate;


    /*** 创建角色发送邮件*/
    private int createMail;

    public int getIniGold() {
        return this.iniGold;
    }

    public void setIniGold(int iniGold) {
        this.iniGold = iniGold;
    }

    public int getIniDiamond() {
        return this.iniDiamond;
    }

    public void setIniDiamond(int iniDiamond) {
        this.iniDiamond = iniDiamond;
    }

    public int[] getIniPet() {
        return this.iniPet;
    }

    public void setIniPet(int[] iniPet) {
        this.iniPet = iniPet;
    }

    public int[] getIniPart() {
        return this.iniPart;
    }

    public void setIniPart(int[] iniPart) {
        this.iniPart = iniPart;
    }

    public int getBattletime() {
        return this.battletime;
    }

    public void setBattletime(int battletime) {
        this.battletime = battletime;
    }

    public int getBCTime() {
        return this.BCTime;
    }

    public void setBCTime(int BCTime) {
        this.BCTime = BCTime;
    }

    public int getBCTimelast() {
        return this.BCTimelast;
    }

    public void setBCTimelast(int BCTimelast) {
        this.BCTimelast = BCTimelast;
    }

    public int getBCTimelv() {
        return this.BCTimelv;
    }

    public void setBCTimelv(int BCTimelv) {
        this.BCTimelv = BCTimelv;
    }

    public int getAddspeed() {
        return this.addspeed;
    }

    public void setAddspeed(int addspeed) {
        this.addspeed = addspeed;
    }

    public int getBattlelimit() {
        return this.battlelimit;
    }

    public void setBattlelimit(int battlelimit) {
        this.battlelimit = battlelimit;
    }

    public int[] getComboRate() {
        return this.comboRate;
    }

    public void setComboRate(int[] comboRate) {
        this.comboRate = comboRate;
    }

    public int getOffCTime() {
        return this.OffCTime;
    }

    public void setOffCTime(int OffCTime) {
        this.OffCTime = OffCTime;
    }

    public int getOffULTime() {
        return this.OffULTime;
    }

    public void setOffULTime(int OffULTime) {
        this.OffULTime = OffULTime;
    }

    public int[] getBattleBackGroundSize() {
        return this.battleBackGroundSize;
    }

    public void setBattleBackGroundSize(int[] battleBackGroundSize) {
        this.battleBackGroundSize = battleBackGroundSize;
    }

    public int[] getBattleWalkSize() {
        return this.battleWalkSize;
    }

    public void setBattleWalkSize(int[] battleWalkSize) {
        this.battleWalkSize = battleWalkSize;
    }

    public int[] getBattlePosX() {
        return this.battlePosX;
    }

    public void setBattlePosX(int[] battlePosX) {
        this.battlePosX = battlePosX;
    }

    public int[] getBattlePosY() {
        return this.battlePosY;
    }

    public void setBattlePosY(int[] battlePosY) {
        this.battlePosY = battlePosY;
    }

    public int getBattleWalkZoneY() {
        return this.battleWalkZoneY;
    }

    public void setBattleWalkZoneY(int battleWalkZoneY) {
        this.battleWalkZoneY = battleWalkZoneY;
    }

    public int[] getPos_1_targets() {
        return this.pos_1_targets;
    }

    public void setPos_1_targets(int[] pos_1_targets) {
        this.pos_1_targets = pos_1_targets;
    }

    public int[] getPos_2_4_targets() {
        return this.pos_2_4_targets;
    }

    public void setPos_2_4_targets(int[] pos_2_4_targets) {
        this.pos_2_4_targets = pos_2_4_targets;
    }

    public int[] getPos_3_5_targets() {
        return this.pos_3_5_targets;
    }

    public void setPos_3_5_targets(int[] pos_3_5_targets) {
        this.pos_3_5_targets = pos_3_5_targets;
    }

    public int getThrowSpeed() {
        return this.throwSpeed;
    }

    public void setThrowSpeed(int throwSpeed) {
        this.throwSpeed = throwSpeed;
    }

    public int[] getEffectPos() {
        return this.effectPos;
    }

    public void setEffectPos(int[] effectPos) {
        this.effectPos = effectPos;
    }

    public int getRageMax() {
        return this.rageMax;
    }

    public void setRageMax(int rageMax) {
        this.rageMax = rageMax;
    }

    public int getRageAtk() {
        return this.rageAtk;
    }

    public void setRageAtk(int rageAtk) {
        this.rageAtk = rageAtk;
    }

    public int getRageBehit() {
        return this.rageBehit;
    }

    public void setRageBehit(int rageBehit) {
        this.rageBehit = rageBehit;
    }

    public int getInitialLevel() {
        return this.InitialLevel;
    }

    public void setInitialLevel(int InitialLevel) {
        this.InitialLevel = InitialLevel;
    }

    public int getQCITime() {
        return this.QCITime;
    }

    public void setQCITime(int QCITime) {
        this.QCITime = QCITime;
    }

    public int getQCULTime() {
        return this.QCULTime;
    }

    public void setQCULTime(int QCULTime) {
        this.QCULTime = QCULTime;
    }

    public int getRTime() {
        return this.RTime;
    }

    public void setRTime(int RTime) {
        this.RTime = RTime;
    }

    public int[] getWeekTime() {
        return this.WeekTime;
    }

    public void setWeekTime(int[] WeekTime) {
        this.WeekTime = WeekTime;
    }

    public int getPSGold() {
        return this.PSGold;
    }

    public void setPSGold(int PSGold) {
        this.PSGold = PSGold;
    }

    public int[] getPSTime() {
        return this.PSTime;
    }

    public void setPSTime(int[] PSTime) {
        this.PSTime = PSTime;
    }

    public int getMultiple() {
        return this.Multiple;
    }

    public void setMultiple(int Multiple) {
        this.Multiple = Multiple;
    }

    public int getSMCVIPlv() {
        return this.SMCVIPlv;
    }

    public void setSMCVIPlv(int SMCVIPlv) {
        this.SMCVIPlv = SMCVIPlv;
    }

    public int getSMCLevel() {
        return this.SMCLevel;
    }

    public void setSMCLevel(int SMCLevel) {
        this.SMCLevel = SMCLevel;
    }

    public int getLevelCap() {
        return this.LevelCap;
    }

    public void setLevelCap(int LevelCap) {
        this.LevelCap = LevelCap;
    }

    public int getEquipNum() {
        return this.EquipNum;
    }

    public void setEquipNum(int EquipNum) {
        this.EquipNum = EquipNum;
    }

    public String[] getHead() {
        return this.Head;
    }

    public void setHead(String[] Head) {
        this.Head = Head;
    }

    public int[] getJoin() {
        return this.Join;
    }

    public void setJoin(int[] Join) {
        this.Join = Join;
    }

    public int[] getBackup() {
        return this.Backup;
    }

    public void setBackup(int[] Backup) {
        this.Backup = Backup;
    }

    public int getPriceM() {
        return this.PriceM;
    }

    public void setPriceM(int PriceM) {
        this.PriceM = PriceM;
    }

    public int getQCPrice() {
        return this.QCPrice;
    }

    public void setQCPrice(int QCPrice) {
        this.QCPrice = QCPrice;
    }

    public int getMUPVip() {
        return this.MUPVip;
    }

    public void setMUPVip(int MUPVip) {
        this.MUPVip = MUPVip;
    }

    public int getFBFprice() {
        return this.FBFprice;
    }

    public void setFBFprice(int FBFprice) {
        this.FBFprice = FBFprice;
    }

    public int getOverlayPrice() {
        return this.OverlayPrice;
    }

    public void setOverlayPrice(int OverlayPrice) {
        this.OverlayPrice = OverlayPrice;
    }

    public int getBuyQuantity() {
        return this.BuyQuantity;
    }

    public void setBuyQuantity(int BuyQuantity) {
        this.BuyQuantity = BuyQuantity;
    }

    public int[] getCardItem() {
        return this.CardItem;
    }

    public void setCardItem(int[] CardItem) {
        this.CardItem = CardItem;
    }

    public int getCardDiamonds() {
        return this.CardDiamonds;
    }

    public void setCardDiamonds(int CardDiamonds) {
        this.CardDiamonds = CardDiamonds;
    }

    public int getCardDiamondsTen() {
        return this.CardDiamondsTen;
    }

    public void setCardDiamondsTen(int CardDiamondsTen) {
        this.CardDiamondsTen = CardDiamondsTen;
    }

    public int getCardTenItem() {
        return this.CardTenItem;
    }

    public void setCardTenItem(int CardTenItem) {
        this.CardTenItem = CardTenItem;
    }

    public int[] getCardTime() {
        return this.CardTime;
    }

    public void setCardTime(int[] CardTime) {
        this.CardTime = CardTime;
    }

    public int getCardLimited() {
        return this.CardLimited;
    }

    public void setCardLimited(int CardLimited) {
        this.CardLimited = CardLimited;
    }

    public int getCardLimitedTen() {
        return this.CardLimitedTen;
    }

    public void setCardLimitedTen(int CardLimitedTen) {
        this.CardLimitedTen = CardLimitedTen;
    }

    public int getCardAwaken() {
        return this.CardAwaken;
    }

    public void setCardAwaken(int CardAwaken) {
        this.CardAwaken = CardAwaken;
    }

    public int getAwakenMax() {
        return this.AwakenMax;
    }

    public void setAwakenMax(int AwakenMax) {
        this.AwakenMax = AwakenMax;
    }

    public int getRefineMax() {
        return this.RefineMax;
    }

    public void setRefineMax(int RefineMax) {
        this.RefineMax = RefineMax;
    }

    public int getRankingTime() {
        return this.RankingTime;
    }

    public void setRankingTime(int RankingTime) {
        this.RankingTime = RankingTime;
    }

    public int getArenaCost() {
        return this.ArenaCost;
    }

    public void setArenaCost(int ArenaCost) {
        this.ArenaCost = ArenaCost;
    }

    public int getArenaMax() {
        return this.ArenaMax;
    }

    public void setArenaMax(int ArenaMax) {
        this.ArenaMax = ArenaMax;
    }

    public int getArenaVictory() {
        return this.ArenaVictory;
    }

    public void setArenaVictory(int ArenaVictory) {
        this.ArenaVictory = ArenaVictory;
    }

    public int getArenaFailure() {
        return this.ArenaFailure;
    }

    public void setArenaFailure(int ArenaFailure) {
        this.ArenaFailure = ArenaFailure;
    }

    public int getArenaTime() {
        return this.ArenaTime;
    }

    public void setArenaTime(int ArenaTime) {
        this.ArenaTime = ArenaTime;
    }

    public int getArenaItem() {
        return this.ArenaItem;
    }

    public void setArenaItem(int ArenaItem) {
        this.ArenaItem = ArenaItem;
    }

    public int getArenaScroll() {
        return this.ArenaScroll;
    }

    public void setArenaScroll(int ArenaScroll) {
        this.ArenaScroll = ArenaScroll;
    }

    public int getArenaVip() {
        return this.ArenaVip;
    }

    public void setArenaVip(int ArenaVip) {
        this.ArenaVip = ArenaVip;
    }

    public int[] getArenaEnd() {
        return this.ArenaEnd;
    }

    public void setArenaEnd(int[] ArenaEnd) {
        this.ArenaEnd = ArenaEnd;
    }

    public int getArenaScene() {
        return this.ArenaScene;
    }

    public void setArenaScene(int ArenaScene) {
        this.ArenaScene = ArenaScene;
    }

    public int getDuelLV() {
        return this.duelLV;
    }

    public void setDuelLV(int duelLV) {
        this.duelLV = duelLV;
    }

    public int getDuelVIP() {
        return this.duelVIP;
    }

    public void setDuelVIP(int duelVIP) {
        this.duelVIP = duelVIP;
    }

    public int[] getDuelMatch() {
        return this.duelMatch;
    }

    public void setDuelMatch(int[] duelMatch) {
        this.duelMatch = duelMatch;
    }

    public int getShopRefresh() {
        return this.ShopRefresh;
    }

    public void setShopRefresh(int ShopRefresh) {
        this.ShopRefresh = ShopRefresh;
    }

    public int getChat() {
        return this.Chat;
    }

    public void setChat(int Chat) {
        this.Chat = Chat;
    }

    public int getCharmItem() {
        return this.CharmItem;
    }

    public void setCharmItem(int CharmItem) {
        this.CharmItem = CharmItem;
    }

    public int getResolveItem() {
        return this.ResolveItem;
    }

    public void setResolveItem(int ResolveItem) {
        this.ResolveItem = ResolveItem;
    }

    public int getFirst1() {
        return this.First1;
    }

    public void setFirst1(int First1) {
        this.First1 = First1;
    }

    public int getFirst2() {
        return this.First2;
    }

    public void setFirst2(int First2) {
        this.First2 = First2;
    }

    public int getEquipShop() {
        return this.EquipShop;
    }

    public void setEquipShop(int EquipShop) {
        this.EquipShop = EquipShop;
    }

    public int getDonateExp1() {
        return this.DonateExp1;
    }

    public void setDonateExp1(int DonateExp1) {
        this.DonateExp1 = DonateExp1;
    }

    public int getDonateExp2() {
        return this.DonateExp2;
    }

    public void setDonateExp2(int DonateExp2) {
        this.DonateExp2 = DonateExp2;
    }

    public int getDonate1() {
        return this.Donate1;
    }

    public void setDonate1(int Donate1) {
        this.Donate1 = Donate1;
    }

    public int getDonateNum1() {
        return this.DonateNum1;
    }

    public void setDonateNum1(int DonateNum1) {
        this.DonateNum1 = DonateNum1;
    }

    public int getDonate2() {
        return this.Donate2;
    }

    public void setDonate2(int Donate2) {
        this.Donate2 = Donate2;
    }

    public int getDonateNum2() {
        return this.DonateNum2;
    }

    public void setDonateNum2(int DonateNum2) {
        this.DonateNum2 = DonateNum2;
    }

    public int getDonateMoney() {
        return this.DonateMoney;
    }

    public void setDonateMoney(int DonateMoney) {
        this.DonateMoney = DonateMoney;
    }

    public int getDonateDiamonds() {
        return this.DonateDiamonds;
    }

    public void setDonateDiamonds(int DonateDiamonds) {
        this.DonateDiamonds = DonateDiamonds;
    }

    public int getGuildApply() {
        return this.GuildApply;
    }

    public void setGuildApply(int GuildApply) {
        this.GuildApply = GuildApply;
    }

    public int getGuildCreate() {
        return this.GuildCreate;
    }

    public void setGuildCreate(int GuildCreate) {
        this.GuildCreate = GuildCreate;
    }

    public int getGuildCapital() {
        return this.GuildCapital;
    }

    public void setGuildCapital(int GuildCapital) {
        this.GuildCapital = GuildCapital;
    }

    public int getGuildVip() {
        return this.GuildVip;
    }

    public void setGuildVip(int GuildVip) {
        this.GuildVip = GuildVip;
    }

    public int getGuildCd() {
        return this.GuildCd;
    }

    public void setGuildCd(int GuildCd) {
        this.GuildCd = GuildCd;
    }

    public int getFPaySwitch() {
        return this.FPaySwitch;
    }

    public void setFPaySwitch(int FPaySwitch) {
        this.FPaySwitch = FPaySwitch;
    }

    public int[] getFPay() {
        return this.FPay;
    }

    public void setFPay(int[] FPay) {
        this.FPay = FPay;
    }

    public int[] getFPayQ() {
        return this.FPayQ;
    }

    public void setFPayQ(int[] FPayQ) {
        this.FPayQ = FPayQ;
    }

    public String getMonth() {
        return this.Month;
    }

    public void setMonth(String Month) {
        this.Month = Month;
    }

    public int getEnchantItem() {
        return this.EnchantItem;
    }

    public void setEnchantItem(int EnchantItem) {
        this.EnchantItem = EnchantItem;
    }

    public int getEnchantChange() {
        return this.EnchantChange;
    }

    public void setEnchantChange(int EnchantChange) {
        this.EnchantChange = EnchantChange;
    }

    public int getEnchantChangeNum() {
        return this.EnchantChangeNum;
    }

    public void setEnchantChangeNum(int EnchantChangeNum) {
        this.EnchantChangeNum = EnchantChangeNum;
    }

    public int getEnchantBreach() {
        return this.EnchantBreach;
    }

    public void setEnchantBreach(int EnchantBreach) {
        this.EnchantBreach = EnchantBreach;
    }

    public int[] getEnchantLv() {
        return this.EnchantLv;
    }

    public void setEnchantLv(int[] EnchantLv) {
        this.EnchantLv = EnchantLv;
    }

    public int[] getEnchantTeamLv() {
        return this.EnchantTeamLv;
    }

    public void setEnchantTeamLv(int[] EnchantTeamLv) {
        this.EnchantTeamLv = EnchantTeamLv;
    }

    public int[] getEnchantLvNum() {
        return this.EnchantLvNum;
    }

    public void setEnchantLvNum(int[] EnchantLvNum) {
        this.EnchantLvNum = EnchantLvNum;
    }

    public int[] getEnchantId1() {
        return this.EnchantId1;
    }

    public void setEnchantId1(int[] EnchantId1) {
        this.EnchantId1 = EnchantId1;
    }

    public int[] getEnchantId2() {
        return this.EnchantId2;
    }

    public void setEnchantId2(int[] EnchantId2) {
        this.EnchantId2 = EnchantId2;
    }

    public int[] getEnchantId3() {
        return this.EnchantId3;
    }

    public void setEnchantId3(int[] EnchantId3) {
        this.EnchantId3 = EnchantId3;
    }

    public int[] getEnchantId4() {
        return this.EnchantId4;
    }

    public void setEnchantId4(int[] EnchantId4) {
        this.EnchantId4 = EnchantId4;
    }

    public int[] getRanktime() {
        return this.ranktime;
    }

    public void setRanktime(int[] ranktime) {
        this.ranktime = ranktime;
    }

    public int getGoldBuy() {
        return this.GoldBuy;
    }

    public void setGoldBuy(int GoldBuy) {
        this.GoldBuy = GoldBuy;
    }

    public int getBuyGoldC() {
        return this.BuyGoldC;
    }

    public void setBuyGoldC(int BuyGoldC) {
        this.BuyGoldC = BuyGoldC;
    }

    public int getTowerTime() {
        return this.TowerTime;
    }

    public void setTowerTime(int TowerTime) {
        this.TowerTime = TowerTime;
    }

    public int getTrain() {
        return this.Train;
    }

    public void setTrain(int Train) {
        this.Train = Train;
    }

    public int getRetroactive() {
        return this.Retroactive;
    }

    public void setRetroactive(int Retroactive) {
        this.Retroactive = Retroactive;
    }

    public int getDialDiamond() {
        return this.DialDiamond;
    }

    public void setDialDiamond(int DialDiamond) {
        this.DialDiamond = DialDiamond;
    }

    public int getDialItem() {
        return this.DialItem;
    }

    public void setDialItem(int DialItem) {
        this.DialItem = DialItem;
    }

    public int getDialValue() {
        return this.DialValue;
    }

    public void setDialValue(int DialValue) {
        this.DialValue = DialValue;
    }

    public int getRushBoss() {
        return this.RushBoss;
    }

    public void setRushBoss(int RushBoss) {
        this.RushBoss = RushBoss;
    }

    public int getWishChip() {
        return this.WishChip;
    }

    public void setWishChip(int WishChip) {
        this.WishChip = WishChip;
    }

    public int getWishUpExp() {
        return this.WishUpExp;
    }

    public void setWishUpExp(int WishUpExp) {
        this.WishUpExp = WishUpExp;
    }

    public int getWishUpNum() {
        return this.WishUpNum;
    }

    public void setWishUpNum(int WishUpNum) {
        this.WishUpNum = WishUpNum;
    }

    public int getWishItem() {
        return this.WishItem;
    }

    public void setWishItem(int WishItem) {
        this.WishItem = WishItem;
    }

    public int getWishVip() {
        return this.WishVip;
    }

    public void setWishVip(int WishVip) {
        this.WishVip = WishVip;
    }

    public int getOnePoint() {
        return this.OnePoint;
    }

    public void setOnePoint(int OnePoint) {
        this.OnePoint = OnePoint;
    }

    public int getTenPoint() {
        return this.TenPoint;
    }

    public void setTenPoint(int TenPoint) {
        this.TenPoint = TenPoint;
    }

    public int getLimitedPoint() {
        return this.LimitedPoint;
    }

    public void setLimitedPoint(int LimitedPoint) {
        this.LimitedPoint = LimitedPoint;
    }

    public int getLimitedTenPoint() {
        return this.LimitedTenPoint;
    }

    public void setLimitedTenPoint(int LimitedTenPoint) {
        this.LimitedTenPoint = LimitedTenPoint;
    }

    public int getPointItem() {
        return this.PointItem;
    }

    public void setPointItem(int PointItem) {
        this.PointItem = PointItem;
    }

    public int getEnchantVip() {
        return this.EnchantVip;
    }

    public void setEnchantVip(int EnchantVip) {
        this.EnchantVip = EnchantVip;
    }

    public int getEnchantVipNum() {
        return this.EnchantVipNum;
    }

    public void setEnchantVipNum(int EnchantVipNum) {
        this.EnchantVipNum = EnchantVipNum;
    }

    public int getBossCd() {
        return this.BossCd;
    }

    public void setBossCd(int BossCd) {
        this.BossCd = BossCd;
    }

    public int getGuildMate() {
        return this.GuildMate;
    }

    public void setGuildMate(int GuildMate) {
        this.GuildMate = GuildMate;
    }

    public int[] getGuildWar() {
        return this.GuildWar;
    }

    public void setGuildWar(int[] GuildWar) {
        this.GuildWar = GuildWar;
    }

    public int getOpenDay() {
        return this.OpenDay;
    }

    public void setOpenDay(int OpenDay) {
        this.OpenDay = OpenDay;
    }

    public int getGrouping() {
        return this.Grouping;
    }

    public void setGrouping(int Grouping) {
        this.Grouping = Grouping;
    }

    public int getGuildDekaron() {
        return this.GuildDekaron;
    }

    public void setGuildDekaron(int GuildDekaron) {
        this.GuildDekaron = GuildDekaron;
    }

    public int getBasicsPC() {
        return this.BasicsPC;
    }

    public void setBasicsPC(int BasicsPC) {
        this.BasicsPC = BasicsPC;
    }

    public int getAddedPC() {
        return this.AddedPC;
    }

    public void setAddedPC(int AddedPC) {
        this.AddedPC = AddedPC;
    }

    public int getGuildVictory() {
        return this.GuildVictory;
    }

    public void setGuildVictory(int GuildVictory) {
        this.GuildVictory = GuildVictory;
    }

    public int getGuildFail() {
        return this.GuildFail;
    }

    public void setGuildFail(int GuildFail) {
        this.GuildFail = GuildFail;
    }

    public int getGuildDraw() {
        return this.GuildDraw;
    }

    public void setGuildDraw(int GuildDraw) {
        this.GuildDraw = GuildDraw;
    }

    public int getAddedGuild() {
        return this.AddedGuild;
    }

    public void setAddedGuild(int AddedGuild) {
        this.AddedGuild = AddedGuild;
    }

    public int getCDRWin() {
        return this.CDRWin;
    }

    public void setCDRWin(int CDRWin) {
        this.CDRWin = CDRWin;
    }

    public int getCDRFail() {
        return this.CDRFail;
    }

    public void setCDRFail(int CDRFail) {
        this.CDRFail = CDRFail;
    }

    public int getCDRDraw() {
        return this.CDRDraw;
    }

    public void setCDRDraw(int CDRDraw) {
        this.CDRDraw = CDRDraw;
    }

    public int[] getCutoff() {
        return this.Cutoff;
    }

    public void setCutoff(int[] Cutoff) {
        this.Cutoff = Cutoff;
    }

    public int[] getWarRankTime() {
        return this.WarRankTime;
    }

    public void setWarRankTime(int[] WarRankTime) {
        this.WarRankTime = WarRankTime;
    }

    public int getWarMail1() {
        return this.WarMail1;
    }

    public void setWarMail1(int WarMail1) {
        this.WarMail1 = WarMail1;
    }

    public int getWarMail2() {
        return this.WarMail2;
    }

    public void setWarMail2(int WarMail2) {
        this.WarMail2 = WarMail2;
    }

    public int getWarMail3() {
        return this.WarMail3;
    }

    public void setWarMail3(int WarMail3) {
        this.WarMail3 = WarMail3;
    }

    public int getOrnamentsItem() {
        return this.OrnamentsItem;
    }

    public void setOrnamentsItem(int OrnamentsItem) {
        this.OrnamentsItem = OrnamentsItem;
    }

    public int getJewelryItem() {
        return this.JewelryItem;
    }

    public void setJewelryItem(int JewelryItem) {
        this.JewelryItem = JewelryItem;
    }

    public int getJewelryItemExp() {
        return this.JewelryItemExp;
    }

    public void setJewelryItemExp(int JewelryItemExp) {
        this.JewelryItemExp = JewelryItemExp;
    }

    public int getFollow() {
        return this.Follow;
    }

    public void setFollow(int Follow) {
        this.Follow = Follow;
    }

    public int getInviteNum() {
        return this.InviteNum;
    }

    public void setInviteNum(int InviteNum) {
        this.InviteNum = InviteNum;
    }

    public int getInviteReward() {
        return this.InviteReward;
    }

    public void setInviteReward(int InviteReward) {
        this.InviteReward = InviteReward;
    }

    public int getInviteTime() {
        return this.InviteTime;
    }

    public void setInviteTime(int InviteTime) {
        this.InviteTime = InviteTime;
    }

    public int getAwakenReset() {
        return this.AwakenReset;
    }

    public void setAwakenReset(int AwakenReset) {
        this.AwakenReset = AwakenReset;
    }

    public int[] getSubmenuSort1() {
        return this.SubmenuSort1;
    }

    public void setSubmenuSort1(int[] SubmenuSort1) {
        this.SubmenuSort1 = SubmenuSort1;
    }

    public int[] getSubmenuSort2() {
        return this.SubmenuSort2;
    }

    public void setSubmenuSort2(int[] SubmenuSort2) {
        this.SubmenuSort2 = SubmenuSort2;
    }

    public int[] getSubmenuSort3() {
        return this.SubmenuSort3;
    }

    public void setSubmenuSort3(int[] SubmenuSort3) {
        this.SubmenuSort3 = SubmenuSort3;
    }

    public int getSkipCd() {
        return this.SkipCd;
    }

    public void setSkipCd(int SkipCd) {
        this.SkipCd = SkipCd;
    }

    public int getExchange() {
        return this.Exchange;
    }

    public void setExchange(int Exchange) {
        this.Exchange = Exchange;
    }

    public int getWanBa() {
        return this.WanBa;
    }

    public void setWanBa(int WanBa) {
        this.WanBa = WanBa;
    }

    public int[] getInvestRole() {
        return this.InvestRole;
    }

    public void setInvestRole(int[] InvestRole) {
        this.InvestRole = InvestRole;
    }

    public int[] getInvestRoleID() {
        return this.InvestRoleID;
    }

    public void setInvestRoleID(int[] InvestRoleID) {
        this.InvestRoleID = InvestRoleID;
    }

    public int[] getInvestEnchant() {
        return this.InvestEnchant;
    }

    public void setInvestEnchant(int[] InvestEnchant) {
        this.InvestEnchant = InvestEnchant;
    }

    public int[] getInvestEnchantID() {
        return this.InvestEnchantID;
    }

    public void setInvestEnchantID(int[] InvestEnchantID) {
        this.InvestEnchantID = InvestEnchantID;
    }

    public int[] getInvestJewelry() {
        return this.InvestJewelry;
    }

    public void setInvestJewelry(int[] InvestJewelry) {
        this.InvestJewelry = InvestJewelry;
    }

    public int[] getInvestJewelryID() {
        return this.InvestJewelryID;
    }

    public void setInvestJewelryID(int[] InvestJewelryID) {
        this.InvestJewelryID = InvestJewelryID;
    }

    public int getMoneyCapital() {
        return this.MoneyCapital;
    }

    public void setMoneyCapital(int MoneyCapital) {
        this.MoneyCapital = MoneyCapital;
    }

    public int getDiamondsCapital() {
        return this.DiamondsCapital;
    }

    public void setDiamondsCapital(int DiamondsCapital) {
        this.DiamondsCapital = DiamondsCapital;
    }

    public int getVictoryCapital() {
        return this.VictoryCapital;
    }

    public void setVictoryCapital(int VictoryCapital) {
        this.VictoryCapital = VictoryCapital;
    }

    public int getFailCapital() {
        return this.FailCapital;
    }

    public void setFailCapital(int FailCapital) {
        this.FailCapital = FailCapital;
    }

    public int getDrawCapital() {
        return this.DrawCapital;
    }

    public void setDrawCapital(int DrawCapital) {
        this.DrawCapital = DrawCapital;
    }

    public String getExRole() {
        return this.ExRole;
    }

    public void setExRole(String ExRole) {
        this.ExRole = ExRole;
    }

    public int[] getExRoleID() {
        return this.ExRoleID;
    }

    public void setExRoleID(int[] ExRoleID) {
        this.ExRoleID = ExRoleID;
    }

    public int getFirstCard() {
        return this.FirstCard;
    }

    public void setFirstCard(int FirstCard) {
        this.FirstCard = FirstCard;
    }

    public int getSendIdNum() {
        return this.SendIdNum;
    }

    public void setSendIdNum(int SendIdNum) {
        this.SendIdNum = SendIdNum;
    }

    public int getSend() {
        return this.Send;
    }

    public void setSend(int Send) {
        this.Send = Send;
    }

    public int getSendNum() {
        return this.SendNum;
    }

    public void setSendNum(int SendNum) {
        this.SendNum = SendNum;
    }

    public int getSendMoney() {
        return this.SendMoney;
    }

    public void setSendMoney(int SendMoney) {
        this.SendMoney = SendMoney;
    }

    public int getRob() {
        return this.Rob;
    }

    public void setRob(int Rob) {
        this.Rob = Rob;
    }

    public int getPlundered() {
        return this.Plundered;
    }

    public void setPlundered(int Plundered) {
        this.Plundered = Plundered;
    }

    public int[] getSendMate() {
        return this.SendMate;
    }

    public void setSendMate(int[] SendMate) {
        this.SendMate = SendMate;
    }

    public int getCreateMail() {
        return createMail;
    }

    public void setCreateMail(int createMail) {
        this.createMail = createMail;
    }

}