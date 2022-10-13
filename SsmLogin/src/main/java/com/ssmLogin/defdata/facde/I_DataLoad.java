package com.ssmLogin.defdata.facde;

public interface I_DataLoad {
	
	/**
	 * 数据库读取数据
	 */
	public void initDB();
	/**
	 * 文件更新数据
	 * @param json
	 */
	public void upLoad(String json);
}
