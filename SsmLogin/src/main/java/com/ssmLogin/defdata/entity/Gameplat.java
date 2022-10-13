package com.ssmLogin.defdata.entity;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("Gameplat")
public class Gameplat extends GameplatKey implements Serializable {
    private String pidNa;

    private String gameNa;

    private Date gameOnline;

    private Integer isRecord;

    private static final long serialVersionUID = 1L;

    public String getPidNa() {
        return pidNa;
    }

    public void setPidNa(String pidNa) {
        this.pidNa = pidNa == null ? null : pidNa.trim();
    }

    public String getGameNa() {
        return gameNa;
    }

    public void setGameNa(String gameNa) {
        this.gameNa = gameNa == null ? null : gameNa.trim();
    }

    public Date getGameOnline() {
        return gameOnline;
    }

    public void setGameOnline(Date gameOnline) {
        this.gameOnline = gameOnline;
    }

    public Integer getIsRecord() {
        return isRecord;
    }

    public void setIsRecord(Integer isRecord) {
        this.isRecord = isRecord;
    }
}