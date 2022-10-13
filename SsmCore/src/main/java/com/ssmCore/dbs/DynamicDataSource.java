package com.ssmCore.dbs;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource{
		
	    protected Object determineCurrentLookupKey() {  
	        return CustomerContextHolder.getCustomerType();  
	    }  
	  
}
