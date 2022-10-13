package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Tech;
import com.ssmData.config.facde.I_Template;

public class TtechImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tTech == null){
			ConfigConstant.tTech = new HashMap<Integer, Tech>();
		}else{
			ConfigConstant.tTech.clear();
		}
		Tech[] bs = JsonTransfer._In(json, Tech[].class);
		for(Tech b : bs){
			ConfigConstant.tTech.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
