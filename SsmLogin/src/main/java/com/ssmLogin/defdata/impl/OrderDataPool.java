package com.ssmLogin.defdata.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmLogin.defdata.OrderPress;
import com.ssmLogin.defdata.entity.Paymement;
import com.ssmShare.order.GmItem;
import com.ssmShare.platform.DataConf;

@Service
@Scope("prototype")
public class OrderDataPool implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(OrderDataPool.class);
	public static final BlockingQueue<DataConf> oDate = new LinkedBlockingQueue<DataConf>();
	private ExecutorService order;

	public void start() {
		order = Executors.newSingleThreadExecutor();
		order.execute(this);
	}

	@PreDestroy
	public void stop() {
		if (order != null)
			order.shutdown();
	}

	@Override
	public void run() {
		while (true) { // 每次更新用户基本信息
			DataConf dat = null;
			Paymement paymement = null;
			OrderPress orderpress = null;
			try {
				dat = oDate.take();
				orderpress = OrderPress.getInstance();
				paymement = orderpress.dbOrder(dat); // 充值成功记录
				if (paymement != null) {
					GmItem item = orderpress.createGmitem(paymement);// 创建发送信息
					orderpress.sendGame(item,paymement);
					orderpress.updateSend(paymement);
					orderpress.PayMsgSend(paymement);
				}
			} catch (Exception e) {
				log.warn("订单存储错误：" + JsonTransfer.getJson(dat.rtn.getOrder()), e);
			} finally {
				dat.rtn = null;
				dat = null;
				orderpress = null;
			}
		}
	}
}
