package com.ssmData.config.entity;

/**
 * 升级配置表
 * Created by WYM on 2016/11/7.
 */
public class Grade {
    private int Level;
    private int LvExp;
    private int LvGold;
    private int Scroll;

    public int getLevel() {
        return this.Level;
    }

    public void setLevel(int Level) {
        this.Level = Level;
    }

    public int getLvExp() {
        return this.LvExp;
    }

    public void setLvExp(int LvExp) {
        this.LvExp = LvExp;
    }

    public int getLvGold() {
        return this.LvGold;
    }

    public void setLvGold(int LvGold) {
        this.LvGold = LvGold;
    }

    public int getScroll() {
        return this.Scroll;
    }

    public void setScroll(int Scroll) {
        this.Scroll = Scroll;
    }


}
