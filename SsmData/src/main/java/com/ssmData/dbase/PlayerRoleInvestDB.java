package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerRoleInvestDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerRoleInvestInfo t = new PlayerRoleInvestInfo();
		t.id = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerRoleInvestInfo)obj;
	}
	
	public PlayerRoleInvestInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "id", PlayerRoleInvestInfo.class);
		return (PlayerRoleInvestInfo) m_object;
	}

}
