package com.ssmData.dbase;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class PlayerScrollInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	public String _id;
	
    @Indexed
    public String uid;                      // 玩家唯一ID
	
	public List<ScrollInfo> scroll_list;	//身上有的门票信息
	
	
	
	public ScrollInfo Get(int id)
	{
		for (ScrollInfo i : scroll_list)
		{
			if (i.id == id)
				return i;
		}
		return null;
	}
}
