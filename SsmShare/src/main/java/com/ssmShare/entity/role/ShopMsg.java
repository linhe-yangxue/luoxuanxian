package com.ssmShare.entity.role;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ssmCore.constants.Increment;

@Document // 服务器实体角色
public class ShopMsg implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@Increment
	private Long id;
	private String text; // 商品名称
	
	private int itemId; // 商品id
	private Integer type;
	private Integer moneyType; // 货币类型
	private Float monetaryAmount; // 货币金额
	private Integer diamondsNum; // 购买物品数量
	private Integer award; // 购买赠送数量
	private Integer firstId; //首充
	private String comment; // 商品描述
	private String icon; // 商品图标
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getMoneyType() {
		return moneyType;
	}
	public void setMoneyType(Integer moneyType) {
		this.moneyType = moneyType;
	}
	public Float getMonetaryAmount() {
		return monetaryAmount;
	}
	public void setMonetaryAmount(Float monetaryAmount) {
		this.monetaryAmount = monetaryAmount;
	}
	public Integer getDiamondsNum() {
		return diamondsNum;
	}
	public void setDiamondsNum(Integer diamondsNum) {
		this.diamondsNum = diamondsNum;
	}
	public Integer getAward() {
		return award;
	}
	public void setAward(Integer award) {
		this.award = award;
	}
	public Integer getFirstId() {
		return firstId;
	}
	public void setFirstId(Integer firstId) {
		this.firstId = firstId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
