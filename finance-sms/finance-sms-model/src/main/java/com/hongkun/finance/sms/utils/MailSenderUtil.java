package com.hongkun.finance.sms.utils;

import org.apache.commons.lang.StringUtils;

import com.yirun.framework.core.utils.PropertiesHolder;
import com.yirun.framework.core.utils.mail.MailUtil;

/**
 * @Description   : 邮件发送
 * @Project       : finance-sms-model
 * @Program Name  : com.hongkun.finance.sms.utils.MailSenderUtil.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
public class MailSenderUtil {

	private static MailUtil mailUtil;

	private MailSenderUtil(){}
	
	static{
        String port = PropertiesHolder.getProperty("email.port");
		 mailUtil = new MailUtil(
					PropertiesHolder.getProperty("email.username"),
					PropertiesHolder.getProperty("email.password"),
					PropertiesHolder.getProperty("email.host"),
					Integer.parseInt(StringUtils.isBlank(port) ? "25" : port)
					);
	}

	/**
	 *  @Description    : 邮件发送工具类
	 *  @Method_Name    : send
	 *  @param email 收件人地址，多个收件人地址中间 ,隔开
	 *  @param title 邮件标题
	 *  @param msg	邮件内容
	 *  @return
	 *  @return         : boolean
	 *  @Creation Date  : 2017年6月13日 下午1:59:52 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	public static boolean send(String email, String title, String msg){
//		TODO: zhichaoding  黑白单处理  2017/06/13
		if(StringUtils.isBlank(email)){
			return false;
		}
		String[] arr = email.trim().split(",");
//		发送邮件
		return mailUtil.sendMail(
				PropertiesHolder.getProperty("email.from.email"),
				arr, 
				StringUtils.isBlank(title) ? "未定义" : title, 
				msg);
	}
}
