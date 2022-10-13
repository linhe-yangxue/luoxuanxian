package com.ssmData.config.entity;

public class Jewelryup {
    /** 进阶ID*/
    private int ID;
    /** 强化属性数值*/
    private int[] BasicsValue;
    /** 强化成长值*/
    private float[] BasicsUp;
    /** 进阶所需道具*/
    private int Item;

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int[] getBasicsValue() {
        return this.BasicsValue;
    }

    public void setBasicsValue(int[] BasicsValue) {
        this.BasicsValue = BasicsValue;
    }

    public float[] getBasicsUp() {
        return this.BasicsUp;
    }

    public void setBasicsUp(float[] BasicsUp) {
        this.BasicsUp = BasicsUp;
    }

    public int getItem() {
        return this.Item;
    }

    public void setItem(int Item) {
        this.Item = Item;
    }


}