package com.ssmShare.constants;

public enum E_PayType {
	/** 月卡 */
	MONTH(1),
	/** 终身卡*/
	LIFEALL(2),
	/**一般 */
	NOME(3),
	/**投资 */
	INVEST(4),
	/**等级限时礼包 */
	LVGIFT(5);

	private int code;

	E_PayType(int code) {
		this.setCode(code);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
