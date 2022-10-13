package com.ssmData.config.entity;

public class Techup {
    /** 等级*/
    private int Level;
    /** 科技研究所需资金*/
    private int TechResearch;
    /** 科技研究提升等级*/
    private int TechLvUp;
    /** 科技升级所需贡献*/
    private int Money;

    public int getLevel() {
        return this.Level;
    }

    public void setLevel(int Level) {
        this.Level = Level;
    }

    public int getTechResearch() {
        return this.TechResearch;
    }

    public void setTechResearch(int TechResearch) {
        this.TechResearch = TechResearch;
    }

    public int getTechLvUp() {
        return this.TechLvUp;
    }

    public void setTechLvUp(int TechLvUp) {
        this.TechLvUp = TechLvUp;
    }

    public int getMoney() {
        return this.Money;
    }

    public void setMoney(int Money) {
        this.Money = Money;
    }


}