package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Tower;
import com.ssmData.config.facde.I_Template;

public class TtowerImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tTower == null){
			ConfigConstant.tTower = new HashMap<Integer, Tower>();
		}else{
			ConfigConstant.tTower.clear();
		}
		Tower[] skills = JsonTransfer._In(json, Tower[].class);
		for(Tower skill : skills){
			ConfigConstant.tTower.put(skill.getID(), skill);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
