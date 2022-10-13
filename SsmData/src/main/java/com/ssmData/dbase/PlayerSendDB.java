package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerSendDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerSendInfo t = new PlayerSendInfo();
		t.id = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerSendInfo)obj;
	}
	
	public PlayerSendInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "id", PlayerSendInfo.class);
		return (PlayerSendInfo) m_object;
	}

}
