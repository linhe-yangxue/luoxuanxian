package com.ssmCore.dbs;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Controller;

@Controller
@Aspect
public class CustomerContextHolder {
	
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>(); 

    public static void setCustomerType(String customerType) {  
        contextHolder.set(customerType);
    }  
 
    public static String getCustomerType() {  
        return contextHolder.get();  
    }  
  
    public static void clearCustomerType() {  
        contextHolder.remove();  
    }  
}
