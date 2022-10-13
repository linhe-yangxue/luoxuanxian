/*package com.ssmData.dbase;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerArenaHistoryDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerArenaHistoryInfo t = new PlayerArenaHistoryInfo();
		t.uid = m_uuid;
		t.rank = new ArrayList<Integer>();
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		// TODO Auto-generated method stub
		m_object = (PlayerArenaHistoryInfo)obj;
	}
	
	public PlayerArenaHistoryInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerArenaHistoryInfo.class);
		return (PlayerArenaHistoryInfo) m_object;
	}

}*/
