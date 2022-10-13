package com.ssmLogin.defdata.facde;

import com.ssmLogin.defdata.AdminUser;

public interface I_AdminDataPress {

	/**
	 * 管理员登陆
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public AdminUser userLogin(String username, String password);

	/**
	 * 修改密码
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean updatePw(String username, String newpassword);

	/**
	 * 检测密码是否与旧密码相同
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean checkPwIsSame(String username, String newpassword);
}
