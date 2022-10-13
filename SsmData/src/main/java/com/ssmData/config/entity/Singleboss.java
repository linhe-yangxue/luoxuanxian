package com.ssmData.config.entity;

public class Singleboss {
    /** 副本ID*/
    private int ID;
    /** 副本名字*/
    private String Name;
    /** 开启关卡所需等级*/
    private int OpenLv;
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
    /** 奖励ID*/
    private int Award;
    /** 场景*/
    private int Scene;
    /** boss头像显示*/
    private String Icon;
    /** 掉落道具图片*/
    private int[] ItemIcon;

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

    public int getOpenLv() {
        return this.OpenLv;
    }

    public void setOpenLv(int OpenLv) {
        this.OpenLv = OpenLv;
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


}