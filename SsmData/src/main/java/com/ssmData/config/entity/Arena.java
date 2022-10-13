package com.ssmData.config.entity;

public class Arena {
    private int id;
    private int[] ArenaNum;
    private int RankType;
    private int[] RankInterval;
    private int RankDiamonds;
    private int RankPoints;
    private int RankMail;

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

    public int getRankType() {
        return this.RankType;
    }

    public void setRankType(int RankType) {
        this.RankType = RankType;
    }

    public int[] getRankInterval() {
        return this.RankInterval;
    }

    public void setRankInterval(int[] RankInterval) {
        this.RankInterval = RankInterval;
    }

    public int getRankDiamonds() {
        return this.RankDiamonds;
    }

    public void setRankDiamonds(int RankDiamonds) {
        this.RankDiamonds = RankDiamonds;
    }

    public int getRankPoints() {
        return this.RankPoints;
    }

    public void setRankPoints(int RankPoints) {
        this.RankPoints = RankPoints;
    }

    public int getRankMail() {
        return this.RankMail;
    }

    public void setRankMail(int RankMail) {
        this.RankMail = RankMail;
    }


}