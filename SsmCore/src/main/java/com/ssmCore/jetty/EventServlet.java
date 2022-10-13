package com.ssmCore.jetty;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.SpringContextUtil;

@SuppressWarnings("serial")
@Service
public class EventServlet extends WebSocketServlet{

	private @Value("${WSOCKET_SIZE}") Integer maxSize;
	
	public static EventServlet getInstance(){
		return SpringContextUtil.getBean(EventServlet.class);
	}
	
	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.register(EventSocket.class);
		factory.getPolicy().setMaxTextMessageSize(maxSize*1024);//设置发送接收文本大小
	}

}
