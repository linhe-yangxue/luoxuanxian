package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Award;
import com.ssmData.config.facde.I_Template;

public class TawardImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tAward == null){
			ConfigConstant.tAward = new HashMap<Integer, Award>();
		}else{
			ConfigConstant.tAward.clear();
		}
		Award[] bs = JsonTransfer._In(json, Award[].class);
		for(Award b : bs){
			ConfigConstant.tAward.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		this.loading(json);
	}


}
