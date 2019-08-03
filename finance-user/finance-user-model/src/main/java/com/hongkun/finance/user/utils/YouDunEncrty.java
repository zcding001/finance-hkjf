package com.hongkun.finance.user.utils;

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * 有盾工具类
 * @date 2018-04-25 18:01:05
 * @author liangbin
 *	
 */
public class YouDunEncrty{
	
	 private final static Log log = LogFactory.getLog("YouDunEncrty");
	 private static String fformatStr = "/dsp-front/4.1/dsp-front/default/pubkey/%s/" +
	          "product_code/%s/out_order_id/%s/signature/%s";
	 
	  public static String apiCall(String url,String pubkey,String secretkey,String 
	          serviceCode, String outOrderId,Map<String, String> parameter) throws Exception{
	      if (parameter == null || parameter.isEmpty())
	          throw new Exception("error ! the parameter Map can't be null.");

	      StringBuffer bodySb = new StringBuffer("{");
	      for (Map.Entry<String, String> entry : parameter.entrySet()) {
	          bodySb.append("'").append(entry.getKey()).append("':'").append(entry.getValue()).append("',");
	      }
	      String bodyStr = bodySb.substring(0, bodySb.length() - 1) + "}";
	      String signature = md5(bodyStr + "|" + secretkey);
	      url += String.format(fformatStr, pubkey, serviceCode, System.currentTimeMillis(), signature);
	      log.info("requestUrl=>" + url);
	      log.info("request parameter body=>" + bodyStr);
	      HttpResponse r = makePostRequest(url, bodyStr);
	      return EntityUtils.toString(r.getEntity());
	  }

	  private static final CloseableHttpClient client = HttpClientBuilder.create().build();
	  private static HttpResponse makePostRequest(String uri, String jsonData)
	          throws ClientProtocolException, IOException {
	      HttpPost httpPost = new HttpPost(URIUtil.encodeQuery(uri, "utf-8"));
	      httpPost.setHeader("Connection","close");
	      httpPost.setEntity(new StringEntity(jsonData, "UTF-8"));
	      httpPost.setHeader("Accept", "application/json");
	      httpPost.setHeader("Content-type", "application/json; charset=utf-8");
	      return client.execute(httpPost);
	  }
	  private static String md5(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
	      MessageDigest md = MessageDigest.getInstance("MD5");
	      md.update(data.toString().getBytes("utf-8"));
	      return bytesToHex(md.digest());
	  }

	  private static String bytesToHex(byte[] ch) {
	      StringBuffer ret = new StringBuffer("");
	      for (int i = 0; i < ch.length; i++)
	          ret.append(byteToHex(ch[i]));
	      return ret.toString();
	  }

	  /**
	   *字节转换为16进制字符串
	   */
	  private static String byteToHex(byte ch) {
	      String str[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
	      return str[ch >> 4 & 0xF] + str[ch & 0xF];
	  }
}
