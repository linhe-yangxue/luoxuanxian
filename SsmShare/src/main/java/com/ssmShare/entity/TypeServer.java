package com.ssmShare.entity;

import java.io.Serializable;

import com.ssmShare.constants.E_Diver;

public class TypeServer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ServerList[] iso; // 苹果列表
	private ServerList[] andriod; // 安卓列表
	private ServerList[] other;// 其他列表

	// =====聊天服务器====//
	private String ChatUrl_iso;
	private String ChatUrl_and;
	private String ChatUrl_oth;

	private String isoText;
	private String andText;
	private String othText;

	public ServerList[] getSvList(int type) {

		if (type == E_Diver.IOS.getType())
			return iso;

		if (type == E_Diver.ANDROID.getType())
			return andriod;

		return other;
	}

	public String getChatUrl(int type) {

		if (type == E_Diver.IOS.getType())
			return ChatUrl_iso;

		if (type == E_Diver.ANDROID.getType())
			return ChatUrl_and;

		return othText;
	}

	public String getNotice(int type) {

		if (type == E_Diver.IOS.getType())
			return isoText;

		if (type == E_Diver.ANDROID.getType())
			return andText;

		return othText;
	}

	public ServerList[] getIso() {
		return iso;
	}

	public void setIso(ServerList[] iso) {
		this.iso = iso;
	}

	public ServerList[] getAndriod() {
		return andriod;
	}

	public void setAndriod(ServerList[] andriod) {
		this.andriod = andriod;
	}

	public ServerList[] getOther() {
		return other;
	}

	public void setOther(ServerList[] other) {
		this.other = other;
	}

	public String getIsoText() {
		return isoText;
	}

	public void setIsoText(String isoText) {
		this.isoText = isoText;
	}

	public String getAndText() {
		return andText;
	}

	public void setAndText(String andText) {
		this.andText = andText;
	}

	public String getOthText() {
		return othText;
	}

	public void setOthText(String othText) {
		this.othText = othText;
	}

	public String getChatUrl_iso() {
		return ChatUrl_iso;
	}

	public void setChatUrl_iso(String chatUrl_iso) {
		ChatUrl_iso = chatUrl_iso;
	}

	public String getChatUrl_and() {
		return ChatUrl_and;
	}

	public void setChatUrl_and(String chatUrl_and) {
		ChatUrl_and = chatUrl_and;
	}

	public String getChatUrl_oth() {
		return ChatUrl_oth;
	}

	public void setChatUrl_oth(String chatUrl_oth) {
		ChatUrl_oth = chatUrl_oth;
	}
}
