package com.hongkun.finance.sms.service;

import java.util.Arrays;
import java.util.List;

import com.yirun.framework.redis.JedisClusterUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.model.SmsMsgInfo;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.yirun.framework.core.utils.ApplicationContextUtils;
import com.yirun.framework.jms.polling.JmsFailMsg;
import com.yirun.framework.jms.polling.RecoverJmsFailMsgI;

/**
 * @Description : 测试异常jms的存储于恢复
 * @Project : finance-sms-service
 * @Program Name : com.hongkun.finance.sms.service.TestRecover.java
 * @Author : zhichaoding@hongkun.com zc.ding
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-dubbo-provider.xml",
		"classpath:spring/applicationContext-sms4test.xml", "classpath:spring/applicationContext-jms4test.xml" })
public class TestRecover {

	@Test
	@Ignore
	public void testSendSingleWebMsg() {
		SmsMsgInfo smsMsgInfo = new SmsWebMsg(1, "测试异常JMS修复20171122", "测试异常JMS修复20171122", SmsConstants.SMS_TYPE_NOTICE);
		SmsSendUtil.sendWebMsgToQueue(smsMsgInfo);
	}
	
	/**
	 *  @Description    : 测试jms消息发送失败,需要在发送操作中添加运行时异常
	 *  @Method_Name    : testSendExceptionMsg
	 *  @return         : void
	 *  @Creation Date  : 2017年11月21日 下午4:30:05 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@Test
	@Ignore
	public void testSendExceptionMsg() {
		SmsMsgInfo smsMsgInfo = new SmsWebMsg(1, "测试JMS消息发送失败20171122", "测试JMS消息发送失败20171122", SmsConstants.SMS_TYPE_NOTICE);
		SmsSendUtil.sendWebMsgToQueue(smsMsgInfo);
		try {
			Thread.sleep(30000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Description : 测试重新消费异常消息
	 * @Method_Name : testRecoverJmsExceptionRecord
	 * @return : void
	 * @Creation Date : 2017年10月31日 下午2:44:38
	 * @Author : zhichaoding@hongkun.com zc.ding
	 */
	@Test
	@Ignore
	public void testRecoverJmsExceptionRecord() {
		// RecoverJmsFailMsgI recoverJmsFailMsgI =
		// ApplicationContextUtils.getBean("jdbcRepositoryForJmsExceptionRecord");
//		RecoverJmsFailMsgI recoverJmsFailMsgI = ApplicationContextUtils.getBean("redisRepositoryForJmsExceptionRecord");
		RecoverJmsFailMsgI recoverJmsFailMsgI = ApplicationContextUtils.getBean("jdbcRepositoryForJmsExceptionRecord");
		recoverJmsFailMsgI.recoverJmsFailMsg(Arrays.asList("080e7642-f4ff-4a71-acda-cae292808110"));
	}

	@Test
	@Ignore
	public void testFind() {
		// RecoverJmsFailMsgI recoverJmsFailMsgI =
		// ApplicationContextUtils.getBean("jdbcRepositoryForJmsExceptionRecord");
//		RecoverJmsFailMsgI recoverJmsFailMsgI = ApplicationContextUtils.getBean("redisRepositoryForJmsExceptionRecord");
		RecoverJmsFailMsgI recoverJmsFailMsgI = ApplicationContextUtils.getBean("jdbcRepositoryForJmsExceptionRecord");
		List<JmsFailMsg> list = recoverJmsFailMsgI.findAllJmsFailMsg();
		list.forEach(e -> System.out.println(e));
	}

	@Test
	@Ignore
	public void testRec() {
		List<JmsFailMsg> list = JedisClusterUtils.get("JMS:*", JmsFailMsg.class);
		System.out.println(list);
		System.out.println("hello");
	}
}
