package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class PlayerDuelFightingInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	public String _id;
	
    @Indexed
    public String uid;                      // 玩家唯一ID
    
    public List<Integer> team_fight;		//各队战力，按次序排开
    
    public int fight_all;		//全队战力，用于匹配
    
    public void Init(String uid)
    {
    	this.uid = uid;
    	team_fight = new ArrayList<Integer>();
    	fight_all = 0;
    }
}
