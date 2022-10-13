package com.ssmData.config.entity;

public class Scroll {
    /** ID*/
    private int ID;
    /** 名字*/
    private String Name;
    /** 恢复类型*/
    private int Type;
    /** 购买初始价格*/
    private int InitialP;
    /** 价格叠加数*/
    private int PriceM;
    /** 1次购买量*/
    private int ASpurchase;
    /** 卷轴上限数*/
    private int UpperL;
    /** 恢复时间*/
    private int CSRTime;
    /** 卷轴ICON*/
    private String Icon;
    /** VIP购买次数*/
    private int[] BuyTime;
    /** 提示消息*/
    private int Info;
    /** 道具ID*/
    private int Item;

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getType() {
        return this.Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public int getInitialP() {
        return this.InitialP;
    }

    public void setInitialP(int InitialP) {
        this.InitialP = InitialP;
    }

    public int getPriceM() {
        return this.PriceM;
    }

    public void setPriceM(int PriceM) {
        this.PriceM = PriceM;
    }

    public int getASpurchase() {
        return this.ASpurchase;
    }

    public void setASpurchase(int ASpurchase) {
        this.ASpurchase = ASpurchase;
    }

    public int getUpperL() {
        return this.UpperL;
    }

    public void setUpperL(int UpperL) {
        this.UpperL = UpperL;
    }

    public int getCSRTime() {
        return this.CSRTime;
    }

    public void setCSRTime(int CSRTime) {
        this.CSRTime = CSRTime;
    }

    public String getIcon() {
        return this.Icon;
    }

    public void setIcon(String Icon) {
        this.Icon = Icon;
    }

    public int[] getBuyTime() {
        return this.BuyTime;
    }

    public void setBuyTime(int[] BuyTime) {
        this.BuyTime = BuyTime;
    }

    public int getInfo() {
        return this.Info;
    }

    public void setInfo(int Info) {
        this.Info = Info;
    }

    public int getItem() {
        return this.Item;
    }

    public void setItem(int Item) {
        this.Item = Item;
    }


}