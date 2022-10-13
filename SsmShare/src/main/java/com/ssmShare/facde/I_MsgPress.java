package com.ssmShare.facde;

import com.ssmShare.entity.UserBase;

public interface I_MsgPress {
	
	/**
	 * 用户信息处理
	 * @param ubase  用户基本信息
	 * @param pid 
	 * @param gid 
	 * @param pInfo  平台对接文件
	 * @return  返回 用户数据是否记录成功
	 */
	public boolean pressUser(UserBase ubase, String gid, String pid);
	
	
}
