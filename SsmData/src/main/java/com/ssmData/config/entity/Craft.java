package com.ssmData.config.entity;

public class Craft {
    /** ID*/
    private int ID;
    /** 合成类型*/
    private int Type;
    /** 合成物品ID*/
    private int[] Item;
    /** 合成物品权重*/
    private int[] Itemvalue;
    /** 需要道具*/
    private int[] Require;
    /** 需要道具数量*/
    private int[] Counts;
    /** 需要金币*/
    private int Gold;
    /** 显示ICON的ID*/
    private int Icon;

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getType() {
        return this.Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public int[] getItem() {
        return this.Item;
    }

    public void setItem(int[] Item) {
        this.Item = Item;
    }

    public int[] getItemvalue() {
        return this.Itemvalue;
    }

    public void setItemvalue(int[] Itemvalue) {
        this.Itemvalue = Itemvalue;
    }

    public int[] getRequire() {
        return this.Require;
    }

    public void setRequire(int[] Require) {
        this.Require = Require;
    }

    public int[] getCounts() {
        return this.Counts;
    }

    public void setCounts(int[] Counts) {
        this.Counts = Counts;
    }

    public int getGold() {
        return this.Gold;
    }

    public void setGold(int Gold) {
        this.Gold = Gold;
    }

    public int getIcon() {
        return this.Icon;
    }

    public void setIcon(int Icon) {
        this.Icon = Icon;
    }


}