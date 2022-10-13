package com.ssmData.dbase;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class PlayerShopInfo implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	@Id
	public String _id;
	
    @Indexed
    public String uid;                      // 玩家唯一ID
	
	public List<ShopInfo> shop_list;	//身上有的商铺信息
	
	
	public ShopInfo Get(int item_shop_id)
	{
		for (ShopInfo i : shop_list)
		{
			if (i.is_id == item_shop_id)
				return i;
		}
		return null;
	}
}
