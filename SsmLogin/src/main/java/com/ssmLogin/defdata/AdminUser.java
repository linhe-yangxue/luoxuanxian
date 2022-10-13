package com.ssmLogin.defdata;

import java.util.Date;

import org.springframework.data.annotation.Id;

import com.ssmCore.constants.Increment;

public class AdminUser {
	@Id
	@Increment
	private long id;
	private String username;
	private String password;

	/** 用户类型（USER_TYPE） */
	private int usertype;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/** 最后登录时间 */
	private Date lastlogin;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getUsertype() {
		return usertype;
	}

	public void setUsertype(int usertype) {
		this.usertype = usertype;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastlogin() {
		return lastlogin;
	}

	public void setLastlogin(Date lastlogin) {
		this.lastlogin = lastlogin;
	}

}
