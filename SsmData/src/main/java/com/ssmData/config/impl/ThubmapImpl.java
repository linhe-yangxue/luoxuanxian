package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Hubmap;
import com.ssmData.config.facde.I_Template;

public class ThubmapImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tHubmap == null){
            ConfigConstant.tHubmap = new HashMap<Integer, Hubmap>();
        }else{
            ConfigConstant.tHubmap.clear();
        }

		Hubmap[] levels = JsonTransfer._In(json, Hubmap[].class);
        for(Hubmap level : levels){
            ConfigConstant.tHubmap.put(level.getID(), level);
        }
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
