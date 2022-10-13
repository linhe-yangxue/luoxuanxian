package com.ssmShare.order;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ssmCore.constants.Increment;

@Document
public class ActiveTemp implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@Increment
	private long _id;
	private String  title;
	private Integer times;   //次数
	private Integer jewel;   //砖石
	private Integer gold;    //金币
	private List<Integer> itemid;  //道具id 
	private List<Integer> number;  //数量
	private String exiper;     //到期时间
	
	public Long get_id() {
		return _id;
	}
	public void set_id(Long _id) {
		this._id = _id;
	}
	public Integer getTimes() {
		return times;
	}
	public void setTimes(Integer times) {
		this.times = times;
	}
	public Integer getJewel() {
		return jewel;
	}
	public void setJewel(Integer jewel) {
		this.jewel = jewel;
	}
	public Integer getGold() {
		return gold;
	}
	public void setGold(Integer gold) {
		this.gold = gold;
	}
	public List<Integer> getItemid() {
		return itemid;
	}
	public void setItemid(List<Integer> itemid) {
		this.itemid = itemid;
	}
	public List<Integer> getNumber() {
		return number;
	}
	public void setNumber(List<Integer> number) {
		this.number = number;
	}
	public String getExiper() {
		return exiper;
	}
	public void setExiper(String exiper) {
		this.exiper = exiper;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
