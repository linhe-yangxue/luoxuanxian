package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Wordconfig;
import com.ssmData.config.facde.I_Template;

public class TwordconfigImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tWordconfig == null){
			ConfigConstant.tWordconfig = new HashMap<String, Wordconfig>();
		}else{
			ConfigConstant.tWordconfig.clear();
		}
		Wordconfig[] bs = JsonTransfer._In(json, Wordconfig[].class);
		for(Wordconfig b : bs){
			ConfigConstant.tWordconfig.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		this.loading(json);
	}

}
