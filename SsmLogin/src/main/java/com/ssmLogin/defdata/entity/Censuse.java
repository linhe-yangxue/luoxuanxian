package com.ssmLogin.defdata.entity;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("Censuse")
public class Censuse extends CensuseKey implements Serializable {
    
	private Integer newUser;

    private Integer oldUser;

    private Integer actUser;

    private Float time_left;

    private Float day3_left;

    private Float day7_left;

    private Float newpay;

    private Float money;

    private Float payRate;

    private Float arpu;

    private Float arppu;

    private Float new7pay;

    private static final long serialVersionUID = 1L;

    public Integer getNewUser() {
        return newUser;
    }

    public void setNewUser(Integer newUser) {
        this.newUser = newUser;
    }

    public Integer getOldUser() {
        return oldUser;
    }

    public void setOldUser(Integer oldUser) {
        this.oldUser = oldUser;
    }

    public Integer getActUser() {
        return actUser;
    }

    public void setActUser(Integer actUser) {
        this.actUser = actUser;
    }

    public Float getTime_left() {
        return time_left;
    }

    public void setTime_left(Float time_left) {
        this.time_left = time_left;
    }

    public Float getDay3_left() {
        return day3_left;
    }

    public void setDay3_left(Float day3_left) {
        this.day3_left = day3_left;
    }

    public Float getDay7_left() {
        return day7_left;
    }

    public void setDay7_left(Float day7_left) {
        this.day7_left = day7_left;
    }

    public Float getNewpay() {
        return newpay;
    }

    public void setNewpay(Float newpay) {
        this.newpay = newpay;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Float getPayRate() {
        return payRate;
    }

    public void setPayRate(Float payRate) {
        this.payRate = payRate;
    }

    public Float getArpu() {
        return arpu;
    }

    public void setArpu(Float arpu) {
        this.arpu = arpu;
    }

    public Float getArppu() {
        return arppu;
    }

    public void setArppu(Float arppu) {
        this.arppu = arppu;
    }

    public Float getNew7pay() {
        return new7pay;
    }

    public void setNew7pay(Float new7pay) {
        this.new7pay = new7pay;
    }
}