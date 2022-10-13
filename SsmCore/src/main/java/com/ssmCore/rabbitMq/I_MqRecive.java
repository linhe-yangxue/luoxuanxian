package com.ssmCore.rabbitMq;

public interface I_MqRecive {
	
	/**
	 * 接收到的数据 json格式
	 * @param json
	 */
	void ParsingObject(String json);

}
