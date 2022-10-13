package com.ssmData.config.entity;

public class Award {
    private int ID;
    private int Type;
    private int[] PrizeID;
    private int[] Probability;

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getType() {
        return this.Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public int[] getPrizeID() {
        return this.PrizeID;
    }

    public void setPrizeID(int[] PrizeID) {
        this.PrizeID = PrizeID;
    }

    public int[] getProbability() {
        return this.Probability;
    }

    public void setProbability(int[] Probability) {
        this.Probability = Probability;
    }


}
