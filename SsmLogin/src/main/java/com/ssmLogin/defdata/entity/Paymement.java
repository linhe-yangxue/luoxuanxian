package com.ssmLogin.defdata.entity;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("Paymement")
public class Paymement implements Serializable {
    private String ownOrder;

    private String uuid;

    private Long guid;

    private String gid;

    private String pid;

    private Integer zid;

    private String account;

    private String nickname;

    private Date createDate;

    private Date complateDate;

    private String goodsOrder;

    private Integer shopId;

    private Integer goodsNum;

    private Float amount;

    private Integer sendStatus;

    private String sendTarget;

    private Integer status;

    private static final long serialVersionUID = 1L;

    public String getOwnOrder() {
        return ownOrder;
    }

    public void setOwnOrder(String ownOrder) {
        this.ownOrder = ownOrder == null ? null : ownOrder.trim();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    public Long getGuid() {
        return guid;
    }

    public void setGuid(Long guid) {
        this.guid = guid;
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getComplateDate() {
        return complateDate;
    }

    public void setComplateDate(Date complateDate) {
        this.complateDate = complateDate;
    }

    public String getGoodsOrder() {
        return goodsOrder;
    }

    public void setGoodsOrder(String goodsOrder) {
        this.goodsOrder = goodsOrder == null ? null : goodsOrder.trim();
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getSendTarget() {
        return sendTarget;
    }

    public void setSendTarget(String sendTarget) {
        this.sendTarget = sendTarget == null ? null : sendTarget.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}