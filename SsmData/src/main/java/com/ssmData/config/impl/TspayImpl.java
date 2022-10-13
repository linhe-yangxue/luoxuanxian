package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Spay;
import com.ssmData.config.facde.I_Template;

public class TspayImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tSpay == null){
			ConfigConstant.tSpay = new HashMap<Integer,Spay>();
		}else{
			ConfigConstant.tSpay.clear();
		}
		Spay[] bs = JsonTransfer._In(json, Spay[].class);
		for(Spay b : bs){
			ConfigConstant.tSpay.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		this.loading(json);
	}

}
