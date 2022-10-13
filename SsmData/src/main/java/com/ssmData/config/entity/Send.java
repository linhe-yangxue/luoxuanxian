package com.ssmData.config.entity;

public class Send {
    /** 外派任务编号*/
    private int SendId;
    /** 外派任务等级区间*/
    private int[] Lv;
    /** 品质*/
    private int Quality;
    /** 钻石*/
    private int[] Jewel;
    /** 金币*/
    private int[] Gold;
    /** 战队经验*/
    private int[] Exp;
    /** 道具*/
    private int[] Item;
    /** 道具数量*/
    private int[] Quantity;
    /** 掠夺道具数量*/
    private int[] RobItem;
    /** 抽取权重*/
    private int SendPR;
    /** 至尊探险钻石费用*/
    private int VipSend;
    /** 任务时间*/
    private int Time;
    /** 所需最低斗士数量*/
    private int RoleNum;
    /** 任务场景*/
    private String Print;
    /** 任务对应战斗场景*/
    private int Scene;
    /** 小怪形象*/
    private String[] Model;

    public int getSendId() {
        return this.SendId;
    }

    public void setSendId(int SendId) {
        this.SendId = SendId;
    }

    public int[] getLv() {
        return this.Lv;
    }

    public void setLv(int[] Lv) {
        this.Lv = Lv;
    }

    public int getQuality() {
        return this.Quality;
    }

    public void setQuality(int Quality) {
        this.Quality = Quality;
    }

    public int[] getJewel() {
        return this.Jewel;
    }

    public void setJewel(int[] Jewel) {
        this.Jewel = Jewel;
    }

    public int[] getGold() {
        return this.Gold;
    }

    public void setGold(int[] Gold) {
        this.Gold = Gold;
    }

    public int[] getExp() {
        return this.Exp;
    }

    public void setExp(int[] Exp) {
        this.Exp = Exp;
    }

    public int[] getItem() {
        return this.Item;
    }

    public void setItem(int[] Item) {
        this.Item = Item;
    }

    public int[] getQuantity() {
        return this.Quantity;
    }

    public void setQuantity(int[] Quantity) {
        this.Quantity = Quantity;
    }

    public int[] getRobItem() {
        return this.RobItem;
    }

    public void setRobItem(int[] RobItem) {
        this.RobItem = RobItem;
    }

    public int getSendPR() {
        return this.SendPR;
    }

    public void setSendPR(int SendPR) {
        this.SendPR = SendPR;
    }

    public int getVipSend() {
        return this.VipSend;
    }

    public void setVipSend(int VipSend) {
        this.VipSend = VipSend;
    }

    public int getTime() {
        return this.Time;
    }

    public void setTime(int Time) {
        this.Time = Time;
    }

    public int getRoleNum() {
        return this.RoleNum;
    }

    public void setRoleNum(int RoleNum) {
        this.RoleNum = RoleNum;
    }

    public String getPrint() {
        return this.Print;
    }

    public void setPrint(String Print) {
        this.Print = Print;
    }

    public int getScene() {
        return this.Scene;
    }

    public void setScene(int Scene) {
        this.Scene = Scene;
    }

    public String[] getModel() {
        return this.Model;
    }

    public void setModel(String[] Model) {
        this.Model = Model;
    }


}