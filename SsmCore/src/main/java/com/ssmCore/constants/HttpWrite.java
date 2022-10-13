package com.ssmCore.constants;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmCore.tool.spring.SpringContextUtil;

@Service
@Scope("prototype")
public class HttpWrite {

	private static final Logger log = LoggerFactory.getLogger(HttpWrite.class);

	public static HttpWrite getInstance() {
		return SpringContextUtil.getBean(HttpWrite.class);
	}

	/**
	 * Http 信息返回
	 * 
	 * @param response
	 * @param t
	 *            返回并写出数据
	 */
	public <T> void writeMsg(HttpServletResponse response, T t) {
		response.setContentType("application/json;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		if (response.getHeader("Access-Control-Allow-Origin") == null || !response.getHeader("Access-Control-Allow-Origin").equals("*"))
			response.addHeader("Access-Control-Allow-Origin", "*");
		try {
			if (t == null) {
				log.warn("数据为空1！");
			} else if (t instanceof String || t instanceof Integer) {
				response.getWriter().print(t);
			} else {
				response.getWriter().print(JsonTransfer.getJson(t));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Http返回信息错误！", e);
		} finally {
			t = null;
		}
	}

	public <T> void writeHtml(HttpServletResponse response, T t) {
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		if (response.getHeader("Access-Control-Allow-Origin") == null || !response.getHeader("Access-Control-Allow-Origin").equals("*"))
			response.addHeader("Access-Control-Allow-Origin", "*");
		try {
			if (t == null) {
				System.out.println("数据为空！");
			} else if (t instanceof String || t instanceof Integer) {
				response.getWriter().println(t);
			} else {
				response.getWriter().println(JsonTransfer.getJson(t));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Http返回信息错误！", e);
		} finally {
			t = null;
		}
	}

	/**
	 * 页面跳转
	 * 
	 * @param response
	 * @param url
	 *            跳转地址
	 */
	public <T> void skipURL(HttpServletResponse response, String url) {
		try {
			response.sendRedirect(url);
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Http返回信息错误！", e);
		}
	}

}
