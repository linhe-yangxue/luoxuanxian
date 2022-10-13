package com.ssmLogin.servlet;

import java.util.Map;

import com.ssmCore.constants.I_CoreErro;
import com.ssmCore.constants.ReInfo;
import com.ssmCore.jetty.BaseServlet;
import com.ssmCore.jetty.FunUrl;
import com.ssmLogin.constant.I_ModuleServlet;
import com.ssmLogin.defdata.facde.I_Order;
import com.ssmLogin.defdata.impl.UserOrderImpl;
import com.ssmShare.platform.DataConf;

public class PayServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	private I_Order order = UserOrderImpl.getInstance();

	public PayServlet() {
		super(PayServlet.class, new HttpPayPress());
	}

	/**
	 * 订单支付完成添加物品
	 * 
	 * @param param
	 * @return
	 */
	@FunUrl(value = I_ModuleServlet.PAY_USER)
	public Object payOrderCall(Map<String, Object> param, DataConf dSource) {
		try {
			return order.callPay(param, dSource);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			order = null;
		}
		return new ReInfo(I_CoreErro.ERRO_HTTP_PARAM, "订单回执异常！");
	}

}
