package com.ssmData.config.entity;

public class Task {
    /** 任务ID*/
    private int ID;
    /** 任务名字*/
    private String Name;
    /** 任务描述*/
    private String Des;
    /** 后续任务*/
    private int Last;
    /** 任务类型*/
    private int Type;
    /** 完成条件*/
    private int Condition;
    /** 奖励金币*/
    private int Gold;
    /** 奖励钻石*/
    private int Jewel;
    /** 奖励道具*/
    private int[] Item;
    /** 道具数量*/
    private int[] ItemN;
    /** 引导组ID*/
    private int Guide;
    /** 显示IOCN*/
    private int[] Iocn;

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getDes() {
        return this.Des;
    }

    public void setDes(String Des) {
        this.Des = Des;
    }

    public int getLast() {
        return this.Last;
    }

    public void setLast(int Last) {
        this.Last = Last;
    }

    public int getType() {
        return this.Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public int getCondition() {
        return this.Condition;
    }

    public void setCondition(int Condition) {
        this.Condition = Condition;
    }

    public int getGold() {
        return this.Gold;
    }

    public void setGold(int Gold) {
        this.Gold = Gold;
    }

    public int getJewel() {
        return this.Jewel;
    }

    public void setJewel(int Jewel) {
        this.Jewel = Jewel;
    }

    public int[] getItem() {
        return this.Item;
    }

    public void setItem(int[] Item) {
        this.Item = Item;
    }

    public int[] getItemN() {
        return this.ItemN;
    }

    public void setItemN(int[] ItemN) {
        this.ItemN = ItemN;
    }

    public int getGuide() {
        return this.Guide;
    }

    public void setGuide(int Guide) {
        this.Guide = Guide;
    }

    public int[] getIocn() {
        return this.Iocn;
    }

    public void setIocn(int[] Iocn) {
        this.Iocn = Iocn;
    }


}