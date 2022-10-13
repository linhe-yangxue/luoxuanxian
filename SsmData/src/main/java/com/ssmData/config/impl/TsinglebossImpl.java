package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Singleboss;
import com.ssmData.config.facde.I_Template;

public class TsinglebossImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tSingleboss == null){
			ConfigConstant.tSingleboss = new HashMap<Integer, Singleboss>();
		}else{
			ConfigConstant.tSingleboss.clear();
		}
		Singleboss[] bs = JsonTransfer._In(json, Singleboss[].class);
		for(Singleboss b : bs){
			ConfigConstant.tSingleboss.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		this.loading(json);
	}

}
