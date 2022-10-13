package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Invest;
import com.ssmData.config.facde.I_Template;

public class TinvestImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tInvest == null){
            ConfigConstant.tInvest = new HashMap<Integer, Invest>();
        }else{
            ConfigConstant.tInvest.clear();
        }

		Invest[] levels = JsonTransfer._In(json, Invest[].class);
        for(Invest level : levels){
            ConfigConstant.tInvest.put(level.getID(), level);
        }
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
