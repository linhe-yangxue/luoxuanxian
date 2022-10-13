package com.ssmCore.rabbitMq;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PreDestroy;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;


@Service
public class QueueRecive implements Runnable,MessageListener {

	private static BlockingQueue<String> dataQueue = new LinkedBlockingQueue<String>(); //消息接收队列
	private ExecutorService reciveQueue = Executors.newSingleThreadExecutor();
	private I_MqRecive mqrecive;
	
	public void init(I_MqRecive recive){
		mqrecive = recive;
		if(mqrecive!=null)
			startQueRecive();
	}
	
	@Override
	public void onMessage(Message arg0){
		try {	
			dataQueue.add(new String(arg0.getBody(),"utf-8"));
		} catch (Exception e) {
			return;
		}				
	}
	
	public void startQueRecive(){
		reciveQueue.execute(this);
	}
	
	@PreDestroy
	public void stopQuerecive(){
		reciveQueue.shutdown();
	}
	
	@Override
	public void run() {
		while(true){
			try {
				String json = dataQueue.take();
				if(json!=null)
					mqrecive.ParsingObject(json);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void setRecive(I_MqRecive event) {
		this.mqrecive = event;
	}
	

}
