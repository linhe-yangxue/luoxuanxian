package com.ssmData.config.entity;

public class Turnplate {
    /** ID*/
    private int ID;
    /** 物品组*/
    private int Item;
    /** 物品数量*/
    private int Quantity;
    /** 权重*/
    private int Weight;
    /** 权重加成*/
    private int Addition;
    /** 是否清空*/
    private int TimeReset;

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public int getWeight() {
        return this.Weight;
    }

    public void setWeight(int Weight) {
        this.Weight = Weight;
    }

    public int getAddition() {
        return this.Addition;
    }

    public void setAddition(int Addition) {
        this.Addition = Addition;
    }

    public int getTimeReset() {
        return this.TimeReset;
    }

    public void setTimeReset(int TimeReset) {
        this.TimeReset = TimeReset;
    }


}