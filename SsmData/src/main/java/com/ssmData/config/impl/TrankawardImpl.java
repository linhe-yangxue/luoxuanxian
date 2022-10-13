package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Rankaward;
import com.ssmData.config.facde.I_Template;

public class TrankawardImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tRankaward == null){
			ConfigConstant.tRankaward = new HashMap<Integer,Rankaward>();
		}else{
			ConfigConstant.tRankaward.clear();
		}
		Rankaward[] skills = JsonTransfer._In(json, Rankaward[].class);
		for(Rankaward skill : skills){
			ConfigConstant.tRankaward.put(skill.getID(), skill);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
