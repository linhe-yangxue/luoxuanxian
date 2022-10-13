package com.ssmLogin.defdata.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmLogin.defdata.facde.I_DataLoad;
import com.ssmShare.constants.LoginConstants;
import com.ssmShare.entity.WxPlatform;

@Service("wxplatform")
public class WxPlatformImpl implements I_DataLoad {
	
	public static WxPlatformImpl getInstance(){
		return SpringContextUtil.getBean(WxPlatformImpl.class);
	}
	/**
	 * 数据初始化
	 */
	@PostConstruct
	public void initDB() {
		loadDBInit(); // 微信公众号参数初始化
	}

	public void upLoad(String json) {
		WxPlatform[] infos = JsonTransfer._In(json, WxPlatform[].class);
		BaseDaoImpl db = BaseDaoImpl.getInstance();
		if (db.isExits(WxPlatform.class)) {
			for (WxPlatform info : infos) {
				db.saveOrUpdate(info);
			}
		}else{
			db.add(Arrays.asList(infos));
		}
		loadDBInit();
	}

	//重数据库中加载数据
	private void loadDBInit(){
		if (LoginConstants.wxPlat == null) 
			LoginConstants.wxPlat = new HashMap<String, WxPlatform>();
		else
			LoginConstants.wxPlat.clear();

		List<WxPlatform> ls = BaseDaoImpl.getInstance().findAll(WxPlatform.class);
		if (ls != null && ls.size()>0) {
			for (WxPlatform info : ls) {
				LoginConstants.wxPlat.put(info.getPid(), info);
			}
		}
	}

}
