package com.ssmShare.entity.role;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Platform implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private String text;
	private String  isWx; //可以判断是否是微信
	private Float   rate; // 货币比率

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public Float getRate() {
		return rate;
	}
	public void setRate(Float rate) {
		this.rate = rate;
	}
	public String getIsWx() {
		return isWx;
	}
	public void setIsWx(String isWx) {
		this.isWx = isWx;
	}
}
