package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmData.config.constant.ConfigConstant;

@Service
@Scope("prototype")
public class PlayerLevelDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		PlayerLevelInfo t = new PlayerLevelInfo();
		t.uid = m_uuid;
		t.cur_level = ConfigConstant.tConf.getInitialLevel();
		t.time = 0;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerLevelInfo) obj;
	}

	public PlayerLevelInfo loadByUid(String uid) {
		LoadObjectByUUID(uid, "uid", PlayerLevelInfo.class);
		return (PlayerLevelInfo) m_object;
	}

	@Override
	protected boolean GetDataExsit() {
		PlayerLevelInfo obj = (PlayerLevelInfo)m_object;
		return (null != m_object && obj != null && obj.uid.equals(m_uuid));
	}

}
