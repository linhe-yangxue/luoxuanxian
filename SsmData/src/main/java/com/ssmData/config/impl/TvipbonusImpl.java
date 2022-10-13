package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Vipbonus;
import com.ssmData.config.facde.I_Template;

public class TvipbonusImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tVipbonus == null){
			ConfigConstant.tVipbonus = new HashMap<Integer, Vipbonus>();
		}else{
			ConfigConstant.tVipbonus.clear();
		}
		Vipbonus[] bs = JsonTransfer._In(json, Vipbonus[].class);
		for(Vipbonus b : bs){
			ConfigConstant.tVipbonus.put(b.getVIP(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
