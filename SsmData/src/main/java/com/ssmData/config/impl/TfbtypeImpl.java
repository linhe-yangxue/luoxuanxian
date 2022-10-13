package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Fbtype;
import com.ssmData.config.facde.I_Template;

public class TfbtypeImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tFbtype == null){
			ConfigConstant.tFbtype = new HashMap<Integer, Fbtype>();
		}else{
			ConfigConstant.tFbtype.clear();
		}
		Fbtype[] bs = JsonTransfer._In(json, Fbtype[].class);
		for(Fbtype b : bs){
			ConfigConstant.tFbtype.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
