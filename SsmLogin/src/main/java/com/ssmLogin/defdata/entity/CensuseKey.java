package com.ssmLogin.defdata.entity;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("CensuseKey")
public class CensuseKey implements Serializable {
    private String gid;

    private Date lgdate;

    private String pid;

    private Integer zid;

    private static final long serialVersionUID = 1L;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid == null ? null : gid.trim();
    }

    public Date getLgdate() {
        return lgdate;
    }

    public void setLgdate(Date lgdate) {
        this.lgdate = lgdate;
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