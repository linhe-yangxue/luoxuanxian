package com.ssmLogin.defdata.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmLogin.defdata.facde.I_Stats;
import com.ssmShare.constants.LoginConstants;
import com.ssmShare.facde.I_Platform;
import com.ssmShare.platform.DataConf;

@Service
public class StatsImpl implements I_Stats {
	private static final Logger log = LoggerFactory.getLogger(StatsImpl.class);

	public static StatsImpl getInstance() {
		return SpringContextUtil.getBean(StatsImpl.class);
	}

	@Override
	public Object statslog(Map<String, Object> param, DataConf dSource) {
		I_Platform platform = null;
		String result = null;
		try {
			if (dSource.doc.getIsWx() == 0) {// 平台sdk选择
				platform = (I_Platform) SpringContextUtil.getBean(LoginConstants.SDK + dSource.doc.getPid() + LoginConstants.IMPL); // 实例化平台接口
			} else {
				platform = (I_Platform) SpringContextUtil.getBean(LoginConstants.SDK + "Wx" + LoginConstants.IMPL); // 实例化平台接口
			}
			return platform.statslog(param, dSource);
		} catch (Exception e) {
			log.warn("回调数据：" + result + " || 处理后数据:" + JsonTransfer.getJson(param), e);
		} finally {
			platform = null;
		}
		return null;
	}

	@Override
	public Object getUserinfo(Map<String, Object> param, DataConf dSource) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPayInfo(Map<String, Object> param, DataConf dSource) {
		// TODO Auto-generated method stub
		return null;
	}

}
