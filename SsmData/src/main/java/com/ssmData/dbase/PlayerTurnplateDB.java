package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerTurnplateDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerTurnplateInfo t = new PlayerTurnplateInfo();
		t.uid = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerTurnplateInfo)obj;
	}
	
	public PlayerTurnplateInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerTurnplateInfo.class);
		return (PlayerTurnplateInfo) m_object;
	}

}
