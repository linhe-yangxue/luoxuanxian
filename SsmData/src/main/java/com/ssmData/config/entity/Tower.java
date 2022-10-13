package com.ssmData.config.entity;

public class Tower {
    /** 关卡ID*/
    private int ID;
    /** 关卡名字*/
    private String Name;
    /** 每关1到9层的怪物模型*/
    private String[] TowerMon;
    /** 每关1到9层的怪物战力*/
    private int[] TowerMonFC;
    /** BossID*/
    private int[] Boss;
    /** Boss等级*/
    private int[] BossLv;
    /** Boss形象*/
    private String Model;
    /** 道具ID*/
    private int[] TowerItem;
    /** 道具数量*/
    private int[] ItemNum;
    /** 金币*/
    private int[] Gold;
    /** 奖励ID*/
    private int Award;
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

    public String[] getTowerMon() {
        return this.TowerMon;
    }

    public void setTowerMon(String[] TowerMon) {
        this.TowerMon = TowerMon;
    }

    public int[] getTowerMonFC() {
        return this.TowerMonFC;
    }

    public void setTowerMonFC(int[] TowerMonFC) {
        this.TowerMonFC = TowerMonFC;
    }

    public int[] getBoss() {
        return this.Boss;
    }

    public void setBoss(int[] Boss) {
        this.Boss = Boss;
    }

    public int[] getBossLv() {
        return this.BossLv;
    }

    public void setBossLv(int[] BossLv) {
        this.BossLv = BossLv;
    }

    public String getModel() {
        return this.Model;
    }

    public void setModel(String Model) {
        this.Model = Model;
    }

    public int[] getTowerItem() {
        return this.TowerItem;
    }

    public void setTowerItem(int[] TowerItem) {
        this.TowerItem = TowerItem;
    }

    public int[] getItemNum() {
        return this.ItemNum;
    }

    public void setItemNum(int[] ItemNum) {
        this.ItemNum = ItemNum;
    }

    public int[] getGold() {
        return this.Gold;
    }

    public void setGold(int[] Gold) {
        this.Gold = Gold;
    }

    public int getAward() {
        return this.Award;
    }

    public void setAward(int Award) {
        this.Award = Award;
    }

    public int getNotice() {
        return this.Notice;
    }

    public void setNotice(int Notice) {
        this.Notice = Notice;
    }


}