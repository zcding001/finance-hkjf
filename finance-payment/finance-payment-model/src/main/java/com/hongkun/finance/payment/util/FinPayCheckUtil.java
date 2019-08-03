package com.hongkun.finance.payment.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description : 支付中用到的校验类
 * @Project : finance-payment-model
 * @Program Name : com.hongkun.finance.payment.util.FinPayCheckUtil.java
 * @Author : binliang@hongkun.com 梁彬
 */
public class FinPayCheckUtil {
	private static final Logger logger = LoggerFactory.getLogger(FinPayCheckUtil.class);

	private FinPayCheckUtil() {
	}

	/**
	 * @Description : 校验日期格式
	 * @Method_Name : dateMatcher;
	 * @param reconciliationDate
	 * @return
	 * @return : boolean;
	 * @Creation Date : 2017年8月11日 下午3:00:40;
	 * @Author : yanbinghuang@hongkun.com.cn 黄艳兵;
	 */
	public static boolean dateMatcher(String reconciliationDate) {
		String eL = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
		Pattern pattern = Pattern.compile(eL);
		Matcher matcher = pattern.matcher(reconciliationDate);
		return matcher.matches();
	}
	
}
