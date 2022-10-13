package com.ssmShare.order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 *	Gm工具发送物品的通信包体 
 */
public class GiftItem implements Serializable{
	private static final long serialVersionUID = 1L;
	private List<String> uids;			//玩家uid列表
	private Integer type;		//0是物品邮件，1是通知邮件
	private String title;		//自定义标题
	private String word;		//自定义内容
	private Double gold;		//金币数量
	private Double dmd;			//钻石数量
	private List<Integer> ids;	//物品id列表
	private List<Integer> cnt;	//物品数量列表
	private Date createDate;	//创建日期
	private Integer mailType;  //0是所有  1是选中的玩家
	private String pid; // 平台pid
	
	public Integer getMailType() {
		return mailType;
	}
	public void setMailType(Integer mailType) {
		this.mailType = mailType;
	}
	public List<String> getUid() {
		return uids;
	}
	public void setUid(List<String> uids) {
		this.uids = uids;
	}
	public void setUid(String uid) {
		if(uids==null){
			uids = new ArrayList<String>();
		}
		uids.add(uid);
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public Double getGold() {
		return gold;
	}
	public void setGold(Double gold) {
		this.gold = gold;
	}
	public Double getDmd() {
		return dmd;
	}
	public void setDmd(Double dmd) {
		this.dmd = dmd;
	}
	public List<Integer> getIds() {
		return ids;
	}
	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}
	public List<Integer> getCnt() {
		return cnt;
	}
	public void setCnt(List<Integer> cnt) {
		this.cnt = cnt;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}	
	
	
}
