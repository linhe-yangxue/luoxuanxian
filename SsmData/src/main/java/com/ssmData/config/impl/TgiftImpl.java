package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Gift;
import com.ssmData.config.facde.I_Template;

public class TgiftImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tGift == null){
			ConfigConstant.tGift = new HashMap<Integer, Gift>();
		}else{
			ConfigConstant.tGift.clear();
		}
		Gift[] bs = JsonTransfer._In(json, Gift[].class);
		for(Gift b : bs){
			ConfigConstant.tGift.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
