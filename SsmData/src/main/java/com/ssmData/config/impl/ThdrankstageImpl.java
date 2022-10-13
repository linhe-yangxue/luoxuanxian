package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Hdrankstage;
import com.ssmData.config.facde.I_Template;

public class ThdrankstageImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
        if(ConfigConstant.tHdrankstage == null){
            ConfigConstant.tHdrankstage = new HashMap<Integer, Hdrankstage>();
        }else{
            ConfigConstant.tHdrankstage.clear();
        }

        Hdrankstage[] levels = JsonTransfer._In(json, Hdrankstage[].class);
        for(Hdrankstage level : levels){
            ConfigConstant.tHdrankstage.put(level.getId(), level);
        }
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}


}
