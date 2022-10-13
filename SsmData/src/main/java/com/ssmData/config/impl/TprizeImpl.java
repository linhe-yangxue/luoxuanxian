package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Prize;
import com.ssmData.config.facde.I_Template;

public class TprizeImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tPrize == null){
			ConfigConstant.tPrize = new HashMap<Integer, Prize>();
		}else{
			ConfigConstant.tPrize.clear();
		}
		Prize[] bs = JsonTransfer._In(json, Prize[].class);
		for(Prize b : bs){
			ConfigConstant.tPrize.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		this.loading(json);
	}

}
