package com.ssmData.config.entity;

public class Skill {
    /** 技能ID*/
    private int ID;
    /** 技能名字*/
    private String Name;
    /** 技能类型*/
    private int Type;
    /** 目标类型*/
    private int TargeTp;
    /** 目标范围*/
    private int TargetRate;
    /** 伤害类型*/
    private int DamageType;
    /** 技能加成*/
    private int Damage;
    /** 技能倍率*/
    private int DamageRate;
    /** 怒气增减*/
    private int SpAdd;
    /** 附加状态*/
    private int[] Buff;
    /** 状态命中率*/
    private int[] BuffHit;
    /** 自身状态*/
    private int[] BuffSelf;
    /** 全体状态*/
    private int[] BuffAll;
    /** 驱散效果*/
    private int Disperse;
    /** 自定义参数*/
    private int Value;
    /** 伤害分段*/
    private int[] DamDivide;
    /** 分段时间*/
    private int DamTime;
    /** 特写图片*/
    private String[] RoleRes;
    /** 特写音效*/
    private String SkillSound;
    /** 技能特写名字*/
    private String EffectName;
    /** 动作*/
    private String Action;
    /** 施法特效*/
    private String[] CastE;
    /** 施法绑定点*/
    private int CastP;
    /** 击中特效*/
    private String[] HitE;
    /** 击中绑定点*/
    private int HitP;
    /** 击中音效*/
    private String HitSound;
    /** 投掷物效果*/
    private String[] Throw;
    /** 技能加成成长*/
    private int DamageLv;
    /** 技能倍率成长*/
    private int DamageRateLv;
    /** 自定义成长*/
    private int ValueLv;
    /** 状态命中加成*/
    private int[] BuffLv;
    /** 技能ICON*/
    private String Icon;
    /** 技能描述*/
    private String Des;
    /** 技能描述所用倍率*/
    private int[] DesN1;
    /** 成长值*/
    private int[] DesN2;

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getType() {
        return this.Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public int getTargeTp() {
        return this.TargeTp;
    }

    public void setTargeTp(int TargeTp) {
        this.TargeTp = TargeTp;
    }

    public int getTargetRate() {
        return this.TargetRate;
    }

    public void setTargetRate(int TargetRate) {
        this.TargetRate = TargetRate;
    }

    public int getDamageType() {
        return this.DamageType;
    }

    public void setDamageType(int DamageType) {
        this.DamageType = DamageType;
    }

    public int getDamage() {
        return this.Damage;
    }

    public void setDamage(int Damage) {
        this.Damage = Damage;
    }

    public int getDamageRate() {
        return this.DamageRate;
    }

    public void setDamageRate(int DamageRate) {
        this.DamageRate = DamageRate;
    }

    public int getSpAdd() {
        return this.SpAdd;
    }

    public void setSpAdd(int SpAdd) {
        this.SpAdd = SpAdd;
    }

    public int[] getBuff() {
        return this.Buff;
    }

    public void setBuff(int[] Buff) {
        this.Buff = Buff;
    }

    public int[] getBuffHit() {
        return this.BuffHit;
    }

    public void setBuffHit(int[] BuffHit) {
        this.BuffHit = BuffHit;
    }

    public int[] getBuffSelf() {
        return this.BuffSelf;
    }

    public void setBuffSelf(int[] BuffSelf) {
        this.BuffSelf = BuffSelf;
    }

    public int[] getBuffAll() {
        return this.BuffAll;
    }

    public void setBuffAll(int[] BuffAll) {
        this.BuffAll = BuffAll;
    }

    public int getDisperse() {
        return this.Disperse;
    }

    public void setDisperse(int Disperse) {
        this.Disperse = Disperse;
    }

    public int getValue() {
        return this.Value;
    }

    public void setValue(int Value) {
        this.Value = Value;
    }

    public int[] getDamDivide() {
        return this.DamDivide;
    }

    public void setDamDivide(int[] DamDivide) {
        this.DamDivide = DamDivide;
    }

    public int getDamTime() {
        return this.DamTime;
    }

    public void setDamTime(int DamTime) {
        this.DamTime = DamTime;
    }

    public String[] getRoleRes() {
        return this.RoleRes;
    }

    public void setRoleRes(String[] RoleRes) {
        this.RoleRes = RoleRes;
    }

    public String getSkillSound() {
        return this.SkillSound;
    }

    public void setSkillSound(String SkillSound) {
        this.SkillSound = SkillSound;
    }

    public String getEffectName() {
        return this.EffectName;
    }

    public void setEffectName(String EffectName) {
        this.EffectName = EffectName;
    }

    public String getAction() {
        return this.Action;
    }

    public void setAction(String Action) {
        this.Action = Action;
    }

    public String[] getCastE() {
        return this.CastE;
    }

    public void setCastE(String[] CastE) {
        this.CastE = CastE;
    }

    public int getCastP() {
        return this.CastP;
    }

    public void setCastP(int CastP) {
        this.CastP = CastP;
    }

    public String[] getHitE() {
        return this.HitE;
    }

    public void setHitE(String[] HitE) {
        this.HitE = HitE;
    }

    public int getHitP() {
        return this.HitP;
    }

    public void setHitP(int HitP) {
        this.HitP = HitP;
    }

    public String getHitSound() {
        return this.HitSound;
    }

    public void setHitSound(String HitSound) {
        this.HitSound = HitSound;
    }

    public String[] getThrow() {
        return this.Throw;
    }

    public void setThrow(String[] Throw) {
        this.Throw = Throw;
    }

    public int getDamageLv() {
        return this.DamageLv;
    }

    public void setDamageLv(int DamageLv) {
        this.DamageLv = DamageLv;
    }

    public int getDamageRateLv() {
        return this.DamageRateLv;
    }

    public void setDamageRateLv(int DamageRateLv) {
        this.DamageRateLv = DamageRateLv;
    }

    public int getValueLv() {
        return this.ValueLv;
    }

    public void setValueLv(int ValueLv) {
        this.ValueLv = ValueLv;
    }

    public int[] getBuffLv() {
        return this.BuffLv;
    }

    public void setBuffLv(int[] BuffLv) {
        this.BuffLv = BuffLv;
    }

    public String getIcon() {
        return this.Icon;
    }

    public void setIcon(String Icon) {
        this.Icon = Icon;
    }

    public String getDes() {
        return this.Des;
    }

    public void setDes(String Des) {
        this.Des = Des;
    }

    public int[] getDesN1() {
        return this.DesN1;
    }

    public void setDesN1(int[] DesN1) {
        this.DesN1 = DesN1;
    }

    public int[] getDesN2() {
        return this.DesN2;
    }

    public void setDesN2(int[] DesN2) {
        this.DesN2 = DesN2;
    }


}