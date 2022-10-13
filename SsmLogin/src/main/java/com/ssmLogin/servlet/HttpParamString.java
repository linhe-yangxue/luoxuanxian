package com.ssmLogin.servlet;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ssmCore.constants.HttpWrite;
import com.ssmCore.jetty.BaseServlet;
import com.ssmCore.jetty.HttpParams;

public class HttpParamString extends HttpParams{
	
	@Override
	public void paramsInit(Method method, Class<? extends BaseServlet> clzz, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<String,Object> map = new HashMap<>();
			laodParams(request,map);
			Object result = method.invoke(clzz.newInstance(),map);
			HttpWrite.getInstance().writeMsg(response,result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
