package com.hongkun.finance.user.service.impl;

import java.util.UUID;
import java.util.zip.CRC32;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hongkun.finance.user.support.youdun.YdMD5Utils;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.HttpClientUtils;
import com.yirun.framework.core.utils.json.JsonUtils;

public class TestYouDun {

	private static Logger logger = LoggerFactory.getLogger(TestYouDun.class);
	public static void main(String[] args) {
		realName("吴鹏", "360428199012090412");
//		test2();
	}
	
	public static void realName(String realName, String idCard){
    	logger.info("需要实名的信息：{}, {}", realName, idCard);
    	String randomGlobalID = UUID.randomUUID().toString();
		logger.info("globalID:{}", randomGlobalID);
		CRC32 crc32 = new CRC32();
		crc32.update(randomGlobalID.getBytes());
		long globalNumberID = crc32.getValue();
		String strGlobalID = String.valueOf(globalNumberID);
		logger.info("crc32 number ID = {}", strGlobalID);

    	String orderTime = DateUtils.format(DateUtils.getCurrentDate(), DateUtils.DATE_HHMMSS) + strGlobalID;
    	String orderNumber = "YD"+ orderTime;

    	JSONObject body = new JSONObject(true);
		body.put("name_card", realName);//姓名
		body.put("id_card", idCard);//身份证

		JSONObject header = new JSONObject(true);
		header.put("version", "1.0");   //版本号
//		header.put("merchant", PropertiesHolder.getProperty(YouDunConstant.YOUDUN_MERCHANT)); //商户号   201511055144
		header.put("merchant", "201511055144"); //商户号   201511055144
//		header.put("product", PropertiesHolder.getProperty(YouDunConstant.YOUDUN_BUS_TYPE));//业务类型  B10002
		header.put("product", "B10002");//业务类型  B10002
		header.put("outOrderId", orderNumber);//订单号 单日不重复 
		JSONObject signJson = new JSONObject(true);
		signJson.put("body", body);  //有盾返回报文体
		signJson.put("header", header);//有盾返回报文头
		

		String state="9";
		try {
			JSONObject common = new JSONObject(true);
			common.put("request", signJson);
			String orginSign = JsonUtils.toJson(signJson);
//			String sign = YdMD5Utils.ecodeByMD5(orginSign + PropertiesHolder.getProperty(YouDunConstant.YOUDUN_MD5_KEY)); // 加签   ： pp5bJGNcihEOlBxNN9VN
			String sign = YdMD5Utils.ecodeByMD5(orginSign + "pp5bJGNcihEOlBxNN9VN"); // 加签   ： pp5bJGNcihEOlBxNN9VN
			common.put("sign", sign);
			logger.info("访问有盾josn报文:{}", common.toJSONString());
//			String outbuffer = http.postSendHttp(PropertiesHolder.getProperty(YouDunConstant.YOUDUN_REAL_NAME_AUTH_URL), common.toString());
//			YdHttpRequestSimple http = new YdHttpRequestSimple();
//			String outbuffer = http.postSendHttp("https://api.udcredit.com/api/credit/v1/get_nauth", common.toJSONString());
			
			String outbuffer = HttpClientUtils.httpsPost("https://api.udcredit.com/api/credit/v1/get_nauth", common.toJSONString());
//			String outbuffer = null;
			logger.info("有盾返回josn报文outbuffer: {}", outbuffer);
			if(StringUtils.isNotEmpty(outbuffer)){
				JSONObject resp = (JSONObject) JSON.parse(outbuffer);
				String response = String.valueOf(resp.get("response"));
				logger.info("有盾返回josn报文response:{}", response);
				resp = (JSONObject) JSON.parse(response);
				String headerJs = String.valueOf(resp.get("header"));//有盾返回报文头json串
				String bodyJs = String.valueOf(resp.get("body"));//有盾返回报文体json串
				if(StringUtils.isNotEmpty(headerJs) && StringUtils.isNotEmpty(bodyJs)){
					//有盾返回报文头内容获取
					resp = (JSONObject) JSON.parse(headerJs);
					String retCode = String.valueOf(resp.get("retCode"));//返回“0000”表示请求成功
					logger.info("有盾返回报文头:{}, 返回编码:{}", headerJs, retCode);
					//有盾返回报文体内容获取
					resp = (JSONObject) JSON.parse(bodyJs);
					//1-认证一致，2-认证不一致，3-无结果（在公安数据库中查询不到此条数据）
					String status = String.valueOf(resp.get("status"));
					logger.info("有盾返回报文体:{}, 认证结果:{}", bodyJs, status);
					if ("0000".equals(retCode) && StringUtils.isNotBlank(status) &&
							!"null".equals(status.trim())) {
						state = status;
					} else {
						logger.info("有盾身份证实名认证异常1, realname:{},idCard:{}", realName, idCard);
					}
				}
			}
		} catch (Exception e) {
			logger.info("有盾身份证实名认证异常2, realname:{},idCard:{}", realName, idCard);
			logger.error("有盾身份证实名认证异常2", e);
		}
		logger.info("有盾实名认证状态:{}", state);
		if("1".equals(state)){
			logger.info("okokokok.........");
		}else if("2".equals(state)){
			logger.info("姓名与身份证号码不一致，请核实后重新提交！");
		}else if("3".equals(state)){
			logger.info("无数据记录!");
		}else if("9".equals(state)){
			logger.info("网络异常，请您稍后重试！");
		}
	}
	
	public static void test2(){
		String msg = "{\"response\":{\"header\":{\"resTime\":\"2017-05-31 11:16:09\",\"product\":\"\",\"reqTime\":\"2017-05-31 11:16:09\",\"retCode\":\"1003\",\"version\":\"1.0\",\"retMsg\":\"[1003] 商户请求地址<114.241.205.57>未在白名单\"}}}";
		try {
			
			JSONObject json = (JSONObject) JSON.parse(msg);
			System.out.println(json.get("response"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
