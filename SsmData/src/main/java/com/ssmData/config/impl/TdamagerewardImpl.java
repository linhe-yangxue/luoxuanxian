package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Damagereward;
import com.ssmData.config.facde.I_Template;

public class TdamagerewardImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tDamagereward == null){
			ConfigConstant.tDamagereward = new HashMap<Integer,Damagereward>();
		}else{
			ConfigConstant.tDamagereward.clear();
		}
		Damagereward[] skills = JsonTransfer._In(json, Damagereward[].class);
		for(Damagereward skill : skills){
			ConfigConstant.tDamagereward.put(skill.getId(), skill);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
