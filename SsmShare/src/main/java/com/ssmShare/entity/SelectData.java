package com.ssmShare.entity;

import org.springframework.data.annotation.Id;

public class SelectData {
	@Id
	private Integer type;
	private String id;
	private String text;

	public SelectData(String id,String text){
		setId(id);
		setText(text);
	}
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
	
}
