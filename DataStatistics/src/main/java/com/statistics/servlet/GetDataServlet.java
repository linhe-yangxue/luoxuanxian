package com.statistics.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ssmCore.constants.HttpWrite;
import com.ssmCore.constants.ReInfo;
import com.ssmCore.jetty.FunUrl;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.statistics.constants.I_Constants;
import com.statistics.impl.GetWebData;

public class GetDataServlet extends HttpServlet {


	private static final long serialVersionUID = 1L;
    /**
     * 响应函数对应Map
     */
    protected final Map<String, Method> accFun = new ConcurrentHashMap<String, Method>();
    
	public GetDataServlet() {
		super();
		// TODO Auto-generated constructor stub
        Method[] methods = GetDataServlet.class.getDeclaredMethods();
        for(Method m : methods){
            if (m.isAnnotationPresent(FunUrl.class)) {
                FunUrl f = m.getAnnotation(FunUrl.class);
                accFun.put(f.value(), m);
            }
        }
	}
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        this.doPost(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getRequestURI();
    
        try{
            Method method = accFun.get(url) != null ? accFun.get(url) : accFun.get(url.substring(0, url.length()-1));
            if(method != null){
            	method.invoke(this.getClass().newInstance(), request, response);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	ReInfo m = new ReInfo(1);
        	m.msg = e.getMessage();
        	HttpWrite.getInstance().writeMsg(response, m);
        }
    }

	/**
	 * 获取游戏统计数据
	 * 
	 * @param type
	 * @param page
	 * @param response
	 * @param session
	 */
	@FunUrl(value =I_Constants.GET_DB_CVS)
	public void getdata(HttpServletRequest request, HttpServletResponse response) {
		try {
			ReInfo msg = SpringContextUtil.getBean(GetWebData.class).handleGetWebData(request.getParameter("dbname"));
			if (msg != null) {
				HttpWrite.getInstance().writeMsg(response, msg);
			} else {
				HttpWrite.getInstance().writeMsg(response, new ReInfo(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
        	ReInfo m = new ReInfo(1);
        	m.msg = e.getMessage();
			HttpWrite.getInstance().writeMsg(response, m);
		} finally {
			
		}
	}
}
