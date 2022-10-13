package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Awaken;
import com.ssmData.config.facde.I_Template;

public class TawakenImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tAwaken == null){
			ConfigConstant.tAwaken = new HashMap<Integer,Awaken>();
		}else{
			ConfigConstant.tAwaken.clear();
		}
		Awaken[] skills = JsonTransfer._In(json, Awaken[].class);
		for(Awaken skill : skills){
			ConfigConstant.tAwaken.put(skill.getAwakenID(), skill);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
