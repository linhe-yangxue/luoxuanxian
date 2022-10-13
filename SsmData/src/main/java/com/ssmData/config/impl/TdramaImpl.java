package com.ssmData.config.impl;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Drama;
import com.ssmData.config.facde.I_Template;

public class TdramaImpl implements I_Template{

	@Override
	public void loading(String json) {
		ConfigConstant.tDrama  = JsonTransfer._In(json, Drama.class);
	}

	@Override
	public void upLoad(String json) {
		this.loading(json);
	}
			
}
