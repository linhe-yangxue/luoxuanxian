package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Monthcard;
import com.ssmData.config.facde.I_Template;

public class TmonthcardImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tMonthcard == null){
            ConfigConstant.tMonthcard = new HashMap<Integer, Monthcard>();
        }else{
            ConfigConstant.tMonthcard.clear();
        }

		Monthcard[] levels = JsonTransfer._In(json, Monthcard[].class);
        for(Monthcard level : levels){
            ConfigConstant.tMonthcard.put(level.getId(), level);
        }
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}


}
