package com.ssmData.config.entity;

public class Tech {
    /** 科技ID*/
    private int ID;
    /** 科技属性*/
    private int[] Tech;
    /** 科技属性数值*/
    private int[] TechValue;
    /** 科技成长值*/
    private float[] TechUp;
    /** 科技最大等级*/
    private int LvMax;
    /** 科技名称*/
    private String Name;
    /** 科技图标*/
    private String Icon;

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int[] getTech() {
        return this.Tech;
    }

    public void setTech(int[] Tech) {
        this.Tech = Tech;
    }

    public int[] getTechValue() {
        return this.TechValue;
    }

    public void setTechValue(int[] TechValue) {
        this.TechValue = TechValue;
    }

    public float[] getTechUp() {
        return this.TechUp;
    }

    public void setTechUp(float[] TechUp) {
        this.TechUp = TechUp;
    }

    public int getLvMax() {
        return this.LvMax;
    }

    public void setLvMax(int LvMax) {
        this.LvMax = LvMax;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getIcon() {
        return this.Icon;
    }

    public void setIcon(String Icon) {
        this.Icon = Icon;
    }


}