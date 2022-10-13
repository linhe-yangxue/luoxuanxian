package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Registration;
import com.ssmData.config.facde.I_Template;

public class TregistrationImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tRegistration == null){
			ConfigConstant.tRegistration = new HashMap<Integer, Registration>();
		}else{
			ConfigConstant.tRegistration.clear();
		}
		Registration[] bs = JsonTransfer._In(json, Registration[].class);
		for(Registration b : bs){
			ConfigConstant.tRegistration.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
