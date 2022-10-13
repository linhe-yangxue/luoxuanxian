package com.ssmData.config.entity;

public class Enchant {
    /** 附魔ID*/
    private int ID;
    /** 品质*/
    private int EnchantStar;
    /** 随机权重*/
    private int EnchantPR;
    /** 基础属性*/
    private int EnchantBasics;
    /** 基础属性数值*/
    private float EnchantValue;
    /** 附魔成长值*/
    private float EnchantUp;

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getEnchantStar() {
        return this.EnchantStar;
    }

    public void setEnchantStar(int EnchantStar) {
        this.EnchantStar = EnchantStar;
    }

    public int getEnchantPR() {
        return this.EnchantPR;
    }

    public void setEnchantPR(int EnchantPR) {
        this.EnchantPR = EnchantPR;
    }

    public int getEnchantBasics() {
        return this.EnchantBasics;
    }

    public void setEnchantBasics(int EnchantBasics) {
        this.EnchantBasics = EnchantBasics;
    }

    public float getEnchantValue() {
        return this.EnchantValue;
    }

    public void setEnchantValue(float EnchantValue) {
        this.EnchantValue = EnchantValue;
    }

    public float getEnchantUp() {
        return this.EnchantUp;
    }

    public void setEnchantUp(float EnchantUp) {
        this.EnchantUp = EnchantUp;
    }


}
