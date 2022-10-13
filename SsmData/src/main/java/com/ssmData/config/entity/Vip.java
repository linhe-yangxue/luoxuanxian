package com.ssmData.config.entity;

public class Vip {
    /** vip等级*/
    private int ID;
    /** VIP等级所需累计充值金额*/
    private int Rmb;
    /** 钻石捐献次数*/
    private int VipDonate2;
    /** 快速战斗购买次数*/
    private int QuickB;
    /** 限量抽卡次数*/
    private int Limited;
    /** 商店刷新次数*/
    private int RefreshMax;
    /** 金币兑换次数*/
    private int GoldBuy;
    /** 奖励钻石*/
    private int Diamonds;
    /** 奖励道具id*/
    private int[] ItemId;
    /** 奖励道具数量*/
    private int[] ItemNum;
    /** 金币奖励*/
    private int Money;
    /** 礼包钻石价格*/
    private int Cost;
    /** 礼包原价*/
    private int OriginalCost;
    /** VIP说明文字*/
    private String Txt;
    /** 招募积分加成*/
    private float VipPoint;
    /** 钻石转盘*/
    private int Plate;
    /** 金币捐献次数*/
    private int VipDonate1;

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getRmb() {
        return this.Rmb;
    }

    public void setRmb(int Rmb) {
        this.Rmb = Rmb;
    }

    public int getVipDonate2() {
        return this.VipDonate2;
    }

    public void setVipDonate2(int VipDonate2) {
        this.VipDonate2 = VipDonate2;
    }

    public int getQuickB() {
        return this.QuickB;
    }

    public void setQuickB(int QuickB) {
        this.QuickB = QuickB;
    }

    public int getLimited() {
        return this.Limited;
    }

    public void setLimited(int Limited) {
        this.Limited = Limited;
    }

    public int getRefreshMax() {
        return this.RefreshMax;
    }

    public void setRefreshMax(int RefreshMax) {
        this.RefreshMax = RefreshMax;
    }

    public int getGoldBuy() {
        return this.GoldBuy;
    }

    public void setGoldBuy(int GoldBuy) {
        this.GoldBuy = GoldBuy;
    }

    public int getDiamonds() {
        return this.Diamonds;
    }

    public void setDiamonds(int Diamonds) {
        this.Diamonds = Diamonds;
    }

    public int[] getItemId() {
        return this.ItemId;
    }

    public void setItemId(int[] ItemId) {
        this.ItemId = ItemId;
    }

    public int[] getItemNum() {
        return this.ItemNum;
    }

    public void setItemNum(int[] ItemNum) {
        this.ItemNum = ItemNum;
    }

    public int getMoney() {
        return this.Money;
    }

    public void setMoney(int Money) {
        this.Money = Money;
    }

    public int getCost() {
        return this.Cost;
    }

    public void setCost(int Cost) {
        this.Cost = Cost;
    }

    public int getOriginalCost() {
        return this.OriginalCost;
    }

    public void setOriginalCost(int OriginalCost) {
        this.OriginalCost = OriginalCost;
    }

    public String getTxt() {
        return this.Txt;
    }

    public void setTxt(String Txt) {
        this.Txt = Txt;
    }

    public float getVipPoint() {
        return this.VipPoint;
    }

    public void setVipPoint(float VipPoint) {
        this.VipPoint = VipPoint;
    }

    public int getPlate() {
        return this.Plate;
    }

    public void setPlate(int Plate) {
        this.Plate = Plate;
    }

    public int getVipDonate1() {
        return this.VipDonate1;
    }

    public void setVipDonate1(int VipDonate1) {
        this.VipDonate1 = VipDonate1;
    }


}