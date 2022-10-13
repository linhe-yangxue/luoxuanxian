package com.ssmShare.constants;

public enum E_PlateInfo {
	
	ALL {public Integer getType(){return 0;}},
	
	DOCKING	{public Integer getType(){return 1;}}, //对接文档
	
	SHOPS	{public Integer getType(){return 2;}},  //商品
	
	NOTICE	{public Integer getType(){return 3;}}, //公告
	
	SERVERS	{public Integer getType(){return 4;}};//服务器列表
	
	public abstract Integer getType();
}
