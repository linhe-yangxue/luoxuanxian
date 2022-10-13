package com.ssmCore.tool.colligate;

import java.util.List;
import java.util.Map;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

public class JsonTransfer {

	public static <T> T _In(String jsonstr, final Class<T> clazz) {
		Gson gson = new Gson();
		return gson.fromJson(jsonstr, clazz);
	}

	public static <T> List<T> _In(String jsonstr, TypeToken<List<T>> type) {
		Gson gson = new Gson();
		return gson.fromJson(jsonstr, type.getType());
	}

	public static <T> Map<T, T> _InMap(String jsonstr, TypeToken<Map<T, T>> typeToken) {
		Gson gson = new Gson();
		return gson.fromJson(jsonstr, typeToken.getType());
	}

	public static <T> Map<T, List<T>> _InMapLs(String jsonstr, TypeToken<Map<T, List<T>>> type) {
		Gson gson = new Gson();
		return gson.fromJson(jsonstr, type.getType());
	}

	public static Object _In(String jsonstr, Object obj) {
		Gson gson = new Gson();
		return gson.fromJson(jsonstr, obj.getClass());
	}

	/**
	 * 转JSON
	 * 
	 * @param obj
	 * @return
	 */
	public static String getJson(Object obj) {
		Gson gson = new Gson();
		return gson.toJson(obj);
	}

	/**
	 * JSON转换
	 * 
	 * @param obj
	 * @param dateFormat
	 *            时间格式
	 * @return
	 */
	public static String getJson(Object obj, String dateFormat) {
		Gson gson = new GsonBuilder().setDateFormat(dateFormat).create();
		return gson.toJson(obj);
	}

	/**
	 * 转JSON
	 * 
	 * @param obj
	 * @param keys
	 * @return
	 */
	public static String getJson(Object obj, String[] keys) {
		GsonBuilder gson = new GsonBuilder();
		gson.setExclusionStrategies(new ExclusionStrategy() {
			@Override
			public boolean shouldSkipField(FieldAttributes f) {
				for (String key : keys) {
					if (key.equals(f.getName())) {
						return true;
					}
				}
				return false;
			}

			@Override
			public boolean shouldSkipClass(Class<?> clazz) {
				// 过滤掉 类名包含 Bean的类
				return false;
			}
		});
		return gson.create().toJson(obj);
	}

	/**
	 * 转JSON
	 * 
	 * @param obj
	 * @param keys
	 *            忽略的字段
	 * @param dateFormat
	 *            时间格式化
	 * @return
	 */
	public static String getJson(Object obj, String[] keys, String dateFormat) {
		GsonBuilder gson = new GsonBuilder();
		gson.setExclusionStrategies(new ExclusionStrategy() {
			@Override
			public boolean shouldSkipField(FieldAttributes f) {
				for (String key : keys) {
					if (key.equals(f.getName())) {
						return true;
					}
				}
				return false;
			}

			@Override
			public boolean shouldSkipClass(Class<?> clazz) {
				// 过滤掉 类名包含 Bean的类
				return false;
			}
		});
		gson.setDateFormat(dateFormat);
		return gson.create().toJson(obj);
	}

	public static JsonElement getJsonObject(Object obj) {
		Gson gson = new Gson();
		return gson.toJsonTree(obj);
	}

	@SuppressWarnings("unchecked")
	public static <T> T conleObject(T obj) {
		String result = getJson(obj);
		return (T) _In(result, obj.getClass());
	}

}
