package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Tasklvreward;
import com.ssmData.config.facde.I_Template;

public class TtasklvrewardImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tTaskLvReward == null){
			ConfigConstant.tTaskLvReward = new HashMap<Integer,Tasklvreward>();
		}else{
			ConfigConstant.tTaskLvReward.clear();
		}
		Tasklvreward[] skills = JsonTransfer._In(json, Tasklvreward[].class);
		for(Tasklvreward skill : skills){
			ConfigConstant.tTaskLvReward.put(skill.getId(), skill);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
