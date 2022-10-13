package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerLimitSevenActivityDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerLimitSevenActivityInfo t = new PlayerLimitSevenActivityInfo();
		t.uid = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerLimitSevenActivityInfo)obj;
	}
	
	public PlayerLimitSevenActivityInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerLimitSevenActivityInfo.class);
		return (PlayerLimitSevenActivityInfo) m_object;
	}

}
