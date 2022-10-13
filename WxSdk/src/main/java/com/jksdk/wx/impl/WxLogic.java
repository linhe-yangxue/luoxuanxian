package com.jksdk.wx.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.jksdk.wx.WxOrder;
import com.jksdk.wx.WxResult;
import com.jksdk.wx.XStreamCDATA;
import com.jksdk.wx.constant.I_WxConstant;
import com.ssmCore.tool.colligate.Encryption;
import com.ssmCore.tool.colligate.HttpRequest;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NameCoder;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.XppDomDriver;

public class WxLogic {
	
	public static XStream getXStream() {
        final NameCoder nameCoder = new NoNameCoder();
        XStream xStream = new XStream(new XppDomDriver(nameCoder){
        	public HierarchicalStreamWriter createWriter(Writer out) {
              return new CompactWriter(out, nameCoder){
                  boolean cdata = false;
                  
				@Override
                  public void startNode(String name, @SuppressWarnings("rawtypes") Class clazz) {
                      super.startNode(name, clazz);
                      // 所有的字符串都加上CDATA标识
                      cdata = "String".equals(clazz.getSimpleName());
                  }
                  @Override
                  protected void writeText(QuickWriter writer, String text) {
                      if (cdata) {
                          writer.write("<![CDATA[");
                          writer.write(text);
                          writer.write("]]>");
                      } else {
                          writer.write(text);
                      }
                  }
              };
          }
        });             
        return xStream;
    }
	
	public static String unifiedOrder(WxOrder data,String key){    
		//统一下单支付  
		String returnXml = null;    
		try { //生成sign签名      
			SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
			parameters.put("appid", data.getAppid()); 
			parameters.put("mch_id", data.getMch_id());
			parameters.put("time_start", data.getTime_start());
			parameters.put("time_expire",data.getTime_expire());
			parameters.put("nonce_str", data.getNonce_str()); 
			parameters.put("body", data.getBody());         
			parameters.put("out_trade_no", data.getOut_trade_no());
			parameters.put("spbill_create_ip", data.getSpbill_create_ip());
			parameters.put("total_fee", data.getTotal_fee());      
			parameters.put("trade_type", data.getTrade_type());          
			parameters.put("openid", data.getOpenid()); 
			parameters.put("notify_url", data.getNotify_url());
			data.setSign(createSign(parameters,key));
			XStream xs = getXStream();
			xs.alias("xml", WxOrder.class);      
			String xml = xs.toXML(data);
			returnXml = HttpRequest.PostXML(I_WxConstant.UNIFIEDORDER, xml);
		} catch (Exception e) {     
				e.printStackTrace();    
		}     
		return returnXml;  
	}
	
	public static void launchOrder(WxResult data,String key) throws Exception{        
		SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
		parameters.put("appId", data.getAppid()); 
		parameters.put("timeStamp", data.getTimeStamp());
		parameters.put("nonceStr", data.getNonce_str());
		parameters.put("package",data.getPrepay_id());
		parameters.put("signType", "MD5");
		data.setSign(createSign(parameters,key));
	}
	
	 @SuppressWarnings("rawtypes") 
	 public static String createSign(SortedMap<Object,Object> parameters,String key) throws Exception{     
		 StringBuffer sb = new StringBuffer(); 
		 Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）     
		 Iterator it = es.iterator();     
		 while(it.hasNext()) {      
			 Map.Entry entry = (Map.Entry)it.next();       
			 String k = (String)entry.getKey();       
			 Object v = entry.getValue();
			 if(null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {         
				 sb.append(k + "=" + v + "&");       
			 }     
		 }
		sb.append("key=" + key);
		String sign = Encryption.Encode(sb.toString(), Encryption.MD5).toUpperCase();    
		return sign;   
	 } 
	 
	 public static String getNonceStr() throws Exception{    
		 Random random = new Random();    
		 return Encryption.Encode(String.valueOf(random.nextInt(10000)),Encryption.MD5); 
	 }
	 
	public static String getTimeStamp() {    
		 return String.valueOf(System.currentTimeMillis() / 1000);  
	}

	@SuppressWarnings("unused")
	private static boolean needCDATA(Class<?> targetClass, String fieldAlias){
	    boolean cdata = false;
	    //first, scan self
	    cdata = existsCDATA(targetClass, fieldAlias);
	    if(cdata) return cdata;
	    //if cdata is false, scan supperClass until java.lang.Object
	    Class<?> superClass = targetClass.getSuperclass();
	    while(!superClass.equals(Object.class)){
	      cdata = existsCDATA(superClass, fieldAlias);
	      if(cdata) return cdata;
	      superClass = superClass.getClass().getSuperclass();
	    }
	    return false;
	  }

	private static boolean existsCDATA(Class<?> clazz, String fieldAlias){
	    Field[] fields = clazz.getDeclaredFields();
	    for (Field field : fields) {
	      if(field.getAnnotation(XStreamCDATA.class) != null ){
	        XStreamAlias xStreamAlias = field.getAnnotation(XStreamAlias.class);
	        //2. exists XStreamAlias
	        if(null != xStreamAlias){
	          if(fieldAlias.equals(xStreamAlias.value()))//matched
	            return true;
	        }else{// not exists XStreamAlias
	          if(fieldAlias.equals(field.getName()))
	            return true;
	        }
	      }
	    }
	    return false;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(String xml) throws Exception {
    
        Map<String, String> map = new HashMap<String, String>();
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        Element root = document.getRootElement();
		List<Element> elementList = root.elements();
        for (Element e : elementList)
            map.put(e.getName(), e.getText());
 
        inputStream.close();
        inputStream = null;
        return map;
	}
}
