package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerSevenActivityDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerSevenActivityInfo t = new PlayerSevenActivityInfo();
		t.uid = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerSevenActivityInfo)obj;
	}
	
	public PlayerSevenActivityInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerSevenActivityInfo.class);
		return (PlayerSevenActivityInfo) m_object;
	}

}
