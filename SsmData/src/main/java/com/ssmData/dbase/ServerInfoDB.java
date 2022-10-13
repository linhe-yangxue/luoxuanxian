package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class ServerInfoDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		ServerInfo t = new ServerInfo();
		t.uid = m_uuid;
		t.start_time = 0;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		// TODO Auto-generated method stub
		m_object = (ServerInfo)obj;
	}

	public ServerInfo load()
	{
		LoadObjectByUUID(ServerUUID(), "uid", ServerInfo.class);
		return (ServerInfo) m_object;
	}
}
