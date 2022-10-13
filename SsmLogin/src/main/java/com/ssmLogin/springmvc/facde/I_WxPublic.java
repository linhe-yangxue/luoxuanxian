package com.ssmLogin.springmvc.facde;

import java.util.List;

import com.ssmShare.entity.role.WxPublic;

public interface I_WxPublic {

	/**
	 * 分页查询公众号信息
	 * @param start
	 * @param finshed
	 * @return
	 */
	List<WxPublic> findAll(Integer opt,Integer start, Integer finshed);

	/**
	 * 根据pid 查询公众号信息
	 * @param pid
	 * @return
	 */
	WxPublic find(String pid);

	/**
	 * 添加一个公众号
	 * @param wxp
	 */
	void add(WxPublic wxp);

	/**
	 * 编辑一个共众号信息
	 * @param wxp
	 */
	void edit(WxPublic wxp);

	/**
	 * 删除一个公众号信息
	 * @param pid
	 */
	void delete(String pid);

}
