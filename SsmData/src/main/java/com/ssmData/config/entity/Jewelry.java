package com.ssmData.config.entity;

public class Jewelry {
    /** 饰品ID*/
    private int ID;
    /** 饰品类型*/
    private int Position;
    /** 强化属性*/
    private int[] Basics;
    /** 饰品进阶ID*/
    private int[] Advanced;
    /** 饰品名称*/
    private String Name;
    /** 饰品图标*/
    private String[] Icon;

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

    public int[] getAdvanced() {
        return this.Advanced;
    }

    public void setAdvanced(int[] Advanced) {
        this.Advanced = Advanced;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String[] getIcon() {
        return this.Icon;
    }

    public void setIcon(String[] Icon) {
        this.Icon = Icon;
    }


}