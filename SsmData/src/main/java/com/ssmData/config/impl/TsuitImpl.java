package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Suit;
import com.ssmData.config.facde.I_Template;

public class TsuitImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tSuit == null){
			ConfigConstant.tSuit = new HashMap<Integer,Suit>();
		}else{
			ConfigConstant.tSuit.clear();
		}
		Suit[] skills = JsonTransfer._In(json, Suit[].class);
		for(Suit skill : skills){
			ConfigConstant.tSuit.put(skill.getSuitID(), skill);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
