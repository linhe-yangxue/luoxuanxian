package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Item;
import com.ssmData.config.facde.I_Template;

public class TitemImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
        if(ConfigConstant.tItem == null){
            ConfigConstant.tItem = new HashMap<Integer, Item>();
        }else{
            ConfigConstant.tItem.clear();
        }

        Item[] levels = JsonTransfer._In(json, Item[].class);
        for(Item level : levels){
            ConfigConstant.tItem.put(level.getID(), level);
        }
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
