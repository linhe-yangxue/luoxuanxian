package com.ssmchat.websocket.login;

import org.springframework.stereotype.Service;

import com.ssmCore.jetty.I_Close;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmchat.constanst.Defconstants;

@Service
public class WebsocketClose implements I_Close{

	public static WebsocketClose getInstance(){
		return SpringContextUtil.getBean(WebsocketClose.class);
	}
	
	@Override
	public void unConnect(Object obj) {
		Defconstants.delChannle(obj);
	}

}
