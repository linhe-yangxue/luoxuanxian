package com.ssmLogin.constant;

/**
 * 后台管理 用户分类
 * 
 * @author Only
 *
 */
public enum USER_TYPE {
	/**
	 * 管理员
	 */
	admin(100, "管理员"),
	/**
	 * 其他
	 */
	OTHER(0, "其他");

	private int type;
	private String title;

	USER_TYPE(int type, String title) {
		this.type = type;
		this.title = title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
