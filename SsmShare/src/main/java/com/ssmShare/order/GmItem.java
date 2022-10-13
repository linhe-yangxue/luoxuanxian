package com.ssmShare.order;

public class GmItem {
	
	private Integer zid ; //区id
	private String uid  ; //uid
	private Long guid;    //玩家id
	
	private Integer type; //充值类型
	private Integer dianmod; //钻石金额
	private Integer money;  //充值
	private Integer frist; // 首次充值      //不是首冲为空
	private Integer itemId; //商品id
	private Integer itFrist; //        //该挡不是首充未空
	
	public Integer getZid() {
		return zid;
	}
	public void setZid(Integer zid) {
		this.zid = zid;
	}
	public Long getGuid() {
		return guid;
	}
	public void setGuid(Long guid) {
		this.guid = guid;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getDianmod() {
		return dianmod;
	}
	public void setDianmod(Integer dianmod) {
		this.dianmod = dianmod;
	}
	public Integer getFrist() {
		return frist;
	}
	public void setFrist(Integer frist) {
		this.frist = frist;
	}

	public Integer getMoney() {
		return money;
	}
	public void setMoney(Integer money) {
		this.money = money;
	}
	public Integer getItFrist() {
		return itFrist;
	}
	public void setItFrist(Integer itFrist) {
		this.itFrist = itFrist;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
}
