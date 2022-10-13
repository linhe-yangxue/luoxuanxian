package com.ssmData.config.constant;

import java.util.Map;

import com.ssmData.config.entity.Accumulation;
import com.ssmData.config.entity.Aggregate;
import com.ssmData.config.entity.Arena;
import com.ssmData.config.entity.Awaken;
import com.ssmData.config.entity.Award;
import com.ssmData.config.entity.Backup;
import com.ssmData.config.entity.Basicactivity;
import com.ssmData.config.entity.Beginactivity;
import com.ssmData.config.entity.Bond;
import com.ssmData.config.entity.Breach;
import com.ssmData.config.entity.Buff;
import com.ssmData.config.entity.Card;
import com.ssmData.config.entity.Config;
import com.ssmData.config.entity.Consume;
import com.ssmData.config.entity.Craft;
import com.ssmData.config.entity.Dailytask;
import com.ssmData.config.entity.Damagereward;
import com.ssmData.config.entity.Daypay;
import com.ssmData.config.entity.Diamondplate;
import com.ssmData.config.entity.Drama;
import com.ssmData.config.entity.Draward;
import com.ssmData.config.entity.Duel;
import com.ssmData.config.entity.Duration;
import com.ssmData.config.entity.Enchant;
import com.ssmData.config.entity.Equip;
import com.ssmData.config.entity.Equipup;
import com.ssmData.config.entity.Exrole;
import com.ssmData.config.entity.Fbtype;
import com.ssmData.config.entity.Fullboss;
import com.ssmData.config.entity.Gift;
import com.ssmData.config.entity.Gkgrow;
import com.ssmData.config.entity.Grade;
import com.ssmData.config.entity.Guild;
import com.ssmData.config.entity.Hdrankinglv;
import com.ssmData.config.entity.Hdrankstage;
import com.ssmData.config.entity.Hubmap;
import com.ssmData.config.entity.Instance;
import com.ssmData.config.entity.Invest;
import com.ssmData.config.entity.Investvip;
import com.ssmData.config.entity.Invite;
import com.ssmData.config.entity.Item;
import com.ssmData.config.entity.Itemgood;
import com.ssmData.config.entity.Itemshop;
import com.ssmData.config.entity.Jewelry;
import com.ssmData.config.entity.Jewelryup;
import com.ssmData.config.entity.Katsuji;
import com.ssmData.config.entity.Level;
import com.ssmData.config.entity.Lineup;
import com.ssmData.config.entity.Lvgift;
import com.ssmData.config.entity.Lvgrow;
import com.ssmData.config.entity.Mail;
import com.ssmData.config.entity.Maward;
import com.ssmData.config.entity.Monthcard;
import com.ssmData.config.entity.Prize;
import com.ssmData.config.entity.Rankaward;
import com.ssmData.config.entity.Registration;
import com.ssmData.config.entity.Robot;
import com.ssmData.config.entity.Role;
import com.ssmData.config.entity.Scroll;
import com.ssmData.config.entity.Send;
import com.ssmData.config.entity.ServerConfig;
import com.ssmData.config.entity.Shop;
import com.ssmData.config.entity.Singleboss;
import com.ssmData.config.entity.Skill;
import com.ssmData.config.entity.Spay;
import com.ssmData.config.entity.Suit;
import com.ssmData.config.entity.Talent;
import com.ssmData.config.entity.Task;
import com.ssmData.config.entity.Tasklv;
import com.ssmData.config.entity.Tasklvreward;
import com.ssmData.config.entity.Tech;
import com.ssmData.config.entity.Techup;
import com.ssmData.config.entity.Tower;
import com.ssmData.config.entity.Toweraward;
import com.ssmData.config.entity.Turnplate;
import com.ssmData.config.entity.Victory;
import com.ssmData.config.entity.Vip;
import com.ssmData.config.entity.Vipbonus;
import com.ssmData.config.entity.Vipgift;
import com.ssmData.config.entity.Warrank;
import com.ssmData.config.entity.Wish;
import com.ssmData.config.entity.Wordconfig;

/**
 * 配置文件内存模版
 *
 */
public class ConfigConstant {

	public static Config tConf; 					// 游戏初始配置文件

	public static ServerConfig tServerConf; 		// 服务端专用配置文件
	
	public static Map<Integer,Role> tRole; 			// 游戏角色模版

	public static Map<Integer,Skill> tSkill; 		// 属性表

	public static Map<Integer, Level> tLevel; 		// 关卡表
	
	public static Map<Integer,Buff> tBuff; 			//状态表
	
	public static Map<Integer, Award> tAward; 		//奖励表
	
	public static Map<Integer, Prize> tPrize; 		//奖品表
	
	public static Map<Integer, Grade> tGrade; 		//升级表

	public static Map<Integer, Vip> tVip;			// Vip等级配置表
	
	public static Map<Integer, Breach> tBreach; //突破表
	
	public static Map<Integer, Talent> tTalent; //资质表
	
	public static Map<Integer, Awaken> tAwaken; //觉醒表
	
	public static Map<Integer, Bond> tBond; //羁绊表
	
	public static Map<Integer, Backup> tBackup; //副将表
	
