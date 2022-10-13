package com.ssmCore.jetty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ssmCore.constants.I_CoreErro;
import com.ssmCore.tool.spring.SpringContextUtil;

@Service
public class BaseWebSoket {

	private static final Logger log = LoggerFactory.getLogger(BaseServlet.class);
	private I_SocketPress socket;
	public static Map<String,EventSocket> channel = new ConcurrentHashMap<>(); 

	public static BaseWebSoket getInstance() {
		return SpringContextUtil.getBean(BaseWebSoket.class);
	}
	
	public void initReg(Class<?> clazz){
		this.socket = (I_SocketPress) SpringContextUtil.getBean(clazz);
	}
	/**
	 * 对应方法回调
	 * 
	 * @param session
	 * @param json
	 * @return
	 */
	public void callBack(EventSocket event, String json) {
		try {
			this.socket.recive(event, json);
		} catch (Exception e) {
			log.warn(I_CoreErro.ERRO_WEBSOCKET_INVOKE + "", e);
		}
	}	
}
