package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class GuildDB extends BaseDataSource {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		GuildInfo t = new GuildInfo();
		t.id = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (GuildInfo)obj;
	}
	
	public GuildInfo loadByGuildId(String id)
	{
		LoadObjectByUUID(id, "id", GuildInfo.class);
		return (GuildInfo) m_object;
	}

	public GuildInfo loadByGuildName(String name)
	{
		LoadObjectByUUID(name, "name", GuildInfo.class);
		return (GuildInfo) m_object;
	}
}
