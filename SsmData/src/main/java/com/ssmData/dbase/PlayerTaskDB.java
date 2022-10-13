package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
@Service
@Scope("prototype")
public class PlayerTaskDB extends BaseDataSource {
	private static final long serialVersionUID = 1L;
	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerTaskInfo t = new PlayerTaskInfo();
		t.uid = m_uuid;
		t.cur_id = -1;  //load的时候，处理初始化
		t.is_finish = false;
		t.arg = 0;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerTaskInfo)obj;
	}

	public PlayerTaskInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerTaskInfo.class);
		return (PlayerTaskInfo) m_object;
	}

}
