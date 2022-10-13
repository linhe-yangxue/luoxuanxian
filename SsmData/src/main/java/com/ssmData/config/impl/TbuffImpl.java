package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Buff;
import com.ssmData.config.facde.I_Template;

public class TbuffImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tBuff == null){
			ConfigConstant.tBuff = new HashMap<Integer,Buff>();
		}else{
			ConfigConstant.tBuff.clear();
		}
		Buff[] bs = JsonTransfer._In(json, Buff[].class);
		for(Buff b : bs){
			ConfigConstant.tBuff.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		this.loading(json);
	}

}
