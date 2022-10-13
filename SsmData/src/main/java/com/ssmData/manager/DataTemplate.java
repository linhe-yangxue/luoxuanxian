package com.ssmData.manager;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmData.config.constant.ConfigConstant;

@Service
@Scope("prototype") //配置文件数据源控制管理器
public class DataTemplate extends ConfigConstant{
	
}
