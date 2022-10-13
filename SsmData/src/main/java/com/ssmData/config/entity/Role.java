package com.ssmData.config.entity;

public class Role {
    /** 角色ID*/
    private int ID;
    /** 角色名字*/
    private String Name;
    /** 阵营*/
    private int force;
    /** 角色类型*/
    private int Type;
    /** 角色品质*/
    private int Star;
    /** 升级倍率*/
    private int Gold;
    /** 初始等级*/
    private int Lv;
    /** 最高等级*/
    private int LvMax;
    /** 初始觉醒等级*/
    private int AwakenID;
    /** 觉醒最大等级*/
    private int AwakenMax;
    /** 初始资质*/
    private int Talent;
    /** 基础生命*/
    private int Hp;
    /** 基础物攻*/
    private int Atk;
    /** 基础防御*/
    private int Def;
    /** 基础特技攻击*/
    private int AtkSp;
    /** 基础特技防御*/
    private int DefSp;
    /** 生命成长*/
    private int HpGrowth;
    /** 物攻成长*/
    private int AtkGrowth;
    /** 物防成长*/
    private int DefGrowth;
    /** 特攻成长*/
    private int AtkSpGrowth;
    /** 特防成长*/
    private int DefSpGrowth;
    /** 移动速度*/
    private int Move;
    /** 攻击速度*/
    private int Atkspeed;
    /** 攻击距离*/
    private int Range;
    /** 暴击率*/
    private int CritRate;
    /** 暴击抵抗*/
    private int CritRes;
    /** 暴击伤害*/
    private int CritDamage;
    /** 伤害减免*/
    private int DamageReduce;
    /** 吸血*/
    private int Vampire;
    /** 追击*/
    private int Combo;
    /** 追击伤害*/
    private int ComboDam;
    /** 穿透*/
    private int SpIgnore;
    /** 异常抗性*/
    private int DebuffRes;
    /** 初始怒气*/
    private int En;
    /** 怒气回复*/
    private int EnRecover;
    /** 被击怒气*/
    private int EnHit;
    /** 基础普攻*/
    private int Base;
    /** 特殊技能*/
    private int Skill;
    /** 技能等级*/
    private int SkillLv;
    /** AI类型*/
    private int AiType;
    /** 碰撞范围*/
    private int Collision;
    /** 对应碎片ID*/
    private int Fragment;
    /** 转化碎片数量*/
    private int FragmentNum;
    /** 合成所需碎片数量*/
    private int RoleSynthesis;
    /** 觉醒倍率*/
    private int[] AwakenRate;
    /** 突破ID*/
    private int[] BreachId;
    /** 角色羁绊组合*/
    private String BondRole;
    /** 角色羁绊ID*/
    private int[] BondRoleId;
    /** 角色饰品ID*/
    private int[] OrnamentsID;
    /** 角色装备组合*/
    private String BondEquip;
    /** 装备羁绊id*/
    private int[] BondEquipId;
    /** 副将属性提升所需战力*/
    private int[] BackupForce;
    /** 副将属性ID*/
    private int[] BackupId;
    /** 图片资源*/
    private String Res;
    /** 中心点偏移量*/
    private int center;
    /** 角色比率*/
    private int Size;
    /** 头像ICON*/
    private String Icon;
    /** 原画所需觉醒等级*/
    private int[] RoleCard;
    /** 原画资源*/
    private String[] Resources;
    /** 斗士列表原画资源*/
    private String[] Resources1;
    /** 原画大资源*/
    private String[] Resources2;
    /** 阵容位置*/
    private String Position;
    /** 角色定位*/
    private String Location;
    /** 推荐副将属性ID*/
    private int[] BackupRe;
    /** 是否可许愿*/
    private int Wish;
    /** 许愿消耗*/
    private int WishCost;
    /** 隐藏形象*/
    private String Hidden;
    /** 对应饰品ID*/
    private int[] JewelryId;

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

    public int getForce() {
        return this.force;
    }

    public void setForce(int force) {
        this.force = force;
    }

