package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Jewelry;
import com.ssmData.config.facde.I_Template;

public class TjewelryImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tJewelry == null){
			ConfigConstant.tJewelry = new HashMap<Integer, Jewelry>();
		}else{
			ConfigConstant.tJewelry.clear();
		}
		Jewelry[] bs = JsonTransfer._In(json, Jewelry[].class);
		for(Jewelry b : bs){
			ConfigConstant.tJewelry.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
