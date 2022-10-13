package com.ssmShare.platform;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ssmCore.constants.Increment;
import com.ssmShare.entity.Uaction;
import com.ssmShare.entity.Unions;
import com.ssmShare.entity.UserBase;

//平台用户信息
@Document
public class UserInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Increment
	private long _id; // 游戏使用id
	private String pid; // 平台id
	private String account; // 平台帐号
	private String subPid;
	
	private UserBase userBase; // 用户基本信息
	private Uaction uaction; // 用户游戏操作动作
	private OrderInfo order; // 用户订单只只记录成功单号 key=游戏id
	private Unions[] uions; // 工会信息

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public UserBase getUserBase() {
		return userBase;
	}

	public void setUserBase(UserBase userBase) {
		this.userBase = userBase;
	}

	public Uaction getUaction() {
		return uaction;
	}

	public void setUaction(Uaction uaction) {
		this.uaction = uaction;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Unions[] getUions() {
		return uions;
	}

	public void setUions(Unions[] uions) {
		this.uions = uions;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public OrderInfo getOrder() {
		return order;
	}

	public void setOrder(OrderInfo order) {
		this.order = order;
	}

	public String getSubPid() {
		return subPid;
	}

	public void setSubPid(String subPid) {
		this.subPid = subPid;
	}
}
