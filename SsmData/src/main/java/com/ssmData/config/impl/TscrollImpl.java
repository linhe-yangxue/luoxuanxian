package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Scroll;
import com.ssmData.config.facde.I_Template;

public class TscrollImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tScroll == null){
			ConfigConstant.tScroll = new HashMap<Integer,Scroll>();
		}else{
			ConfigConstant.tScroll.clear();
		}
		Scroll[] bs = JsonTransfer._In(json, Scroll[].class);
		for(Scroll b : bs){
			ConfigConstant.tScroll.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
