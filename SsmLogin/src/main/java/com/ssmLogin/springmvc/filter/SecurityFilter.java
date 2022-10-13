package com.ssmLogin.springmvc.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import com.ssmCore.constants.HttpWrite;
import com.ssmCore.constants.ReInfo;
import com.ssmLogin.constant.I_Error_Login;
import com.ssmLogin.defdata.AdminUser;

@Controller
public class SecurityFilter extends HttpServlet implements Filter {
	private static final long serialVersionUID = 1L;

	private @Value("{$}") String filter;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		HttpSession session = request.getSession(true);
		AdminUser user_role = (AdminUser) session.getAttribute("adminuser");
		String url = request.getRequestURI();
		if (url.indexOf("login.me") < 0 && url.indexOf("updateJson") < 0) {
			if (user_role == null) {
				HttpWrite.getInstance().writeMsg(response, new ReInfo(I_Error_Login.ERROR_ADMIN_NOT_LOGIN));
				return;
			}
		}
		arg2.doFilter(arg0, arg1);
	}

}