	public static Map<Integer, Instance> tInstance; //副本表
	
	public static Map<Integer, Fbtype> tFbtype; //副本类型表
	
	public static Map<Integer, Scroll> tScroll; //门票表
	
	public static Map<Integer, Card> tCard; //门票表
	
	public static Map<Integer, Item> tItem; //物品表
	
	public static Map<Integer, Equip> tEquip; //装备表
	
	public static Map<Integer, Equipup> tEquipup; //装备养成表
	
	public static Map<Integer, Suit> tSuit;//套装
	
	public static Map<Integer, Robot> tRobot; //竞技场机器人
	
	public static Map<Integer, Arena> tArena; //竞技场配置
	
	public static Map<Integer, Rankaward> tRankaward; //竞技场排名奖励配置
	
	public static Map<Integer, Shop> tShop; //商店配置
	
	public static Map<Integer, Itemshop> tItemshop; //商铺配置
	
	public static Map<Integer, Itemgood> tItemgood; //商品配置
	
	public static Map<Integer, Mail> tMail; // 邮件配置
	
	public static Map<Integer, Victory> tVictory; //一骑当千胜利奖励表
	
	public static Map<Integer, Katsuji> tKatsuji; //一骑当千胜利次数奖励表
	
	public static Map<Integer, Maward> tMaward; //一骑当千功勋奖励表
	
	public static Duel tDuel; //一骑当千配置表
	
	public static Map<Integer, Dailytask> tDailytask; //日常任务
	
	public static Map<Integer, Tasklv> tTaskLv; //日常任务升级所加数值
	
	public static Map<Integer, Tasklvreward> tTaskLvReward; //日常任务特定等级奖励
	
	public static Map<Integer, Task> tTask; //主线任务的配置表
	
	public static Map<Integer, Monthcard> tMonthcard; //月卡特权
	
	public static Map<Integer, Enchant> tEnchant; //装备附魔
	
	public static Map<Integer, Tower> tTower; //爬塔
	
	public static Map<Integer, Toweraward> tToweraward; // 爬塔首次奖励
	
	public static Map<Integer, Basicactivity> tBasicactivity; //基本活动表

	public static Map<Integer, Beginactivity> tBeginactivity; //开服活动表

	public static Map<Integer, Accumulation> tAccumulation; //累计充值活动奖励表
	
	public static Map<String, Wordconfig> tWordconfig; //字典表
	
	public static Map<Integer, Draward> tDraward; //登入奖励
	
	public static Map<Integer, Lvgrow> tLvgrow; //等级成长奖励
	
	public static Map<Integer, Hdrankinglv> tHdrankinglv; //等级排名活动奖励
	
	public static Map<Integer, Hdrankstage> tHdrankstage; //关卡排名活动奖励
	
	public static Map<Integer, Gkgrow> tGkgrow; //关卡成长奖励
	
	public static Map<Integer, Invest> tInvest; //投资表
	
	public static Map<Integer, Aggregate> tAggregate; //累计签到表
	
	public static Map<Integer, Registration> tRegistration; //签到表
	
	public static Map<Integer, Gift> tGift; //超值礼包活动表
	
	public static Map<Integer, Consume> tConsume; //超值礼包活动表
	
	public static Map<Integer, Turnplate> tTurnplate; //转盘活动表
	
	public static Map<Integer, Daypay> tDaypay; //转盘活动表
	
	public static Map<Integer, Duration> tDuration; //转盘活动表
	
	public static Map<Integer, Damagereward> tDamagereward; //伤害奖励表
	
	public static Map<Integer, Lineup> tLineup; //阵容推荐表
	
	public static Map<Integer, Wish> tWish; //许愿表
	
	public static Drama tDrama; 					// 剧情战斗
	
	public static Map<Integer, Lvgift> tLvgift; //限时礼包表
	
	public static Map<Integer, Spay> tSpay; //累计充值系统（不是活动）挡位奖励表
	
	public static Map<Integer, Diamondplate> tDiamondplate; //钻石转盘
	
	public static Map<Integer, Guild> tGuild; //公会表
	
	public static Map<Integer, Hubmap> tHubmap; //游服地址表
	
	public static Map<Integer, Warrank> tWarrank; //工会战奖励
	
	public static Map<Integer, Jewelry> tJewelry; //饰品表
	
	public static Map<Integer, Jewelryup> tJewelryup; //饰品表
	
	public static Map<Integer, Invite> tInvite; //邀请人数奖励表
	
	public static Map<Integer, Singleboss> tSingleboss; //个人boss表
	
	public static Map<Integer, Fullboss> tFullboss; //世界boss表
	
	public static Map<Integer, Vipbonus> tVipbonus; //vip福利活动表
	
	public static Map<Integer, Vipgift> tVipgift; //vip周礼包活动表
	
	public static Map<Integer, Investvip> tInvestvip; //vip投资计划活动表
	
	public static Map<Integer, Tech> tTech; //工会科技
	
	public static Map<Integer, Techup> tTechup; //工会科技升级
	
	public static Map<Integer, Exrole> tExrole; //角色Ex活动表
	
	public static Map<Integer, Craft> tCraft; //合成表
	
	public static Map<Integer, Send> tSend; //外派任务表
}
