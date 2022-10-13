package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Victory;
import com.ssmData.config.facde.I_Template;

public class TvictoryImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tVictory == null){
			ConfigConstant.tVictory = new HashMap<Integer, Victory>();
		}else{
			ConfigConstant.tVictory.clear();
		}
		Victory[] bs = JsonTransfer._In(json, Victory[].class);
		for(Victory b : bs){
			ConfigConstant.tVictory.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}
}
