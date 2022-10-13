package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Equipup;
import com.ssmData.config.facde.I_Template;

public class TequipupImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tEquipup == null){
			ConfigConstant.tEquipup = new HashMap<Integer, Equipup>();
		}else{
			ConfigConstant.tEquipup.clear();
		}
		Equipup[] bs = JsonTransfer._In(json, Equipup[].class);
		for(Equipup b : bs){
			ConfigConstant.tEquipup.put(b.getLevel(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
