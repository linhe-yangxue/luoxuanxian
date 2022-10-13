package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Beginactivity;
import com.ssmData.config.facde.I_Template;

public class TbeginactivityImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tBeginactivity == null){
			ConfigConstant.tBeginactivity = new HashMap<Integer,Beginactivity>();
		}else{
			ConfigConstant.tBeginactivity.clear();
		}
		Beginactivity[] skills = JsonTransfer._In(json, Beginactivity[].class);
		for(Beginactivity skill : skills){
			ConfigConstant.tBeginactivity.put(skill.getID(), skill);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}
}
