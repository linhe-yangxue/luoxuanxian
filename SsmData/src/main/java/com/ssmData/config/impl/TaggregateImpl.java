package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Aggregate;
import com.ssmData.config.facde.I_Template;

public class TaggregateImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tAggregate == null){
			ConfigConstant.tAggregate = new HashMap<Integer, Aggregate>();
		}else{
			ConfigConstant.tAggregate.clear();
		}
		Aggregate[] bs = JsonTransfer._In(json, Aggregate[].class);
		for(Aggregate b : bs){
			ConfigConstant.tAggregate.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
