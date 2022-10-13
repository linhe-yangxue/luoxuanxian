package com.ssmData.config.entity;

public class Lineup {
    /** ID*/
    private int ID;
    /** 分类*/
    private int Type;
    /** 阵容包含角色*/
    private int[] LineupRole;
    /** 奖励钻石*/
    private int Diamonds;
    /** 奖励道具id*/
    private int[] ItemId;
    /** 奖励道具数量*/
    private int[] ItemNum;
    /** 金币奖励*/
    private int Money;
    /** 阵容名称*/
    private String Txt;

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

    public int[] getLineupRole() {
        return this.LineupRole;
    }

    public void setLineupRole(int[] LineupRole) {
        this.LineupRole = LineupRole;
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

    public String getTxt() {
        return this.Txt;
    }

    public void setTxt(String Txt) {
        this.Txt = Txt;
    }


}