package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerMonthcardDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerMonthcardInfo t = new PlayerMonthcardInfo();
		t.Init(m_uuid);
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerMonthcardInfo)obj;
	}
	
	public PlayerMonthcardInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerMonthcardInfo.class);
		return (PlayerMonthcardInfo) m_object;
	}

}
