package com.ssmGame.initaliztion;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.ssmData.dbase.DataSaveRole;
import com.ssmGame.manager.ArenaManager;
import com.ssmGame.manager.HubGuildWarManager;
import com.ssmGame.manager.RankManager;
import com.ssmGame.sdk.DataEyeSdk;

@Service
public class GameInitialization implements ApplicationListener<ApplicationEvent>{

	private @Autowired ApplicationContext ctx;
	
	private @Value("${IS_HUB}") int is_hub; 
	
	private void initialize() throws BeansException, Exception {
		
		/// memcache存储线程池
		ctx.getBean(DataSaveRole.class).start();
		
		ctx.getBean(ArenaManager.class).start();
		
		ctx.getBean(RankManager.class).start();
		
		ctx.getBean(DataEyeSdk.class).start();
		
		if (is_hub == 1) {
			ctx.getBean(HubGuildWarManager.class).start();
		}
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
