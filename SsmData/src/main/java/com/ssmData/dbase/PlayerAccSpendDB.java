package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerAccSpendDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerAccSpendInfo t = new PlayerAccSpendInfo();
		t.uid = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerAccSpendInfo)obj;
	}
	
	public PlayerAccSpendInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerAccSpendInfo.class);
		return (PlayerAccSpendInfo) m_object;
	}

}
