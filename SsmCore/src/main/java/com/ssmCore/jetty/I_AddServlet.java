package com.ssmCore.jetty;

import java.io.IOException;

import org.eclipse.jetty.server.Server;

public interface I_AddServlet {
	/**
	 *添加handler
	 * @param server
	 * @throws IOException 
	 */
	public void addServletHandle(Server server) throws IOException;
}