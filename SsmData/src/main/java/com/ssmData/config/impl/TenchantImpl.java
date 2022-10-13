package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Enchant;
import com.ssmData.config.facde.I_Template;

public class TenchantImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tEnchant == null){
			ConfigConstant.tEnchant = new HashMap<Integer, Enchant>();
		}else{
			ConfigConstant.tEnchant.clear();
		}
		Enchant[] bs = JsonTransfer._In(json, Enchant[].class);
		for(Enchant b : bs){
			ConfigConstant.tEnchant.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
