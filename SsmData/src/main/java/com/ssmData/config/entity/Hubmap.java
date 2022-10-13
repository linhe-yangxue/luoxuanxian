package com.ssmData.config.entity;

public class Hubmap {
    /** id*/
    private int ID;
    /** 服务器ID*/
    private int ServerId;
    /** 服务器地址*/
    private String URL;
    /** 服务器名*/
    private String name;

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getServerId() {
        return this.ServerId;
    }

    public void setServerId(int ServerId) {
        this.ServerId = ServerId;
    }

    public String getURL() {
        return this.URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


}