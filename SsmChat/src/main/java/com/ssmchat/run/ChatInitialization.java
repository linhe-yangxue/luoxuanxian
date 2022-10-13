package com.ssmchat.run;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.ssmCore.jetty.BaseWebSoket;
import com.ssmchat.websocket.ChatTalk;
import com.ssmchat.websocket.login.ChartPool;

@Service
public class ChatInitialization implements ApplicationListener<ApplicationEvent>{

	private @Autowired ApplicationContext ctx;
	private @Value("${CHAT_POOL}")Integer chatPool;
	
	private void initialize() throws BeansException, Exception {
		ctx.getBean(BaseWebSoket.class).initReg(ChatTalk.class);
		if(chatPool!=null && chatPool>0){
			for(int i=0;i<chatPool;i++){
				ctx.getBean(ChartPool.class).start();
			}
		}else{
			ctx.getBean(ChartPool.class).start();
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
