package com.ssmData.config.entity;

public class Instance {
    /** 副本ID*/
    private int ID;
    /** 副本名字*/
    private String Name;
    /** 副本类型*/
    private int Type;
    /** 下一个副本*/
    private int LowLevel;
    /** 开启关卡所需通过关卡*/
    private int OpenLevel;
    /** 所需挑战卷数量*/
    private int CScroll;
    /** 每日免费挑战次数*/
    private int FBchallenge;
    /** 副本时间*/
    private int Time;
    /** 金币*/
    private int Gold;
    /** 经验*/
    private int Exp;
    /** 副本怪物*/
    private int[] Mon;
    /** 怪物等级*/
    private int[] MonLv;
    /** 推荐战力*/
    private int Fighting;
    /** 奖励ID*/
    private int Award;
    /** 场景*/
    private int Scene;
    /** 外框品质*/
    private int Quality;
    /** boss头像显示*/
    private String Icon;
    /** 掉落道具图片*/
    private int[] ItemIcon;
    /** 是否公告*/
    private int Notice;

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

    public int getLowLevel() {
        return this.LowLevel;
    }

    public void setLowLevel(int LowLevel) {
        this.LowLevel = LowLevel;
    }

    public int getOpenLevel() {
        return this.OpenLevel;
    }

    public void setOpenLevel(int OpenLevel) {
        this.OpenLevel = OpenLevel;
    }

    public int getCScroll() {
        return this.CScroll;
    }

    public void setCScroll(int CScroll) {
        this.CScroll = CScroll;
    }

    public int getFBchallenge() {
        return this.FBchallenge;
    }

    public void setFBchallenge(int FBchallenge) {
        this.FBchallenge = FBchallenge;
    }

    public int getTime() {
        return this.Time;
    }

    public void setTime(int Time) {
        this.Time = Time;
    }

    public int getGold() {
        return this.Gold;
    }

    public void setGold(int Gold) {
        this.Gold = Gold;
    }

    public int getExp() {
        return this.Exp;
    }

    public void setExp(int Exp) {
        this.Exp = Exp;
    }

    public int[] getMon() {
        return this.Mon;
    }

    public void setMon(int[] Mon) {
        this.Mon = Mon;
    }

    public int[] getMonLv() {
        return this.MonLv;
    }

    public void setMonLv(int[] MonLv) {
        this.MonLv = MonLv;
    }

    public int getFighting() {
        return this.Fighting;
    }

    public void setFighting(int Fighting) {
        this.Fighting = Fighting;
    }

    public int getAward() {
        return this.Award;
    }

    public void setAward(int Award) {
        this.Award = Award;
    }

    public int getScene() {
        return this.Scene;
    }

    public void setScene(int Scene) {
        this.Scene = Scene;
    }

    public int getQuality() {
        return this.Quality;
    }

    public void setQuality(int Quality) {
        this.Quality = Quality;
    }

    public String getIcon() {
        return this.Icon;
    }

    public void setIcon(String Icon) {
        this.Icon = Icon;
    }

    public int[] getItemIcon() {
        return this.ItemIcon;
    }

    public void setItemIcon(int[] ItemIcon) {
        this.ItemIcon = ItemIcon;
    }

    public int getNotice() {
        return this.Notice;
    }

    public void setNotice(int Notice) {
        this.Notice = Notice;
    }


}