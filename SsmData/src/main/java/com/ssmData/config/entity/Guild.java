package com.ssmData.config.entity;

public class Guild {
    /** 公会等级*/
    private int GuildLv;
    /** 升级所需经验*/
    private int GuildExp;
    /** 人数*/
    private int GuildPeople;

    public int getGuildLv() {
        return this.GuildLv;
    }

    public void setGuildLv(int GuildLv) {
        this.GuildLv = GuildLv;
    }

    public int getGuildExp() {
        return this.GuildExp;
    }

    public void setGuildExp(int GuildExp) {
        this.GuildExp = GuildExp;
    }

    public int getGuildPeople() {
        return this.GuildPeople;
    }

    public void setGuildPeople(int GuildPeople) {
        this.GuildPeople = GuildPeople;
    }


}