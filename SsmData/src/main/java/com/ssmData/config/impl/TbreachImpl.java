package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Breach;
import com.ssmData.config.facde.I_Template;

public class TbreachImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tBreach == null){
			ConfigConstant.tBreach = new HashMap<Integer,Breach>();
		}else{
			ConfigConstant.tBreach.clear();
		}
		Breach[] bs = JsonTransfer._In(json, Breach[].class);
		for(Breach b : bs){
			ConfigConstant.tBreach.put(b.getBreachID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		this.loading(json);
	}

}
