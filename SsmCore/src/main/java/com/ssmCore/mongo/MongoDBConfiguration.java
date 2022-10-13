package com.ssmCore.mongo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

@Configuration
public class MongoDBConfiguration extends AbstractMongoConfiguration{

	//================mongodb链接参数==================//
	private @Value("${mongo.connectionsPerHost}") Integer connectionsPerHost;
	private @Value("${mongo.threadsAllowedToBlockForConnectionMultiplier}") Integer tabfcm; 	
	private @Value("${mongo.connectTimeout}") Integer timeout;
	private @Value("${mongo.maxWaitTime}") 	Integer waitTime;
	private	@Value("${mongo.socketKeepAlive}") 	Boolean keepAlive;
	private	@Value("${mongo.socketTimeout}") Integer stimeout;
	//===================mongodb 用户名密码========================//
	private	@Value("${mongo.hostport}") String[] hostpot;
	private	@Value("${mongo.dbname}") String dbName;
	
	private	@Value("${mongo.security}") String security;
	private	@Value("${mongo.username}") String dbUser;
	private	@Value("${mongo.password}") String dbPasswd;
	
	/**
	 * 创建mongo连接参数 
	 * @return
	 */
	@Bean
	public MongoClientOptions options(){
		MongoClientOptions.Builder builder = MongoClientOptions.builder();
		builder.connectionsPerHost(connectionsPerHost);
		builder.threadsAllowedToBlockForConnectionMultiplier(tabfcm);
		builder.connectTimeout(timeout);
		builder.maxWaitTime(waitTime);
		builder.socketKeepAlive(keepAlive);
		builder.socketTimeout(stimeout);
		builder.writeConcern(WriteConcern.FSYNCED);
		return builder.build();
	}
	/**
	 * 创建mongo链接
	 * @return
	 * @throws UnknownHostException
	 */
	@Bean
	public MongoClient mongo() throws UnknownHostException{
		MongoCredential credential=null;
		
		if(security.equals("yes")) //需要用户名密码登录
			credential = MongoCredential.createMongoCRCredential(dbUser,dbName,dbPasswd.toCharArray());
		
		List<ServerAddress> servers = new ArrayList<ServerAddress>();
		for(String addr: hostpot){
			servers.add(new ServerAddress(addr));
		}
		
		MongoClient mongoClient = new MongoClient(
							servers
			                , credential!=null?Arrays.asList(credential):null
			                , options());
		return mongoClient;
	}
	
	/**
	 * mongodb链接工厂
	 * @return
	 * @throws UnknownHostException
	 */
	@Bean
	 public MongoDbFactory mongoDbFactory() throws UnknownHostException{
	    return new SimpleMongoDbFactory(mongo(), dbName);
	 }
	 
	/**
	 * mongodb数据操作模板
	 * @return
	 * @throws Exception
	 */
	@Bean(name="mongoTemplate")
	public MongoTemplate mongoTemplate() throws Exception {
		MappingMongoConverter mmc = super.mappingMongoConverter();
		mmc.setTypeMapper(new DefaultMongoTypeMapper(null));
		mmc.afterPropertiesSet();
		return new MongoTemplate(mongoDbFactory(),mmc);
	}

	@Override
	protected String getDatabaseName() {
		// TODO Auto-generated method stub
		return null;
	}
	  
}
