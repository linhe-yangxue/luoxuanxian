package com.ssmData.dbase;

import java.util.Calendar;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerVipGiftDB extends BaseDataSource {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerVipGiftInfo t = new PlayerVipGiftInfo();
		t.id = m_uuid;
		t.last_t = Calendar.getInstance().getTimeInMillis();
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerVipGiftInfo)obj;
	}
	
	public PlayerVipGiftInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "id", PlayerVipGiftInfo.class);
		return (PlayerVipGiftInfo) m_object;
	}

}
