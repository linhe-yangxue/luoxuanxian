package com.jksdk.wanba;

class OrderToClient {
	public String game_key;// ： 这里是游戏中心提供的game_key（required）
	public String open_id;// ： 小伙伴游戏游戏中心提供的用户id（required）
	public Float total_fee;// ： 道具支付金额（单位元），精确到小数点后两位，（required）
	public String game_orderno;// ： 游戏生成的订单号（required，唯一）
	public String subject;// ： 游戏道具名称（required）
	public String description;// ： 游戏道具描述（option）
	public String notify_url;// ： 支付完成后通知URL（required）, 不要单独对这个参数进行encode
	public Integer timestamp;// ： 时间戳，1970-1-1至今秒数 （required）
	public String nonce;// ： 随机字符串（required）
	public String game_area;// ： 用户所在的游戏区（option）
	public String game_group;// : 用户所在的游戏服（option）
	public String game_level;// : 用户在游戏中的等级（option）
	public Long game_role_id;// : 用户的角色Id（option）
	public String signature;// : 签名（required）
}

class User_Entity {
	public int ret;
	public String msg;
	public int is_lost;
	public String nickname;
	public String gender;
	public String country;
	public String province;
	public String city;
	public String figureurl;
	public int is_yellow_vip;
	public int is_yellow_year_vip;
	public int yellow_vip_level;
	public int is_yellow_high_vip;
}

class ClientParam {
	public String pf;
	public int zoneid;
	public String appid;
	public String openid;
	public String openkey;
}

class PayResult {
	public int code;
	public int subcode;
	public String message;
	public PayData[] data;
}

class PayData {
	public String billno;
	public String cost;
}