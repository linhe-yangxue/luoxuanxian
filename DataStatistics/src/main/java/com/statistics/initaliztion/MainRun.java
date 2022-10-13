package com.statistics.initaliztion;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.ssmCore.jetty.JettyServerStart;

public class MainRun {

	public static String FILEPATH = System.getProperty("user.dir");
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.err.println("***********************************");
				System.err.println(" 网络服务启动失败，我也不知道为什么！");
				System.err.println("***********************************");
			}
		});
		try{ 
			PropertyConfigurator.configure(FILEPATH + "/conf/log4j.properties");
			new FileSystemXmlApplicationContext("file:"+ FILEPATH +"/conf/applicationContext.xml");//加载xml配置文件
			
			JettyServerStart.getInstence().jStart(); //启动jetty服务
		}catch (Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
	}
	 
}
