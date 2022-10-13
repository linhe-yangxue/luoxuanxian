package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Dailytask;
import com.ssmData.config.facde.I_Template;

public class TdailytaskImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tDailytask == null){
			ConfigConstant.tDailytask = new HashMap<Integer, Dailytask>();
		}else{
			ConfigConstant.tDailytask.clear();
		}
		Dailytask[] bs = JsonTransfer._In(json, Dailytask[].class);
		for(Dailytask b : bs){
			ConfigConstant.tDailytask.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
