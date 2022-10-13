package com.ssmCore.memcached;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.transcoders.SerializingTranscoder;
import net.rubyeye.xmemcached.utils.AddrUtil;

@Configuration
public class MemcachedConfiguration {

	private @Value("${ADDRUTIL}") String addrutil; /*memcache服务地址*/
	private @Value("${POOL_SIZE}") Integer pool;    /*memcache服务线程数*/
	private @Value("${MEM_EXPIRE}") Integer expire; /*memcache数据保存时间*/
	
	private @Value("${MEM_SSL}") String switchMem;  	/*memcache服务开关*/
	private @Value("${MEM_SSL}") String memSSL;   /*memcache服务ssl验证*/
	private @Value("${MEM_USER}") String memUser; /*memcache服务登录用户*/
	private @Value("${MEM_PWD}") String memPwd;   /*memcache服务登录密码*/
	
	/**
	 * 创建memcache链接
	 * @return
	 * @throws Exception
	 */
	@Bean
	public MemcachedClient memclient() throws Exception{
			MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddressMap(addrutil));
			if("yes".equals(switchMem))
				builder.addAuthInfo(AddrUtil.getOneAddress(addrutil), AuthInfo.plain(memUser,memPwd));
			builder.setConnectionPoolSize(pool);
			builder.setCommandFactory(new BinaryCommandFactory());
			builder.setSessionLocator(new KetamaMemcachedSessionLocator());
			builder.setTranscoder(new SerializingTranscoder());
			builder.setFailureMode(false);
			MemcachedClient client = builder.build();
			return client;
	}
	
	/**
	 * memcache 数据操作访问
	 * @return
	 * @throws Exception
	 */
	@Bean
	public MemAccess memAccess() throws Exception{
		return new MemAccess(memclient(),expire);
	}
	
	
}
