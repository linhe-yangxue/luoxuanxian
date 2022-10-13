package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerDmgRewardDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerDmgRewardInfo t = new PlayerDmgRewardInfo();
		t.uid = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerDmgRewardInfo)obj;
	}
	
	public PlayerDmgRewardInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerDmgRewardInfo.class);
		return (PlayerDmgRewardInfo) m_object;
	}

}
