package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Warrank;
import com.ssmData.config.facde.I_Template;

public class TwarrankImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tWarrank == null){
			ConfigConstant.tWarrank = new HashMap<Integer, Warrank>();
		}else{
			ConfigConstant.tWarrank.clear();
		}
		Warrank[] bs = JsonTransfer._In(json, Warrank[].class);
		for(Warrank b : bs){
			ConfigConstant.tWarrank.put(b.getId(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
