package com.ssmData.config.entity;

public class Drama {
    /** 剧情战斗我方角色*/
    private int[] Friend;
    /** 我方角色等级*/
    private int[] FriendLV;
    /** 剧情战斗敌方角色*/
    private int[] Enemy;
    /** 敌方角色等级*/
    private int[] EnemyLV;
    /** 战斗场景*/
    private int Scene;
    /** 我方头像*/
    private String OurIcon;
    /** 敌方头像*/
    private String EnemyIcon;

    public int[] getFriend() {
        return this.Friend;
    }

    public void setFriend(int[] Friend) {
        this.Friend = Friend;
    }

    public int[] getFriendLV() {
        return this.FriendLV;
    }

    public void setFriendLV(int[] FriendLV) {
        this.FriendLV = FriendLV;
    }

    public int[] getEnemy() {
        return this.Enemy;
    }

    public void setEnemy(int[] Enemy) {
        this.Enemy = Enemy;
    }

    public int[] getEnemyLV() {
        return this.EnemyLV;
    }

    public void setEnemyLV(int[] EnemyLV) {
        this.EnemyLV = EnemyLV;
    }

    public int getScene() {
        return this.Scene;
    }

    public void setScene(int Scene) {
        this.Scene = Scene;
    }

    public String getOurIcon() {
        return this.OurIcon;
    }

    public void setOurIcon(String OurIcon) {
        this.OurIcon = OurIcon;
    }

    public String getEnemyIcon() {
        return this.EnemyIcon;
    }

    public void setEnemyIcon(String EnemyIcon) {
        this.EnemyIcon = EnemyIcon;
    }


}