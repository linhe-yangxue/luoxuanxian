package com.ssmData.config.entity;

public class Awaken {
    /** 觉醒等级*/
    private int AwakenID;
    /** 对应勾玉数量*/
    private int AwakenStar;
    /** 角色碎片数量*/
    private int[] AwakenFragment;
    /** 觉醒所需金币*/
    private int AwakenMoney;
    /** 觉醒加成基础倍率*/
    private int AwakenRate;

    public int getAwakenID() {
        return this.AwakenID;
    }

    public void setAwakenID(int AwakenID) {
        this.AwakenID = AwakenID;
    }

    public int getAwakenStar() {
        return this.AwakenStar;
    }

    public void setAwakenStar(int AwakenStar) {
        this.AwakenStar = AwakenStar;
    }

    public int[] getAwakenFragment() {
        return this.AwakenFragment;
    }

    public void setAwakenFragment(int[] AwakenFragment) {
        this.AwakenFragment = AwakenFragment;
    }

    public int getAwakenMoney() {
        return this.AwakenMoney;
    }

    public void setAwakenMoney(int AwakenMoney) {
        this.AwakenMoney = AwakenMoney;
    }

    public int getAwakenRate() {
        return this.AwakenRate;
    }

    public void setAwakenRate(int AwakenRate) {
        this.AwakenRate = AwakenRate;
    }


}