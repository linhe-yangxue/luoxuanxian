package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Talent;
import com.ssmData.config.facde.I_Template;

public class TtalentImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tTalent == null){
			ConfigConstant.tTalent = new HashMap<Integer, Talent>();
		}else{
			ConfigConstant.tTalent.clear();
		}
		Talent[] bs = JsonTransfer._In(json, Talent[].class);
		for(Talent b : bs){
			ConfigConstant.tTalent.put(b.getTalentID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
