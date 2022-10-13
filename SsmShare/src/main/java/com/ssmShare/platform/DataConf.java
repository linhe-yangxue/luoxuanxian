package com.ssmShare.platform;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmShare.constants.E_PlateInfo;
import com.ssmShare.constants.LoginConstants;
import com.ssmShare.entity.Docking;
import com.ssmShare.entity.WxPlatform;

@Service
@Scope("prototype")
public class DataConf {

	public String gid;
	public Docking doc; // 对接文档
	public WxPlatform wx; // 微信平台文档
	public ReturnMag rtn; // 返回服务器信息
	public Map<String, Object> order;
	public String ipdress;
	public String paytype;
	public Map<String, Object> params;

	public static DataConf getInstance() {
		return SpringContextUtil.getBean(DataConf.class);
	}

	public void wxload(String pid) {
		 wx = LoginConstants.wxPlat.get(pid);
	}

	public void load(String gid, String pid, Integer... type) {
		rtn = new ReturnMag(); // 实例话返回信息
		rtn.setGid(this.gid = gid); // 游戏id
		rtn.setPid(pid); // 平台id

		for (int t : type) {
			if (t == E_PlateInfo.ALL.getType()) {
				load(gid, pid); // 加载全部数据
				break;
			}
			// 根据类型载入相关数据
			if (t == E_PlateInfo.DOCKING.getType()) {
				doc = MemDat.getDocking(gid, pid);
				if (doc.getIsWx() == 1) {
					wx = LoginConstants.wxPlat.get(doc.getWxPlat());
				}
				rtn.setNotice(doc.getNotice());
			}
		}
	}

	/**
	 * 读取全部数据
	 */
	private void load(String gid, String pid) {
		doc = MemDat.getDocking(gid, pid);
		if (doc != null) {
			if (doc.getIsWx() == 1) {
				wx = LoginConstants.wxPlat.get(doc.getWxPlat());
			}
			rtn.setNotice(doc.getNotice());
		}
	}

	public void saveMsg(String token, int mkeyTime) {
		MemDat.setServerMsg(token, mkeyTime, rtn);
	}

	public void Destory() {
		gid = null;
		doc = null; // 对接文档
		wx = null; // 微信平台文档
	}
}
