package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Arena;
import com.ssmData.config.facde.I_Template;

public class TarenaImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tArena == null){
			ConfigConstant.tArena = new HashMap<Integer,Arena>();
		}else{
			ConfigConstant.tArena.clear();
		}
		Arena[] skills = JsonTransfer._In(json, Arena[].class);
		for(Arena skill : skills){
			ConfigConstant.tArena.put(skill.getId(), skill);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
