package com.ssmLogin.springmvc.facde;

import com.ssmShare.order.ActiveTemp;

public interface I_ActiveCode {

	/**
	 * 创建激活码
	 * @param gid  游戏id
	 * @param pid  平台id
	 * @param iden 生成标识
	 * @param num  生成数量
	 */
	void createCode(String gid,String pid,String iden,Integer num) throws Exception;
	
	/**
	 * 查看激活码
	 * @param gid  游戏id
	 * @param pid  平台id
	 */
	Object viewCode(String gid,String pid,Integer sign)throws Exception;
	
	/**
	 * 校验激活码 发放物品
	 * @param gid
	 * @param pid
	 * @param zid
	 * @param uid
	 * @param code
	 */
	Object grantItem(String gid, String pid,Integer zid, String uid, String code)throws Exception;
	
	/**
	 * 添加模版成功
	 * @param act
	 */
	Object createTemplate(ActiveTemp act);

	Object viewTemplate(String tempid);

	boolean delTemplate(String tempid);
}
