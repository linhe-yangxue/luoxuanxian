package com.ssmCore.ssh;

import com.jcraft.jsch.UserInfo;

public class MyUserInfo implements UserInfo {  
    
    @Override  
    public String getPassphrase() {  
        return null;  
    }  
  
    @Override  
    public String getPassword() {  
        return null;  
    }  
  
    @Override  
    public boolean promptPassphrase(final String arg0) {  
        return false;  
    }  
  
    @Override  
    public boolean promptPassword(final String arg0) {  
        return false;  
    }  
  
    @Override  
    public boolean promptYesNo(final String arg0) {  
        if (arg0.contains("The authenticity of host")) {  
            return true;  
        }  
        return false;  
    }  
  
    @Override  
    public void showMessage(final String arg0) {  
        System.out.println("MyUserInfo.showMessage()");  
    }

}  