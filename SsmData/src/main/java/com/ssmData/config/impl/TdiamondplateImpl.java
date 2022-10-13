package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Diamondplate;
import com.ssmData.config.facde.I_Template;

public class TdiamondplateImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tDiamondplate == null){
			ConfigConstant.tDiamondplate = new HashMap<Integer, Diamondplate>();
		}else{
			ConfigConstant.tDiamondplate.clear();
		}
		Diamondplate[] bs = JsonTransfer._In(json, Diamondplate[].class);
		for(Diamondplate b : bs){
			ConfigConstant.tDiamondplate.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
