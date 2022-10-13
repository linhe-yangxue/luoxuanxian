package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerDrawCardDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerDrawCardInfo t = new PlayerDrawCardInfo();
		t.Init(m_uuid);
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		// TODO Auto-generated method stub
		m_object = (PlayerDrawCardInfo)obj;
	}

	public PlayerDrawCardInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerDrawCardInfo.class);
		return (PlayerDrawCardInfo) m_object;
	}
}
