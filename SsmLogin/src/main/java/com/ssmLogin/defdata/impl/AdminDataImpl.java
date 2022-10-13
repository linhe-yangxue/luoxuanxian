package com.ssmLogin.defdata.impl;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.tool.colligate.Encryption;
import com.ssmLogin.constant.USER_TYPE;
import com.ssmLogin.defdata.AdminUser;
import com.ssmLogin.defdata.facde.I_AdminDataPress;

@Service
public class AdminDataImpl implements I_AdminDataPress {

	private static final Logger log = LoggerFactory.getLogger(AdminDataImpl.class);
	private @Value("${admin_user}") String adminuser;
	private @Value("${admin_pw}") String adminpw;
	private BaseDaoImpl db = BaseDaoImpl.getInstance();

	/**
	 * 初始化超级管理员
	 */
	@PostConstruct
	private void initSuperAdmin() {
		try {
			Query query = new Query(Criteria.where("username").is(adminuser));
			AdminUser user = db.find(query, AdminUser.class);
			if (user == null) {
				user = new AdminUser();
				user.setUsername(adminuser);
				user.setPassword(Encryption.Encode(adminpw, Encryption.MD5));
				user.setUsertype(USER_TYPE.admin.getType());
				user.setCreateDate(new Date());
				user.setLastlogin(new Date());
				db.add(user);
			}
			// else {
			// // 检测密码是否改变
			// if (!user.getPassword().equals(adminpw)) {
			// user.setPassword(adminpw);
			// db.saveOrUpdate(user);
			// }
			// }
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
		}
	}

	@Override
	public AdminUser userLogin(String username, String password) {
		try {
			String pw = Encryption.Encode(password, Encryption.MD5);
			Query query = new Query(Criteria.where("username").is(adminuser).and("password").is(pw));
			AdminUser userEntity = db.find(query, AdminUser.class);
			if (userEntity != null) {
				// 记录最后登录时间
				Update update = new Update();
				update.set("lastlogin", new Date()); // 更新最后登陆时间
				db.saveOrUpdate(query, update, AdminUser.class);
			}
			return userEntity;
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
		}
		return null;
	}

	@Override
	public boolean updatePw(String username, String newpassword) {
		try {
			Update update = new Update();
			update.set("password", Encryption.Encode(newpassword, Encryption.MD5));
			db.saveOrUpdate(new Query(Criteria.where("username").is(username)), update, AdminUser.class);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean checkPwIsSame(String username, String newpassword) {
		AdminUser userEntity = db.find(new Query(Criteria.where("username").is(adminuser)), AdminUser.class);
		String newPw = Encryption.Encode(newpassword, Encryption.MD5);
		return userEntity.getPassword().equals(newPw);
	}

}
