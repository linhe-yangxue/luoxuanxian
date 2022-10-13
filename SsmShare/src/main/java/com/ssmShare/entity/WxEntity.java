package com.ssmShare.entity;

import java.io.Serializable;

public class WxEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	public String appid;   //
	public String secret;
	public String token;
	public String aeskey;
	public String access_token;
	public String nonceStr; //
	public String jsapi_ticket;
	public String timestamp;  //
	public Integer expires_in;
	public String  url;  //
	public String signature;//
	
	public void clear(){
		secret = null;
		aeskey = null;
		access_token = null;
		expires_in = null;
		jsapi_ticket = null;
		token = null;
	}
}
