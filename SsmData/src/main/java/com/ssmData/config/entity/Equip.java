package com.ssmData.config.entity;

public class Equip {
    /** 装备ID*/
    private int ID;
    /** 装备部位*/
    private int Position;
    /** 基础属性*/
    private int[] Basics;
    /** 基础属性数值*/
    private int[] BasicsValue;
    /** 强化成长值*/
    private float[] BasicsUp;
    /** 精炼属性*/
    private int[] Refine;
    /** 精炼属性数值*/
    private float[] RefineValue;
    /** 精炼成长值*/
    private float[] RefineUp;
    /** 套装ID*/
    private int SuitID;
    /** 套装内的装备ID*/
    private int[] EquipSuit;
    /** 分解获得数量*/
    private int Resolve;

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getPosition() {
        return this.Position;
    }

    public void setPosition(int Position) {
        this.Position = Position;
    }

    public int[] getBasics() {
        return this.Basics;
    }

    public void setBasics(int[] Basics) {
        this.Basics = Basics;
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

    public int[] getRefine() {
        return this.Refine;
    }

    public void setRefine(int[] Refine) {
        this.Refine = Refine;
    }

    public float[] getRefineValue() {
        return this.RefineValue;
    }

    public void setRefineValue(float[] RefineValue) {
        this.RefineValue = RefineValue;
    }

    public float[] getRefineUp() {
        return this.RefineUp;
    }

    public void setRefineUp(float[] RefineUp) {
        this.RefineUp = RefineUp;
    }

    public int getSuitID() {
        return this.SuitID;
    }

    public void setSuitID(int SuitID) {
        this.SuitID = SuitID;
    }

    public int[] getEquipSuit() {
        return this.EquipSuit;
    }

    public void setEquipSuit(int[] EquipSuit) {
        this.EquipSuit = EquipSuit;
    }

    public int getResolve() {
        return this.Resolve;
    }

    public void setResolve(int Resolve) {
        this.Resolve = Resolve;
    }


}