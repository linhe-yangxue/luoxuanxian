package com.ssmLogin.servlet;

import java.util.HashMap;
import java.util.Map;

import com.ssmCore.constants.ReInfo;
import com.ssmCore.jetty.BaseServlet;
import com.ssmCore.jetty.FunUrl;
import com.ssmCore.tool.colligate.JsonTransfer;
import com.ssmCore.tool.colligate.RegExp;
import com.ssmCore.tool.spring.SpringContextUtil;
import com.ssmLogin.constant.I_Error_Login;
import com.ssmLogin.constant.I_ModuleServlet;
import com.ssmLogin.defdata.facde.I_Order;
import com.ssmLogin.defdata.impl.LogindataImpl;
import com.ssmLogin.defdata.impl.PlatformInfoImpl;
import com.ssmLogin.defdata.impl.UserOrderImpl;
import com.ssmShare.constants.E_PlateInfo;
import com.ssmShare.constants.LoginConstants;
import com.ssmShare.facde.I_Platform;
import com.ssmShare.platform.DataConf;

public class OrderServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	private DataConf dSource;
	private I_Order order;
	I_Platform platform; 

	public OrderServlet() {
		super(OrderServlet.class, new HttpParamsPress());
	}

	/**
	 * 创建订单
	 * 
	 * @param param
	 * @return
	 */
	@FunUrl(value = I_ModuleServlet.CREATE_ORDER)
	public Object CreateOrder(Map<String, Object> param) {
		try {
			String gid = (String) param.get("gid");
			String pid = (String) param.get("pid");
			String zid = (String) param.get("zid");
			String uid = (String) param.get("uid");
			String guid = (String) param.get("guid");
			String itemid = (String) param.get("itemId");
		
			if(gid != null && pid != null && itemid!=null
				&& zid != null && uid!=null && guid!=null) {
				
				if (dSource == null)
					dSource = DataConf.getInstance();
				
				dSource.load(gid, pid, E_PlateInfo.ALL.getType());
				if (dSource.doc == null) {// mem缓存失效加载
					PlatformInfoImpl.getInstance().initDB(gid);
					dSource.load(gid,pid, E_PlateInfo.ALL.getType());
				}
				dSource.ipdress = (String) param.get("ip");
				dSource.paytype = (String) param.get("paytype");
				Integer phone = (Integer) param.get("phone");
				if (order == null)
					order = UserOrderImpl.getInstance();
				if (RegExp.isNumber(zid) && RegExp.isNumber(guid) ) {
					order.CreateOrder(dSource, Integer.parseInt(zid),Long.parseLong(guid),
							uid,itemid,phone);
				}
				if (dSource.order != null) {
					if (dSource.doc.getIsWx() == 0) {// 平台sdk选择
						platform = (I_Platform) SpringContextUtil
								.getBean(LoginConstants.SDK + dSource.doc.getPid() + LoginConstants.IMPL); // 实例化平台接口
					} else {
						platform = (I_Platform) SpringContextUtil
								.getBean(LoginConstants.SDK + "Wx" + LoginConstants.IMPL); // 实例化平台接口
					}


					if (platform != null) {
						dSource.params = param;
						return platform.payVerification(dSource);
					}
				}


			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dSource = null;
			platform = null;
			order = null;
		}
		return new ReInfo(I_Error_Login.ERROR,JsonTransfer.getJson(param));
	}

}
