package com.ssmData.config.impl;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Duel;
import com.ssmData.config.facde.I_Template;

public class TduelImpl implements I_Template {

	@Override
	public void loading(String json) {
		ConfigConstant.tDuel  = JsonTransfer._In(json, Duel.class);
	}

	@Override
	public void upLoad(String json) {
		this.loading(json);
	}

}
