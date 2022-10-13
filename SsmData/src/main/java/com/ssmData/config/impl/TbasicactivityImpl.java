package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Basicactivity;
import com.ssmData.config.facde.I_Template;

public class TbasicactivityImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tBasicactivity == null){
			ConfigConstant.tBasicactivity = new HashMap<Integer,Basicactivity>();
		}else{
			ConfigConstant.tBasicactivity.clear();
		}
		Basicactivity[] skills = JsonTransfer._In(json, Basicactivity[].class);
		for(Basicactivity skill : skills){
			ConfigConstant.tBasicactivity.put(skill.getID(), skill);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
