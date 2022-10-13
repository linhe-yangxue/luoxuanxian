package com.ssmLogin.springmvc.facde.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssmCore.constants.ReInfo;
import com.ssmLogin.constant.I_Error_Login;
import com.ssmLogin.defdata.AdminUser;
import com.ssmLogin.defdata.facde.I_AdminDataPress;
import com.ssmLogin.springmvc.facde.I_AdminLogin;

@Service
public class AdminLogin implements I_AdminLogin {
	@Autowired
	I_AdminDataPress press;

	@Override
	public ReInfo adminLogin(String userName, String password) {
		AdminUser user = press.userLogin(userName, password);
		if (user == null) {
			return new ReInfo(I_Error_Login.ERROR_LOGIN);
		} else {
			return new ReInfo(user);
		}
	}

	@Override
	public ReInfo updatePw(String username, String pw, String newPw) {
		boolean isSame = press.checkPwIsSame(username, newPw);
		if (isSame) {
			// 与旧密码相同
			return new ReInfo(I_Error_Login.ERROR_ADMIN_USER_PW_IS_SAME, "不能与旧密码相同");
		} else {
			AdminUser login = press.userLogin(username, pw);
			if (login == null) {
				// 旧密码错误
				return new ReInfo(I_Error_Login.ERROR_ADMIN_USER_OLDPW_ERROR, "旧密码错误");
			} else {
				boolean res = press.updatePw(username, newPw);
				if (res) {
					// 修改成功
					return new ReInfo(I_Error_Login.SUCCESS);
				} else {
					// 修改异常
					return new ReInfo(I_Error_Login.ERROR);
				}
			}
		}
	}
}
