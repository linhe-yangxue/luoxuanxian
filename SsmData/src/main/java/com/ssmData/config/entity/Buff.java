package com.ssmData.config.entity;

public class Buff {
    private int ID;
    private int Type;
    private int Time;
    private int[] Property;
    private int Calculate;
    private int[] Value;
    private int[] LvValue;
    private int TickTime;
    private int TickDamage;
    private int TickDamageLv;
    private int CoverTp;
    private int MaxStack;
    private int SpecialTp;
    private String[] Effect;
    private int Pos;

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

    public int getTime() {
        return this.Time;
    }

    public void setTime(int Time) {
        this.Time = Time;
    }

    public int[] getProperty() {
        return this.Property;
    }

    public void setProperty(int[] Property) {
        this.Property = Property;
    }

    public int getCalculate() {
        return this.Calculate;
    }

    public void setCalculate(int Calculate) {
        this.Calculate = Calculate;
    }

    public int[] getValue() {
        return this.Value;
    }

    public void setValue(int[] Value) {
        this.Value = Value;
    }

    public int[] getLvValue() {
        return this.LvValue;
    }

    public void setLvValue(int[] LvValue) {
        this.LvValue = LvValue;
    }

    public int getTickTime() {
        return this.TickTime;
    }

    public void setTickTime(int TickTime) {
        this.TickTime = TickTime;
    }

    public int getTickDamage() {
        return this.TickDamage;
    }

    public void setTickDamage(int TickDamage) {
        this.TickDamage = TickDamage;
    }

    public int getTickDamageLv() {
        return this.TickDamageLv;
    }

    public void setTickDamageLv(int TickDamageLv) {
        this.TickDamageLv = TickDamageLv;
    }

    public int getCoverTp() {
        return this.CoverTp;
    }

    public void setCoverTp(int CoverTp) {
        this.CoverTp = CoverTp;
    }

    public int getMaxStack() {
        return this.MaxStack;
    }

    public void setMaxStack(int MaxStack) {
        this.MaxStack = MaxStack;
    }

    public int getSpecialTp() {
        return this.SpecialTp;
    }

    public void setSpecialTp(int SpecialTp) {
        this.SpecialTp = SpecialTp;
    }

    public String[] getEffect() {
        return this.Effect;
    }

    public void setEffect(String[] Effect) {
        this.Effect = Effect;
    }

    public int getPos() {
        return this.Pos;
    }

    public void setPos(int Pos) {
        this.Pos = Pos;
    }


}
