package com.ssmLogin.servlet;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssmCore.constants.ReInfo;
import com.ssmCore.jetty.BaseServlet;
import com.ssmCore.jetty.FunUrl;
import com.ssmCore.tool.colligate.RegExp;
import com.ssmLogin.constant.I_Error_Login;
import com.ssmLogin.constant.I_ModuleServlet;
import com.ssmLogin.springmvc.facde.impl.ActiveCodeImpl;

public class ActiveCodeServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(ActiveCodeServlet.class);

	public ActiveCodeServlet() {
		super(ActiveCodeServlet.class, new HttpParamsPress());
	}

	@FunUrl(value = I_ModuleServlet.CODE_AUTHORITY)
	public Object codeCheck(Map<String, Object> param) {
		try {
			String gid = (String) param.get("g_id");
			String pid = (String) param.get("p_id");
			String zid = (String) param.get("zid");
			String uid = (String) param.get("uid");
			String code = (String) param.get("cdkey");

			if (gid != null && pid != null && zid != null && uid != null && code != null) {
				if (RegExp.isNumber(zid)) {
					return ActiveCodeImpl.getInstance().grantItem(gid, pid, Integer.parseInt(zid), uid, code);
				}
			}

		} catch (Exception e) {
			log.warn(I_Error_Login.ERRO_USER_MODFIY + "修改信息错误！", e);
			e.printStackTrace();
		}
		return new ReInfo(I_Error_Login.ERRO_ACTIVE_CODE, "激活码检测异常！");
	}
}
