package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Guild;
import com.ssmData.config.facde.I_Template;

public class TguildImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tGuild == null){
			ConfigConstant.tGuild = new HashMap<Integer, Guild>();
		}else{
			ConfigConstant.tGuild.clear();
		}
		Guild[] bs = JsonTransfer._In(json, Guild[].class);
		for(Guild b : bs){
			ConfigConstant.tGuild.put(b.getGuildLv(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
