package com.ssmGame.module.battle;

public class SkillTarget {
    public static final int Null = 0;
    public static final int Self = 1; //自己 
    public static final int SingleEnemy = 2; //敌方单体
    public static final int RandomEnemy = 3; //敌方目标符加随机，需要用参数
    public static final int AllEnemy = 4; //敌方全体
    public static final int AllFriendly = 5; //我方全体
    public static final int LowHpEnemy = 6; //低血量的，需要用参数
    public static final int Special = 7; //4,5,1,2,3顺序，即先打后排
    public static final int LowHpSelf = 8; // 我方最低血量，需要用参数
    public static final int ZhaoyunSkill = 9;  //赵云类型目标，随机发动7次攻击，每次攻击目标随机
    
    
    public static final int[] SpecialList = {4,5,1,2,3};
}
