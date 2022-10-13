package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Accumulation;
import com.ssmData.config.facde.I_Template;

public class TaccumulationImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tAccumulation == null){
			ConfigConstant.tAccumulation = new HashMap<Integer,Accumulation>();
		}else{
			ConfigConstant.tAccumulation.clear();
		}
		Accumulation[] skills = JsonTransfer._In(json, Accumulation[].class);
		for(Accumulation skill : skills){
			ConfigConstant.tAccumulation.put(skill.getId(), skill);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
