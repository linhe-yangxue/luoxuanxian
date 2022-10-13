package com.ssmData.dbase;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ssmCore.memcached.MemAccess;
import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.tool.colligate.CommUtil;
import com.ssmShare.entity.UserBase;

@Service
@Scope("prototype")
public abstract class BaseDataSource implements Serializable{
	private static final long serialVersionUID = 1L;
	protected String m_uuid = null; /// 当前角色的UUID
	protected Object m_object = null; /// 数据库存储的Data对象
	
	public static final String SERVER_UUID = "server_";
	private @Value("${mongo.dbname}") String dbName;
	public String ServerUUID()
	{
		return SERVER_UUID + dbName;
	}

	/**
	 * 子类可覆盖，默认前缀为类名
	 * 
	 * @return
	 */
	protected String GetDataType() {
		return this.getClass().getSimpleName();
	}

	/**
	 * 子类可覆盖，是否需要重新加载，在存库和取库时的判断
	 * 
	 * @return
	 */
	protected boolean GetDataExsit() {
		return m_object != null;
	}

	/**
	 * 具体的存储数据库的操作，子类可重载
	 */
	protected void onSave() {
		if (GetDataExsit()) {
			BaseDaoImpl.getInstance().saveOrUpdate(m_object);
		}
	}

	/**
	 * 实现模版方法 创建相应data结构对象
	 */
	protected abstract void CreateInstance();
	/**
	 * 填充并强制转换类型
	 * 
	 * @param obj
	 */
	protected abstract void setData(Object obj);

	protected void destory() {
		m_uuid = null;
		m_object = null;
	}

	/**
	 * 外部调用的存储接口
	 */
	public void save() {
		/// 立即保存Memcache
		if (GetDataExsit())
			UpdateValue(m_uuid, m_object);

		if (this != null) {
			BaseDataSource dSource = CommUtil.cloneObject(this);//深度克隆
			try {
				DataSaveRole.savedb.put(dSource);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Object createDB(String uid)
	{
		try
		{
			m_uuid = uid;
			CreateInstance();
			BaseDaoImpl.getInstance().add(m_object);
			UpdateValue(uid, m_object);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return m_object;
	}

	/**
	 * 使用UUID加载DB内容，需要使用setData的模版函数，加载不到，返回null
	 */
	protected Object LoadObjectByUUID(String uid, String nameInDB, Class<?> entityClass) {
		Object info = GetValue(uid);
		if (null == info) {
			info = BaseDaoImpl.getInstance().find(new Query(Criteria.where(nameInDB).is(uid)), entityClass);
			if (null != info) {
				setData(info);
			} else {
				return null;
			}
			UpdateValue(uid, m_object);
		} else {
			setData(info);
		}

		return m_object;
	}
	
	//===============================仅角色创建使用！！！！！
	/**
	 * 使用UUID加载DB内容，需要使用CreateInstance、setData的模版函数
	 */
	protected Object LoadObjectByUUID_PlayerInfo(String uid, String nameInDB, Class<?> entityClass, UserBase userbase) {
		m_uuid = uid;
		if (GetDataExsit()) {
			return m_object;
		}
		Object info = GetValue(uid);
		if (null == info) {
			info = BaseDaoImpl.getInstance().find(new Query(Criteria.where(nameInDB).is(uid)), entityClass);
			if (null != info) {
				setData(info);
			} else {
				CreateInstance_playerinfo(userbase);
				BaseDaoImpl.getInstance().add(m_object);
			}
			UpdateValue(uid, m_object);
		} else {
			setData(info);
		}

		return m_object;
	}
	//===============================仅角色创建使用！！！！！
	protected void CreateInstance_playerinfo(UserBase userbase) {
	
	}
	/**
	 * 使用UUID加载DB内容，需要使用CreateInstance、setData的模版函数
	 */
	//加载，遇到空时返回空，不会创建
	/*protected Object LoadButNotCreate(String uid, String nameInDB, Class<?> entityClass)
	{
		Object info = GetValue(uid);
		if (null == info) {
			info = BaseDaoImpl.getInstance().find(new Query(Criteria.where(nameInDB).is(uid)), entityClass);
			if (null != info) {
				setData(info);
			} else {
				return null;
			}
			UpdateValue(uid, m_object);
		} else {
			setData(info);
		}

		return m_object;
	}*/

	/*
	 * XMemcachedClient的add方法不允许key值相等，即内存中有key为hello的值了，再次执行add方法，就会失败。而经过测试，
	 * 第二次执行xmc.set("hello",0,"ppms");就不会出错，原因是set方法在缓存服务器上，没有对应的key，便新建key-
	 * value， 如果有，就替换该key对应的value值。
	 */
	protected Object GetValue(String uuid) {
		this.m_uuid = uuid;
		return MemAccess.GetValue(GetDataType() + uuid);
	}

	protected void AddValue(String uuid, Object obj) {
		if (obj != null)
			MemAccess.Add(GetDataType() + uuid, obj);
	}

	protected boolean UpdateValue(String uuid, Object obj) {
		if (obj != null)
			return MemAccess.Update(GetDataType() + uuid, obj);
		return false;
	}
	
	public void CleanMem(String uuid){
		MemAccess.Delete(GetDataType() + uuid);
	}
}
