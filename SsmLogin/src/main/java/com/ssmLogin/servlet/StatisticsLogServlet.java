package com.ssmLogin.servlet;

import java.util.Map;

import com.ssmCore.constants.I_CoreErro;
import com.ssmCore.constants.ReInfo;
import com.ssmCore.jetty.BaseServlet;
import com.ssmCore.jetty.FunUrl;
import com.ssmLogin.constant.I_ModuleServlet;
import com.ssmLogin.defdata.facde.I_Stats;
import com.ssmLogin.defdata.impl.StatsImpl;
import com.ssmShare.constants.E_PlateInfo;
import com.ssmShare.facde.I_Platform;
import com.ssmShare.platform.DataConf;

public class StatisticsLogServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	I_Stats stats = StatsImpl.getInstance();
	DataConf dSource;
	I_Platform platform;

	public StatisticsLogServlet() {
		super(StatisticsLogServlet.class, new HttpParamsPress());
	}

	/**
	 * 统计接口
	 * 
	 * @param param
	 * @return
	 */
	@FunUrl(value = I_ModuleServlet.STATS_LOG)
	public Object statslog(Map<String, Object> param) {
		try {
			String gid = String.valueOf(param.get("gid"));
			String pid = String.valueOf(param.get("pid"));
			if (dSource == null)
				dSource = DataConf.getInstance();
			dSource.load(gid, pid, E_PlateInfo.ALL.getType());
			return stats.statslog(param, dSource);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dSource = null;
			stats = null;
		}
		return new ReInfo(I_CoreErro.ERRO_HTTP_PARAM, "获取统计数据异常");
	}

}
