package com.ssmData.dbase;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerScrollDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerScrollInfo t = new PlayerScrollInfo();
		t.scroll_list = new ArrayList<ScrollInfo>();
		t.uid = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerScrollInfo)obj;
	}
	
	public PlayerScrollInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerScrollInfo.class);
		return (PlayerScrollInfo) m_object;
	}

}
