package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Consume;
import com.ssmData.config.facde.I_Template;

public class TconsumeImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tConsume == null){
			ConfigConstant.tConsume = new HashMap<Integer, Consume>();
		}else{
			ConfigConstant.tConsume.clear();
		}
		Consume[] bs = JsonTransfer._In(json, Consume[].class);
		for(Consume b : bs){
			ConfigConstant.tConsume.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
