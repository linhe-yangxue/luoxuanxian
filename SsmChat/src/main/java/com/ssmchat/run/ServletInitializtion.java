package com.ssmchat.run;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ssmCore.jetty.EventServlet;
import com.ssmCore.jetty.I_AddServlet;
import com.ssmchat.constanst.I_DefMoudle;
import com.ssmchat.servlet.SysPushServlet;

@Service
public class ServletInitializtion implements I_AddServlet {

	private @Value("${WSOCKET_EXPIRE}")Integer expire;
	private @Value("${LOG_FILE_PATH}")String logFile;

	@Override
	public void addServletHandle(Server server) {

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        //添加跨域
		FilterHolder filterHolder = new FilterHolder(CrossOriginFilter.class);
		filterHolder.setInitParameter("allowCredentials", "true");
		filterHolder.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
		filterHolder.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
		context.addFilter(filterHolder, "/", null);
		
		context.addServlet(SysPushServlet.class, I_DefMoudle.MSG_PUSH);
		context.addServlet(SysPushServlet.class, I_DefMoudle.PAY_SUCCESS); //支付成功消息推送
		//注册Websocket
        ServletHolder holderEvents = new ServletHolder("ws-events", EventServlet.getInstance());
		server.setStopTimeout(expire); //连接超时设置
		context.addServlet(holderEvents,I_DefMoudle.WEBSOCKET_CLIENT);
		//静态资源访问
		ResourceHandler handler = new ResourceHandler();  //静态资源处理的handler
		handler.setDirectoriesListed(true);  
		handler.setResourceBase(logFile);  
		handler.setStylesheet(""); 
  
		// 多haddler设置
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] {handler,context});
		// 载入hander
		server.setStopAtShutdown(true);
		server.setHandler(handlers);
	}
}
