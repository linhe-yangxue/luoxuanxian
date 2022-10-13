package com.ssmLogin.defdata.entity;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("LogindataKey")
public class LogindataKey implements Serializable {
    private Long guid;

    private Date lgdate;

    private static final long serialVersionUID = 1L;

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
}