package com.ssmData.config.entity;

public class Bond {
    private int BondID;
    private String BondName;
    private int[] BondAtt;
    private int[] BondAttvalue;
    private String BondTxt;

    public int getBondID() {
        return this.BondID;
    }

    public void setBondID(int BondID) {
        this.BondID = BondID;
    }

    public String getBondName() {
        return this.BondName;
    }

    public void setBondName(String BondName) {
        this.BondName = BondName;
    }

    public int[] getBondAtt() {
        return this.BondAtt;
    }

    public void setBondAtt(int[] BondAtt) {
        this.BondAtt = BondAtt;
    }

    public int[] getBondAttvalue() {
        return this.BondAttvalue;
    }

    public void setBondAttvalue(int[] BondAttvalue) {
        this.BondAttvalue = BondAttvalue;
    }

    public String getBondTxt() {
        return this.BondTxt;
    }

    public void setBondTxt(String BondTxt) {
        this.BondTxt = BondTxt;
    }


}
