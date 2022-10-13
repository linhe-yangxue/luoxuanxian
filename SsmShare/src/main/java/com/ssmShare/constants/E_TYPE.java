package com.ssmShare.constants;

public enum E_TYPE {
	HEART(0),
	LOGIN(1),
	NOTICE(2),         //公告
	MESSAGE(3),		   //聊天信息
	QUITGEAME(4),
	MSGLIST(5),
	GUILD_CHANGE(6),      //改变自己所在工会
	GUILD_CHANGE_MSG(7),  // 工会变更通知
	PAY_SUCESS_MSG(8);  // 支付成功通知

	private int code;

	E_TYPE(int code){
		this.setCode(code);
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}

}
