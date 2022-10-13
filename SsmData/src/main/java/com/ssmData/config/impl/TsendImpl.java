package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Send;
import com.ssmData.config.facde.I_Template;

public class TsendImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tSend == null){
			ConfigConstant.tSend = new HashMap<Integer, Send>();
		}else{
			ConfigConstant.tSend.clear();
		}
		Send[] bs = JsonTransfer._In(json, Send[].class);
		for(Send b : bs){
			ConfigConstant.tSend.put(b.getSendId(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		this.loading(json);
	}

}
