package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Level;
import com.ssmData.config.facde.I_Template;

/**
 * 关卡表读取
 * Created by WYM on 2016/11/2.
 */
public class TlevelImpl implements I_Template {
    @Override
    public void loading(String json) {
        if(ConfigConstant.tLevel == null){
            ConfigConstant.tLevel = new HashMap<Integer, Level>();
        }else{
            ConfigConstant.tLevel.clear();
        }

        Level[] levels = JsonTransfer._In(json, Level[].class);
        for(Level level : levels){
            ConfigConstant.tLevel.put(level.getID(), level);
        }

    }

    @Override
    public void upLoad(String json) {
        this.loading(json);
    }
}
