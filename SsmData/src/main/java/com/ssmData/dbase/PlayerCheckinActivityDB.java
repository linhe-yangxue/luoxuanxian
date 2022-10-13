package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerCheckinActivityDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerCheckinActivityInfo t = new PlayerCheckinActivityInfo();
		t.uid = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerCheckinActivityInfo)obj;
	}
	
	public PlayerCheckinActivityInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerCheckinActivityInfo.class);
		return (PlayerCheckinActivityInfo) m_object;
	}

}
