package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerLvgrowActivityDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerLvgrowActivityInfo t = new PlayerLvgrowActivityInfo();
		t.uid = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerLvgrowActivityInfo)obj;
	}
	
	public PlayerLvgrowActivityInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerLvgrowActivityInfo.class);
		return (PlayerLvgrowActivityInfo) m_object;
	}

}
