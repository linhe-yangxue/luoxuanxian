package com.ssmCore.rabbitMq;

import java.io.IOException;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;

/**
 * MQ队列
 *
 */
@Service
public class RabbitMqService {
	
	private @Autowired FastJsonMessageConverter message;
	private @Autowired QueueRecive recive;
	private SimpleMessageListenerContainer ListenerContainer;
	private ThreadPoolTaskExecutor taskExecutor;
	private Channel channel;
	private ConnectionFactory connectionFactory;
	private AmqpTemplate rabbitTemplate;
	private AmqpAdmin amqpAdmin;
	
	/**
	 * 直发送不接收
	 * @param host
	 * @param port
	 * @param user
	 * @param pw
	 */
	public void init(String host,int port,String user,String pw){
		setFactory(host, port, user, pw);
		setTemplate();
	}
	/**
	 * 接收对象实例化
	 * @param host
	 * @param port
	 * @param user
	 * @param pw
	 * @param corePool
	 * @param maxPool
	 */
	public void init(String host,int port,String user,String pw,int corePool,int maxPool){
		setFactory(host, port, user, pw);
		setTemplate();
		setTaskExecutor(corePool,maxPool);
	}
	/**
	 * 创建mq连接工厂
	 * @param host
	 * @param port
	 * @param user
	 * @param pw
	 */
	private void setFactory(String host,int port,String user,String pw){
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(); 
		connectionFactory.setHost(host);
		connectionFactory.setPort(port);
		connectionFactory.setUsername(user);
		connectionFactory.setPassword(pw);
		this.connectionFactory = connectionFactory;
		amqpAdmin = new RabbitAdmin(this.connectionFactory);
	}
	/**
	 * 创建mq数据模板
	 */
	private void setTemplate(){
		RabbitTemplate rabbitTemplate = new RabbitTemplate();
		rabbitTemplate.setConnectionFactory(connectionFactory);
		rabbitTemplate.setMessageConverter(message);
		this.setRabbitTemplate(rabbitTemplate);
	}
	/**
	 * 接收数据定时器
	 * @param corePool
	 * @param maxPool
	 */
	private void setTaskExecutor(int corePool,int maxPool){
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(corePool);
		taskExecutor.setMaxPoolSize(maxPool);
		taskExecutor.setAllowCoreThreadTimeOut(true);
		this.taskExecutor = taskExecutor;
		this.taskExecutor.initialize();
	}

	/**
	 * 创建队列并创建路由
	 * @param route
	 * @param queues
	 * @throws IOException
	 */
	public void CreateRouteQueue(String route,String queues,String type) throws IOException{
		channel = connectionFactory.createConnection().createChannel(true);
		channel.exchangeDeclare(route,type); //任务对列绑定
		String[] qus = StringUtils.split(queues, ',');
		for(String name : qus){
			channel.queueDeclare(name, true, false, false, null);			
			channel.queueBind(name,route,null);
		}
	}
	
	/**
	 * 创建队列路由绑定队列
	 * @param route
	 * @param queues
	 * @param type
	 * @throws IOException
	 */
	public void BindingCreateQueue(String route,String queues,String type) throws IOException{
		channel = connectionFactory.createConnection().createChannel(true);
		channel.exchangeDeclare(route,type); //任务对列绑定
		channel.queueDeclare(queues, true, false, false, null);
		channel.queueBind(queues,route,queues);
	}
	/**
	 * 绑定队列
	 * @param route
	 * @param queues
	 * @throws IOException
	 */
	public void BindingQueue(String route,String queue,String type) throws IOException{
		channel = connectionFactory.createConnection().createChannel(true);
		channel.exchangeDeclare(route,type); //任务对列绑定
		channel.queueBind(queue,route,queue);
	}
	/**
	 * 只创建队列
	 * @param queues
	 * @throws IOException
	 */
	public void CreateQueue(String queues) throws IOException{
		channel = connectionFactory.createConnection().createChannel(true);
		String[] qus = StringUtils.split(queues, ',');
		for(String name : qus){
			channel.queueDeclare(name, true, false, false, null);
			channel.basicQos(1);
		}
	}	
	/**
	 * 创建监听队列
	 * @throws IOException 
	 */
	public void CreateListenQueue(String queue) throws IOException{
		ListenerContainer = new SimpleMessageListenerContainer();
		ListenerContainer.setConnectionFactory(connectionFactory);
		CreateQueue(queue);
    	ListenerContainer.addQueueNames(StringUtils.split(queue, ','));
    	ListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
       	ListenerContainer.setTaskExecutor(taskExecutor);
    	ListenerContainer.setMessageListener(recive);
    	ListenerContainer.start();
	}
	
	public ThreadPoolTaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public ConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}
	
	public AmqpTemplate getRabbitTemplate() {
		return rabbitTemplate;
	}

	public void setRabbitTemplate(AmqpTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public AmqpAdmin getAmqpAdmin() {
		return amqpAdmin;
	}

	public void setAmqpAdmin(AmqpAdmin amqpAdmin) {
		this.amqpAdmin = amqpAdmin;
	}
	
	@PreDestroy
	public void destory() throws Exception{
		this.channel.clearConfirmListeners();
		this.channel.close();
		if(ListenerContainer!=null){
			ListenerContainer.destroy();
			this.taskExecutor.shutdown();
		}
	}
}
