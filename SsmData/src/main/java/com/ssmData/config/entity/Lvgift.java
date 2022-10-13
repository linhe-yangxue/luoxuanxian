package com.ssmData.config.entity;

public class Lvgift {
    /** 商品ID*/
    private int ID;
    /** 持续时间*/
    private int Time;
    /** 所需等级*/
    private int GiftLv;
    /** 钻石*/
    private int Jewel;
    /** 金币*/
    private int Gold;
    /** 道具*/
    private int[] Item;
    /** 道具数量*/
    private int[] Quantity;

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getTime() {
        return this.Time;
    }

    public void setTime(int Time) {
        this.Time = Time;
    }

    public int getGiftLv() {
        return this.GiftLv;
    }

    public void setGiftLv(int GiftLv) {
        this.GiftLv = GiftLv;
    }

    public int getJewel() {
        return this.Jewel;
    }

    public void setJewel(int Jewel) {
        this.Jewel = Jewel;
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

    public int[] getQuantity() {
        return this.Quantity;
    }

    public void setQuantity(int[] Quantity) {
        this.Quantity = Quantity;
    }


}
