package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Shop;
import com.ssmData.config.facde.I_Template;

public class TshopImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tShop == null){
			ConfigConstant.tShop = new HashMap<Integer,Shop>();
		}else{
			ConfigConstant.tShop.clear();
		}
		Shop[] skills = JsonTransfer._In(json, Shop[].class);
		for(Shop skill : skills){
			ConfigConstant.tShop.put(skill.getId(), skill);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
