package com.jksdk.wx.impl;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jksdk.wx.WxOrder;
import com.jksdk.wx.WxParam;
import com.jksdk.wx.WxUser;
import com.jksdk.wx.constant.I_WxConstant;
import com.jksdk.wx.facde.I_WxService;
import com.ssmCore.tool.colligate.DateUtil;
import com.ssmCore.tool.colligate.HttpRequest;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmShare.entity.WxEntity;
import com.ssmShare.entity.WxPlatform;
import com.ssmShare.platform.DataConf;
import com.ssmShare.platform.MemDat;

@Service
public class WxServiceImpl implements I_WxService{

	private static final Logger log = LoggerFactory.getLogger(WxServiceImpl.class);
	@Override
	public String uintOrder(DataConf dSource) throws Exception {

		WxOrder dat = new WxOrder();
		dat.setAppid(dSource.wx.getAppid());
		dat.setMch_id(dSource.wx.getShopId());    //微信商户号
		Date date = (Date) dSource.order.get("createDate");
		dat.setTime_start(DateUtil.get_DefaultTime(date.getTime()));
		dat.setTime_expire(DateUtil.get_DefaultTime(date.getTime() + 600000));
		dat.setNonce_str(WxLogic.getNonceStr().toUpperCase());
		dat.setBody(dSource.order.get("goodsNum")+ "Diamond");
		dat.setSpbill_create_ip(dSource.ipdress);
		dat.setOut_trade_no((String) dSource.order.get("ownOrder"));
		Float money = (Float)dSource.order.get("amount");
		dat.setTotal_fee(money.intValue() + "");
		dat.setTrade_type(dSource.paytype);
		dat.setOpenid((String)dSource.order.get("account"));
		dat.setNotify_url(dSource.wx.getCallPay() + (String)dSource.order.get("pid") 
				+ "/" + dSource.gid + "/" + dSource.order.get("zid") + "/pay");
		return WxLogic.unifiedOrder(dat, dSource.wx.getPaykey());
	}

	@Override
	public String getToken(WxEntity wx,String pid) throws IOException {
		String url = I_WxConstant.ACCESSTOKEN_URL + "&appid=" + wx.appid;
		url += "&secret=" + wx.secret;
		String result = HttpRequest.GetFunction(url);
		WxParam param = JsonTransfer._In(result, WxParam.class);
		if(param.errcode!= null && param.errcode>0){
			log.warn("获取微信访问token错误：错误id：", param.errcode + "消息：" + param.errmsg);
		}else{
			wx.access_token = param.access_token;
			MemDat.setWxToken(pid,wx.access_token,param.expires_in.intValue()-60);
			return param.access_token;
		}
		return null;
	}

	@Override
	public void getJsTicket(WxEntity wx,String pid) throws IOException {
		  String url = I_WxConstant.JSTICKET + wx.access_token + "&type=jsapi";  
          String result = HttpRequest.GetFunction(url);
          WxParam param = JsonTransfer._In(result, WxParam.class);
          if(param.errcode!=null && param.errcode>0){
        	  log.warn("获取微信访问ticket错误：错误id：", param.errcode + "消息：" + param.errmsg);
          }else{
        	  wx.jsapi_ticket = param.ticket;
        	  MemDat.setWxTicket(pid,param.ticket,param.expires_in.intValue()-60);
          }
	}

	@Override
	public WxUser getWxUserInfo(String token,String account) throws IOException {
		String url = I_WxConstant.WXUSERINFO_URL + "access_token=" + token;
		url += "&openid=" + account;
		url += I_WxConstant.LANGUAGE;
		String result = HttpRequest.GetFunction(url);
		WxUser userInfo = JsonTransfer._In(result, WxUser.class);
		return userInfo;
	}
	
	public WxUser getUserWeb(String code,WxPlatform wx) throws IOException{
		String url = I_WxConstant.ACCESSTOKEN_URL_WEB + "appid=" + wx.getAppid();
		url += "&secret=" + wx.getSecret();
		url += "&code=" + code;
		url += I_WxConstant.GANTTYPE;
		String result = HttpRequest.GetFunction(url);
		WxParam param = JsonTransfer._In(result, WxParam.class);
		if(param.errcode!=null){
			log.warn("获取用户openid和token信息错误！"+ url);
			return null;
		}
		String urluser =I_WxConstant.WXUSERINFO_URL_WEB + "access_token=" + param.access_token;
		urluser += "&openid=" + param.openid;
		urluser += I_WxConstant.LANGUAGE;
		String userStr = HttpRequest.GetFunction(urluser);
		WxUser userInfo = JsonTransfer._In(userStr, WxUser.class);
		if(userInfo.errcode!=null){
			log.warn("获取用户信息错误！");
			return null;
		}
		return userInfo;
	}
}
