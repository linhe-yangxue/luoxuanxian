package com.ssmData.config.entity;

public class Card {
    /** 卡牌编号*/
    private int CardId;
    /** 抽卡类别*/
    private int CardType;
    /** 奖励类型*/
    private int type;
    /** 对应ID*/
    private int CardPet;
    /** 奖励数量*/
    private int CardNum;
    /** 抽取权重*/
    private int CardPR;
    /** 十连抽必出权重*/
    private int CardTen;
    /** 权重加成*/
    private int CardValue;

    public int getCardId() {
        return this.CardId;
    }

    public void setCardId(int CardId) {
        this.CardId = CardId;
    }

    public int getCardType() {
        return this.CardType;
    }

    public void setCardType(int CardType) {
        this.CardType = CardType;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCardPet() {
        return this.CardPet;
    }

    public void setCardPet(int CardPet) {
        this.CardPet = CardPet;
    }

    public int getCardNum() {
        return this.CardNum;
    }

    public void setCardNum(int CardNum) {
        this.CardNum = CardNum;
    }

    public int getCardPR() {
        return this.CardPR;
    }

    public void setCardPR(int CardPR) {
        this.CardPR = CardPR;
    }

    public int getCardTen() {
        return this.CardTen;
    }

    public void setCardTen(int CardTen) {
        this.CardTen = CardTen;
    }

    public int getCardValue() {
        return this.CardValue;
    }

    public void setCardValue(int CardValue) {
        this.CardValue = CardValue;
    }


}