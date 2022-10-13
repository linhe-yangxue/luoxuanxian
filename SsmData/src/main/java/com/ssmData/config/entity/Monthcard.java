package com.ssmData.config.entity;

public class Monthcard {
    /** ID*/
    private int id;
    /** 在线收益*/
    private int[] onLine;
    /** 离线收益*/
    private int[] offLine;
    /** 每日邮件ID*/
    private int mail;
    /** 首次购买邮件ID*/
    private int mailFirst;
    /** 首次购买图标*/
    private int icon;
    /** 描述*/
    private String des;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getOnLine() {
        return this.onLine;
    }

    public void setOnLine(int[] onLine) {
        this.onLine = onLine;
    }

    public int[] getOffLine() {
        return this.offLine;
    }

    public void setOffLine(int[] offLine) {
        this.offLine = offLine;
    }

    public int getMail() {
        return this.mail;
    }

    public void setMail(int mail) {
        this.mail = mail;
    }

    public int getMailFirst() {
        return this.mailFirst;
    }

    public void setMailFirst(int mailFirst) {
        this.mailFirst = mailFirst;
    }

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getDes() {
        return this.des;
    }

    public void setDes(String des) {
        this.des = des;
    }


}
