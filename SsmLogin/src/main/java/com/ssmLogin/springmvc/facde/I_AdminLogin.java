package com.ssmLogin.springmvc.facde;

import com.ssmCore.constants.ReInfo;

public interface I_AdminLogin {

	/**
	 * 管理员登录
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	public ReInfo adminLogin(String userName, String password);

	/**
	 * 修改密码
	 * 
	 * @param userName
	 * @param newPw
	 * @return
	 */
	public ReInfo updatePw(String userName, String pw, String newPw);
}
