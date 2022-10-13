package com.ssmData.config.entity;

public class Breach {
    /** 天赋ID*/
    private int BreachID;
    /** 突破基础属性倍率*/
    private int BreachRate;
    /** 天赋属性*/
    private int[] BreachAtt;
    /** 天赋属性值*/
    private int[] BreachAttvalue;
    /** 突破道具ID*/
    private int[] BreachItem;
    /** 突破材料数量*/
    private int[] BreachCounts;
    /** 突破所需金币*/
    private int BreachMoney;
    /** 碎片数量*/
    private int FragmentNum;
    /** 所需斗士等级*/
    private int RoleLv;
    /** 天赋描述*/
    private String BreachTxt;

    public int getBreachID() {
        return this.BreachID;
    }

    public void setBreachID(int BreachID) {
        this.BreachID = BreachID;
    }

    public int getBreachRate() {
        return this.BreachRate;
    }

    public void setBreachRate(int BreachRate) {
        this.BreachRate = BreachRate;
    }

    public int[] getBreachAtt() {
        return this.BreachAtt;
    }

    public void setBreachAtt(int[] BreachAtt) {
        this.BreachAtt = BreachAtt;
    }

    public int[] getBreachAttvalue() {
        return this.BreachAttvalue;
    }

    public void setBreachAttvalue(int[] BreachAttvalue) {
        this.BreachAttvalue = BreachAttvalue;
    }

    public int[] getBreachItem() {
        return this.BreachItem;
    }

    public void setBreachItem(int[] BreachItem) {
        this.BreachItem = BreachItem;
    }

    public int[] getBreachCounts() {
        return this.BreachCounts;
    }

    public void setBreachCounts(int[] BreachCounts) {
        this.BreachCounts = BreachCounts;
    }

    public int getBreachMoney() {
        return this.BreachMoney;
    }

    public void setBreachMoney(int BreachMoney) {
        this.BreachMoney = BreachMoney;
    }

    public int getFragmentNum() {
        return this.FragmentNum;
    }

    public void setFragmentNum(int FragmentNum) {
        this.FragmentNum = FragmentNum;
    }

    public int getRoleLv() {
        return this.RoleLv;
    }

    public void setRoleLv(int RoleLv) {
        this.RoleLv = RoleLv;
    }

    public String getBreachTxt() {
        return this.BreachTxt;
    }

    public void setBreachTxt(String BreachTxt) {
        this.BreachTxt = BreachTxt;
    }


}