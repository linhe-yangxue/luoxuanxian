package com.ssmData.config.entity;

public class Item {
    /** ID*/
    private int ID;
    /** 道具名称*/
    private String iName;
    /** 道具类型*/
    private int iType;
    /** 品质*/
    private int iStar;
    /** 叠加数量*/
    private int iNum;
    /** 对应道具ID*/
    private int EquipId;
    /** 合成所需数量*/
    private int Synthesis;
    /** 道具礼包物品*/
    private int[] BonusItem;
    /** 道具礼包物品数量*/
    private int[] BonusCounts;
    /** 道具钻石单价*/
    private int Cost;
    /** 对应附魔ID*/
    private int EnchantId;
    /** 产出途径*/
    private String iWay;
    /** 物品获取说明*/
    private String itemTxt1;
    /** 图标*/
    private String iIcon;
    /** 物品说明*/
    private String itemTxt;

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getIName() {
        return this.iName;
    }

    public void setIName(String iName) {
        this.iName = iName;
    }

    public int getIType() {
        return this.iType;
    }

    public void setIType(int iType) {
        this.iType = iType;
    }

    public int getIStar() {
        return this.iStar;
    }

    public void setIStar(int iStar) {
        this.iStar = iStar;
    }

    public int getINum() {
        return this.iNum;
    }

    public void setINum(int iNum) {
        this.iNum = iNum;
    }

    public int getEquipId() {
        return this.EquipId;
    }

    public void setEquipId(int EquipId) {
        this.EquipId = EquipId;
    }

    public int getSynthesis() {
        return this.Synthesis;
    }

    public void setSynthesis(int Synthesis) {
        this.Synthesis = Synthesis;
    }

    public int[] getBonusItem() {
        return this.BonusItem;
    }

    public void setBonusItem(int[] BonusItem) {
        this.BonusItem = BonusItem;
    }

    public int[] getBonusCounts() {
        return this.BonusCounts;
    }

    public void setBonusCounts(int[] BonusCounts) {
        this.BonusCounts = BonusCounts;
    }

    public int getCost() {
        return this.Cost;
    }

    public void setCost(int Cost) {
        this.Cost = Cost;
    }

    public int getEnchantId() {
        return this.EnchantId;
    }

    public void setEnchantId(int EnchantId) {
        this.EnchantId = EnchantId;
    }

    public String getIWay() {
        return this.iWay;
    }

    public void setIWay(String iWay) {
        this.iWay = iWay;
    }

    public String getItemTxt1() {
        return this.itemTxt1;
    }

    public void setItemTxt1(String itemTxt1) {
        this.itemTxt1 = itemTxt1;
    }

    public String getIIcon() {
        return this.iIcon;
    }

    public void setIIcon(String iIcon) {
        this.iIcon = iIcon;
    }

    public String getItemTxt() {
        return this.itemTxt;
    }

    public void setItemTxt(String itemTxt) {
        this.itemTxt = itemTxt;
    }


}