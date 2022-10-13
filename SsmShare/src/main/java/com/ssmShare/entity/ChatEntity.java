package com.ssmShare.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ssmCore.constants.Increment;

@Document
public class ChatEntity {
	@Id
	@Increment
	private long _id;
	/** 游戏玩家id */
	private String uid;
	/** 用户帐号 */
	private String guid;
	/** 游戏玩家频道 */
	private Integer channel;
	/**工会id**/
	private String guildId; // 公会id
	/**工会成员uid**/
	private List<String>uids;
	/** 游戏信息类型 */
	private Integer msgType;
	/** 游戏信息内容 */
	private String context;
	/** 游戏信息内容 */
	private Object obj;
	/** 消息时间 **/
	private Long sendTime;
	/**游戏id**/
	private String gid;
	/**游戏区id**/
	private Integer zid;
	/**vip等级**/
	private Integer vip;
	/** 发送者 */
	private ChartUser sender;

	public Integer getChannel() {
		return channel;
	}

	public void setChannel(Integer channel) {
		this.channel = channel;
	}

	public Integer getMsgType() {
		return msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public Integer getZid() {
		return zid;
	}

	public void setZid(Integer zid) {
		this.zid = zid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Long getSendTime() {
		return sendTime;
	}

	public void setSendTime(Long sendTime) {
		this.sendTime = sendTime;
	}

	public Integer getVip() {
		return vip;
	}

	public void setVip(Integer vip) {
		this.vip = vip;
	}

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public ChartUser getSender() {
		return sender;
	}

	public void setSender(ChartUser sender) {
		this.sender = sender;
	}

	public List<String> getUids() {
		return uids;
	}

	public void setUids(List<String> uids) {
		this.uids = uids;
	}

	public String getGuildId() {
		return guildId;
	}

	public void setGuildId(String guildId) {
		this.guildId = guildId;
	}

}
