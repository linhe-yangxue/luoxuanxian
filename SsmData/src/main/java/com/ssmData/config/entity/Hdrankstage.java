package com.ssmData.config.entity;

public class Hdrankstage {
    /** ID*/
    private int id;
    /** 名次*/
    private int[] rank;
    /** 奖励邮件ID*/
    private int[] mail;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getRank() {
        return this.rank;
    }

    public void setRank(int[] rank) {
        this.rank = rank;
    }

    public int[] getMail() {
        return this.mail;
    }

    public void setMail(int[] mail) {
        this.mail = mail;
    }


}