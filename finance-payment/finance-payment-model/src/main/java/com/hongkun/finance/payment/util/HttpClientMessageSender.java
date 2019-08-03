package com.hongkun.finance.payment.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * (1) 创建HttpClient对象。
 * (2)创建请求方法的实例，并指定请求URL。如果需要发送GET请求，创建HttpGet对象；如果需要发送POST请求，创建HttpPost对象。 (3)
 * 如果需要发送请求参数，可调用HttpGet、HttpPost共同的setParams(HetpParams
 * params)方法来添加请求参数；对于HttpPost对象而言，也可调用setEntity(HttpEntity entity)方法来设置请求参数。
 * (4) 调用HttpClient对象的execute(HttpUriRequest request)发送请求，该方法返回一个HttpResponse。
 * (5) 调用HttpResponse的getAllHeaders()、getHeaders(String
 * name)等方法可获取服务器的响应头；调用HttpResponse的getEntity()方法可获取HttpEntity对象，该对象包装了服务器
 * 的响应内容。程序可通过该对象获取服务器的响应内容。 (6) 释放连接。无论执行方法是否成功，都必须释放连接
 * 
 * @author yanbinghuang
 *
 */
public class HttpClientMessageSender {
	private static final Logger logger = LoggerFactory.getLogger(HttpClientMessageSender.class);
	// HTTP内容类型。相当于form表单的形式，提交数据
	public static final String CONTENT_TYPE_JSON_URL = "application/json;charset=utf-8";

	// utf-8字符编码
	public static final String CHARSET_UTF_8 = "utf-8";

	// 连接管理器
	private static PoolingHttpClientConnectionManager pool;

	// 请求配置
	private static RequestConfig requestConfig;

