package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Wish;
import com.ssmData.config.facde.I_Template;

public class TwishImpl implements I_Template {

	@Override
	public void loading(String json) {
        if(ConfigConstant.tWish == null){
            ConfigConstant.tWish = new HashMap<Integer,Wish>();
        }else{
            ConfigConstant.tWish.clear();
        }
        Wish[] vips = JsonTransfer._In(json, Wish[].class);
        for(Wish vip : vips){
            ConfigConstant.tWish.put(vip.getLv(), vip);
        }
	}

	@Override
	public void upLoad(String json) {
		this.loading(json);
	}

}
