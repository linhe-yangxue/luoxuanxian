package com.statistics.initaliztion;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

@Service
public class GameInitialization implements ApplicationListener<ApplicationEvent>{

	//private @Autowired ApplicationContext ctx;
	//private @Value("${FILE_PATH_JSON}")String jsonPath;
	
	private void initialize() throws BeansException, Exception {
		/*ctx.getBean(FileCleaner.class)
		.FileMonitor(jsonPath
		,".json"
		,ctx.getBean(FileJsonlisten.class));*/
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
