package com.ssmCore.jetty;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class HttpParams {
	
	protected abstract void paramsInit(Method method, Class<? extends BaseServlet> clzz, HttpServletRequest request,
			HttpServletResponse response);
	
	/**
	 * 获取HTTP所有参数
	 * @param request
	 */
	protected void laodParams(HttpServletRequest request, Map<String,Object>map) {
        Enumeration<?> paramNames = request.getParameterNames();  
        while (paramNames.hasMoreElements()) {  
            String paramName = (String) paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);  
            if (paramValues.length == 1) {  
                String paramValue = paramValues[0];  
                if (paramValue.length() != 0) { 
                	if(paramName.equals("gid"))
                		paramName = "g_id";
                	if(paramName.equals("pid"))
                		paramName = "p_id";
                    map.put(paramName, paramValue);
                }  
            }  
        }  
    }
}
