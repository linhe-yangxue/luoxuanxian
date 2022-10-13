package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerLimitGiftDB extends BaseDataSource {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerLimitGiftInfo t = new PlayerLimitGiftInfo();
		t.uid = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerLimitGiftInfo)obj;
	}
	
	public PlayerLimitGiftInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerLimitGiftInfo.class);
		return (PlayerLimitGiftInfo) m_object;
	}

}
