package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Daypay;
import com.ssmData.config.facde.I_Template;

public class TdaypayImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tDaypay == null){
			ConfigConstant.tDaypay = new HashMap<Integer, Daypay>();
		}else{
			ConfigConstant.tDaypay.clear();
		}
		Daypay[] bs = JsonTransfer._In(json, Daypay[].class);
		for(Daypay b : bs){
			ConfigConstant.tDaypay.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
