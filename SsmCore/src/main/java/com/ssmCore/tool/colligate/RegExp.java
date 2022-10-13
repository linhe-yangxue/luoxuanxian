package com.ssmCore.tool.colligate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class RegExp {

	/**
	 * 长度在6~12之间，只能包含字符、数字和下划线。
	 * @param str
	 * @return
	 */
	public static boolean isAlphabetic(String str){
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{5,11}$");
		Matcher m = pattern.matcher(str);
		return m.lookingAt();
	}
	
	/**
	 * 验证Email地址
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str){
		Pattern pattern = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
		Matcher m = pattern.matcher(str);
		return m.lookingAt();
	}
	
	/**
	 * 验证uuid是否正确
	 * @param str
	 * @return
	 */
	public static boolean isUuid(String str){
		Pattern pattern = Pattern.compile("^([A-Za-z0-9]{8}-)+([A-Za-z0-9]{4}-)+([A-Za-z0-9]{4}-)+([A-Za-z0-9]{4}-)+([A-Za-z0-9]{12})+$");
		Matcher m = pattern.matcher(str);
		return m.lookingAt();
	}
	
	/**
	 * 检验是否是数字
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str){
		Pattern pattern = Pattern.compile("^[0-9]*$");
		Matcher m = pattern.matcher(str);
		return m.lookingAt();
	}
	
	public static boolean isChinese(String str){
		Pattern pattern = Pattern.compile("[\\u4e00-\\u9fbb]");
		Matcher m = pattern.matcher(str);
		return m.lookingAt();
	}
	
	/**
	 * 过滤特殊字符
	 * @param str
	 * @return
	 */
	public static String specialWord(String str) {
		String regEx=("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]");
		Pattern p = Pattern.compile(regEx);  
		Matcher m = p.matcher(str);  
		return m.replaceAll("*");  
	}
  
    public static String CUUID(){
    	return java.util.UUID.randomUUID().toString();
    }
    
    public static String UUID16(){
    	 String key = StringUtils.replace(CUUID(), "-","");
    	 return StringUtils.substring(key, 16, key.length());
    }
        
}


