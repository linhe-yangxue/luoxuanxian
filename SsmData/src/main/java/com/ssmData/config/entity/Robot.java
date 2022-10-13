package com.ssmData.config.entity;

public class Robot {
    private int Id;
    private int Rank;
    private String Name;
    private int[] Teams;
    private String Icon;
    private int[] Lv;

    public int getId() {
        return this.Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getRank() {
        return this.Rank;
    }

    public void setRank(int Rank) {
        this.Rank = Rank;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int[] getTeams() {
        return this.Teams;
    }

    public void setTeams(int[] Teams) {
        this.Teams = Teams;
    }

    public String getIcon() {
        return this.Icon;
    }

    public void setIcon(String Icon) {
        this.Icon = Icon;
    }

    public int[] getLv() {
        return this.Lv;
    }

    public void setLv(int[] Lv) {
        this.Lv = Lv;
    }


}
