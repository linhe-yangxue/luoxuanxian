package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Lineup;
import com.ssmData.config.facde.I_Template;

public class TlineupImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tLineup == null){
			ConfigConstant.tLineup = new HashMap<Integer, Lineup>();
		}else{
			ConfigConstant.tLineup.clear();
		}
		Lineup[] bs = JsonTransfer._In(json, Lineup[].class);
		for(Lineup b : bs){
			ConfigConstant.tLineup.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
