package com.ssmData.config.entity;

public class Wish {
    /** 许愿等级*/
    private int Lv;
    /** 升级所需经验*/
    private int Exp;
    /** 当前级能量上限*/
    private int WishMax;
    /** 升到下级回复的能量*/
    private int WishRe;

    public int getLv() {
        return this.Lv;
    }

    public void setLv(int Lv) {
        this.Lv = Lv;
    }

    public int getExp() {
        return this.Exp;
    }

    public void setExp(int Exp) {
        this.Exp = Exp;
    }

    public int getWishMax() {
        return this.WishMax;
    }

    public void setWishMax(int WishMax) {
        this.WishMax = WishMax;
    }

    public int getWishRe() {
        return this.WishRe;
    }

    public void setWishRe(int WishRe) {
        this.WishRe = WishRe;
    }


}
