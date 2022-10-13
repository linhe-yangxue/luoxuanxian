package com.ssmData.config.impl;

import java.util.HashMap;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Vip;
import com.ssmData.config.facde.I_Template;

/**
 * Vip等级配置表读取
 * Created by WYM on 2016/11/5.
 */
public class TvipImpl implements I_Template {

    @Override
    public void loading(String json) {
        if(ConfigConstant.tVip == null){
            ConfigConstant.tVip = new HashMap<Integer,Vip>();
        }else{
            ConfigConstant.tVip.clear();
        }
        Vip[] vips = JsonTransfer._In(json, Vip[].class);
        for(Vip vip : vips){
            ConfigConstant.tVip.put(vip.getID(), vip);
        }
    }

    @Override
    public void upLoad(String json) {
        this.loading(json);
    }

}
