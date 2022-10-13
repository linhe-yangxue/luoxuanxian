package com.ssmData.config.constant;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.internal.LinkedTreeMap;
import com.ssmCore.constants.PrimaryKey;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmCore.tool.spring.SpringContextUtil;

/**
 * 实体注册器
 */
// TODO: 2018\10\12 0012 ??? 这代码没用？？？ 
@Service
public class EntityReg {

	public final static Map<String, Object> conf = new ConcurrentHashMap<>();
	private @Value("${CONF_PACK_NAME}") String pack;

	public static EntityReg getInstance() {
		return SpringContextUtil.getBean(EntityReg.class);
	}

	/**
	 * 添加数据实体
	 * 
	 * @param clzz
	 */
	public <T> void regClass(String fname, String json) throws Exception {
		fname = fname.substring(0, 1).toUpperCase() + fname.substring(1);
		Class<?> clz = Class.forName(pack.concat("." + fname));
		Field[] fields = clz.getDeclaredFields();
		String key = null;
		for (Field f : fields) {
			if (f.isAnnotationPresent(PrimaryKey.class)) {
				key = f.getName();
				break;
			}
		}
		Object[] objs = null;
		try{
			objs = JsonTransfer._In(json,Object[].class);
		}catch(Exception e){
			objs  = new Object[1];
			objs [0] = JsonTransfer._In(json,Object.class);
		}
		Map<Object, String> entity = new HashMap<>();
		if (key != null) {
			for(Object ob : objs) {
				LinkedTreeMap<?,?> obj = (LinkedTreeMap<?, ?>) ob;
				entity.put(obj.get(key), JsonTransfer.getJson(obj));
			};
		}else if(objs.length==1){
			entity.put(fname, JsonTransfer.getJson(objs [0]));
		}
		if(entity.size()>0)
			conf.put(fname, entity);
	}

	/**
	 * 返回要查询的模版条目
	 * @param clazz
	 * @param t
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getTemplet(Class<T> clazz,Object t) {
		Map<String, String> entity = (Map<String, String>) conf.get(clazz.getSimpleName());

		if (entity == null)
			return null;
		
		String json=null;
		if(t instanceof Integer)
			json = entity.get(Double.parseDouble(t.toString()));
		else
			entity.get(t);
			
		if (json != null)
			return JsonTransfer._In(json, clazz);
		return null;
	}
	
	/**
	 * 返回模版列表
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getTempList(Class<T> clazz){
		Map<String, String> entity = (Map<String, String>) conf.get(clazz.getSimpleName());
		List<T> ls = new ArrayList<>();
		Iterator<T> it = (Iterator<T>) entity.entrySet().iterator();
		while(it.hasNext()){
			  Map.Entry<String,String> entry = (Entry<String, String>) it.next();  
			  String value = (String)entry.getValue(); 
			  ls.add(JsonTransfer._In(value, clazz));
		}
		if(ls.size()>0)
			return ls;
		return null;
	}

}
