package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

//匹配服斗士外派数据库
@Service
@Scope("prototype")
public class HubSendDB extends BaseDataSource {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		HubSendInfo t = new HubSendInfo();
		t.id = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (HubSendInfo)obj;
	}
	
	public HubSendInfo loadById(String id)
	{
		LoadObjectByUUID(id, "id", HubSendInfo.class);
		return (HubSendInfo) m_object;
	}
}
