package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class HubPlayerGuildDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		HubPlayerGuildInfo t = new HubPlayerGuildInfo();
		t.id = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (HubPlayerGuildInfo)obj;
	}
	
	public HubPlayerGuildInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "id", HubPlayerGuildInfo.class);
		return (HubPlayerGuildInfo) m_object;
	}

}
