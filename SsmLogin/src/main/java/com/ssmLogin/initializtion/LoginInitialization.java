package com.ssmLogin.initializtion;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.ssmCore.file.FileCleaner;
import com.ssmCore.tool.spring.ClasspathPackageScanner;
import com.ssmLogin.defdata.impl.OrderDataPool;
import com.ssmLogin.defdata.impl.PlatFormList;
import com.ssmLogin.defdata.impl.PlatformInfoImpl;
import com.ssmLogin.defdata.impl.StatisticsPool;
import com.ssmLogin.defdata.impl.UserDataPool;
import com.ssmLogin.defdata.impl.WxPlatformImpl;
import com.ssmLogin.filelisten.FileJarlisten;
import com.ssmLogin.filelisten.FileJsonlisten;
import com.ssmLogin.springmvc.facde.impl.ActiveCodeImpl;
import com.ssmLogin.springmvc.task.DataCensuse;
import com.ssmLogin.springmvc.task.DataInitialization;

@Service
public class LoginInitialization implements ApplicationListener<ApplicationEvent> {

	private @Value("${FILE_PATH_JSON}") String jsonPath;
	private @Value("${FILE_PATH_JAR}") String jarPath;
	private @Value("${TASK_OPEN}")Integer taskOpen;  //是否开启统计任务
	
	private @Autowired ApplicationContext ctx;

	private void initialize() throws BeansException, Exception {
	
		ctx.getBean(FileCleaner.class).FileMonitor(jsonPath, ".json", ctx.getBean(FileJsonlisten.class));
		ctx.getBean(FileCleaner.class).FileMonitor(jarPath, ".jar", ctx.getBean(FileJarlisten.class));
		
		ctx.getBean(PlatFormList.class).init();
		ctx.getBean(PlatformInfoImpl.class).initDB(); // 平台文件
		ctx.getBean(ActiveCodeImpl.class).init();
		
		
		ctx.getBean(ClasspathPackageScanner.class).init(jarPath, ctx);
		
		ctx.getBean(UserDataPool.class).start(); //登录线程池
	
		ctx.getBean(OrderDataPool.class).start(); //订单线程池
	
		ctx.getBean(StatisticsPool.class).start();//数据统计线程池
		
		ctx.getBean(DataInitialization.class).init();
		
		if(taskOpen!=null && taskOpen==1) {
			ctx.getBean(DataCensuse.class).init(); //数据查询任务
		}

	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if ((event instanceof ContextRefreshedEvent)) {
			try {
				Object obj = event.getSource();
				if (obj instanceof WebApplicationContext)
					initialize();
				System.gc();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}
	
	public static final String getProcessID() {  
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return runtimeMXBean.getName().split("@")[0]; 
    }
}
