package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Katsuji;
import com.ssmData.config.facde.I_Template;

public class TkatsujiImpl implements I_Template {

    @Override
    public void loading(String json) {
        if(ConfigConstant.tKatsuji == null){
            ConfigConstant.tKatsuji = new HashMap<Integer, Katsuji>();
        }else{
            ConfigConstant.tKatsuji.clear();
        }

        Katsuji[] levels = JsonTransfer._In(json, Katsuji[].class);
        for(Katsuji level : levels){
            ConfigConstant.tKatsuji.put(level.getID(), level);
        }

    }

    @Override
    public void upLoad(String json) {
        this.loading(json);
    }

}
