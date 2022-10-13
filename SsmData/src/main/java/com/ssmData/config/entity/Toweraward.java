package com.ssmData.config.entity;

public class Toweraward {
    /** 所需层数*/
    private int ID;
    /** 奖励钻石*/
    private int Diamonds;
    /** 奖励道具id*/
    private int[] ItemId;
    /** 奖励道具数量*/
    private int[] ItemNum;

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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


}
