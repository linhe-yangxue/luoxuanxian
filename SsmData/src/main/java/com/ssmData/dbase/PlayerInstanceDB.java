package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerInstanceDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		PlayerInstanceInfo t = new PlayerInstanceInfo();
		t.Init(m_uuid);
		m_object = t;
	}
	
	@Override
	protected void setData(Object obj) {
		m_object = (PlayerInstanceInfo)obj;
	}
	
	public PlayerInstanceInfo loadByUid(String player_id)
	{
		LoadObjectByUUID(player_id, "uid", PlayerInstanceInfo.class);
		return (PlayerInstanceInfo) m_object;
	}
}
