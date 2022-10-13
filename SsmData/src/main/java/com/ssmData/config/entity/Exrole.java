package com.ssmData.config.entity;

public class Exrole {
    /** 礼包ID*/
    private int ID;
    /** 礼包金币*/
    private int Gold;
    /** 礼包道具*/
    private int[] Item;
    /** 道具数量*/
    private int[] Counts;
    /** VIP等级*/
    private int VIP;
    /** 购买次数*/
    private int Times;
    /** 购买钻石*/
    private int Diamond;
    /** 折扣钻石*/
    private int Off;

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getGold() {
        return this.Gold;
    }

    public void setGold(int Gold) {
        this.Gold = Gold;
    }

    public int[] getItem() {
        return this.Item;
    }

    public void setItem(int[] Item) {
        this.Item = Item;
    }

    public int[] getCounts() {
        return this.Counts;
    }

    public void setCounts(int[] Counts) {
        this.Counts = Counts;
    }

    public int getVIP() {
        return this.VIP;
    }

    public void setVIP(int VIP) {
        this.VIP = VIP;
    }

    public int getTimes() {
        return this.Times;
    }

    public void setTimes(int Times) {
        this.Times = Times;
    }

    public int getDiamond() {
        return this.Diamond;
    }

    public void setDiamond(int Diamond) {
        this.Diamond = Diamond;
    }

    public int getOff() {
        return this.Off;
    }

    public void setOff(int Off) {
        this.Off = Off;
    }


}