package com.ssmData.config.entity;

public class Basicactivity {
    /** 活动编号*/
    private int ID;
    /** 平台*/
    private String[] PlatformID;
    /** 活动类型*/
    private int Type;
    /** 开服屏蔽*/
    private int Close;
    /** 开启日期*/
    private String OpenT;
    /** 结束日期*/
    private String FinishT;

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String[] getPlatformID() {
        return this.PlatformID;
    }

    public void setPlatformID(String[] PlatformID) {
        this.PlatformID = PlatformID;
    }

    public int getType() {
        return this.Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public int getClose() {
        return this.Close;
    }

    public void setClose(int Close) {
        this.Close = Close;
    }

    public String getOpenT() {
        return this.OpenT;
    }

    public void setOpenT(String OpenT) {
        this.OpenT = OpenT;
    }

    public String getFinishT() {
        return this.FinishT;
    }

    public void setFinishT(String FinishT) {
        this.FinishT = FinishT;
    }


}
