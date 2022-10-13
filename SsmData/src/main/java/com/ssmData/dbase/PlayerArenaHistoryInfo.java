package com.ssmData.dbase;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class PlayerArenaHistoryInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final int HISTORY_MAX_CNT = 7;
	
	@Id
	public String _id;
	
    @Indexed
    public String uid; 				//通用表 只会等于固定值
    
    public List<Integer> rank;	//历史排名, 最多HISTORY_MAX_CNT天
    
    public void AddRank(int new_rank)
    {
    	if (rank.size() >= HISTORY_MAX_CNT)
    	{
    		rank.remove(0);
    	}
    	rank.add(new_rank);
    }
}
