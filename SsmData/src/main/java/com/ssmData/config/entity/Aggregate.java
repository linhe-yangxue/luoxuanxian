package com.ssmData.config.entity;

public class Aggregate {
    /** 天数*/
    private int ID;
    /** 钻石*/
    private int Jewel;
    /** 金币*/
    private int Gold;
    /** 道具*/
    private int Item;
    /** 道具数量*/
    private int Quantity;

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public int getItem() {
        return this.Item;
    }

    public void setItem(int Item) {
        this.Item = Item;
    }

    public int getQuantity() {
        return this.Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }


}