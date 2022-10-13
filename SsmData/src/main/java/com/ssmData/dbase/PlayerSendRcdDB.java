package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerSendRcdDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerSendRcdInfo t = new PlayerSendRcdInfo();
		t.id = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerSendRcdInfo)obj;
	}
	
	public PlayerSendRcdInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "id", PlayerSendRcdInfo.class);
		return (PlayerSendRcdInfo) m_object;
	}

}
