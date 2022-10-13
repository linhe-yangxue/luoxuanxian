package com.ssmData.config.entity;

public class Talent {
    private int TalentID;
    private int TalentNum;
    private int TalentSkill;
    private int[] TalentItem;
    private int[] TalentCounts;
    private int TalentMoney;
    private String TalentFrame;

    public int getTalentID() {
        return this.TalentID;
    }

    public void setTalentID(int TalentID) {
        this.TalentID = TalentID;
    }

    public int getTalentNum() {
        return this.TalentNum;
    }

    public void setTalentNum(int TalentNum) {
        this.TalentNum = TalentNum;
    }

    public int getTalentSkill() {
        return this.TalentSkill;
    }

    public void setTalentSkill(int TalentSkill) {
        this.TalentSkill = TalentSkill;
    }

    public int[] getTalentItem() {
        return this.TalentItem;
    }

    public void setTalentItem(int[] TalentItem) {
        this.TalentItem = TalentItem;
    }

    public int[] getTalentCounts() {
        return this.TalentCounts;
    }

    public void setTalentCounts(int[] TalentCounts) {
        this.TalentCounts = TalentCounts;
    }

    public int getTalentMoney() {
        return this.TalentMoney;
    }

    public void setTalentMoney(int TalentMoney) {
        this.TalentMoney = TalentMoney;
    }

    public String getTalentFrame() {
        return this.TalentFrame;
    }

    public void setTalentFrame(String TalentFrame) {
        this.TalentFrame = TalentFrame;
    }


}
