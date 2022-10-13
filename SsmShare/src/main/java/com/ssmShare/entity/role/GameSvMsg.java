package com.ssmShare.entity.role;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ssmCore.constants.Increment;

@Document // 服务器实体角色
public class GameSvMsg implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Increment
	private Long id;
	/** 服务器编号 (一区号对应一个服务器) */
	private int zid;
	/** 服务器名称 */
	private String name;
	/** 服务器地址 */
	private String dress;
	/** 服务器状态 0:维护中 1:爆满 2:拥挤 3:良好 */
	private Integer status;
	/** 是否是新区 0：老区 1：新区 */
	private Integer isNew;
	
	private Integer type; //服务器类型

	public int getZid() {
		return zid;
	}

	public void setZid(int zid) {
		this.zid = zid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDress() {
		return dress;
	}

	public void setDress(String dress) {
		this.dress = dress;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsNew() {
		return isNew;
	}

	public void setIsNew(Integer isNew) {
		this.isNew = isNew;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
