package com.ssmData.config.entity;

public class Accumulation {
    /** ID*/
    private int id;
    /** 充值额度*/
    private int rmb;
    /** 奖励钻石*/
    private int diamond;
    /** 奖励金币*/
    private int gold;
    /** 奖励道具*/
    private int[] item;
    /** 道具数量*/
    private int[] quantity;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRmb() {
        return this.rmb;
    }

    public void setRmb(int rmb) {
        this.rmb = rmb;
    }

    public int getDiamond() {
        return this.diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }

    public int getGold() {
        return this.gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int[] getItem() {
        return this.item;
    }

    public void setItem(int[] item) {
        this.item = item;
    }

    public int[] getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int[] quantity) {
        this.quantity = quantity;
    }


}
