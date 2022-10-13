package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerGkgrowActivityDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerGkgrowActivityInfo t = new PlayerGkgrowActivityInfo();
		t.uid = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerGkgrowActivityInfo)obj;
	}
	
	public PlayerGkgrowActivityInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerGkgrowActivityInfo.class);
		return (PlayerGkgrowActivityInfo) m_object;
	}

}
