package com.ssmData.config.impl;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Config;
import com.ssmData.config.facde.I_Template;

public class TconfigImpl implements I_Template{

	@Override
	public void loading(String json) {
		ConfigConstant.tConf  = JsonTransfer._In(json, Config.class);
	}

	@Override
	public void upLoad(String json) {
		this.loading(json);
	}
			
}
