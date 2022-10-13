package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Exrole;
import com.ssmData.config.facde.I_Template;

public class TexroleImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tExrole == null){
			ConfigConstant.tExrole = new HashMap<Integer,Exrole>();
		}else{
			ConfigConstant.tExrole.clear();
		}
		Exrole[] skills = JsonTransfer._In(json, Exrole[].class);
		for(Exrole skill : skills){
			ConfigConstant.tExrole.put(skill.getID(), skill);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
