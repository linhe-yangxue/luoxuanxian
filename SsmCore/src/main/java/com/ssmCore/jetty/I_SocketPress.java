package com.ssmCore.jetty;

public interface I_SocketPress {

	void recive(EventSocket event, String json);
	
	void sendmsg(EventSocket event ,Object obj);

}
