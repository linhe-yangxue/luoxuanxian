package com.ssmData.config.entity;

public class Investvip {
    /** 奖励ID*/
    private int ID;
    /** 领取类型*/
    private int type;
    /** 领取参数*/
    private int value;
    /** 奖励道具*/
    private int[] Item;
    /** 道具数量*/
    private int[] Counts;

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
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


}