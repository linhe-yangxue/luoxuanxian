package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Bond;
import com.ssmData.config.facde.I_Template;

public class TbondImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tBond == null){
			ConfigConstant.tBond = new HashMap<Integer,Bond>();
		}else{
			ConfigConstant.tBond.clear();
		}
		Bond[] skills = JsonTransfer._In(json, Bond[].class);
		for(Bond skill : skills){
			ConfigConstant.tBond.put(skill.getBondID(), skill);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
