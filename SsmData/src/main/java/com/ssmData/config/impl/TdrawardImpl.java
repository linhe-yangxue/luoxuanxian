package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Draward;
import com.ssmData.config.facde.I_Template;

public class TdrawardImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tDraward == null){
			ConfigConstant.tDraward = new HashMap<Integer, Draward>();
		}else{
			ConfigConstant.tDraward.clear();
		}
		Draward[] bs = JsonTransfer._In(json, Draward[].class);
		for(Draward b : bs){
			ConfigConstant.tDraward.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
