package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Invite;
import com.ssmData.config.facde.I_Template;

public class TinviteImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
        if(ConfigConstant.tInvite == null){
            ConfigConstant.tInvite = new HashMap<Integer, Invite>();
        }else{
            ConfigConstant.tInvite.clear();
        }

        Invite[] levels = JsonTransfer._In(json, Invite[].class);
        for(Invite level : levels){
            ConfigConstant.tInvite.put(level.getID(), level);
        }
	}

	@Override
	public void upLoad(String json) {
		// TODO Auto-generated method stub
		this.loading(json);
	}

}
