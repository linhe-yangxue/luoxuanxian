package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class HubGuildDB extends BaseDataSource {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		HubGuildInfo t = new HubGuildInfo();
		t.id = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (HubGuildInfo)obj;
	}
	
	public HubGuildInfo loadByGuildId(String id)
	{
		LoadObjectByUUID(id, "id", HubGuildInfo.class);
		return (HubGuildInfo) m_object;
	}
}
