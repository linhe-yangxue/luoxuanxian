package com.ssmData.config.impl;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.ServerConfig;
import com.ssmData.config.facde.I_Template;

/**
 * 服务器专用杂项配置表读取
 * Created by WYM on 2016/11/3.
 */
public class Tserver_configImpl implements I_Template {

    @Override
    public void loading(String json) {
        ConfigConstant.tServerConf  = JsonTransfer._In(json, ServerConfig.class);
    }

    @Override
    public void upLoad(String json) {
        this.loading(json);
    }
}
