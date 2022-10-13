package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerVipBenefitDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerVipBenefitInfo t = new PlayerVipBenefitInfo();
		t.id = m_uuid;
		t.last_t = 0L;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerVipBenefitInfo)obj;
	}
	
	public PlayerVipBenefitInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "id", PlayerVipBenefitInfo.class);
		return (PlayerVipBenefitInfo) m_object;
	}

}
