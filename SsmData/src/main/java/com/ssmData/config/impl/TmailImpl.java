package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Mail;
import com.ssmData.config.facde.I_Template;

public class TmailImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tMail == null){
            ConfigConstant.tMail = new HashMap<Integer, Mail>();
        }else{
            ConfigConstant.tMail.clear();
        }

		Mail[] levels = JsonTransfer._In(json, Mail[].class);
        for(Mail level : levels){
            ConfigConstant.tMail.put(level.getMID(), level);
        }
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
