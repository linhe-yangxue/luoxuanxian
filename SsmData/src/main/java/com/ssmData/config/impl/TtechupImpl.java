package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Techup;
import com.ssmData.config.facde.I_Template;

public class TtechupImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tTechup == null){
			ConfigConstant.tTechup = new HashMap<Integer, Techup>();
		}else{
			ConfigConstant.tTechup.clear();
		}
		Techup[] bs = JsonTransfer._In(json, Techup[].class);
		for(Techup b : bs){
			ConfigConstant.tTechup.put(b.getLevel(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
