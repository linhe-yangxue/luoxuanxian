package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Grade;
import com.ssmData.config.facde.I_Template;

/**
 * 升级表读取
 * Created by WYM on 2016/11/7.
 */
public class TgradeImpl implements I_Template {

	@Override
	public void loading(String json) {
		// TODO Auto-generated method stub
		if(ConfigConstant.tGrade == null){
			ConfigConstant.tGrade = new HashMap<Integer,Grade>();
		}else{
			ConfigConstant.tGrade.clear();
		}
		Grade[] bs = JsonTransfer._In(json, Grade[].class);
		for(Grade b : bs){
			ConfigConstant.tGrade.put(b.getLevel(), b);
		}
	}

    @Override
    public void upLoad(String json) {
        this.loading(json);
    }

}
