package com.ssmShare.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class PlatForm {
	@Id
	private String _id = "游戏对接平台";
	private String uid = "platid";
	private List<SelectData> wx;
	private List<SelectData> plat;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public List<SelectData> getWx() {
		return wx;
	}
	public void setWx(List<SelectData> wx) {
		this.wx = wx;
	}
	public List<SelectData> getPlat() {
		return plat;
	}
	public void setPlat(List<SelectData> plat) {
		this.plat = plat;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
}
