package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Toweraward;
import com.ssmData.config.facde.I_Template;

public class TtowerawardImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tToweraward == null){
			ConfigConstant.tToweraward = new HashMap<Integer, Toweraward>();
		}else{
			ConfigConstant.tToweraward.clear();
		}
		Toweraward[] skills = JsonTransfer._In(json, Toweraward[].class);
		for(Toweraward skill : skills){
			ConfigConstant.tToweraward.put(skill.getID(), skill);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
