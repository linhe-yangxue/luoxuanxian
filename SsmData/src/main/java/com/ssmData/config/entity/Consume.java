package com.ssmData.config.entity;

public class Consume {
    /** ID*/
    private int ID;
    /** limit*/
    private float limit;
    /** 奖励钻石数*/
    private int Jewel;
    /** 奖励金币数*/
    private int Gold;
    /** 奖励道具*/
    private int[] Item;
    /** 奖励道具的数量*/
    private int[] Quantity;

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public float getLimit() {
        return this.limit;
    }

    public void setLimit(float limit) {
        this.limit = limit;
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
