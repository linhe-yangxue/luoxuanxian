package com.ssmData.dbase;

import java.io.Serializable;

public class ArenaRankInfo implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	public String uuid;
	public int type;
	
	public ArenaRankInfo Clone()
	{
		ArenaRankInfo c = new ArenaRankInfo();
		c.uuid = uuid;
		c.type = type;
		return c;
	}
}
