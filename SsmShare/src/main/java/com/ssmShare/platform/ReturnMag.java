package com.ssmShare.platform;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import com.ssmShare.entity.ServerList;
import com.ssmShare.entity.UserBase;
import com.ssmShare.order.ShopItem;

public class ReturnMag  implements Serializable{	

	private static final long serialVersionUID = 1L;
	
	private String gid; //游戏id
	private String pid; //平台id
	private Long guid;
	private Integer isWx = 0;
	
	private UserInfo info;
	
	private Map<String, Object> Order;
	//=======登录前发送数据=======
	/**最后登录的区*/
	private Integer lastZid;
	/**登录创建过角色的区*/
	private Set<Integer> logZid;
	/**游戏交易商品信息*/
	private ShopItem [] shops;
	/**游戏服务器列表 */
	private ServerList[] svList;
	
	private String notice;  //公告
	
	private String chatUrl;  //聊天服务器地址
	
	private String gameUrl_login;//游戏登录地址  
	private String gameUrl_pay;  //游戏订单创建 地址
	
	private String payUrl; //第三方游戏支付地址；
	
	public String getGameUrl_login() {
		return gameUrl_login;
	}

	public void setGameUrl_login(String gameUrl_login) {
		this.gameUrl_login = gameUrl_login;
	}

	public String getGameUrl_pay() {
		return gameUrl_pay;
	}

	public void setGameUrl_pay(String gameUrl_pay) {
		this.gameUrl_pay = gameUrl_pay;
	}
	public ReturnMag(){
		info = new UserInfo();
	}
	
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	
	public UserInfo getInfo() {
		return info;
	}
	public void setInfo(UserInfo info) {
		this.info = info;
	}
	public ShopItem[] getShops() {
		return shops;
	}
	public void setShops(ShopItem[] shops) {
		this.shops = shops;
	}
	public ServerList[] getSvList() {
		return svList;
	}
	public void setSvList(ServerList[] svList) {
		this.svList = svList;
	}
	public Integer getLastZid() {
		return lastZid;
	}
	public void setLastZid(Integer lastZid) {
		this.lastZid = lastZid;
	}
	/**
	 * 清除服务器列表信息i
	 */
	public void clearServrmsg(){
		shops = null;
		svList = null;
	}
	
	public String getChatUrl() {
		return chatUrl;
	}
	public void setChatUrl(String chatUrl) {
		this.chatUrl = chatUrl;
	}
	public Set<Integer> getLogZid() {
		return logZid;
	}
	public void setLogZid(Set<Integer> logZid) {
		this.logZid = logZid;
	}

	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}

	public void setUsebase(UserBase ubase) {
		if(info == null)
			this.info = new UserInfo();
		this.info.setUserBase(ubase);
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public Map<String, Object> getOrder() {
		return Order;
	}

	public void setOrder(Map<String, Object> order) {
		Order = order;
	}

	public Long getGuid() {
		return guid;
	}

	public void setGuid(Long guid) {
		this.guid = guid;
	}

	public Integer getIsWx() {
		return isWx;
	}

	public void setIsWx(Integer isWx) {
		this.isWx = isWx;
	}
}
