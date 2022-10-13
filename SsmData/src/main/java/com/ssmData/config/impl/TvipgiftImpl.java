package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Vipgift;
import com.ssmData.config.facde.I_Template;

public class TvipgiftImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tVipgift == null){
			ConfigConstant.tVipgift = new HashMap<Integer, Vipgift>();
		}else{
			ConfigConstant.tVipgift.clear();
		}
		Vipgift[] bs = JsonTransfer._In(json, Vipgift[].class);
		for(Vipgift b : bs){
			ConfigConstant.tVipgift.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
