package com.ssmData.dbase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class ArenaInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	public String _id;
	
    @Indexed
    public String uid; 				//通用表 只会等于固定值
	
	public Map<Integer, ArenaRankInfo> rank;
	
	public ArenaInfo Clone()
	{
		ArenaInfo c = new ArenaInfo();
		c.rank = new HashMap<Integer, ArenaRankInfo>();
		for (Entry<Integer, ArenaRankInfo> i : rank.entrySet())
		{
			c.rank.put(i.getKey(), i.getValue().Clone());
		}
		return c;
	}
	
	public Entry<Integer, ArenaRankInfo> findMyRank(String uuid)
	{
		for (Entry<Integer, ArenaRankInfo> i : rank.entrySet())
		{
			if (i.getValue().uuid.equals(uuid))
				return i;
		}
		return null;
	}
}
