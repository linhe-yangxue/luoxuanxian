package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class PlayerDuelTeamInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	public String _id;
	
    @Indexed
    public String uid;                      // 玩家唯一ID
    
    public List<Integer> team_0;		//一番队id  位置没人填 0
    public List<Integer> team_1;		//二番队id  位置没人填 0
    public List<Integer> team_2;		//三番队id  位置没人填 0
    
    public PlayerRolesInfo roles_0;	//一番队在挑战的那一刻，计算完数值后，当时的快照
    public PlayerRolesInfo roles_1;	//二番队在挑战的那一刻，计算完数值后，当时的快照
    public PlayerRolesInfo roles_2;	//三番队在挑战的那一刻，计算完数值后，当时的快照
    
    public void Init(String uid)
    {
    	this.uid = uid;
    	
    	team_0 = new ArrayList<Integer>();
    	team_1 = new ArrayList<Integer>();
    	team_2 = new ArrayList<Integer>();
    	
    	roles_0 = new PlayerRolesInfo();
    	roles_0.InitEmpty();
    	roles_1 = new PlayerRolesInfo();
    	roles_1.InitEmpty();
    	roles_2 = new PlayerRolesInfo();
    	roles_2.InitEmpty();
    }
}
