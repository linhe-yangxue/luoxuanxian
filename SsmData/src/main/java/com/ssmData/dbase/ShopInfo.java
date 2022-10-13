package com.ssmData.dbase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Itemshop;

public class ShopInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public int is_id;		//itemshop配置id
	public int refresh_cnt;	//当日已经使用的刷新次数
	public long last_refresh;	//上次自动刷新时间
	public long last_reset;		//上一次重置刷新次数时间
	public List<Integer> gd_list; //货品id //3个List要一一对应
	public List<Boolean> sold_out;	//是否已购买 //3个List要一一对应
	public List<Integer> price_list;  //算完后的价格列表 //3个List要一一对应
	
	public void init(int item_shop_id)
	{
		Itemshop cfg = ConfigConstant.tItemshop.get(item_shop_id);
		if (null == cfg)
		{
			return;
		}
		
		is_id = item_shop_id;
		refresh_cnt = 0;
		last_refresh = 0;
		last_reset = 0;
		gd_list = new ArrayList<Integer>();
		sold_out = new ArrayList<Boolean>();
		price_list = new ArrayList<Integer>();
	}
}
