package com.ssmShare.order;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 订单基本信息
 */
public class OrderMsg implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** 区id      充值档位*/		    
	public Map<Integer,Set<Integer>> zidpay;

	public OrderMsg(){}
	
	public OrderMsg(int zid, int itemid) {
		zidpay = new HashMap<Integer,Set<Integer>>();
		Set<Integer> temp  = new HashSet<Integer>(itemid);
		temp.add(itemid);
		zidpay.put(zid,temp);
	}

	public boolean addItem(GmItem item) {
		if(zidpay.get(item.getZid())==null){
			Set<Integer> temp  = new HashSet<Integer>();
			temp.add(item.getItemId());
			zidpay.put(item.getZid(),temp);
			item.setFrist(1);
			return true;
		}else{
			return zidpay.get(item.getZid()).add(item.getItemId());
		}
	}
	
}
