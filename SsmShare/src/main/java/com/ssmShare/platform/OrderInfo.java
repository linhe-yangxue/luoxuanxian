package com.ssmShare.platform;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.ssmShare.order.GmItem;
import com.ssmShare.order.OrderMsg;

public class OrderInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public Map<String,OrderMsg> gidpay;
	
	public boolean addItem(String gid,GmItem item){
		if(gidpay == null){
			gidpay = new HashMap<String,OrderMsg>();
			gidpay.put(gid, new OrderMsg(item.getZid(),item.getItemId()));
			item.setFrist(1);
			return true;
		}else{
			return gidpay.get(gid).addItem(item);
		}
	}
}
