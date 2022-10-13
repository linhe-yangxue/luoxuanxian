package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerGiftActivityDB extends BaseDataSource {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerGiftActivityInfo t = new PlayerGiftActivityInfo();
		t.uid = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerGiftActivityInfo)obj;
	}
	
	public PlayerGiftActivityInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerGiftActivityInfo.class);
		return (PlayerGiftActivityInfo) m_object;
	}

}
