package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Craft;
import com.ssmData.config.facde.I_Template;

public class TcraftImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tCraft == null){
			ConfigConstant.tCraft = new HashMap<Integer, Craft>();
		}else{
			ConfigConstant.tCraft.clear();
		}
		Craft[] bs = JsonTransfer._In(json, Craft[].class);
		for(Craft b : bs){
			ConfigConstant.tCraft.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		this.loading(json);
	}

}
