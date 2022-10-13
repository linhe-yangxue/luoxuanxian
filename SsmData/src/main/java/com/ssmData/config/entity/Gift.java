package com.ssmData.config.entity;

public class Gift {
    /** ID*/
    private int ID;
    /** 类型*/
    private int Type;
    /** 物品*/
    private int[] Item;
    /** 物品数量*/
    private int[] Quantity;
    /** 购买次数*/
    private int Next;
    /** 原价*/
    private int OriginalPrice;
    /** 打折价*/
    private int Price;

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

    public int[] getItem() {
        return this.Item;
    }

    public void setItem(int[] Item) {
        this.Item = Item;
    }

    public int[] getQuantity() {
        return this.Quantity;
    }

    public void setQuantity(int[] Quantity) {
        this.Quantity = Quantity;
    }

    public int getNext() {
        return this.Next;
    }

    public void setNext(int Next) {
        this.Next = Next;
    }

    public int getOriginalPrice() {
        return this.OriginalPrice;
    }

    public void setOriginalPrice(int OriginalPrice) {
        this.OriginalPrice = OriginalPrice;
    }

    public int getPrice() {
        return this.Price;
    }

    public void setPrice(int Price) {
        this.Price = Price;
    }


}
