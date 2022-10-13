package com.ssmData.config.entity;

public class Damagereward {
    /** id*/
    private int Id;
    /** 所需等级*/
    private int Damage;
    /** 奖励钻石*/
    private int Diamonds;
    /** 金币奖励*/
    private int Money;
    /** 奖励道具id*/
    private int[] ItemId;
    /** 奖励道具数量*/
    private int[] ItemNum;

    public int getId() {
        return this.Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getDamage() {
        return this.Damage;
    }

    public void setDamage(int Damage) {
        this.Damage = Damage;
    }

    public int getDiamonds() {
        return this.Diamonds;
    }

    public void setDiamonds(int Diamonds) {
        this.Diamonds = Diamonds;
    }

    public int getMoney() {
        return this.Money;
    }

    public void setMoney(int Money) {
        this.Money = Money;
    }

    public int[] getItemId() {
        return this.ItemId;
    }

    public void setItemId(int[] ItemId) {
        this.ItemId = ItemId;
    }

    public int[] getItemNum() {
        return this.ItemNum;
    }

    public void setItemNum(int[] ItemNum) {
        this.ItemNum = ItemNum;
    }


}