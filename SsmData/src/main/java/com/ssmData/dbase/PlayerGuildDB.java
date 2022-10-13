package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerGuildDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerGuildInfo t = new PlayerGuildInfo();
		t.id = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerGuildInfo)obj;
	}
	
	public PlayerGuildInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "id", PlayerGuildInfo.class);
		return (PlayerGuildInfo) m_object;
	}

}
