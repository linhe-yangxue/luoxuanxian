package com.ssmData.config.entity;

public class Tasklvreward {
    private int Id;
    private int Lv;
    private int Diamonds;
    private int[] ItemId;
    private int[] ItemNum;
    private int Money;

    public int getId() {
        return this.Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getLv() {
        return this.Lv;
    }

    public void setLv(int Lv) {
        this.Lv = Lv;
    }

    public int getDiamonds() {
        return this.Diamonds;
    }

    public void setDiamonds(int Diamonds) {
        this.Diamonds = Diamonds;
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

    public int getMoney() {
        return this.Money;
    }

    public void setMoney(int Money) {
        this.Money = Money;
    }


}
