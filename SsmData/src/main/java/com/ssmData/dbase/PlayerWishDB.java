package com.ssmData.dbase;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmData.config.constant.ConfigConstant;
import com.ssmData.config.entity.Wish;

@Service
@Scope("prototype")
public class PlayerWishDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		PlayerWishInfo t = new PlayerWishInfo();
		t.uid = m_uuid;
		Wish w_cfg = ConfigConstant.tWish.get(0);
		if (w_cfg != null) {
			t.eng = w_cfg.getWishMax();
		}
		m_object = t;
	}

	@Override
	protected void setData(Object obj) {
		m_object = (PlayerWishInfo)obj;
	}
	
	public PlayerWishInfo loadByUid(String uid)
	{
		LoadObjectByUUID(uid, "uid", PlayerWishInfo.class);
		return (PlayerWishInfo) m_object;
	}

}