    public int getType() {
        return this.Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public int getStar() {
        return this.Star;
    }

    public void setStar(int Star) {
        this.Star = Star;
    }

    public int getGold() {
        return this.Gold;
    }

    public void setGold(int Gold) {
        this.Gold = Gold;
    }

    public int getLv() {
        return this.Lv;
    }

    public void setLv(int Lv) {
        this.Lv = Lv;
    }

    public int getLvMax() {
        return this.LvMax;
    }

    public void setLvMax(int LvMax) {
        this.LvMax = LvMax;
    }

    public int getAwakenID() {
        return this.AwakenID;
    }

    public void setAwakenID(int AwakenID) {
        this.AwakenID = AwakenID;
    }

    public int getAwakenMax() {
        return this.AwakenMax;
    }

    public void setAwakenMax(int AwakenMax) {
        this.AwakenMax = AwakenMax;
    }

    public int getTalent() {
        return this.Talent;
    }

    public void setTalent(int Talent) {
        this.Talent = Talent;
    }

    public int getHp() {
        return this.Hp;
    }

    public void setHp(int Hp) {
        this.Hp = Hp;
    }

    public int getAtk() {
        return this.Atk;
    }

    public void setAtk(int Atk) {
        this.Atk = Atk;
    }

    public int getDef() {
        return this.Def;
    }

    public void setDef(int Def) {
        this.Def = Def;
    }

    public int getAtkSp() {
        return this.AtkSp;
    }

    public void setAtkSp(int AtkSp) {
        this.AtkSp = AtkSp;
    }

    public int getDefSp() {
        return this.DefSp;
    }

    public void setDefSp(int DefSp) {
        this.DefSp = DefSp;
    }

    public int getHpGrowth() {
        return this.HpGrowth;
    }

    public void setHpGrowth(int HpGrowth) {
        this.HpGrowth = HpGrowth;
    }

    public int getAtkGrowth() {
        return this.AtkGrowth;
    }

    public void setAtkGrowth(int AtkGrowth) {
        this.AtkGrowth = AtkGrowth;
    }

    public int getDefGrowth() {
        return this.DefGrowth;
    }

    public void setDefGrowth(int DefGrowth) {
        this.DefGrowth = DefGrowth;
    }

    public int getAtkSpGrowth() {
        return this.AtkSpGrowth;
    }

    public void setAtkSpGrowth(int AtkSpGrowth) {
        this.AtkSpGrowth = AtkSpGrowth;
    }

    public int getDefSpGrowth() {
        return this.DefSpGrowth;
    }

    public void setDefSpGrowth(int DefSpGrowth) {
        this.DefSpGrowth = DefSpGrowth;
    }

    public int getMove() {
        return this.Move;
    }

    public void setMove(int Move) {
        this.Move = Move;
    }

    public int getAtkspeed() {
        return this.Atkspeed;
    }

    public void setAtkspeed(int Atkspeed) {
        this.Atkspeed = Atkspeed;
    }

    public int getRange() {
        return this.Range;
    }

    public void setRange(int Range) {
        this.Range = Range;
    }

    public int getCritRate() {
        return this.CritRate;
    }

    public void setCritRate(int CritRate) {
        this.CritRate = CritRate;
    }

    public int getCritRes() {
        return this.CritRes;
    }

    public void setCritRes(int CritRes) {
        this.CritRes = CritRes;
    }

    public int getCritDamage() {
        return this.CritDamage;
    }

    public void setCritDamage(int CritDamage) {
        this.CritDamage = CritDamage;
    }

    public int getDamageReduce() {
        return this.DamageReduce;
    }

    public void setDamageReduce(int DamageReduce) {
        this.DamageReduce = DamageReduce;
    }

    public int getVampire() {
        return this.Vampire;
    }

    public void setVampire(int Vampire) {
        this.Vampire = Vampire;
    }

    public int getCombo() {
        return this.Combo;
    }

    public void setCombo(int Combo) {
        this.Combo = Combo;
    }

    public int getComboDam() {
        return this.ComboDam;
    }

    public void setComboDam(int ComboDam) {
        this.ComboDam = ComboDam;
    }

    public int getSpIgnore() {
        return this.SpIgnore;
    }

    public void setSpIgnore(int SpIgnore) {
        this.SpIgnore = SpIgnore;
    }

    public int getDebuffRes() {
        return this.DebuffRes;
    }

    public void setDebuffRes(int DebuffRes) {
        this.DebuffRes = DebuffRes;
    }

    public int getEn() {
        return this.En;
    }

    public void setEn(int En) {
        this.En = En;
    }

    public int getEnRecover() {
        return this.EnRecover;
    }

    public void setEnRecover(int EnRecover) {
        this.EnRecover = EnRecover;
    }

    public int getEnHit() {
        return this.EnHit;
    }

    public void setEnHit(int EnHit) {
        this.EnHit = EnHit;
    }

    public int getBase() {
        return this.Base;
    }

    public void setBase(int Base) {
        this.Base = Base;
    }

    public int getSkill() {
        return this.Skill;
    }

    public void setSkill(int Skill) {
        this.Skill = Skill;
    }

    public int getSkillLv() {
        return this.SkillLv;
    }

    public void setSkillLv(int SkillLv) {
        this.SkillLv = SkillLv;
    }

    public int getAiType() {
        return this.AiType;
    }

    public void setAiType(int AiType) {
        this.AiType = AiType;
    }

    public int getCollision() {
        return this.Collision;
    }

    public void setCollision(int Collision) {
        this.Collision = Collision;
    }

    public int getFragment() {
        return this.Fragment;
    }

    public void setFragment(int Fragment) {
        this.Fragment = Fragment;
    }

    public int getFragmentNum() {
        return this.FragmentNum;
    }

    public void setFragmentNum(int FragmentNum) {
        this.FragmentNum = FragmentNum;
    }

    public int getRoleSynthesis() {
        return this.RoleSynthesis;
    }

    public void setRoleSynthesis(int RoleSynthesis) {
        this.RoleSynthesis = RoleSynthesis;
    }

    public int[] getAwakenRate() {
        return this.AwakenRate;
    }

    public void setAwakenRate(int[] AwakenRate) {
        this.AwakenRate = AwakenRate;
    }

    public int[] getBreachId() {
        return this.BreachId;
    }

    public void setBreachId(int[] BreachId) {
        this.BreachId = BreachId;
    }

    public String getBondRole() {
        return this.BondRole;
    }

    public void setBondRole(String BondRole) {
        this.BondRole = BondRole;
    }

    public int[] getBondRoleId() {
        return this.BondRoleId;
    }

    public void setBondRoleId(int[] BondRoleId) {
        this.BondRoleId = BondRoleId;
    }

    public int[] getOrnamentsID() {
        return this.OrnamentsID;
    }

    public void setOrnamentsID(int[] OrnamentsID) {
        this.OrnamentsID = OrnamentsID;
    }

    public String getBondEquip() {
        return this.BondEquip;
    }

    public void setBondEquip(String BondEquip) {
        this.BondEquip = BondEquip;
    }

    public int[] getBondEquipId() {
        return this.BondEquipId;
    }

    public void setBondEquipId(int[] BondEquipId) {
        this.BondEquipId = BondEquipId;
    }

    public int[] getBackupForce() {
        return this.BackupForce;
    }

    public void setBackupForce(int[] BackupForce) {
        this.BackupForce = BackupForce;
    }

    public int[] getBackupId() {
        return this.BackupId;
    }

    public void setBackupId(int[] BackupId) {
        this.BackupId = BackupId;
    }

    public String getRes() {
        return this.Res;
    }

    public void setRes(String Res) {
        this.Res = Res;
    }

    public int getCenter() {
        return this.center;
    }

    public void setCenter(int center) {
        this.center = center;
    }

    public int getSize() {
        return this.Size;
    }

    public void setSize(int Size) {
        this.Size = Size;
    }

    public String getIcon() {
        return this.Icon;
    }

    public void setIcon(String Icon) {
        this.Icon = Icon;
    }

    public int[] getRoleCard() {
        return this.RoleCard;
    }

    public void setRoleCard(int[] RoleCard) {
        this.RoleCard = RoleCard;
    }

    public String[] getResources() {
        return this.Resources;
    }

    public void setResources(String[] Resources) {
        this.Resources = Resources;
    }

    public String[] getResources1() {
        return this.Resources1;
    }

    public void setResources1(String[] Resources1) {
        this.Resources1 = Resources1;
    }

    public String[] getResources2() {
        return this.Resources2;
    }

    public void setResources2(String[] Resources2) {
        this.Resources2 = Resources2;
    }

    public String getPosition() {
        return this.Position;
    }

    public void setPosition(String Position) {
        this.Position = Position;
    }

    public String getLocation() {
        return this.Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

    public int[] getBackupRe() {
        return this.BackupRe;
    }

    public void setBackupRe(int[] BackupRe) {
        this.BackupRe = BackupRe;
    }

    public int getWish() {
        return this.Wish;
    }

    public void setWish(int Wish) {
        this.Wish = Wish;
    }

    public int getWishCost() {
        return this.WishCost;
    }

    public void setWishCost(int WishCost) {
        this.WishCost = WishCost;
    }

    public String getHidden() {
        return this.Hidden;
    }

    public void setHidden(String Hidden) {
        this.Hidden = Hidden;
    }

    public int[] getJewelryId() {
        return this.JewelryId;
    }

    public void setJewelryId(int[] JewelryId) {
        this.JewelryId = JewelryId;
    }


}