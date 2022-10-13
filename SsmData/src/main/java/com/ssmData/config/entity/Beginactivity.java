package com.ssmData.config.entity;

public class Beginactivity {
    /** 活动编号*/
    private int ID;
    /** 平台*/
    private String[] PlatformID;
    /** 开服类型*/
    private int Type;
    /** 延迟开启时间*/
    private int DelayT;
    /** 活动持续时间*/
    private float Time;
    /** icon排序*/
    private int Rank;

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

    public int getDelayT() {
        return this.DelayT;
    }

    public void setDelayT(int DelayT) {
        this.DelayT = DelayT;
    }

    public float getTime() {
        return this.Time;
    }

    public void setTime(float Time) {
        this.Time = Time;
    }

    public int getRank() {
        return this.Rank;
    }

    public void setRank(int Rank) {
        this.Rank = Rank;
    }


}