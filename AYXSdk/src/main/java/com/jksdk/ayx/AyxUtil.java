package com.jksdk.ayx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class AyxUtil {

	public static List<String> paramConcat(Map<String, String> requestData) {
		List<String> params = new ArrayList<String>();

		StringBuffer keySort = new StringBuffer();
		StringBuffer values = new StringBuffer();
		if (requestData != null && requestData.size() > 0) {
			for (Entry<String, String> entry : requestData.entrySet()) {
				String name = entry.getKey() == null ? "" : entry.getKey();
				String value = entry.getValue() == null ? "" : entry.getValue();
				keySort.append(name).append("&");
				values.append(value);
			}
			keySort.deleteCharAt(keySort.lastIndexOf("&"));
		}

		params.add(keySort.toString());
		params.add(values.toString());
		return params;
	}

	public static String paramToString(Map<String, String> requestData) {
		String param = "";
		if (requestData != null && requestData.size() > 0) {
			for (Entry<String, String> entry : requestData.entrySet()) {
				String name = entry.getKey() == null ? "" : entry.getKey();
				String value = entry.getValue() == null ? "" : entry.getValue();
				param += name + "=" + value + "&";
			}
		}
		param = param.substring(0, param.length() - 1);
		return param;
	}

	public static String PostFunction(String url, Map<String, String> param) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		Iterator<Map.Entry<String, String>> entries = param.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) entries.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			formParams.add(new BasicNameValuePair(key, value));
		}
		try {
			HttpPost httpost = new HttpPost(url);
			httpost.setHeader("Content-Type", "application/x-www-form-urlencoded; text/html; charset=UTF-8");
			httpost.setHeader("User-Agent", "Mozilla/4.0");
			httpost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
			HttpResponse httpResponse = httpclient.execute(httpost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				return EntityUtils.toString(entity, "UTF-8");
				// return EntityUtils.toByteArray(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpclient.close();
		}
		return null;
	}
}
