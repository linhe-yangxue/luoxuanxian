package com.ssmCore.jetty;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssmCore.constants.HttpWrite;
import com.ssmCore.constants.I_CoreErro;
import com.ssmCore.constants.ReInfo;

public class BaseServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(BaseServlet.class); 
	private static final Map<String, Method> accFun = new ConcurrentHashMap<>();
	private HttpParams params;
	
	public BaseServlet(Class<?> clz,HttpParams params){
		super();
		this.params = params;
		Method[] methods = clz.getDeclaredMethods();
		for(Method m : methods){
			if (m.isAnnotationPresent(FunUrl.class)) {  
				FunUrl f = m.getAnnotation(FunUrl.class);  
                accFun.put(f.value(), m);  
            }  
		}
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = request.getRequestURI();
		if(url.indexOf("goods")>-1)
			url = StringUtils.replace(url, "/goods", "/order");
		if(url.indexOf("pay/")>-1)
			url = StringUtils.replace(url, "pay/", "pay");
		try{
			Method method = accFun.get(url)!=null?accFun.get(url)
					:accFun.get(StringUtils.substring(url,url.lastIndexOf('/')));
			if(method!=null){
				params.paramsInit(method,this.getClass(),request,response);
				return;
			}
		}catch(Exception e){
			log.warn(I_CoreErro.ERRO_HTTP_INVOKE+"",e);
		}
		HttpWrite.getInstance().writeMsg(response
				, new ReInfo(I_CoreErro.ERRO_HTTP_INVOKE,request.getRequestURI()));
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{	
		this.doPost(request, response);
	}

}
