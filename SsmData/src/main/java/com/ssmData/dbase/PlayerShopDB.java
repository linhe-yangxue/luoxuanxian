package com.ssmData.dbase;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PlayerShopDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerShopInfo t = new PlayerShopInfo();
		t.shop_list = new ArrayList<ShopInfo>();
		t.uid = m_uuid;
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerShopInfo)obj;
	}

	public PlayerShopInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerShopInfo.class);
		return (PlayerShopInfo) m_object;
	}

}
