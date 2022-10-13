package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerDaypayDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerDaypayInfo t = new PlayerDaypayInfo();
		t.uid = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerDaypayInfo)obj;
	}
	
	public PlayerDaypayInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerDaypayInfo.class);
		return (PlayerDaypayInfo) m_object;
	}

}
