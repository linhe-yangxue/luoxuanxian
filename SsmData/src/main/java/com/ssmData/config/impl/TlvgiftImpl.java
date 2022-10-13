package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Lvgift;
import com.ssmData.config.facde.I_Template;

public class TlvgiftImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tLvgift == null){
			ConfigConstant.tLvgift = new HashMap<Integer, Lvgift>();
		}else{
			ConfigConstant.tLvgift.clear();
		}
		Lvgift[] bs = JsonTransfer._In(json, Lvgift[].class);
		for(Lvgift b : bs){
			ConfigConstant.tLvgift.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
