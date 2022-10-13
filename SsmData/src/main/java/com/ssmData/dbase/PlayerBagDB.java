package com.ssmData.dbase;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * 玩家背包Model
 */
@Service
@Scope("prototype")
public class PlayerBagDB extends BaseDataSource {

	private static final long serialVersionUID = 1L;

	// private PlayerBagInfo m_object = null;
	protected PlayerBagInfo getData() {
		return (PlayerBagInfo) m_object;
	}

	protected void setData(Object obj) {
		m_object = (PlayerBagInfo) obj;
	}

	/**
	 * 使用Id检索背包文档
	 * 
	 * @param _id
	 *            背包条目唯一Id
	 * @return
	 */
	/*public PlayerBagInfo loadById(String _id) {
		if (null == m_object || (m_object != null && getData()._id.equals(_id))) {
			m_object = BaseDaoImpl.getInstance().find(new Query(Criteria.where("_id").is(_id)), PlayerBagInfo.class);
			m_uuid = getData().uid;
		}
		return getData();
	}*/

	/**
	 * 使用用户Id检索背包文档
	 * 
	 * @param uid
	 *            用户唯一Id
	 * @return
	 */
	public PlayerBagInfo loadByUid(String uid) {
		LoadObjectByUUID(uid, "uid", PlayerBagInfo.class);
		return getData();
	}

	@Override
	protected void CreateInstance() {
		// TODO Auto-generated method stub
		m_object = new PlayerBagInfo();
		getData().uid = m_uuid;
		getData().items = new HashMap<Integer, Integer>();
		getData().equips = new ArrayList<Integer>();
		getData().initPlayerBag();
	}

}
