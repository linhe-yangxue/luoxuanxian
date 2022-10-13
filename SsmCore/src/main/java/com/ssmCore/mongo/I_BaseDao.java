package com.ssmCore.mongo;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface I_BaseDao{
	
	<T> T findById(Class<T> entityClass, String id);

	<T> List<T> findAll(Class<T> entityClass);

	void remove(Object obj);

	void add(Object obj);

	void saveOrUpdate(Object obj);
	
	<T> void dorp(String name);
	
}
