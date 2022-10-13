package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerDmdplateDB extends BaseDataSource {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerDmdplateInfo t = new PlayerDmdplateInfo();
		t.uid = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerDmdplateInfo)obj;
	}
	
	public PlayerDmdplateInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerDmdplateInfo.class);
		return (PlayerDmdplateInfo) m_object;
	}

}
