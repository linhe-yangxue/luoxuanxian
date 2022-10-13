package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class FullBossDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		FullBossInfo t = new FullBossInfo();
		t.id = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		// TODO Auto-generated method stub
		m_object = (FullBossInfo)obj;
	}

	public FullBossInfo load()
	{
		LoadObjectByUUID(ServerUUID(), "id", FullBossInfo.class);
		return (FullBossInfo) m_object;
	}
}
