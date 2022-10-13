package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerRolesInfoDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	protected PlayerRolesInfo getData() {
		return (PlayerRolesInfo) m_object;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerRolesInfo) obj;
	}

	public PlayerRolesInfo load(String player_info_id) {
		LoadObjectByUUID(player_info_id, "player_info_id", PlayerRolesInfo.class);
		return getData();
	}

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		m_object = new PlayerRolesInfo();
		PlayerRolesInfo tmp = (PlayerRolesInfo )m_object;
		tmp.m_mei_li_lv = 0;
		tmp.player_info_id = m_uuid;
	}
	
	@Override
	protected boolean GetDataExsit() {
		return (null != m_object && getData() != null && getData().player_info_id.equals(m_uuid));
	}

}
