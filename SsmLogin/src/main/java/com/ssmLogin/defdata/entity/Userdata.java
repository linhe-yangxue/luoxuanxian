package com.ssmLogin.defdata.entity;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("Userdata")
public class Userdata implements Serializable {
    private String uid;

    private Long guid;

    private Date lgdate;

    private String account;

    private String gid;

    private String pid;

    private Integer zid;

    private static final long serialVersionUID = 1L;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid == null ? null : uid.trim();
    }

    public Long getGuid() {
        return guid;
    }

    public void setGuid(Long guid) {
        this.guid = guid;
    }

    public Date getLgdate() {
        return lgdate;
    }

    public void setLgdate(Date lgdate) {
        this.lgdate = lgdate;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
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
}