package com.ssmLogin.defdata.facde;

import java.util.Map;

import com.ssmShare.platform.DataConf;

public interface I_Order {
	/**
	 * 创建商品订单
	 * @param dSource
	 * @return
	 */
	public void CreateOrder(DataConf dSource,int zid,long guid,String uid,String itemid,Integer phone);

	/**
	 * 支付回调
	 * @param param
	 * @param dSource
	 * @return 
	 */
	public Object callPay(Map<String, Object> param, DataConf dSource)throws Exception;

}