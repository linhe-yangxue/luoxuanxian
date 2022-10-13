package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Duration;
import com.ssmData.config.facde.I_Template;

public class TdurationImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tDuration == null){
			ConfigConstant.tDuration = new HashMap<Integer, Duration>();
		}else{
			ConfigConstant.tDuration.clear();
		}
		Duration[] bs = JsonTransfer._In(json, Duration[].class);
		for(Duration b : bs){
			ConfigConstant.tDuration.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
