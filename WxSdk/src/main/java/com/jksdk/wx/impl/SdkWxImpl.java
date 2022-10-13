package com.jksdk.wx.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jksdk.wx.WxResult;
import com.jksdk.wx.WxUser;
import com.jksdk.wx.facde.I_WxService;
import com.ssmCore.constants.ReInfo;
import com.ssmCore.mongo.BaseDaoImpl;
import com.ssmCore.tool.colligate.Encryption;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmShare.entity.UserBase;
import com.ssmShare.entity.WxEntity;
import com.ssmShare.entity.WxPlatform;
import com.ssmShare.facde.I_Platform;
import com.ssmShare.platform.DataConf;
import com.ssmShare.platform.MemDat;

@Service("SdkWxImpl")
@Scope("prototype")
public class SdkWxImpl implements I_Platform {
	private static final Logger log = LoggerFactory.getLogger(SdkWxImpl.class);
	private static List<WxPlatform> ls;
	@Autowired
	I_WxService wxservice;

	public void init() throws Exception {
		ls = BaseDaoImpl.getInstance().findAll(WxPlatform.class);
		for (WxPlatform plat : ls) {
			WxEntity wx = getWxPlatMsg(plat);
			this.wxservice.getToken(wx, plat.getPid());
			this.wxservice.getJsTicket(wx, plat.getPid());
		}
	}

	private WxEntity getWxPlatMsg(WxPlatform plat) throws Exception {
		WxEntity wx = new WxEntity();
		wx.appid = plat.getAppid();
		wx.secret = plat.getSecret();
		wx.timestamp = WxLogic.getTimeStamp();
		wx.nonceStr = WxLogic.getNonceStr();
		return wx;
	}

	public Object platInit(DataConf dSource, String url) throws Exception {
		WxEntity wx = getWxPlatMsg(dSource.wx);
		wx.access_token = MemDat.getWxToken(dSource.wx.getPid());
		wx.jsapi_ticket = MemDat.getWxTicket(dSource.wx.getPid());
		if ((wx.access_token == null) || (wx.jsapi_ticket == null)) {
			init();
			wx.access_token = MemDat.getWxToken(dSource.wx.getPid());
			wx.jsapi_ticket = MemDat.getWxTicket(dSource.wx.getPid());
		}

		if (url != null) {
			String sign = "jsapi_ticket=" + wx.jsapi_ticket + "&noncestr=" + wx.nonceStr + "&timestamp=" + wx.timestamp
					+ "&url=" + url;

			if (url != null) {
				wx.signature = Encryption.Encode(sign, "SHA-1");
				wx.clear();
				return wx;
			}
		}
		return new ReInfo(4005, "微信签名生成失败！");
	}

	@Override
	public UserBase logVerification(Map<String, Object> param, DataConf dSource) throws Exception {
		// 访问微信服务器获取用户信息
		WxUser wUser = wxservice.getUserWeb((String) param.get("code"), dSource.wx);
		// 用户对象赋值
		if (wUser == null || wUser.openid == null)
			return null;

		UserBase ubase = new UserBase();
		String state = (String) param.get("state");
		if (state != null) {
			String[] temp = StringUtils.split(state, "*");
			if (temp.length > 1) {
				if(temp[0].equals("0")){
					ubase.setInitPid((String) param.get("pid"));
					ubase.pid = temp[1];
					dSource.rtn.getInfo().setSubPid(temp[1]);
				}else if(temp[0].equals("1")){
					ubase.setInvitID(temp[1]); //设置分享人的id
				}
			}
			if (wUser.openid != null) {
				dSource.rtn.setIsWx(Integer.valueOf(1));
				dSource.rtn.getInfo().setAccount(wUser.openid);
				ubase.setDisFollow(Integer
						.valueOf(dSource.doc.getDisFollow() != null ? dSource.doc.getDisFollow().intValue() : 0));
				ubase.setDisShare(
						Integer.valueOf(dSource.doc.getDisShare() != null ? dSource.doc.getDisShare().intValue() : 0));

				ubase.setuSex(wUser.sex);
				ubase.setNickname(wUser.nickname);
				String imgurl = StringUtils.replace(wUser.headimgurl, "/0", "/64");
				ubase.setuImg(imgurl);

				if (ubase.getDisFollow().intValue() == 1) {
					String token = MemDat.getWxToken(dSource.wx.getPid());
					if (token != null) {
						wUser = this.wxservice.getWxUserInfo(token, wUser.openid);
						ubase.setIsFollow(Integer.valueOf(wUser.subscribe));
					}
				}
				dSource.rtn.setUsebase(ubase);
			}
		}
		return ubase;
	}

	@Override
	public Object payVerification(DataConf dSource) throws Exception {
		String xml = wxservice.uintOrder(dSource);
		Map<String, String> result = WxLogic.parseXml(xml);
		WxResult wxres = new WxResult();
		wxres.setAppid(result.get("appid"));
		wxres.setResult_code(result.get("return_code"));
		wxres.setNonce_str(result.get("nonce_str"));
		wxres.setPrepay_id(result.get("prepay_id"));
		wxres.setTimeStamp(WxLogic.getTimeStamp());
		wxres.setTrade_type("MD5");
		wxres.setReturn_msg(dSource.wx.getShopId());
		WxLogic.launchOrder(wxres, dSource.wx.getPaykey());
		return wxres;
	}

	@Override
	public ReInfo shareVerification(Map<String, Object> param, DataConf dSource) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReInfo followVerification(Map<String, Object> param, DataConf dSource) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void callPay(Map<String, Object> param, DataConf dSource) {
		String xml = (String) param.get("dat");
		Map<String, String> result = null;
		try {
			result = WxLogic.parseXml(xml);
			if (result.get("return_code").equals("SUCCESS") && result.get("result_code").equals("SUCCESS")) {
				param.put("account", result.get("openid"));
				param.put("goodsOrder", result.get("transaction_id"));
				param.put("Amount", result.get("total_fee"));
				param.put("ownOrder", result.get("out_trade_no"));
				param.put("complateDate", result.get("time_end"));
				param.put("status", 1); // 支付成功
			}
			param.put("success", result.get("return_code"));
			return;
		} catch (Exception e) {
			log.warn(JsonTransfer.getJson(param), e); // 支付回调错误
		}
		param.put("fail", result.get("return_code"));
	}

	@Override
	public Object statslog(Map<String, Object> param, DataConf dSource) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getUserinfo(Map<String, Object> param, DataConf dSource) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPayInfo(Map<String, Object> param, DataConf dSource) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object other(Map<String, Object> param, DataConf dSource) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
