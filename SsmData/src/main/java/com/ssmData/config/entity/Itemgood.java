package com.ssmData.config.entity;

public class Itemgood {
    /** 货品ID*/
    private int Id;
    /** 道具ID*/
    private int Item;
    /** 道具数量*/
    private int Counts;
    /** 兑换消耗*/
    private int Price;
    /** 权重*/
    private int Random;
    /** 打折几率*/
    private int Rate;
    /** 打几折*/
    private int Off;
    /** 所需vip*/
    private int Vip;

    public int getId() {
        return this.Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getItem() {
        return this.Item;
    }

    public void setItem(int Item) {
        this.Item = Item;
    }

    public int getCounts() {
        return this.Counts;
    }

    public void setCounts(int Counts) {
        this.Counts = Counts;
    }

    public int getPrice() {
        return this.Price;
    }

    public void setPrice(int Price) {
        this.Price = Price;
    }

    public int getRandom() {
        return this.Random;
    }

    public void setRandom(int Random) {
        this.Random = Random;
    }

    public int getRate() {
        return this.Rate;
    }

    public void setRate(int Rate) {
        this.Rate = Rate;
    }

    public int getOff() {
        return this.Off;
    }

    public void setOff(int Off) {
        this.Off = Off;
    }

    public int getVip() {
        return this.Vip;
    }

    public void setVip(int Vip) {
        this.Vip = Vip;
    }


}