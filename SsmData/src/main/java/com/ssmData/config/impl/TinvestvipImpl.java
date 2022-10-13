package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Investvip;
import com.ssmData.config.facde.I_Template;

public class TinvestvipImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tInvestvip == null){
			ConfigConstant.tInvestvip = new HashMap<Integer,Investvip>();
		}else{
			ConfigConstant.tInvestvip.clear();
		}
		Investvip[] bs = JsonTransfer._In(json, Investvip[].class);
		for(Investvip b : bs){
			ConfigConstant.tInvestvip.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		this.loading(json);
	}

}
