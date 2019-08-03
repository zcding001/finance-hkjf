package com.hongkun.finance.sms.utils;

import com.yirun.framework.core.utils.PropertiesHolder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description   : 漫道短信接口
 * @Project       : finance-sms-model
 * @Program Name  : com.hongkun.finance.sms.utils.ClientOfManDao.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
public class ClientOfManDao {
	private static final Logger log = LoggerFactory.getLogger(ClientOfManDao.class);
	private String serviceURL = "";
	private String sn = "";// 序列号
	private String pwd = "";// 密码

	public ClientOfManDao(String sn, String password) throws UnsupportedEncodingException {
		this.sn = sn;
		//密码为md5(sn+password)
		this.pwd = this.getMD5(sn + password);
	}
	
	public ClientOfManDao(String sn, String password, String serviceURL) throws UnsupportedEncodingException {
		this.sn = sn;
		this.serviceURL = serviceURL;
		//密码为md5(sn+password)
		this.pwd = this.getMD5(sn + password);
	}

	/*
	 * 方法名称：getMD5 
	 * 功    能：字符串MD5加密 
	 * 参    数：待转换字符串 
	 * 返 回 值：加密之后字符串
	 */
	public String getMD5(String sourceStr) throws UnsupportedEncodingException {
		String resultStr = "";
		try {
			byte[] temp = sourceStr.getBytes();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(temp);
			// resultStr = new String(md5.digest());
			byte[] b = md5.digest();
			for (int i = 0; i < b.length; i++) {
				char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
						'9', 'A', 'B', 'C', 'D', 'E', 'F' };
				char[] ob = new char[2];
				ob[0] = digit[(b[i] >>> 4) & 0X0F];
				ob[1] = digit[b[i] & 0X0F];
				resultStr += new String(ob);
			}
			return resultStr;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 *  @Description    : 发送短信,多用户手机号用“,”分割
	 *  @Method_Name    : sendSms
	 *  @param mobile 手机号
	 *  @param content 短信内容
	 *  @param mark   短信标志， 默认鸿坤金服
	 *  @param ext 扩展码
	 *  @param time 定时时间
	 *  @param rrid 唯一标识
	 *  @param msgfmt 内容格式
	 *  @return         : String
	 *  @Creation Date  : 2017年6月8日 下午7:22:30 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public String sendSms(String mobile, String content, String mark, String ext, String time, String rrid,String msgfmt){
		log.info("------------漫道短信接口调用开始-------手机号：{}, 内容{}", mobile, content);
		if(!StringUtils.isNotEmpty(mobile)){
			log.info("手机号为空!");
			return "9999999";
		}
		String result = "";
		if(!"1".equals(PropertiesHolder.getProperty("open.mandao.sms"))){
			log.info("发送短信未开启");
			return "9999999";
		}else{
			try {
				// 后缀加【鸿坤金服】 且UTF-8 编码
				if(StringUtils.isBlank(mark)){
//					mark = "鸿坤金服";
                    mark = "鸿坤金服";
				}
//				content = URLEncoder.encode(content + "【" + mark + "】", "utf8");
                //鸿坤金服 将后缀放置在开头
				content = URLEncoder.encode("【" + mark + "】" + content, "utf8");
			
				String soapAction = "http://entinfo.cn/mdsmssend";
				String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
				xml += "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">";
				xml += "<soap:Body>";
				xml += "<mdsmssend  xmlns=\"http://entinfo.cn/\">";
				xml += "<sn>" + sn + "</sn>";
				xml += "<pwd>" + pwd + "</pwd>";
				xml += "<mobile>" + mobile + "</mobile>";
				xml += "<content>" + content + "</content>";
				xml += "<ext>" + ext + "</ext>";
				xml += "<stime>" + time + "</stime>";
				xml += "<rrid>" + rrid + "</rrid>";
				xml += "<msgfmt>" + msgfmt + "</msgfmt>";
				xml += "</mdsmssend>";
				xml += "</soap:Body>";
				xml += "</soap:Envelope>";
		
				URL url = new URL(serviceURL);

				URLConnection connection = url.openConnection();
				HttpURLConnection httpconn = (HttpURLConnection) connection;
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				bout.write(xml.getBytes());
				byte[] b = bout.toByteArray();
				httpconn.setRequestProperty("Content-Length", String.valueOf(b.length));
				httpconn.setRequestProperty("Content-Type", "text/xml; charset=gb2312");
				httpconn.setRequestProperty("SOAPAction", soapAction);
				httpconn.setRequestMethod("POST");
				httpconn.setDoInput(true);
				httpconn.setDoOutput(true);

				OutputStream out = httpconn.getOutputStream();
				out.write(b);
				out.close();

				InputStreamReader isr = new InputStreamReader(httpconn.getInputStream());
				BufferedReader in = new BufferedReader(isr);
				String inputLine;
				while (null != (inputLine = in.readLine())) {
					Pattern pattern = Pattern.compile("<mdsmssendResult>(.*)</mdsmssendResult>");
					Matcher matcher = pattern.matcher(inputLine);
					while (matcher.find()) {
						result = matcher.group(1);
					}
				}
			} catch (UnsupportedEncodingException e1) {
				log.error("漫道短信接口调用异常----内容转换为UTF-8异常---内容：{}", content);
				e1.printStackTrace();
			} catch (Exception e) {
				log.error("漫道短信接口调用异常-------手机号：{}", mobile);
				e.printStackTrace();
			}
			log.info("漫道短信接口调用返回结果:" + result+ "----手机号：{}", mobile);
			return result;
		}
	}
	
	/**
	 *  @Description    : 单个用户发送短信重载方法,多用户手机号用“,”分割
	 *  @Method_Name    : sendSms
	 *  @param tel 手机号
	 *  @param context 内容
	 *  @return         : int > 0表示成功
	 *  @Creation Date  : 2017年6月8日 下午7:32:11 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public int sendSms(String tel, String context) {
		String result = this.sendSms(tel, context, "", "", "", "", "");
		if(StringUtils.isBlank(result) || result.contains("-")){
			return -1;
		}
		return 1;
	}
	
	/**
	 *  @Description    : 单个用户发送短信重载方法,多用户手机号用“,”分割
	 *  @Method_Name    : sendSmsWithMark
	 *  @param tel 手机号
	 *  @param context 内容
	 *  @param mark 短信后缀
	 *  @return         : int > 0表示成功
	 *  @Creation Date  : 2017年6月8日 下午7:32:11 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public int sendSmsWithMark(String tel, String context, String mark) {
		String result = this.sendSms(tel, context, mark, "", "", "", "");
		if(StringUtils.isBlank(result) || result.contains("-")){
			return -1;
		}
		return 1;
	}
	
	/**
	 *  @Description    : 定时发送,多用户手机号用“,”分割
	 *  @Method_Name    : mdsmssend
	 *  @param tel 手机号
	 *  @param context 内容
	 *  @param time 定时时间  时间格式 '2016-01-01 10:00:00'
	 *  @return         : int > 0表示成功
	 *  @Creation Date  : 2017年6月8日 下午7:31:32 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public int sendSms(String tel, String context, String time) {
		String result = this.sendSms(tel, context, "", "", time, "", "");
		if(StringUtils.isBlank(result) || result.contains("-")){
			return -1;
		}
		return 1;
	}
	
	/**
	 *  @Description    : 定时发送,多用户手机号用“,”分割
	 *  @Method_Name    : sendSms
	 *  @param tel 手机号
	 *  @param context 内容
	 *  @param mark 短信标志
	 *  @param time 定时时间  时间格式 '2016-01-01 10:00:00'
	 *  @return         : int > 0表示成功
	 *  @Creation Date  : 2017年6月8日 下午7:31:32 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public int sendSmsWithSuffix(String tel, String context, String mark, String time) {
		String result = this.sendSms(tel, context, mark, "", time, "", "");
		if(StringUtils.isBlank(result) || result.contains("-")){
			return -1;
		}
		return 1;
	}
}
