package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Robot;
import com.ssmData.config.facde.I_Template;

public class TrobotImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tRobot == null){
			ConfigConstant.tRobot = new HashMap<Integer, Robot>();
		}else{
			ConfigConstant.tRobot.clear();
		}
		Robot[] bs = JsonTransfer._In(json, Robot[].class);
		for(Robot b : bs){
			ConfigConstant.tRobot.put(b.getId(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
