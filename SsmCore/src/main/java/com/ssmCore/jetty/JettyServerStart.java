package com.ssmCore.jetty;

import javax.annotation.PreDestroy;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.SpringContextUtil;

@Service
public class JettyServerStart {

	private static final Logger log = LoggerFactory.getLogger(JettyServerStart.class);
	private Server server;
	
	private @Value("${IP_DRESS}") String dress;
	private @Value("${CONNECTOR_TYPE}") Integer connectorType;
	
	private @Value("${PORT_HTTP}") Integer http_port;
	private @Value("${EXPIRE_HTTP}") Integer http_expire;
	
	private @Value("${PORT_HTTPS}") Integer https_port;
	private @Value("${EXPIRE_HTTPS}") Integer https_expire;
	private @Value("${KEY_PRIVATE_HTTPS}") String https_key_private;
	private @Value("${KEY_PUBLIC_HTTPS}") String https_key_public;
	private @Value("${KEY_STRONE_HTTPS_FILE}") String https_strone_file;
	
	
	private @Value("${MIN_threads}") Integer minThread;
	private @Value("${MAX_threads}") Integer maxThread;
	private @Value("${REQUEST_SIZE}") Integer size;
	private @Value("${WSOCKET_EXPIRE}") Integer expire;
	

	private @Autowired I_AddServlet addHandle;

	public static JettyServerStart getInstence() {
		return SpringContextUtil.getBean(JettyServerStart.class);
	}

	/**
	 * jetty服务器启动
	 * 
	 * @param dress
	 *            服务器地址
	 * @param port
	 *            服务器端口
	 */
	public void jStart() {
		try {
			server = new Server(initPool());// 服务
			if(connectorType==1)
				server.addConnector(connector(server)); // 添加 线程管道
			else if(connectorType==2)
				server.addConnector(sslConnector(server));
			else if(connectorType==3){
				server.setConnectors(new Connector[]{connector(server),sslConnector(server)});
			}
			addHandle.addServletHandle(server); // 添加servlet
			server.start();
			server.join();
		} catch (Exception e) {
			log.error("jetty服务启动失败！", e);
			System.exit(0);
		}
	}

	/**
	 * 初始化线程池
	 * 
	 * @return
	 */
	private QueuedThreadPool initPool() {
		QueuedThreadPool threadPool = new QueuedThreadPool();
		threadPool.setMinThreads(minThread);
		threadPool.setMaxThreads(maxThread);
		return threadPool;
	}

	/**
	 * 创建一个线程通道
	 * 
	 * @return
	 */
	private ServerConnector connector(Server server) {
		HttpConfiguration config = new HttpConfiguration();
		ServerConnector connector = new ServerConnector(server,new HttpConnectionFactory(config));
		connector.setReuseAddress(true);
		connector.setIdleTimeout(expire);
		connector.setHost(dress);
		connector.setPort(http_port);
		connector.setAcceptQueueSize(size * 1024);
		return connector;
	}

	private ServerConnector sslConnector(Server server) {
		 HttpConfiguration https_config = new HttpConfiguration();
	     https_config.setSecureScheme("https");

	     
	     SslContextFactory sslContextFactory = new SslContextFactory();
	     sslContextFactory.setKeyStorePath(https_strone_file);
	        // 私钥
	     sslContextFactory.setKeyStorePassword(https_key_private);
	        // 公钥
	     sslContextFactory.setKeyManagerPassword(https_key_public);
	     
	     ServerConnector httpsConnector = new ServerConnector(server,
	                new SslConnectionFactory(sslContextFactory,"http/1.1"),
	                new HttpConnectionFactory(https_config));
	                // 设置访问端口
	     httpsConnector.setHost(dress);
	     httpsConnector.setPort(https_port);
	     httpsConnector.setIdleTimeout(https_expire);
	     return httpsConnector;
	}
	
	@PreDestroy
	public void jDestory() {
		try {
			
			server.stop();
			server.destroy();
		} catch (Exception e) {
			log.error("jetty服务关闭异常！", e);
		}
	}
}
