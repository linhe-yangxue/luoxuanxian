package com.ssmCore.ssh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SSHCommandExecutor {
	
	private static final Logger log = LoggerFactory.getLogger(SSHCommandExecutor.class);
	public static final String SERVICE="service ";
	public static final String STARTUP="./startup.sh ";
	public static final String START=" start";
	public static final String RESTART=" restart";	
	public static final String STOP=" stop";
	
	private static String ipAddress;
	private static String username;
	private static String password;
	private static Integer port;
 
    public static void setConnectMsg(final String Address, final String name, final String pass,final Integer pt) {  
         ipAddress = Address;
         username = name;
         password = pass;
         port = pt;
    }  
   
    public static String execute(final String command) {  
        try {   
            JSch jsch = new JSch();
            MyUserInfo userInfo = new MyUserInfo();  
            Session session = jsch.getSession(username, ipAddress, port);  
            session.setPassword(password);  
            session.setUserInfo(userInfo);  
            session.connect();
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(
            (channel.getInputStream())));
            channel.connect();
            StringBuffer sb = new StringBuffer();
            
	        try {      
	            Thread.sleep(1000);
	            while (fromServer.ready()) {
	            	String tt = fromServer.readLine();
	            	sb.append(tt+'\n');
	            }
	            return sb.toString();
	         } catch (Exception e) {      
	                e.printStackTrace();      
	         } finally {      
	               try {      
	            	   fromServer.close();
	                } catch (IOException e) {      
	                    e.printStackTrace();      
	                }
	               log.info(sb.toString());
	               channel.disconnect();
	               session.disconnect();
	         }  
	
        }catch (Exception e) {  
            e.printStackTrace();  
        }
		return "命令执行异常！";  
    }    
    
}
