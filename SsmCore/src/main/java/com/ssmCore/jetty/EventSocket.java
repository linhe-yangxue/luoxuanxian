package com.ssmCore.jetty;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.constants.I_CoreErro;

@Service
@Scope("prototype")
public class EventSocket extends WebSocketAdapter {

	private static final Logger log = LoggerFactory.getLogger(EventSocket.class);
	private I_Close  unconnet;
	private Object obj;  //玩家信息

	@Override
	public void onWebSocketConnect(Session sess) {
		super.onWebSocketConnect(sess);
		log.info(sess.getRemoteAddress().getHostName() + "创建连接");
	}

	@Override
	public void onWebSocketText(String message) {
		super.onWebSocketText(message);
		BaseWebSoket.getInstance().callBack(this, message);
	}

	@Override
	public void onWebSocketClose(int statusCode, String reason){
		if(obj!=null)
			unconnet.unConnect(obj);
		super.onWebSocketClose(statusCode, reason);
		log.info("Socket Closed: [" + statusCode + "] " + reason);
	}

	@Override
	public void onWebSocketError(Throwable cause) {
		super.onWebSocketError(cause);
		if(cause.hashCode()==1001)
			log.warn(getSession().getRemoteAddress().getHostName(), "连接超时！");
		else
			log.warn(getSession().getRemoteAddress().getHostName(), cause);
	}

	public void sendMsg(String text) {
		try {
			getRemote().sendString(text);
		} catch (IOException e) {
			log.warn(I_CoreErro.ERRO_WEBSOCKET_SEND + "", e);
		}
	}
	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public I_Close getUnconnet() {
		return unconnet;
	}

	public void setUnconnet(I_Close unconnet) {
		this.unconnet = unconnet;
	}
}
