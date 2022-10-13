package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerActivityDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerActivityInfo t = new PlayerActivityInfo();
		t.uid = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerActivityInfo)obj;
	}
	
	public PlayerActivityInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerActivityInfo.class);
		return (PlayerActivityInfo) m_object;
	}

}
