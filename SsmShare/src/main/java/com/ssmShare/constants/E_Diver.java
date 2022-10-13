package com.ssmShare.constants;

public enum E_Diver {

	/**
	 * 苹果系统
	 */
	IOS(0),
	/**
	 * 按卓系统
	 */
	ANDROID(1);

	private int type;

	E_Diver(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
}
