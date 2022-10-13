package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Backup;
import com.ssmData.config.facde.I_Template;

public class TbackupImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tBackup == null){
			ConfigConstant.tBackup = new HashMap<Integer, Backup>();
		}else{
			ConfigConstant.tBackup.clear();
		}
		Backup[] bs = JsonTransfer._In(json, Backup[].class);
		for(Backup b : bs){
			ConfigConstant.tBackup.put(b.getBackupID(), b);
		}
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
