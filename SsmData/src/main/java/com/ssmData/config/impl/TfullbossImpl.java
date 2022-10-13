package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Fullboss;
import com.ssmData.config.facde.I_Template;

public class TfullbossImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tFullboss == null){
			ConfigConstant.tFullboss = new HashMap<Integer, Fullboss>();
		}else{
			ConfigConstant.tFullboss.clear();
		}
		Fullboss[] bs = JsonTransfer._In(json, Fullboss[].class);
		for(Fullboss b : bs){
			ConfigConstant.tFullboss.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		this.loading(json);
	}

}
