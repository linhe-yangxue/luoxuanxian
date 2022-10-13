package com.ssmShare.entity.role;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ssmCore.constants.Increment;

@Document // 
public class NoticeMsg implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@Increment
	private Long id;
	private String context;  //公告内容
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
}
