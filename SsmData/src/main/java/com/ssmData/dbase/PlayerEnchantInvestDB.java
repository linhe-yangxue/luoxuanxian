package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerEnchantInvestDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerEnchantInvestInfo t = new PlayerEnchantInvestInfo();
		t.id = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerEnchantInvestInfo)obj;
	}
	
	public PlayerEnchantInvestInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "id", PlayerEnchantInvestInfo.class);
		return (PlayerEnchantInvestInfo) m_object;
	}

}
