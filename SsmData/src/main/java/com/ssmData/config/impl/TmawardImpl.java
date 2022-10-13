package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Maward;
import com.ssmData.config.facde.I_Template;

public class TmawardImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tMaward == null){
            ConfigConstant.tMaward = new HashMap<Integer, Maward>();
        }else{
            ConfigConstant.tMaward.clear();
        }

		Maward[] levels = JsonTransfer._In(json, Maward[].class);
        for(Maward level : levels){
            ConfigConstant.tMaward.put(level.getID(), level);
        }
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
