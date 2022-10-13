package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Lvgrow;
import com.ssmData.config.facde.I_Template;

public class TlvgrowImpl implements I_Template {

	@Override
    public void loading(String json) {
        if(ConfigConstant.tLvgrow == null){
            ConfigConstant.tLvgrow = new HashMap<Integer, Lvgrow>();
        }else{
            ConfigConstant.tLvgrow.clear();
        }

        Lvgrow[] levels = JsonTransfer._In(json, Lvgrow[].class);
        for(Lvgrow level : levels){
            ConfigConstant.tLvgrow.put(level.getID(), level);
        }

    }

    @Override
    public void upLoad(String json) {
        this.loading(json);
    }

}
