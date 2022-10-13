package com.ssmData.initaliztion;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.ssmCore.file.FileCleaner;
import com.ssmData.config.constant.ConfigLoad;

@Service
public class DataInitialization implements ApplicationListener<ApplicationEvent>{

	
	private @Value("${FILE_PATH_JSON}")String jsonPath;
	private @Autowired ApplicationContext ctx;
	
	private void initialize() throws BeansException, Exception {
		ctx.getBean(FileCleaner.class)
				.FileMonitor(jsonPath
				,".json"
				,ctx.getBean(FileJsonlisten.class));
		
		ctx.getBean(ConfigLoad.class).initConfig(jsonPath); //加载配置文件
		
		
		JsonToZipListen j2z = ctx.getBean(JsonToZipListen.class);
		ctx.getBean(FileCleaner.class).FileMonitor(jsonPath,".json",j2z);
		j2z.initConfig(jsonPath);
	}

	
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if ((event instanceof ContextRefreshedEvent))
			try {
				initialize();
				System.gc();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
	}
}
