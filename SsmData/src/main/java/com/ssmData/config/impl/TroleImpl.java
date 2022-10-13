package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Role;
import com.ssmData.config.facde.I_Template;

public class TroleImpl implements I_Template{

	@Override
	public void loading(String json) {
		if(ConfigConstant.tRole == null){
			ConfigConstant.tRole = new HashMap<Integer,Role>();
		}else{
			ConfigConstant.tRole.clear();
		}
		Role[] roles = JsonTransfer._In(json, Role[].class);
		for(Role role : roles){
			ConfigConstant.tRole.put(role.getID(), role);
		}
	}

	@Override
	public void upLoad(String json) {
		this.loading(json);
	}

}
