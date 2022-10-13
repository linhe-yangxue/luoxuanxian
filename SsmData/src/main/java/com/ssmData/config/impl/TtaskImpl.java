package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Task;
import com.ssmData.config.facde.I_Template;

public class TtaskImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tTask == null){
			ConfigConstant.tTask = new HashMap<Integer, Task>();
		}else{
			ConfigConstant.tTask.clear();
		}
		Task[] bs = JsonTransfer._In(json, Task[].class);
		for(Task b : bs){
			ConfigConstant.tTask.put(b.getID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
