package com.ssmGame.module.battle;

public class BuffInfo {
	public int id;  //技能状态表id
	public int end_step;  //战斗中状态结束的时间戳，会有一个对应时间戳的结束命令
	public int stack;   //层数
	public int special_type;	//特殊类型
	public int lv;		//buff等级，等于中这个技能的技能等级
	
	public BuffInfo Clone()
	{
		BuffInfo r = new BuffInfo();
		r.id = id;
		r.end_step = end_step;
		r.stack = stack;
		r.special_type = special_type;
		r.lv = lv;
		return r;
	}
}
