package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerBossDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		PlayerBossInfo t = new PlayerBossInfo();
		t.id = m_uuid;
		m_object = t;
	}
	
	@Override
	protected void setData(Object obj) {
		m_object = (PlayerBossInfo)obj;
	}
	
	public PlayerBossInfo loadByUid(String player_id)
	{
		LoadObjectByUUID(player_id, "id", PlayerBossInfo.class);
		return (PlayerBossInfo) m_object;
	}
}
