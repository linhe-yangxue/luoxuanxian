package com.ssmData.config.entity;

public class Itemshop {
    private int Booth;
    private int Counts;
    private int[] ItemGoodId;
    private int[] Time;
    private int[] Type;
    private String BoothName;

    public int getBooth() {
        return this.Booth;
    }

    public void setBooth(int Booth) {
        this.Booth = Booth;
    }

    public int getCounts() {
        return this.Counts;
    }

    public void setCounts(int Counts) {
        this.Counts = Counts;
    }

    public int[] getItemGoodId() {
        return this.ItemGoodId;
    }

    public void setItemGoodId(int[] ItemGoodId) {
        this.ItemGoodId = ItemGoodId;
    }

    public int[] getTime() {
        return this.Time;
    }

    public void setTime(int[] Time) {
        this.Time = Time;
    }

    public int[] getType() {
        return this.Type;
    }

    public void setType(int[] Type) {
        this.Type = Type;
    }

    public String getBoothName() {
        return this.BoothName;
    }

    public void setBoothName(String BoothName) {
        this.BoothName = BoothName;
    }


}
