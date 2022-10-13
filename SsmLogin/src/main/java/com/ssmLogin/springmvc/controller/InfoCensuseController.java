package com.ssmLogin.springmvc.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssmCore.constants.HttpWrite;
import com.ssmCore.constants.ReInfo;
import com.ssmLogin.constant.I_Error_Login;
import com.ssmLogin.defdata.OrderPress;
import com.ssmLogin.defdata.entity.Censuse;
import com.ssmLogin.defdata.entity.Paymement;
import com.ssmLogin.defdata.impl.CensusPlatImpl;
import com.ssmLogin.defdata.impl.CensuseImpl;
import com.ssmLogin.defdata.impl.PaymementImpl;

@Controller
@Scope("prototype")
@RequestMapping("censuse")
public class InfoCensuseController {

	private static final Logger log = LoggerFactory.getLogger(InfoCensuseController.class);
//	private DataConf dSource = DataConf.getInstance();

	@RequestMapping(value = "total")
	public void getInfo(@RequestParam("gid") String gid
			,@RequestParam("pid") String pid,@RequestParam("zd") Integer zid, HttpServletResponse response) {
		try {
			Object obj = CensusPlatImpl.getInstance().censusplatmsg(gid, pid,zid);
			HttpWrite.getInstance().writeMsg(response, new ReInfo(obj));
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
			HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR));
		}
	}

	@RequestMapping(value = "day")
	public void getInfoDay(@RequestParam("gid") String gid, @RequestParam("pid") String pid
			,@RequestParam("zd") Integer zid, @RequestParam("starTime") String starTime, @RequestParam("endTime") String endTime, HttpServletResponse response) {
		try {
			List<Censuse> ls = null;
			
			if(starTime!=null && !starTime.trim().isEmpty() && endTime!=null && !endTime.trim().isEmpty())
				ls= CensuseImpl.instance().findList(gid, pid,zid,starTime, endTime);
			//Object obj = CensusPlatImpl.getInstance().dayCensus(gid, pid,zid,starTime, endTime);
			if(ls!=null)
				HttpWrite.getInstance().writeMsg(response, new ReInfo(ls));
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
			HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR));
		}
	}

	/**
	 * 获取发送失败列表
	 * 
	 * @param response
	 */
	@RequestMapping(value = "getFailSign")
	public void getFailSign(HttpServletResponse response) {
		try {
			List<Paymement> list = PaymementImpl.getInstance().findSendFail();
			HttpWrite.getInstance().writeMsg(response, new ReInfo(list));
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
			HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR));
		}
	}

	/**
	 * 发送失败
	 * 
	 * @param response
	 */
	@RequestMapping(value = "reSendFailSign")
	public void reSendFailSign(@RequestParam("orderId") String order, HttpServletResponse response) {
		try {
			Paymement paymement = PaymementImpl.getInstance().find(order);
			if (paymement != null) {
				if (paymement.getSendStatus() != null && paymement.getSendStatus().intValue() == 0) {
					OrderPress orderSend = OrderPress.getInstance();
					orderSend.sendGmitem(paymement);
					orderSend.PayMsgSend(paymement);
					if (paymement.getSendStatus() == 1) {
						HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.SUCCESS));
					} else {
						HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR, "发送失败！"));
					}
				} else {
					HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR, "订单已经发送！"));
				}
			} else {
				HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR, "未查询到订单！"));
			}
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
			HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR));
		}
	}

}
