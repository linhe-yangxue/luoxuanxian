package com.ssmData.config.entity;

public class Invite {
    /** 任务编号*/
    private int ID;
    /** 邀请人数*/
    private int People;
    /** 钻石*/
    private int Jewel;

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getPeople() {
        return this.People;
    }

    public void setPeople(int People) {
        this.People = People;
    }

    public int getJewel() {
        return this.Jewel;
    }

    public void setJewel(int Jewel) {
        this.Jewel = Jewel;
    }


}