package com.jksdk.wx;

public class WxUser{

	public String access_token; //访问token
	public String  openid;		//用户openid
	public Long expires_in;		//有效时间
	public Integer errcode;     //错误代码
	public String errmsg;       //错误信息
	public int subscribe;   //用户没有关注 0-取不到信息，1-取得到信息
	public String  nickname;	//用户昵称
	public Integer sex;			//用户性别
	public String city;			//城市
	public String province;		//省份
	public String country;		//国家
	public String headimgurl;	//用户头像
	public Long subscribe_time; //关注时间
	public String scope;  //用户授权的作用域
	public String ticket; //接口调用凭证
}
