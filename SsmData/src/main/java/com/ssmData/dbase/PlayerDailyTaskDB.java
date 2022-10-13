package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerDailyTaskDB extends BaseDataSource {
	private static final long serialVersionUID = 1L;
	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerDailyTaskInfo t = new PlayerDailyTaskInfo();
		t.Init(m_uuid);
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		// TODO Auto-generated method stub
		m_object = (PlayerDailyTaskInfo)obj;
	}

	public PlayerDailyTaskInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerDailyTaskInfo.class);
		return (PlayerDailyTaskInfo) m_object;
	}
}
