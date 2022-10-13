package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class PlayerDuelInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	public String _id;
	
    @Indexed
    public String uid;                      // 玩家唯一ID
    
    public int wins;					//当天胜利次数
    
    public List<Integer> wins_awards;	//当天已领取的胜利次数奖
    
    public long last_reset_wins;		//上次重置胜利次数时间戳
    
    public int max_score;			//累计功勋
    
    public List<Integer> score_awards;	//功勋领奖情况
    
    public int duel_count;			//当天已挑战次数
    
    public void Init(String uid)
    {
    	this.uid = uid;
    	wins = 0;
    	wins_awards = new ArrayList<Integer>();
    	last_reset_wins = 0;
    	max_score = 0;
    	score_awards = new ArrayList<Integer>();
    	duel_count = 0;
    }
}
