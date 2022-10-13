package com.ssmchat.entity;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ssmCore.constants.HttpWrite;
import com.ssmCore.jetty.BaseServlet;
import com.ssmCore.jetty.HttpParams;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmShare.entity.ChatEntity;

public class HttpParamString extends HttpParams{
	@Override
	public void paramsInit(Method method, Class<? extends BaseServlet> clzz, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			laodParams(request,map);
			String json = (String) map.get("param");
			ChatEntity chat = JsonTransfer._In(json, ChatEntity.class);
			if(chat!=null){
				Object result = method.invoke(clzz.newInstance(),chat);
				HttpWrite.getInstance().writeMsg(response,result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
