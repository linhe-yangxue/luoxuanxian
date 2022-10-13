package com.ssmData.config.entity;

public class Equipup {
    /** 等级*/
    private int Level;
    /** 精炼所需道具ID*/
    private int[] RefineItem;
    /** 精炼所需道具数量*/
    private int[] RefineItemNum;
    /** 强化所需金币*/
    private int BasicsGold;
    /** 饰品升级所需材料数量*/
    private int Ornaments;
    /** 附魔所需道具数量*/
    private int EnchantItemNum;
    /** 饰品进阶所经验*/
    private int JewelryExp;
    /** 饰品进阶所需等级*/
    private int JewelryNeed;

    public int getLevel() {
        return this.Level;
    }

    public void setLevel(int Level) {
        this.Level = Level;
    }

    public int[] getRefineItem() {
        return this.RefineItem;
    }

    public void setRefineItem(int[] RefineItem) {
        this.RefineItem = RefineItem;
    }

    public int[] getRefineItemNum() {
        return this.RefineItemNum;
    }

    public void setRefineItemNum(int[] RefineItemNum) {
        this.RefineItemNum = RefineItemNum;
    }

    public int getBasicsGold() {
        return this.BasicsGold;
    }

    public void setBasicsGold(int BasicsGold) {
        this.BasicsGold = BasicsGold;
    }

    public int getOrnaments() {
        return this.Ornaments;
    }

    public void setOrnaments(int Ornaments) {
        this.Ornaments = Ornaments;
    }

    public int getEnchantItemNum() {
        return this.EnchantItemNum;
    }

    public void setEnchantItemNum(int EnchantItemNum) {
        this.EnchantItemNum = EnchantItemNum;
    }

    public int getJewelryExp() {
        return this.JewelryExp;
    }

    public void setJewelryExp(int JewelryExp) {
        this.JewelryExp = JewelryExp;
    }

    public int getJewelryNeed() {
        return this.JewelryNeed;
    }

    public void setJewelryNeed(int JewelryNeed) {
        this.JewelryNeed = JewelryNeed;
    }


}