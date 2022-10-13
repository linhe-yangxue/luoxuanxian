package com.ssmGame.defdata.msg.role;

/**
 * 改变上阵角色消息
 * Created by WYM on 2016/11/11.
 */
public class RoleChangeHeroMsg {

    // 被更换的角色在pve_team中的id
    public int seat_id;

    // 本次要上阵的角色id
    public int role_id;

}
