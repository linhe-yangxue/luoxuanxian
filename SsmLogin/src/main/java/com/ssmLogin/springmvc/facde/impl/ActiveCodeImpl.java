package com.ssmLogin.springmvc.facde.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ssmCore.constants.ReInfo;
import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.tool.colligate.DateUtil;
import com.ssmCore.tool.colligate.Encryption;
import com.ssmCore.tool.colligate.HttpRequest;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmCore.tool.colligate.RegExp;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmLogin.constant.ConstData;
import com.ssmLogin.constant.I_Error_Login;
import com.ssmLogin.defdata.AcodeStroe;
import com.ssmLogin.defdata.entity.Acodedate;
import com.ssmLogin.defdata.entity.Activecode;
import com.ssmLogin.defdata.impl.AcodedateImpl;
import com.ssmLogin.defdata.impl.ActivecodeImpl;
import com.ssmLogin.springmvc.facde.I_ActiveCode;
import com.ssmShare.order.ActiveTemp;
import com.ssmShare.order.GiftItem;
import com.ssmShare.platform.MemDat;

@Service
@Scope("prototype")
public class ActiveCodeImpl implements I_ActiveCode {

	private static final Logger log = LoggerFactory.getLogger(ActiveCodeImpl.class);
	@Autowired
	AcodedateImpl acode;
	@Autowired
	ActivecodeImpl activeDb;

	public static ActiveCodeImpl getInstance() {
		return SpringContextUtil.getBean(ActiveCodeImpl.class);
	}

	// 添加激活码模版
	public void init() {
		List<ActiveTemp> ls = BaseDaoImpl.getInstance().findAll(ActiveTemp.class);
		for (ActiveTemp act : ls) {
			MemDat.setActiveCode(act.get_id().toString(), act);
		}
	}

	@Override
	public void createCode(String gid, String pid, String iden, Integer num) throws Exception {
		Set<String> actCode = new HashSet<String>();
		Acodedate code = acode.find(gid, pid, iden);
		if (code == null) {
			code = new Acodedate();
			code.setGid(gid);
			code.setPid(pid);
			code.setGenerateNum(num);
			code.setSign(iden);
			code.setProductDate(new Date());
			code.setGenerateDate(System.currentTimeMillis());
			AcodedateImpl.instance().addRecord(code);
		} else {
			code.setGenerateNum(code.getGenerateNum() + num);
		}
		for (int i = 0; i < num; i++) {
			long time = code.getGenerateDate() + 999l;
			code.setGenerateDate(time);
			String temp = gid + pid + code.getGenerateDate().toString();
			temp = Encryption.Encode16(temp, Encryption.MD5);
			String first = StringUtils.substring(gid, 0, 1).toUpperCase();
			String sencend = StringUtils.substring(pid, 0, 2).toUpperCase();
			temp = first + sencend + StringUtils.substring(temp, 3);
			actCode.add(temp);
		}
		AcodedateImpl.instance().upRecord(code);
		AcodeStroe.getInstance().start(actCode, gid, pid, iden);
	}

	@Override
	public Object viewCode(String gid, String pid, Integer sign) throws Exception {
		ActivecodeImpl db = ActivecodeImpl.instance();
		List<Activecode> ls = db.findList(gid, pid, sign);
		return ls;
	}

	@Override
	public Object grantItem(String gid, String pid, Integer zid, String uid, String code) throws Exception {
		// 检测激活码是否存在是否使用过
		ActivecodeImpl db = ActivecodeImpl.instance();
		Activecode activecode = db.find(code);
		ReInfo info = new ReInfo();

		if (activecode == null) {
			info.rt = 1179;
			return info;
		}

		ActiveTemp temp = MemDat.getActiveCode(activecode.getStatus().toString());
		if (temp == null || !activecode.getGid().equals(gid) || !activecode.getPid().equals(pid)) {// 判断是否该游戏该平台的激活码
			info.rt = 1179;
			return info;
		}
		String currDate = DateUtil.getYear2Day(System.currentTimeMillis());
		if (activecode.getExpire() != null && temp.getExiper().equals(currDate)) { // 判断激活码是否使用过
			info.rt = 1180;
			return info;
		}
		if (activecode.getUid() != null) { // 判断激活码是否使用过
			info.rt = 1182;
			return info;
		}
		int count = db.getCount(uid, zid, temp.get_id()); // 判断是否当前去激活码类型使用过3次
		if (count < temp.getTimes()) {
			GiftItem item = new GiftItem();
			item.setTitle("激活码邮件");
			item.setWord("获得激活码物品");
			item.setUid(uid);
			item.setPid(pid);
			if (temp.getJewel() != null)
				item.setDmd(temp.getJewel().doubleValue());
			if (temp.getGold() != null)
				item.setGold(temp.getGold().doubleValue());
			if (temp.getItemid() != null && temp.getItemid().size() > 0)
				item.setIds(temp.getItemid());
			if (temp.getNumber() != null && temp.getNumber().size() > 0)
				item.setCnt(temp.getNumber());
			item.setType(0);
			String dress = MemDat.getSvMap(gid, zid);
			if (dress != null) {
				info = sendMail(item, dress);
				activecode.setZid(zid);
				activecode.setUid(uid);
				activecode.setExpire(new Date());
				db.upRecord(activecode);
			} else {
				log.warn(JsonTransfer.getJson(item) + "邮件发送失败！");
			}
		} else {
			info.rt = 1181;
		}
		return info;
	}

	/**
	 * 发送激活码邮件
	 * 
	 * @param gift
	 * @param dress
	 * @return
	 * @throws Exception
	 */
	private ReInfo sendMail(GiftItem gift, String dress) throws Exception {
		gift.setMailType(1);
		String sendstr = Encryption.encrypt1(JsonTransfer.getJson(gift), ConstData.MSG_KEY);
		String result = HttpRequest.PostFunction(dress + "/bill/gift", sendstr);
		if (result != null) {
			BaseDaoImpl.getInstance().add(gift);
			return JsonTransfer._In(result, ReInfo.class);
		}
		return new ReInfo(I_Error_Login.ERROR, "激活码激活错误！");
	}

	@Override
	public Object createTemplate(ActiveTemp act) {
		BaseDaoImpl db = BaseDaoImpl.getInstance();
		db.add(act);
		MemDat.setActiveCode(act.get_id().toString(), act);
		return db.findAll(ActiveTemp.class);
	}

	@Override
	public Object viewTemplate(String tempid) {
		if (tempid == null || !RegExp.isNumber(tempid)) {
			return BaseDaoImpl.getInstance().findAll(ActiveTemp.class);
		} else {
			long id = Long.parseLong(tempid);
			return BaseDaoImpl.getInstance().find(new Query(Criteria.where("_id").is(id)), ActiveTemp.class);
		}
	}

	@Override
	public boolean delTemplate(String tempid) {
		if (tempid != null && RegExp.isNumber(tempid)) {
			long id = Long.parseLong(tempid);
			BaseDaoImpl.getInstance()// 删除模版
					.remove(new Query(Criteria.where("_id").is(id)), ActiveTemp.class);
			AcodedateImpl.instance().delAll(tempid);
			ActivecodeImpl.instance().delAll(tempid);
			return true;
		}
		return false;
	}

}
