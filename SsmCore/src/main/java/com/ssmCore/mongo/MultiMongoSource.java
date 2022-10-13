package com.ssmCore.mongo;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.stereotype.Service;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.WriteConcern;

@Service
public class MultiMongoSource
{

  @Value("${mongo.connectionsPerHost}")
  private Integer connectionsPerHost;

  @Value("${mongo.threadsAllowedToBlockForConnectionMultiplier}")
  private Integer tabfcm;

  @Value("${mongo.connectTimeout}")
  private Integer timeout;

  @Value("${mongo.maxWaitTime}")
  private Integer waitTime;

  @Value("${mongo.socketKeepAlive}")
  private Boolean keepAlive;

  @Value("${mongo.socketTimeout}")
  private Integer stimeout;
  private MongoClientOptions.Builder builder;
  private MongoClient mongoClient;

  public BaseDaoImpl getBaseDaoImpl(String addUrl, String name)
    throws UnknownHostException
  {
    BaseDaoImpl db = BaseDaoImpl.getInstance();
    db.setMongoTemplet(mongo(addUrl, name));
    return db;
  }

  public MongoClientOptions options()
  {
    this.builder = MongoClientOptions.builder();
    this.builder.connectionsPerHost(this.connectionsPerHost.intValue());
    this.builder.threadsAllowedToBlockForConnectionMultiplier(this.tabfcm.intValue());
    this.builder.connectTimeout(this.timeout.intValue());
    this.builder.maxWaitTime(this.waitTime.intValue());
    this.builder.socketKeepAlive(this.keepAlive.booleanValue());
    this.builder.socketTimeout(this.stimeout.intValue());
    this.builder.writeConcern(WriteConcern.FSYNCED);
    return this.builder.build();
  }

  public MongoTemplate mongo(String addUrl, String dbname) throws UnknownHostException {
    this.mongoClient = new MongoClient(addUrl, options());
    return new MongoTemplate(new SimpleMongoDbFactory(this.mongoClient, dbname));
  }

  public void destroy() {
    this.mongoClient.close();
    this.mongoClient = null;
    this.builder = null;
  }
}