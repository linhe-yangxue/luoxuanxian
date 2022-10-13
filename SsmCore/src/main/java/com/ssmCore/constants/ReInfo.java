package com.ssmCore.constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 返回信息实体
 * @param <T>
 *
 */
public class ReInfo{
	
	private static final Logger log = LoggerFactory.getLogger(ReInfo.class);
	public int rt = 0;    //默认0为成功
	public Object msg;
	
	public ReInfo(){}
	
	public ReInfo(int t){
		rt = t;
		if(t>0)
			log.warn("erro:" + t);
	}
	
	public <T> ReInfo(T t){
		msg = t;
	}
	
	public <T> ReInfo(int val,T t){
		rt = val;
		msg = t;
	}
}
