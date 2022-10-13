package com.ssmLogin.servlet;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssmCore.constants.ReInfo;
import com.ssmCore.jetty.BaseServlet;
import com.ssmCore.jetty.FunUrl;
import com.ssmLogin.constant.I_Error_Login;
import com.ssmLogin.constant.I_ModuleServlet;

/**
 * 错误信息
 * 
 * @author Only
 *
 */
public class ErrorLogServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(ErrorLogServlet.class);

	public ErrorLogServlet() {
		super(ErrorLogServlet.class, new HttpParamsPress());
	}

	@FunUrl(value = I_ModuleServlet.ERROR_LOG)
	public Object codeCheck(Map<String, Object> param) {
		log.error("clientLog:" + param.get("info"));
		return new ReInfo(I_Error_Login.SUCCESS);
	}
}
