package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Instance;
import com.ssmData.config.facde.I_Template;

public class TinstanceImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
        if(ConfigConstant.tInstance == null){
            ConfigConstant.tInstance = new HashMap<Integer, Instance>();
        }else{
            ConfigConstant.tInstance.clear();
        }

        Instance[] levels = JsonTransfer._In(json, Instance[].class);
        for(Instance level : levels){
            ConfigConstant.tInstance.put(level.getID(), level);
        }
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
