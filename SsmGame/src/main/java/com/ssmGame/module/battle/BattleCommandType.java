package com.ssmGame.module.battle;

public class BattleCommandType {
	public static final int Null = 0;
	public static final int Search = 1;     //参数格式  0-搜索到的目标
	public static final int Attack = 2;     //参数格式  0-攻击的目标 1-使用的技能ID(包括普通攻击) 2-攻击后怒气变更值，3-吸血值，4-追击次数  5-追击伤害 (循环 6-施放的目标id  7-造成变化值  8 是否暴击  9怒气变更值）
	public static final int Die = 3;
	public static final int GameOver = 4;
	public static final int AddBuff = 5;	//增加某个状态    参数格式 0-添加的buff表id, 1-添加的技能等级
	public static final int RemoveBuff = 6; //删除某个状态    参数格式 0-要删除的buff表id
	public static final int BuffHit = 7;  //持续伤害 参数格式： 0-buff的id， 1-剩余次数（包括本次，扣掉本次为0的话就不生成下一次了）2-本次伤害数值
}
