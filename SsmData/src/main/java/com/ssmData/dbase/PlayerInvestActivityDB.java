package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerInvestActivityDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerInvestActivityInfo t = new PlayerInvestActivityInfo();
		t.uid = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerInvestActivityInfo)obj;
	}
	
	public PlayerInvestActivityInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerInvestActivityInfo.class);
		return (PlayerInvestActivityInfo) m_object;
	}

}
