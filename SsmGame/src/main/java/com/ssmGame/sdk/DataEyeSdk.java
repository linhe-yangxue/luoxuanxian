package com.ssmGame.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dataeye.sdk.client.DCAgent;
import com.dataeye.sdk.proto.DCServerSync.DCMessage.AccountType;
import com.dataeye.sdk.proto.DCServerSync.DCMessage.DCOnline;
import com.dataeye.sdk.proto.DCServerSync.DCMessage.DCUserInfo;
import com.dataeye.sdk.proto.DCServerSync.DCMessage.Gender;
import com.dataeye.sdk.proto.DCServerSync.DCMessage.NetType;
import com.dataeye.sdk.proto.DCServerSync.DCMessage.PlatformType;
import com.ssmCore.tool.spring.SpringContextUtil;

@Service
public class DataEyeSdk {
	private static final Logger log = LoggerFactory.getLogger(DataEyeSdk.class);
	
	private @Value("${DE_ID}") String de_id;
	private @Value("${DE_NAME}") String de_name;
	private @Value("${DE_LOG_PATH}") String de_log_path;
	private @Value("${DE_LOG_MAX_DAY}") Integer de_log_max_day;
	private @Value("${DE_LOG_MAX_COUNT}") Integer de_log_max_count;
	
	private DCAgent agent = null;
	
	//private static String DE_NAME = null;
	
	public void start()
	{
		log.info("Starting DataEye...");
		//DE_NAME = de_name;
		DCAgent.setBaseConf(de_name, de_log_path, de_log_max_day, de_log_max_count);
		agent = DCAgent.getInstance(de_id);
		log.info("Starting DataEye...Complete!");
	}
	
	public static DCAgent Inst()
	{
		return SpringContextUtil.getBean(DataEyeSdk.class).agent;
	}
	
	//todo 注册
	public static void Reg(String account)
	{
		Inst().reg(DCUserInfo.newBuilder()
				.setAccountId(account) //设置账号ID，必填
				.setMac(account)//设置MAC地址
				.setImei(account) //设置Imei号
				.setPlatform(PlatformType.ADR) //设置平台类型，android
				.setAccountType(AccountType.QQ)//设置账号类型
				.setAge(25)//设置年龄
				.setBrand("MI2S") //设置机型
				.setChannel("渠道") //设置渠道
				.setCountry("中国")//设置国家
				.setGameRegion("区服") //设置区服
				.setGender(Gender.FEMALE) //设置玩家性别
				.setIp("58.60.168.110") //设置玩家IP
				.setLanguage("cn") //设置 语言
				.setNetType(NetType._2G) //设置网络类型
				.setOperators("电信") //设置运营商
				.setOsVersion("4.1.1") //设置操作系统版本
				.setProvince("广东") //设置省份
				.setResolution("720*1080")//设置分辨率
				.build(),(int) (System.currentTimeMillis() / 1000));
	}
	
	//todo 在线
	public static void Online(String account)
	{
		Inst().online(DCUserInfo.newBuilder()
				.setAccountId(account) //设置账号ID，必填
				.setMac(account)
				.setImei(account)
				.setPlatform(PlatformType.ADR)
				.setAccountType(AccountType.QQ)
				.setAge(25)
				.setBrand("MI2S")
				.setChannel("渠道")
				.setCountry("中国")
				.setGameRegion("区服")
				.setGender(Gender.FEMALE)
				.setIp("58.60.168.110")
				.setLanguage("cn")
				.setNetType(NetType._2G)
				.setOperators("电信")
				.setOsVersion("4.1.1")
				.setProvince("广东")
				.setResolution("720*1080")
				.build(),
				DCOnline.newBuilder().setLoginTime((int) (System.currentTimeMillis() / 1000)) //登陆时间，必填
				.setOnlineTime(500)//在线时长，必填
				.build(),null);
	}
	
	//todo 激活
	public static void Act(String account)
	{
		Inst().act(DCUserInfo.newBuilder().setMac(account)
				.setImei(account)
				.setPlatform(PlatformType.ADR)
				.setBrand("MI2S")
				.setChannel("渠道")
				.setCountry("中国")
				.setIp("58.60.168.110")
				.setLanguage("cn")
				.setNetType(NetType._2G)
				.setOperators("电信").setOsVersion("4.1.1")
				.setProvince("广东")
				.setResolution("720*1080")
				.build(),(int) (System.currentTimeMillis() / 1000));
	}
}
