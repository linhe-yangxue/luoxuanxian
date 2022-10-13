package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Itemshop;
import com.ssmData.config.facde.I_Template;

public class TitemshopImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tItemshop == null){
			ConfigConstant.tItemshop = new HashMap<Integer,Itemshop>();
		}else{
			ConfigConstant.tItemshop.clear();
		}
		Itemshop[] skills = JsonTransfer._In(json, Itemshop[].class);
		for(Itemshop skill : skills){
			ConfigConstant.tItemshop.put(skill.getBooth(), skill);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
