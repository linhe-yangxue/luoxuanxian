package com.ssmData.dbase;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerMailDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerMailInfo t = new PlayerMailInfo();
		t.mails = new ArrayList<MailInfo>();
		t.uid = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		// TODO Auto-generated method stub
		m_object = (PlayerMailInfo)obj;
	}
	
	public PlayerMailInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerMailInfo.class);
		return (PlayerMailInfo) m_object;
	}
	
	@Override
	protected boolean GetDataExsit() {
		PlayerMailInfo obj = (PlayerMailInfo)m_object;
		return (null != m_object && obj != null && obj.uid != null 
				&& obj.uid.equals(m_uuid));
	}

}
