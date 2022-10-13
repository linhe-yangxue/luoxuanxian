package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class HubWarRcdDB extends BaseDataSource {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		HubWarRcdInfo t = new HubWarRcdInfo();
		t.id = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (HubWarRcdInfo)obj;
	}
	
	public HubWarRcdInfo loadById(String id)
	{
		LoadObjectByUUID(id, "id", HubWarRcdInfo.class);
		return (HubWarRcdInfo) m_object;
	}
}
