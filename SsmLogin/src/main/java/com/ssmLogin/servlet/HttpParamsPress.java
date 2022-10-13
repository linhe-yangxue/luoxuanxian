package com.ssmLogin.servlet;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssmCore.constants.HttpWrite;
import com.ssmCore.constants.ReInfo;
import com.ssmCore.jetty.BaseServlet;
import com.ssmCore.jetty.HttpParams;

public class HttpParamsPress extends HttpParams {

	private static final Logger log = LoggerFactory.getLogger(HttpParamsPress.class);
	static String ios = "\\b(ipad|iphone|mac)";

	@Override
	public void paramsInit(Method method, Class<? extends BaseServlet> clzz, HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> map = new HashMap<>();
			String[] str = StringUtils.split(request.getRequestURI(), '/');
			if (str.length > 1) {
				map.put("pid", str[0]);
				map.put("gid", str[1]);
				if (str.length > 2)
					map.put("zid", str[2]);

				String dress = request.getHeader("x-forwarded-for");
				if (dress != null) {
					if (dress.indexOf(',') > -1) {
						dress = StringUtils.substringBefore(dress, ",");
					}
					map.put("ip", dress);
				}
				////////////////// 移动设备类型IOS、android////////////////////
				boolean ios = isIOS(request.getHeader("USER-AGENT"));
				map.put("phone", ios ? 0:1);
				/////////////////////////////////////

				laodParams(request, map);
				Object obj = method.invoke(clzz.newInstance(), map);
				if (obj instanceof ReInfo) {
					ReInfo refinfo = (ReInfo) obj;
					if (refinfo.rt == -1) {
						response.setContentType("application/json;charset=utf-8");
						response.setStatus(HttpServletResponse.SC_OK);
						if (response.getHeader("Access-Control-Allow-Origin") == null || !response.getHeader("Access-Control-Allow-Origin").equals("*"))
							response.addHeader("Access-Control-Allow-Origin", "*");
						if(refinfo!=null && refinfo.msg!=null)
							response.sendRedirect((String) refinfo.msg); // 页面跳转
						return;
					}
				}
				HttpWrite.getInstance().writeMsg(response, obj);
				return;
			}
		} catch (Exception e) {
			log.warn("Http请求连接错误", e);
		}
		HttpWrite.getInstance().writeMsg(response, new ReInfo("请输入正确的连接地址！"));
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
