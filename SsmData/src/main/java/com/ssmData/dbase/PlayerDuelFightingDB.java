package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerDuelFightingDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerDuelFightingInfo t = new PlayerDuelFightingInfo();
		t.Init(m_uuid);
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerDuelFightingInfo)obj;
	}
	
	public PlayerDuelFightingInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerDuelFightingInfo.class);
		return (PlayerDuelFightingInfo) m_object;
	}

}
