package com.ssmData.config.entity;

public class Warrank {
    /** 编号*/
    private int id;
    /** 排名区间*/
    private int[] ArenaNum;
    /** 奖励邮件ID*/
    private int RankMail;
    /** 奖励公会资金*/
    private int Capital;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getArenaNum() {
        return this.ArenaNum;
    }

    public void setArenaNum(int[] ArenaNum) {
        this.ArenaNum = ArenaNum;
    }

    public int getRankMail() {
        return this.RankMail;
    }

    public void setRankMail(int RankMail) {
        this.RankMail = RankMail;
    }

    public int getCapital() {
        return this.Capital;
    }

    public void setCapital(int Capital) {
        this.Capital = Capital;
    }


}