package com.hongkun.finance.payment.client.yeepay.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HeePayUtil {
	private final static Log log = LogFactory.getLog(HeePayUtil.class);
	
	public static boolean checkSign(String data,String sign){
		log.info("校验签名原签名为"+sign);
		log.info("检验签名数据串"+data);
		String getSign = Md5Tools.MD5(data).toLowerCase();
		log.info("校验签名加密获取的签名为"+getSign);
		if(sign.equals(getSign)){
			return true;
		}
		return false;
	}
}
