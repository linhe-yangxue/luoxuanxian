package com.ssmCore.rabbitMq;

import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.stereotype.Controller;

import com.ssmCore.tool.colligate.JsonTransfer;

@Controller
public class FastJsonMessageConverter extends AbstractMessageConverter {
	
	 private static Log log = LogFactory.getLog(FastJsonMessageConverter.class);
	 public static final String DEFAULT_CHARSET = "UTF-8";
	 private volatile String defaultCharset = DEFAULT_CHARSET;
	      
	 public FastJsonMessageConverter() {
	     super();
	 }
	      
	 public void setDefaultCharset(String defaultCharset) {
	     this.defaultCharset = (defaultCharset != null) ? defaultCharset
	                : DEFAULT_CHARSET;
	 }
	      
	 public Object fromMessage(Message message)
	            throws MessageConversionException {
	        return null;
	 }
	      
	@SuppressWarnings("unchecked")
	public <T> T fromMessage(Message message,T t) {
	        String json = "";
	        try {
	            json = new String(message.getBody(),"UTF-8");
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        }
	        return (T) JsonTransfer._In(json, t.getClass());
	}   
	      
	protected Message createMessage(Object objectToConvert,
	            MessageProperties messageProperties)
	            throws MessageConversionException {
	        byte[] bytes = null;
	        try {
	            String jsonString = JsonTransfer.getJson(objectToConvert);
	            bytes = jsonString.getBytes(this.defaultCharset);
	        } catch (UnsupportedEncodingException e) {
	            throw new MessageConversionException(
	                    "Failed to convert Message content", e);
	        } 
	        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
	        messageProperties.setContentEncoding(this.defaultCharset);
	        if (bytes != null) {
	            messageProperties.setContentLength(bytes.length);
	        }
	        return new Message(bytes, messageProperties);
	  
	    }

	public static Log getLog() {
		return log;
	}

	public static void setLog(Log log) {
		FastJsonMessageConverter.log = log;
	}
		
}
