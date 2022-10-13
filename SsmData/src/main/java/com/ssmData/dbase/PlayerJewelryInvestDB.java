package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerJewelryInvestDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerJewelryInvestInfo t = new PlayerJewelryInvestInfo();
		t.id = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerJewelryInvestInfo)obj;
	}
	
	public PlayerJewelryInvestInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "id", PlayerJewelryInvestInfo.class);
		return (PlayerJewelryInvestInfo) m_object;
	}

}
