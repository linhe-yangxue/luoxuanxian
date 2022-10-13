package com.ssmLogin.servlet;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.ssmCore.constants.HttpWrite;
import com.ssmCore.jetty.BaseServlet;
import com.ssmCore.jetty.HttpParams;
import com.ssmLogin.defdata.impl.PlatformInfoImpl;
import com.ssmShare.constants.E_PlateInfo;
import com.ssmShare.platform.DataConf;

public class HttpPayPress extends HttpParams {
	static String ios = "\\b(ipad|iphone|mac)";

	@Override
	public void paramsInit(Method method, Class<? extends BaseServlet> clzz, HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> map = new HashMap<>();
			DataConf dSource = DataConf.getInstance();
			String[] str = StringUtils.split(request.getRequestURI(), '/');
			if (str.length > 1) {
				map.put("pid", str[0]);
				map.put("gid", str[1]);
				if (str.length > 2)
					map.put("zid", str[2]);
			}

			////////////////// 移动设备类型IOS、android////////////////////
			
			boolean ios = isIOS(request.getHeader("USER-AGENT"));
			map.put("phone", ios ? 0 : 1);
			/////////////////////////////////////
			dSource.load((String) map.get("gid"), (String) map.get("pid"), E_PlateInfo.ALL.getType());

			if (dSource.doc == null) {
				PlatformInfoImpl.getInstance().initDB((String) map.get("gid"));
				dSource.load((String) map.get("gid"), (String) map.get("pid"), E_PlateInfo.ALL.getType());
			}
			if (dSource.doc.getIsWx() == 1) {
				getRequestReader(map, request); // 微信数据为xml request.getReader()
			} else {
				laodParams(request, map);// request.getParameterNames()// 注：request.getReader() 和
											// request.getParameterNames()同时使用
			}
			Object result = method.invoke(clzz.newInstance(), map, dSource);
			if (map.get("header") != null) {
				String[] res = StringUtils.split((String) map.get("header"), ":");
				response.addHeader(res[0], res[1]);
			}
			HttpWrite.getInstance().writeMsg(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getRequestReader(Map<String, Object> map, HttpServletRequest request) throws Exception {
		String inputLine;
		String notityXml = "";
		while ((inputLine = request.getReader().readLine()) != null) {
			notityXml += inputLine;
		}
		if (!notityXml.trim().isEmpty())
			map.put("dat", notityXml);
	}

	/**
	 * 判断是否是ios
	 * 
	 * @param userAgent
	 * @return
	 */
	private boolean isIOS(String userAgent) {
		if (null == userAgent) {
			return false;
		}
		userAgent = userAgent.toLowerCase();
		Matcher matcher_and = Pattern.compile(ios, Pattern.CASE_INSENSITIVE).matcher(userAgent);
		if (matcher_and.find()) {
			return true;
		} else {
			return false;
		}
	}
}
