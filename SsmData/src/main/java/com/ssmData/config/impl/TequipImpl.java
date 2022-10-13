package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Equip;
import com.ssmData.config.facde.I_Template;

public class TequipImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tEquip == null){
			ConfigConstant.tEquip = new HashMap<Integer, Equip>();
		}else{
			ConfigConstant.tEquip.clear();
		}
		Equip[] bs = JsonTransfer._In(json, Equip[].class);
		for(Equip b : bs){
			ConfigConstant.tEquip.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
