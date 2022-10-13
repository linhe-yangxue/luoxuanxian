package com.ssmLogin.run;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.rewrite.handler.RewriteRegexRule;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ssmCore.jetty.I_AddServlet;
import com.ssmLogin.constant.I_ModuleServlet;
import com.ssmLogin.servlet.ActiveCodeServlet;
import com.ssmLogin.servlet.ErrorLogServlet;
import com.ssmLogin.servlet.GameServlet;
import com.ssmLogin.servlet.LoginServlet;
import com.ssmLogin.servlet.OrderServlet;
import com.ssmLogin.servlet.PayServlet;
import com.ssmLogin.servlet.ShareServlet;
import com.ssmLogin.servlet.StatisticsLogServlet;
import com.ssmLogin.servlet.WxServlet;

@Service
public class ServletInitializtion implements I_AddServlet {

	private @Value("${WSOCKET_EXPIRE}") Integer expire;
	private @Value("${DEFAULT_WEBAPP_PATH}") String webPath;
	private @Value("${WX_FILE_PATH}") String wxFile;
	private @Value("${LOG_FILE_PATH}") String logFile;
	private @Value("${JAR_PATH}") String jarPath;

	@Override
	public void addServletHandle(Server server) {
		// ** ==========纯servlet服务===============*//
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		// 微信服务器验证服务
		context.addServlet(WxServlet.class, I_ModuleServlet.WX_SV_AUTHORITY);
		// 微信初始化
		context.addServlet(WxServlet.class, I_ModuleServlet.WX_INIT_CONF);
		// 登录服务1
		context.addServlet(LoginServlet.class, I_ModuleServlet.LONGIN_GAME);
		// 登录服务1
		context.addServlet(LoginServlet.class, I_ModuleServlet.GAME_LOGIN);
		// tonken服务
		context.addServlet(LoginServlet.class, I_ModuleServlet.TOKEN_LOGIN);
		// 创建订单
		context.addServlet(OrderServlet.class, I_ModuleServlet.CREATE_ORDER);
		// 支付服务
		context.addServlet(PayServlet.class, I_ModuleServlet.PAY_USER);
		// 统计
		context.addServlet(StatisticsLogServlet.class, I_ModuleServlet.STATS_LOG);
		// 激活码校验
		context.addServlet(ActiveCodeServlet.class, I_ModuleServlet.CODE_AUTHORITY);
		// 游戏相关信息服务
		context.addServlet(GameServlet.class, I_ModuleServlet.LOGIN_INFO + "*");
		/** 错误日志 */
		context.addServlet(ErrorLogServlet.class, I_ModuleServlet.ERROR_LOG);
		/** 分享 */
		context.addServlet(ShareServlet.class, I_ModuleServlet.SHARE);
		/** 管理后台服务 */
		WebAppContext webcontext = new WebAppContext(webPath, "/admin");
		webcontext.setDescriptor(webPath + "/WEB-INF/web.xml"); // 指定web.xml配置文件
		webcontext.setResourceBase(webPath + "/");// 设置webapp的位置

		List<File> files = (List<File>) FileUtils.listFiles(new File(jarPath), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		URL[] urls = new URL[files.size()];
		for (int i = 0; i < files.size(); i++) {
			try {
				urls[i] = files.get(i).toURI().toURL();
			} catch (MalformedURLException e) {
				continue;
			}
		}
		URLClassLoader loader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
		webcontext.setClassLoader(loader);
		webcontext.setDefaultsDescriptor(webPath + "/WEB-INF/webdefault.xml");

		/** 微信认证文件--静态资源访问 */
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(false);
		resourceHandler.setResourceBase(wxFile);
		resourceHandler.setStylesheet("");

		/** 服务器日志--静态资源访问 */
		ResourceHandler logHandler = new ResourceHandler();
		logHandler.setDirectoriesListed(false);
		logHandler.setResourceBase(logFile);
		logHandler.setStylesheet("");

		// 多haddler设置
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resourceHandler, logHandler, webcontext, getRewriteHandler(context) });
		// 载入hander
		server.setHandler(handlers);
		server.setStopAtShutdown(true);
	}

	/**
	 * servlet连接正则匹配规则
	 * 
	 * @param handler
	 * @return
	 */
	private RewriteHandler getRewriteHandler(Handler handler) {

		// 匹配规则 正则表达 登录匹配规则
		RewriteRegexRule login = new RewriteRegexRule();
		login.setRegex("(.*.)" + I_ModuleServlet.LONGIN_GAME); //
		login.setReplacement(I_ModuleServlet.LONGIN_GAME);

		// 匹配规则 正则表达 登录匹配规则
		RewriteRegexRule glogin = new RewriteRegexRule();
		glogin.setRegex("(.*.)" + I_ModuleServlet.GAME_LOGIN); //
		glogin.setReplacement(I_ModuleServlet.GAME_LOGIN);

		RewriteRegexRule tlogin = new RewriteRegexRule();
		tlogin.setRegex("(.*.)" + I_ModuleServlet.TOKEN_LOGIN); //
		tlogin.setReplacement(I_ModuleServlet.TOKEN_LOGIN);

		// 订单匹配规则
		// TODO: 2018\10\11 0011 测试 这些老代码是否要删掉
		RewriteRegexRule old_order = new RewriteRegexRule();
		old_order.setRegex("(.*.)/goods"); //
		old_order.setReplacement(I_ModuleServlet.CREATE_ORDER);

		RewriteRegexRule order = new RewriteRegexRule();
		order.setRegex("(.*.)" + I_ModuleServlet.CREATE_ORDER); //
		order.setReplacement(I_ModuleServlet.CREATE_ORDER);

		// 支付匹配规则
		RewriteRegexRule pay = new RewriteRegexRule();
		pay.setRegex("(.*.)" + I_ModuleServlet.PAY_USER); //
		pay.setReplacement(I_ModuleServlet.PAY_USER);

		// 统计数据
		RewriteRegexRule stats = new RewriteRegexRule();
		stats.setRegex("(.*.)" + I_ModuleServlet.STATS_LOG); //
		stats.setReplacement(I_ModuleServlet.STATS_LOG);

		/** 错误日志 */
		RewriteRegexRule errorlog = new RewriteRegexRule();
		errorlog.setRegex("(.*.)" + I_ModuleServlet.ERROR_LOG); //
		errorlog.setReplacement(I_ModuleServlet.ERROR_LOG);

		/** 错误日志 */
		RewriteRegexRule sharelog = new RewriteRegexRule();
		sharelog.setRegex("(.*.)" + I_ModuleServlet.SHARE); //
		sharelog.setReplacement(I_ModuleServlet.SHARE);

		// 匹配规则装配器
		RewriteHandler rewrite = new RewriteHandler();
		rewrite.setRewriteRequestURI(false); // true 改写发过来的请求连接。
		rewrite.setRewritePathInfo(false);
		rewrite.setOriginalPathAttribute("requestedPath");
		// 可添加多个规则
		rewrite.addRule(login);
		rewrite.addRule(glogin);
		rewrite.addRule(pay);
		rewrite.addRule(order);
		rewrite.addRule(old_order);
		rewrite.addRule(tlogin);
		rewrite.addRule(stats);
		rewrite.addRule(errorlog);
		rewrite.addRule(sharelog);
		rewrite.setHandler(handler); // 放入handler

		return rewrite;
	}

}
