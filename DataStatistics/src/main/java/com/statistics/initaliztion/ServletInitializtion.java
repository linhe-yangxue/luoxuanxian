package com.statistics.initaliztion;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ssmCore.jetty.I_AddServlet;
import com.statistics.servlet.GetDataServlet;

@Service
public class ServletInitializtion implements I_AddServlet {

	private @Value("${WSOCKET_EXPIRE}") Integer expire;
	private @Value("${NET_BASE}") String net_base_path;

	@Override
	public void addServletHandle(Server server) {
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		// 注册Servlet
		context.addServlet(GetDataServlet.class, "/datastatic/*"); 

		FilterHolder filterHolder = new FilterHolder(CrossOriginFilter.class);
		filterHolder.setInitParameter("allowCredentials", "true");
		filterHolder.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
		filterHolder.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
		context.addFilter(filterHolder, "/", null);

		WebAppContext webcontext = new WebAppContext(net_base_path, "/" + net_base_path);
		webcontext.addFilter(filterHolder, "/*", null);
		// 设置webapp的位置
		webcontext.setResourceBase(net_base_path);
		webcontext.setClassLoader(Thread.currentThread().getContextClassLoader());

		// 多haddler设置
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { webcontext, context });
		// 载入hander
		server.setStopAtShutdown(true);
		server.setHandler(handlers);
	}

}
