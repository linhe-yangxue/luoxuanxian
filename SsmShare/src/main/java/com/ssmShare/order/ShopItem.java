package com.ssmShare.order;

import java.io.Serializable;

import org.springframework.data.mongodb.core.index.Indexed;

/**
 * 游戏商品
 */
public class ShopItem implements Serializable {

	private static final long serialVersionUID = 1L;
	@Indexed
	private int itemId; // 商品id
	private String name; // 商品名称
	private Integer type;
	private Integer moneyType; // 货币类型
	private Float monetaryAmount; // 货币金额
	private Integer diamondsNum; // 购买物品数量
	private Integer award; // 购买赠送数量
	private Integer firstId; //首充
	private String comment; // 商品描述
	private String icon; // 商品图标

	public int getItemId(){
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	public Integer getFirstId() {
		return firstId;
	}

	public void setFirstId(Integer firstId) {
		this.firstId = firstId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
