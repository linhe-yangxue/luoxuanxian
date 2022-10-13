package com.ssmData.config.entity;

public class Prize {
    private int ID;
    private int[] ItemID;
    private int[] Number;
    private int[] Probability;

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int[] getItemID() {
        return this.ItemID;
    }

    public void setItemID(int[] ItemID) {
        this.ItemID = ItemID;
    }

    public int[] getNumber() {
        return this.Number;
    }

    public void setNumber(int[] Number) {
        this.Number = Number;
    }

    public int[] getProbability() {
        return this.Probability;
    }

    public void setProbability(int[] Probability) {
        this.Probability = Probability;
    }


}
