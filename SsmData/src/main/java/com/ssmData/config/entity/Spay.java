package com.ssmData.config.entity;

public class Spay {
    /** 首冲ID*/
    private int ID;
    /** 充值金额*/
    private int Spay;
    /** 金币奖励*/
    private int SpayGold;
    /** 金币翻倍*/
    private int GoldDouble;
    /** 道具奖励*/
    private int[] Item;
    /** 道具数量*/
    private int[] Counts;
    /** 翻倍*/
    private int[] Double;
    /** 限时时间*/
    private int Ltime;

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getSpay() {
        return this.Spay;
    }

    public void setSpay(int Spay) {
        this.Spay = Spay;
    }

    public int getSpayGold() {
        return this.SpayGold;
    }

    public void setSpayGold(int SpayGold) {
        this.SpayGold = SpayGold;
    }

    public int getGoldDouble() {
        return this.GoldDouble;
    }

    public void setGoldDouble(int GoldDouble) {
        this.GoldDouble = GoldDouble;
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

    public int[] getDouble() {
        return this.Double;
    }

    public void setDouble(int[] Double) {
        this.Double = Double;
    }

    public int getLtime() {
        return this.Ltime;
    }

    public void setLtime(int Ltime) {
        this.Ltime = Ltime;
    }


}