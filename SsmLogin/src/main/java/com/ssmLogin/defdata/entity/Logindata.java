package com.ssmLogin.defdata.entity;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("Logindata")
public class Logindata extends LogindataKey implements Serializable {
    private String account;

    private String uid;

    private String gid;

    private String pid;

    private Integer zid;

    private Integer isold;

    private static final long serialVersionUID = 1L;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid == null ? null : uid.trim();
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid == null ? null : gid.trim();
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid == null ? null : pid.trim();
    }

    public Integer getZid() {
        return zid;
    }

    public void setZid(Integer zid) {
        this.zid = zid;
    }

    public Integer getIsold() {
        return isold;
    }

    public void setIsold(Integer isold) {
        this.isold = isold;
    }
}