package com.ssmCore.tool.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class SpringContextUtil implements ApplicationContextAware {  

	private static final Logger log = LoggerFactory.getLogger(SpringContextUtil.class);
	// Spring应用上下文环
	private static ApplicationContext applicationContext;  

/** 
 * 实现ApplicationContextAware接口的回调方法，设置上下文
 * @param applicationContext 
 */  
	@Autowired
	public void setApplicationContext(ApplicationContext applicationContext) {  
		SpringContextUtil.applicationContext = applicationContext;  
	}  

/** 
 * @return ApplicationContext 
 */  
	public static ApplicationContext getApplicationContext() {  
		return applicationContext;  
	}  

/** 
 * 获取对象 
 * 这里重写了bean方法，起主要作用 
 * @param name 
 * @return Object 以所给名字注册的bean的实
 * @throws BeansException 
 */  
	public static Object getBean(String name){
		try{
		return applicationContext.getBean(name); 
		}catch(Exception e){
			log.warn("实例化bean：" + name + "错误",e);
		}
		return null;
	}

	public static <T> T getBean(Class<T> arg) {
		return applicationContext.getBean(arg);
	}
	/**
	 * 重新扫描jar包加载
	 */
	public static void refresh(){
		ConfigurableApplicationContext configgur = (ConfigurableApplicationContext) applicationContext;
		configgur.refresh();
	}
	
}  
