package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerDurationDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerDurationInfo t = new PlayerDurationInfo();
		t.uid = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerDurationInfo)obj;
	}
	
	public PlayerDurationInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerDurationInfo.class);
		return (PlayerDurationInfo) m_object;
	}

}
