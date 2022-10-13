package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Itemgood;
import com.ssmData.config.facde.I_Template;

public class TitemgoodImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tItemgood == null){
            ConfigConstant.tItemgood = new HashMap<Integer, Itemgood>();
        }else{
            ConfigConstant.tItemgood.clear();
        }

		Itemgood[] levels = JsonTransfer._In(json, Itemgood[].class);
        for(Itemgood level : levels){
            ConfigConstant.tItemgood.put(level.getId(), level);
        }
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
