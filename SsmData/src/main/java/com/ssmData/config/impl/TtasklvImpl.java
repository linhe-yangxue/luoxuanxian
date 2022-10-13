package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Tasklv;
import com.ssmData.config.facde.I_Template;

public class TtasklvImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tTaskLv == null){
			ConfigConstant.tTaskLv = new HashMap<Integer,Tasklv>();
		}else{
			ConfigConstant.tTaskLv.clear();
		}
		Tasklv[] skills = JsonTransfer._In(json, Tasklv[].class);
		for(Tasklv skill : skills){
			ConfigConstant.tTaskLv.put(skill.getLv(), skill);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
