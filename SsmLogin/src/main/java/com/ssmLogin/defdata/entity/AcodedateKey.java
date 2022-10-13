package com.ssmLogin.defdata.entity;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("AcodedateKey")
public class AcodedateKey implements Serializable {
    private String gid;

    private String pid;

    private String sign;

    private static final long serialVersionUID = 1L;

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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign == null ? null : sign.trim();
    }
}