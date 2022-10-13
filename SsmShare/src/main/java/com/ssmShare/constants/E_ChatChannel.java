package com.ssmShare.constants;

public enum E_ChatChannel {
	/** 全区 */
	ALL(0),
	/** 当前区 */
	AREEA(1),
	/** 公会 */
	GUILD(2),
	/** 好友 */
	FRIEND(3);

	private int code;

	E_ChatChannel(int code) {
		this.setCode(code);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