	static {

		try {
			SSLContextBuilder builder = new SSLContextBuilder();
			builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
			// 配置同时支持 HTTP 和 HTPPS
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
					.register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslsf).build();
			// 初始化连接管理器
			pool = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			// 将最大连接数增加到200，实际项目最好从配置文件中读取这个值
			pool.setMaxTotal(200);
			// 设置最大路由
			pool.setDefaultMaxPerRoute(2);

			requestConfig = requestConfig();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			logger.debug(e.getLocalizedMessage());
		} catch (KeyStoreException e) {
			e.printStackTrace();
			logger.debug(e.getLocalizedMessage());
		} catch (KeyManagementException e) {
			e.printStackTrace();
			logger.debug(e.getLocalizedMessage());
		}
	}

	// 获取httpClient对象
	public static CloseableHttpClient getHttpClient() {

		CloseableHttpClient httpClient = HttpClients.custom()
				// 设置连接池管理
				.setConnectionManager(pool)
				// 设置请求配置
				.setDefaultRequestConfig(requestConfig)
				// 设置重试次数
				.setRetryHandler(new DefaultHttpRequestRetryHandler(0, false)).build();

		return httpClient;
	}

	public static HttpClient buildHttpClientByNullKeyStore() throws Exception {
		SSLContext sslContext = SSLContext.getInstance("TLS");
		TrustManager tm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

		};
		sslContext.init(null, new TrustManager[] { tm }, null);

		sslContext.getSupportedSSLParameters().setProtocols(new String[] { "TLSv1" });
		// (X509HostnameVerifier)
		// SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER
		// 不验证证书中的主机ip是否和keystore中的主机ip一致
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext,
				(X509HostnameVerifier) SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		RegistryBuilder<ConnectionSocketFactory> rb = RegistryBuilder.create();
		rb.register("https", csf);
		org.apache.http.config.Registry<ConnectionSocketFactory> reg = rb.build();
		PoolingHttpClientConnectionManager pccm = new PoolingHttpClientConnectionManager(reg);
		HttpClientBuilder build = HttpClientBuilder.create();
		build.setConnectionManager(pccm);
		build.setDefaultRequestConfig(requestConfig());
		return build.build();
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

	/***
	 * &#13;
	 * 
	 * @Description : 请求格式为JSON,头部信息可以自己设置
	 * @Method_Name : sendGetJson&#13;
	 * @param url
	 *            请求地址
	 * @param param
	 *            请求参数
	 * @param requestHead
	 *            头部信息
	 * @return
	 * @return : String&#13;
	 * @Creation Date : 2017年5月26日 下午3:59:38 &#13;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵&#13;
	 */
	public static String sendGetJson(String url, String param, Map<String, String> requestHead) {
		String result = "";
		CloseableHttpResponse response = null;
		CloseableHttpClient httpclient = null;
		try {
			httpclient = getHttpClient();
			URI uri = null;
			if (param == null || param.equals("")) {
				uri = new URIBuilder(url).build();
			} else {
				uri = new URIBuilder(url + "?" + param).build();
			}
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
			// result = decodeData(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				// 不可以关闭，不然连接池就会被关闭
				// httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/***
	 * @Description :请求格式为JSON,头部信息可以自己设置
	 * @Method_Name : sendPostJson&#13;
	 * @param url
	 *            请求地址
	 * @param param
	 *            请求参数
	 * @param requestHead
	 *            头部信息
	 * @return
	 * @return : String&#13;
	 * @throws Exception
	 * @Creation Date : 2017年5月26日 下午4:01:12 &#13;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵&#13;
	 */
	public static String sendPostJson(String url, String param, Map<String, String> requestHead) throws Exception {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = (CloseableHttpClient) buildHttpClientByNullKeyStore(); // getHttpClient();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建请求内容
			StringEntity entity = new StringEntity(param, ContentType.APPLICATION_JSON);
			entity.setContentType(CONTENT_TYPE_JSON_URL);
			httpPost.setEntity(entity);
			httpPost.setConfig(requestConfig());
			if (null != requestHead) {
				for (Map.Entry<String, String> entry : requestHead.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					httpPost.addHeader(key, value);
				}
			}
			// 执行http请求
			response = httpClient.execute(httpPost);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				resultString = EntityUtils.toString(response.getEntity(), CHARSET_UTF_8);
			}
			// resultString = decodeData(resultString);
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

		return resultString;
	}

	/***
	 * @Description : 数据例:merId=9996&amtType=1&retCode=1001&retMsg=
	 *              zOG9u8r9vt1bP13R6dakyqew3A==&versin =3 .0
	 * @Method_Name : doGet&#13;
	 * @param url
	 *            请求地址
	 * @param param
	 *            请求参数
	 * @param requestHead
	 *            关部信息
	 * @param signFlag
	 *            分隔符号
	 * @return
	 * @return : String&#13;
	 * @Creation Date : 2017年5月26日 下午4:02:36 &#13;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵&#13;
	 */
	public static String doGet(String url, Map<String, String> param, Map<String, String> requestHead,
			String signFlag) {
		String result = "";
		CloseableHttpResponse response = null;
		CloseableHttpClient httpclient = null;
		try {
			String params = toHttpGetParams(param, signFlag);
			httpclient = getHttpClient();
			URI uri = new URIBuilder(url + "?" + params).build();
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
			// result = decodeData(result);
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

	public static String doGetString(String url, String param, Map<String, String> requestHead) {
		String result = "";
		CloseableHttpResponse response = null;
		CloseableHttpClient httpclient = null;
		try {
			httpclient = getHttpClient();
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
			// result = decodeData(result);
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

	public static String doGet(String url, Map<String, String> param, String signFlag) {
		return doGet(url, param, null, signFlag);
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

	/**
	 * 这里只是其中的一种场景,也就是把参数用&符号进行连接且进行URL编码 根据实际情况拼接参数
	 */
	private static String toHttpGetParams(Map<String, String> param, String signFlag) throws Exception {
		String res = "";
		if (param == null) {
			return res;
		}
		for (Map.Entry<String, String> entry : param.entrySet()) {
			res += entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), CHARSET_UTF_8) + signFlag;
		}
		return "".equals(res) ? "" : StringUtils.chop(res);
	}

	public static String doPost(String url, Map<String, String> param, Map<String, String> requestHead) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = getHttpClient();
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
			if (param != null) {
				List<NameValuePair> paramList = new ArrayList<NameValuePair>();
				for (String key : param.keySet()) {
					paramList.add(new BasicNameValuePair(key, param.get(key)));
				}
				// 模拟表单
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
				httpPost.setEntity(entity);
			}
			// 执行http请求
			response = httpClient.execute(httpPost);
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

	/***
	 * @Description : POST请求发送String信息
	 * @Method_Name : doPostString&#13;
	 * @param url
	 *            地址
	 * @param param
	 *            参数
	 * @param requestHead
	 *            请求头格式
	 * @return
	 * @return : String&#13;
	 * @Creation Date : 2017年5月26日 下午4:32:47 &#13;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵&#13;
	 */
	public static String doPostString(String url, String param, Map<String, String> requestHead) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = getHttpClient();
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
			response = httpClient.execute(httpPost);
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

	/**
	 * @Description :发送给联动，get方法，返回InputStream
	 * @Method_Name :sendGetForLiandong
	 * @param urlName
	 *            param urlName 请求参数应该是
	 *            http://localhost/yrtz/login.do?name1=value1&name2=value2 的形式。
	 * @author yanbinghuang@hongkun.com.cn 黄艳兵&#13;
	 * @return
	 */

	@SuppressWarnings("rawtypes")
	public static Map sendGetForLiandong(String urlName) {
		Map map = null;
		PrintWriter out = null;
		InputStream in = null;
		try {
			URL realUrl = new URL(urlName);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
			// // 发送请求参数
			// out.print(param);
			conn.connect(); // 建立实际连接
			// flush输出流的缓冲
			out.flush();
			in = conn.getInputStream();
			String html = HttpMerParserUtil.getHtml(in);
			String content = HttpMerParserUtil.getMeta(html);
			map = HttpMerParserUtil.getDataByContent(content);
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return map;
	}

	public static String doPost(String url, Map<String, String> param) {

		return doPost(url, param, null);
	}

	public static String doPost(String url) {
		return doPost(url, null);
	}

	/***
	 * 
	 * @Description : 模拟表单发送请求
	 * @Method_Name : doPostForm;
	 * @param url
	 * @param param
	 * @param requestHead
	 * @return
	 * @return : String;
	 * @Creation Date : 2017年6月6日 下午10:58:51;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static String doPostForm(String url, Map<String, Object> param, Map<String, String> requestHead) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = getHttpClient();

		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(requestConfig());
			if (null != requestHead) {
				for (Map.Entry<String, String> entry : requestHead.entrySet()) {
					String key = entry.getKey();
					String value = (String) entry.getValue();
					httpPost.addHeader(key, value);
				}
			}
			// 创建参数列表
			if (param != null) {
				List<NameValuePair> paramList = new ArrayList<NameValuePair>();
				for (String key : param.keySet()) {
					paramList.add(new BasicNameValuePair(key, (String) param.get(key)));
				}
				// 模拟表单
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
				httpPost.setEntity(entity);
			}
			// 执行http请求
			response = httpClient.execute(httpPost);
			String location = null;
			if (response.getStatusLine().getStatusCode() == 302 || response.getStatusLine().getStatusCode() == 301) {
				Header hs = response.getFirstHeader("Location");
				location = hs.getValue();
				// 然后再对新的location发起请求即可
				// HttpGet httpGet = new HttpGet(location);
				// CloseableHttpResponse response2 =
				// httpClient.execute(httpGet);
				// httpGet.releaseConnection();
				// System.out.println("返回报文" +
				// EntityUtils.toString(response2.getEntity(),
				// "UTF-8"));
			} else {
				location = String.valueOf(response.getStatusLine().getStatusCode());
			}
			// resultString =
			// String.valueOf(response2.getStatusLine().getStatusCode());
			// resultString = EntityUtils.toString(response2.getEntity(),
			// CHARSET_UTF_8);
			return location;
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

}
