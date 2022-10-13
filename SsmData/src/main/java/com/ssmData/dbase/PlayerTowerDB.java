package com.ssmData.dbase;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerTowerDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerTowerInfo t = new PlayerTowerInfo();
		t.uid = m_uuid;
		t.cur_lv = 1;
		t.first_award = new ArrayList<Integer>();
		t.history_open = 1;
		t.history_box = 0;
		t.box_time = 0;
		t.win = false;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerTowerInfo)obj;
	}
	
	public PlayerTowerInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerTowerInfo.class);
		return (PlayerTowerInfo) m_object;
	}

}
