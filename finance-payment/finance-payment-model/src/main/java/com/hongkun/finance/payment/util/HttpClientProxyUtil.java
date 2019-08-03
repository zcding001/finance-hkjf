package com.hongkun.finance.payment.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpClientProxyUtil {
	private CloseableHttpClient httpclient;
	private static HttpClientMessageSender instance;
	// utf-8字符编码
	public static final String CHARSET_UTF_8 = "utf-8";

	public static HttpClientMessageSender getInstance() {
		if (instance == null) {
			instance = new HttpClientMessageSender();
		}
		return instance;
	}

	/**
	 * 构建请求配置信息 超时时间什么的
	 */
	private static RequestConfig requestConfig() {
		// 根据默认超时限制初始化requestConfig
		int socketTimeout = 10000;
		int connectTimeout = 10000;
		int connectionRequestTimeout = 10000;
		RequestConfig config = RequestConfig.custom().setConnectTimeout(connectTimeout) // 创建连接的最长时间
				.setConnectionRequestTimeout(connectionRequestTimeout) // 从连接池中获取到连接的最长时间
				.setSocketTimeout(socketTimeout) // 数据传输的最长时间
				.setStaleConnectionCheckEnabled(true) // 提交请求前测试连接是否可用
				.build();
		return config;
	}

	public String doPostProxyString(String url, String param, Map<String, String> requestHead) {
		// 创建Httpclient对象
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(requestConfig());
			if (null != requestHead) {
				for (Map.Entry<String, String> entry : requestHead.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					httpPost.addHeader(key, value);
				}
			}
			// 创建参数列表
			StringEntity stringEntity = new StringEntity(param, "UTF-8");
			stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING, "UTF-8"));
			// 设置请求主体
			httpPost.setEntity(stringEntity);
			// 执行http请求
			response = httpclient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				resultString = EntityUtils.toString(response.getEntity(), CHARSET_UTF_8);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
		}
		return resultString;
	}

	public String doGetString(String url, String param, Map<String, String> requestHead) {
		String result = "";
		CloseableHttpResponse response = null;
		try {
			URI uri = new URIBuilder(url + "?" + param).build();
			// 创建http GET请求
			HttpGet httpGet = new HttpGet(uri);
			httpGet.setConfig(requestConfig());
			if (null != requestHead) {
				for (Map.Entry<String, String> entry : requestHead.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					httpGet.addHeader(key, value);
				}
			}
			response = httpclient.execute(httpGet);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity(), CHARSET_UTF_8);
			}
			result = decodeData(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 根据实际需要决定是否需要解码
	 */
	static String decodeData(String base64Data) {
		String str = "";
		if (base64Data == null || base64Data.equals("")) {
			str = "";
		}
		try {
			String e = new String(Base64.decodeBase64(base64Data.getBytes(CHARSET_UTF_8)), CHARSET_UTF_8);
			return e;
		} catch (UnsupportedEncodingException var5) {
		}
		return str;
	}
}
