package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerDuelTeamDB extends BaseDataSource {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		PlayerDuelTeamInfo t = new PlayerDuelTeamInfo();
		t.Init(m_uuid);
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerDuelTeamInfo) obj;
	}

	public PlayerDuelTeamInfo loadByUid(String uid) {
		LoadObjectByUUID(uid, "uid", PlayerDuelTeamInfo.class);
		return (PlayerDuelTeamInfo) m_object;
	}

	@Override
	protected boolean GetDataExsit() {
		PlayerDuelTeamInfo obj = (PlayerDuelTeamInfo)m_object;
		return (null != m_object && obj != null
				&& obj.uid != null && obj.uid.equals(m_uuid));
	}

}
