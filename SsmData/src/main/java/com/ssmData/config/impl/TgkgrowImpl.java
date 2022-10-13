package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Gkgrow;
import com.ssmData.config.facde.I_Template;

public class TgkgrowImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tGkgrow == null){
			ConfigConstant.tGkgrow = new HashMap<Integer,Gkgrow>();
		}else{
			ConfigConstant.tGkgrow.clear();
		}
		Gkgrow[] bs = JsonTransfer._In(json, Gkgrow[].class);
		for(Gkgrow b : bs){
			ConfigConstant.tGkgrow.put(b.getID(), b);
		}
	}

    @Override
    public void upLoad(String json) {
        this.loading(json);
    }

}
