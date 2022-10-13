package com.ssmData.config.entity;

public class Shop {
    private int Id;
    private int[] Booth;
    private int TellType;
    private int TellPar;
    private String ShopName;
    private String ShopIcon;

    public int getId() {
        return this.Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int[] getBooth() {
        return this.Booth;
    }

    public void setBooth(int[] Booth) {
        this.Booth = Booth;
    }

    public int getTellType() {
        return this.TellType;
    }

    public void setTellType(int TellType) {
        this.TellType = TellType;
    }

    public int getTellPar() {
        return this.TellPar;
    }

    public void setTellPar(int TellPar) {
        this.TellPar = TellPar;
    }

    public String getShopName() {
        return this.ShopName;
    }

    public void setShopName(String ShopName) {
        this.ShopName = ShopName;
    }

    public String getShopIcon() {
        return this.ShopIcon;
    }

    public void setShopIcon(String ShopIcon) {
        this.ShopIcon = ShopIcon;
    }


}
