package com.ssmCore.tool.colligate;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import sun.misc.BASE64Encoder;

public class HttpRequest {

	/**
	 * http get
	 * 
	 * @param id
	 * @param token
	 * @return
	 * @throws IOException
	 */
	public static String GetFunction(String url) throws IOException {
		String result = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(5000).build();
		try {
			HttpGet httpget = new HttpGet(url);
			httpget.setConfig(requestConfig);
			CloseableHttpResponse response = httpclient.execute(httpget);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, "UTF-8");
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			httpclient.close();
		}
	}

	/**
	 * http get 图片
	 * 
	 * @param id
	 * @param token
	 * @return
	 * @throws IOException
	 */
	public static String encodeImgage(URL imageUrl) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		ByteArrayOutputStream outputStream = null;
		try {
			BufferedImage bufferedImage = ImageIO.read(imageUrl);
			outputStream = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "jpg", outputStream);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Arrays.toString(outputStream.toByteArray());// 返回Base64编码过的字节数组字符串
	}

	/**
	 * http get 图片
	 * 
	 * @param id
	 * @param token
	 * @return
	 * @throws IOException
	 */
	public static String encodeImgageToBase64(URL imageUrl) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		ByteArrayOutputStream outputStream = null;
		try {
			BufferedImage bufferedImage = ImageIO.read(imageUrl);
			outputStream = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "jpg", outputStream);
			// 对字节数组Base64编码
			BASE64Encoder encoder = new BASE64Encoder();
			return "data:image/jpg;base64," + encoder.encode(outputStream.toByteArray());// 返回Base64编码过的字节数组字符串
		} catch (Exception e) {
			System.out.println(imageUrl.toString() + "图片连接访问错误！");
		}
		return null;
	}

	/**
	 * http post
	 * 
	 * @param url
	 * @param param
	 * @return
	 * @throws IOException
	 */
	public static String PostFunction(String url, String param) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		formParams.add(new BasicNameValuePair("param", param));
		try {
			HttpPost httpost = new HttpPost(url);
			httpost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
			HttpResponse httpResponse = httpclient.execute(httpost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				return EntityUtils.toString(entity, "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpclient.close();
		}
		return null;
	}

	/**
	 * http post
	 * 
	 * @param url
	 * @param param
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public static String PostFunction(String url, Map<String, String> param) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		Iterator entries = param.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			formParams.add(new BasicNameValuePair(key, value));
		}
		try {
			HttpPost httpost = new HttpPost(url);
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

	public static String PostXML(String url, String param) throws Exception {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httppost = new HttpPost(url);
			StringEntity myEntity = new StringEntity(param);
			httppost.addHeader("Content-Type", "text/xml");
			httppost.setEntity(myEntity);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity resEntity = response.getEntity();
			return EntityUtils.toString(resEntity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpclient.close();
		}
		return null;
	}
}
