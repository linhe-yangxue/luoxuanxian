package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerLineupDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerLineupInfo t = new PlayerLineupInfo();
		t.uid = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerLineupInfo)obj;
	}
	
	public PlayerLineupInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerLineupInfo.class);
		return (PlayerLineupInfo) m_object;
	}

}
