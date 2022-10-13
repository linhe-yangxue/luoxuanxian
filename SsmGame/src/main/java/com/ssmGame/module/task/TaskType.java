package com.ssmGame.module.task;

public class TaskType {
		
	//关卡类：完成条件，通过某一关。类型号1 完成条件填写具体关卡ID
	public static final int HAS_PVE = 1;
	
	//装备强化：完成条件，接任务后强化装备N次。类型号2 完成条件填写具体次数
	public static final int EQUIP_STR = 2;
	
	//装备精炼：完成条件，接任务后精炼装备N次。类型号3 完成条件填写具体次数
	public static final int EQUIP_REFINE = 3;
	
	//斗士升级：完成条件，接任务后升级斗士N次。类型号4 完成条件填写具体次数
	public static final int ROLE_LV_UP = 4;
	
	//斗士进阶：完成条件，接任务后进阶斗士N次。类型号5 完成条件填写具体次数
	public static final int ROLE_TALENT = 5;
	
	//斗士突破：完成条件，接任务后突破斗士N次。类型号6 完成条件填写具体次数
	public static final int ROLE_BREACH = 6;
	
	//斗士觉醒：完成条件，接任务后觉醒斗士N次。类型号7 完成条件填写具体次数
	public static final int ROLE_AWAKEN = 7;
	
	//装备副本挑战：完成条件，接任务后完成副本挑战N次。类型号8 完成条件填写具体次数
	public static final int INSTANCE_EQUIP = 8; 
	
	//金币副本挑战：完成条件，接任务后完成副本挑战N次。类型号9 完成条件填写具体次数
	public static final int INSTANCE_GOLD = 9;
	
	//一骑当千：完成条件，接任务后完成一骑当千挑战N次。类型号10 完成条件填写具体次数
	public static final int DUEL = 10;
	
	//斗士等级：完成条件，接任务后检测是否有斗士等级达到N。类型号11 完成条件填写具体等级
	public static final int HAS_ROLE_LV = 11;
	
	//装备等级：完成条件，接任务后检测是否有装备等级达到N。类型号12 完成条件填写具体等级
	public static final int HAS_EQUIP_STR = 12;
	
	//装备副本某关：完成条件，接任务后检测装备副本是否达到N关。类型号13 完成条件填写具体副本ID
	public static final int HAS_INSTANCE_EQUIP = 13;
	
	//金币副本某关：完成条件，接任务后检测金币副本是否达到N关。类型号14 完成条件填写具体副本ID
	public static final int HAS_INSTANCE_GOLD = 14;
	
	//装备精炼等级：完成条件，接任务后检测装备精炼等级是否有达到N级。类型号15 完成条件填写具体等级
	public static final int HAS_EQUIP_REFINE = 15;
	
	//斗士突破等级：完成条件，接任务检测斗士突破等级是否有达到N级。类型号16 完成条件填写具体等级
	public static final int HAS_ROLE_BREACH = 16;
	
	//拥有指定角色ID的角色，类型号17 完成条件填写具体角色ID
	public static final int HAS_ROLE_ID = 17;
	
	//上阵满多少角色，类型18，完成条件填具体上阵数量
	public static final int HAS_ROLE_PVE_CNT = 18;
	
	//接任务后，已经进行了多少次免费道具抽， 类型19，完成条件填具体抽的次数
	public static final int ITEM_DRAW_CNT = 19;
	
	//接任务后，已经进行了多少次快速战斗， 类型20，完成条件填具体快速战斗的次数
	public static final int QUICK_BATTLE_CNT = 20;
	
	//检测魅力等级是否有达到N级， 类型21，完成条件填具体等级数
	public static final int HAS_CHARM_LV = 21;
	
	//检测已经单个角色装备的数量达到某个数。 类型22， 完成条件填具体数量
	public static final int HAS_ROLE_EQUIP_CNT = 22;
	
	//战队等级达到N级
	public static final int HAS_PLAYER_LEVEL = 23;
	
	//所有角色总等级达到N级
	public static final int HAS_ROLE_LV_ALL_ROLE = 24;
	
	//接任务后竞技场挑战N次，
	public static final int ARENA_CNT = 25;
	
	//上阵队伍的历史战力达到N
	public static final int HAS_HISTORY_FIGHT = 26;
	
	//是否已经领取了竞技场某ID的奖励
	public static final int HAS_GET_ARENA_AWARD_ID = 27;
	
	//是否领取了魅力等级奖励
	public static final int HAS_GET_CHARM_LV_AWARD_ID = 28;
	
	//是否领取了一骑当千奖励任务
	public static final int HAS_GET_DUEL_AWARD_ID = 29;
	
	//英雄副本挑战：完成条件，接任务后完成副本挑战N次。类型号30 完成条件填写具体次数
	public static final int INSTANCE_HERO = 30; 
	
	//材料副本挑战：完成条件，接任务后完成副本挑战N次。类型号31 完成条件填写具体次数
	public static final int INSTANCE_MAT = 31;
	
	//英雄副本某关：完成条件，接任务后检测英雄副本是否达到N关。类型号32 完成条件填写具体副本ID
	public static final int HAS_INSTANCE_HERO = 32;
	
	//材料副本某关：完成条件，接任务后检测材料副本是否达到N关。类型号33 完成条件填写具体副本ID
	public static final int HAS_INSTANCE_MAT = 33;
	
	//附魔等级：单个角色附魔单个等级最大达到多少级，类型34 
	public static final int HAS_ENCHANT_ROLE = 34;
	
	//爬塔层数：爬塔最高层数达到多少，类型35    
	public static final int HAS_TOWER_LV = 35;
	
	//购买金币：接任务后，计算购买金币次数，类型36
	public static final int GOLD_BUY_CNT = 36;
	
	//初次钻石招募：接任务后是否已经使用过第一次招募（无论单次还是十连抽只要首次必得使用过就算完成），类型37     【弹出抽卡界面】
	public static final int HAS_FIRST_DMD_DRAW = 37;
	
	//上阵1个副将：任意斗士是否已上阵1个副将，类型38  
	public static final int HAS_ONE_BACKUP = 38;
	
	//爬塔奖励：是否已领取第X当爬塔等级奖励，填写奖励ID，类型39
	public static final int HAS_GET_TOWER_AWARD_ID = 39;
	
	//领取伤害统计奖励：接任务后是否已经领取过统计对应ID的奖励，类型40
	public static final int HAS_GET_DMG_AWARD_ID = 40;
	
	//领取推荐奖励：接任务后是否已经领取过推荐阵容对应ID的奖励，类型41
	public static final int HAS_GET_LINEUP_AWARD_ID = 41;
	
	//许愿1次：接任务后是否进行过1次许愿，类型42
	public static final int WISH_CNT = 42;
	
	//许愿池等级：领取任务后许愿池等级是否达到XXX级，类型43
	public static final int HAS_WISH_LV = 43;
}
