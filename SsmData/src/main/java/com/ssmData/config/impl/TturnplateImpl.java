package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Turnplate;
import com.ssmData.config.facde.I_Template;

public class TturnplateImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tTurnplate == null){
			ConfigConstant.tTurnplate = new HashMap<Integer,Turnplate>();
		}else{
			ConfigConstant.tTurnplate.clear();
		}
		Turnplate[] bs = JsonTransfer._In(json, Turnplate[].class);
		for(Turnplate b : bs){
			ConfigConstant.tTurnplate.put(b.getID(), b);
		}
	}

    @Override
    public void upLoad(String json) {
        this.loading(json);
    }

}
