package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Hdrankinglv;
import com.ssmData.config.facde.I_Template;

public class ThdrankinglvImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
        if(ConfigConstant.tHdrankinglv == null){
            ConfigConstant.tHdrankinglv = new HashMap<Integer, Hdrankinglv>();
        }else{
            ConfigConstant.tHdrankinglv.clear();
        }

        Hdrankinglv[] levels = JsonTransfer._In(json, Hdrankinglv[].class);
        for(Hdrankinglv level : levels){
            ConfigConstant.tHdrankinglv.put(level.getId(), level);
        }
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}


}
