package com.ssmData.config.entity;

public class Vipbonus {
    /** VIP等级*/
    private int VIP;
    /** VIP钻石奖励*/
    private int VIPDiamond;
    /** VIP金币*/
    private int VIPGold;
    /** VIP福利道具*/
    private int[] VIPItem;
    /** 道具数量*/
    private int[] Counts;

    public int getVIP() {
        return this.VIP;
    }

    public void setVIP(int VIP) {
        this.VIP = VIP;
    }

    public int getVIPDiamond() {
        return this.VIPDiamond;
    }

    public void setVIPDiamond(int VIPDiamond) {
        this.VIPDiamond = VIPDiamond;
    }

    public int getVIPGold() {
        return this.VIPGold;
    }

    public void setVIPGold(int VIPGold) {
        this.VIPGold = VIPGold;
    }

    public int[] getVIPItem() {
        return this.VIPItem;
    }

    public void setVIPItem(int[] VIPItem) {
        this.VIPItem = VIPItem;
    }

    public int[] getCounts() {
        return this.Counts;
    }

    public void setCounts(int[] Counts) {
        this.Counts = Counts;
    }


}