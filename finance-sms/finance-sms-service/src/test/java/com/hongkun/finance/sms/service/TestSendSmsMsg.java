package com.hongkun.finance.sms.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.sms.constants.SmsConstants;
import com.hongkun.finance.sms.dao.SmsTelMsgDao;
import com.hongkun.finance.sms.model.SmsEmailMsg;
import com.hongkun.finance.sms.model.SmsMsgInfo;
import com.hongkun.finance.sms.model.SmsTelMsg;
import com.hongkun.finance.sms.model.SmsWebMsg;
import com.hongkun.finance.sms.utils.SmsSendUtil;
import com.yirun.framework.core.model.ResponseEntity;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description   : <p>
 * 						执行测试时去掉@Ignore注解
 * 					</p>
 * @Project       : finance-sms-service
 * @Program Name  : com.yirun.framework.sms.TestSendSmsMsg.java
 * @Author        : zhichaoding@hongkun.com zc.ding
 */
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = {
		"classpath:spring/applicationContext-dubbo-provider.xml",
		"classpath:spring/applicationContext-sms4test.xml",
		"classpath:spring/applicationContext-jms4test.xml"
		}
)
public class TestSendSmsMsg{
	
	Logger logger = LoggerFactory.getLogger(TestSendSmsMsg.class);
	
	final static String TEL = "15701230895";
	final static String TITLE = "消息通知";
	final static String EMAIL = "zhichaoding@hongkun.com.cn";
	final static String REGIST_TEMPLATE = "恭喜您已成功注册为鸿坤金服用户。如有任何问题请随时致电乾坤袋，客服电话：400-900-9630，感谢您对鸿坤金服的关注与支持！";

	@Reference
	private SmsTelMsgService smsTelMsgService;
	@Reference
	private SmsWebMsgService smsWebMsgService;
	@Reference
	private SmsEmailMsgService smsEmailMsgService;
	
	/** 异步发送消息    测试    start **/
	@Test
	@Ignore
	public void testSendSingleTelMsg(){
		SmsMsgInfo smsMsgInfo = new SmsTelMsg(
				12, 
				Long.parseLong(TEL), 
				"恭喜您已成功注册为鸿坤金服用户。如有任何问题请随时致电乾坤袋，客服电话：400-900-9630，感谢您对鸿坤金服的关注与支持！", 
				SmsConstants.SMS_TYPE_NOTICE
				);
		SmsSendUtil.sendTelMsgToQueue(smsMsgInfo);
	}
	
	/**
	 *  @Description    : 发送站内信
	 *  @Method_Name    : testSendSingleWebMsg
	 *  @return         : void
	 *  @Creation Date  : 2017年6月12日 下午6:32:13 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@Test
	@Ignore
	public void testSendSingleWebMsg(){
		SmsMsgInfo smsMsgInfo = new SmsWebMsg(
				1, 
				TITLE,
				REGIST_TEMPLATE, 
				SmsConstants.SMS_TYPE_NOTICE
				);
		SmsSendUtil.sendWebMsgToQueue(smsMsgInfo);
	}
	
	/**
	 *  @Description    : 发送邮件消息
	 *  @Method_Name    : testSendSingleEmailMsg
	 *  @return         : void
	 *  @Creation Date  : 2017年6月13日 上午9:20:13 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@Test
//	@Ignore
	public void testSendSingleEmailMsg(){
		SmsMsgInfo smsMsgInfo = new SmsEmailMsg(
				12, 
				EMAIL,
				"注册",
				REGIST_TEMPLATE, 
				SmsConstants.SMS_TYPE_NOTICE
				);
		SmsSendUtil.sendEmailMsgToQueue(smsMsgInfo);
        try {
            Thread.sleep(1000 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
	
//	@Test
//	@Ignore
//	public void testInvest() {
//		String template = "尊敬的用户：您投资的借款标《%s》,预期收益如下：投资期数：%s%s,年利率：%s%% 投资金额：%s元 到期收益总额：%s元 ,另外您使用了投资红包,投资红包收益：%s元!";
//		String msg = String.format(template, new Object[]{"测试投资", "1", "2", "5", "1000", "100", "100"});
//		SmsMsgInfo smsMsgInfo = new SmsEmailMsg(12, EMAIL, msg, SmsContants.SMS_TYPE_NOTICE);
//		SmsSendUtil.sendEmailMsgToQueue(smsMsgInfo);
//	}
	
	/**
	 *  @Description    : 向一个用户发送多条消息
	 *  @Method_Name    : testSendListToOnUser
	 *  @return         : void
	 *  @Creation Date  : 2017年6月12日 下午4:52:12 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@Test
	@Ignore
	public void testSendListToOnUser(){
		List<SmsMsgInfo> list = new ArrayList<>();
		for(int i = 10; i < 10; i++){
			list.add(new SmsWebMsg(12, TITLE, REGIST_TEMPLATE, SmsConstants.SMS_TYPE_NOTICE));
		}
		SmsSendUtil.sendEmailMsgToQueue(list);
	}

	
	/**
	 * @Described			: 同时给一个人发送三种类型消息
	 * @author				: zc.ding
	 * @project				: framework-sms
	 * @package				: com.yirun.framework.sms.SendMsgTelEmailWeb.java
	 * @return				: void
	 * @date 				: 2017年3月14日
	 */
	@Test
	@Ignore
	public void testSendSameMsgToOneUser(){
		SmsMsgInfo arg0 = new SmsTelMsg(12, Long.parseLong(TEL), REGIST_TEMPLATE, SmsConstants.SMS_TYPE_NOTICE);
		SmsMsgInfo arg1 = new SmsEmailMsg(12, EMAIL, REGIST_TEMPLATE, SmsConstants.SMS_TYPE_NOTICE);
		SmsMsgInfo arg2 = new SmsWebMsg(12, TITLE, REGIST_TEMPLATE, SmsConstants.SMS_TYPE_NOTICE);
		SmsSendUtil.sendSmsMsgToQueue(arg0, arg1, arg2);
	}
	/** 异步发送消息    测试    end **/
	
	/** 同步发送消息    测试    start **/

	/**
	 *  @Description    : 同步发送短信消息测试 
	 *  @Method_Name    : testSendTelMsg
	 *  @return         : void
	 *  @Creation Date  : 2017年6月13日 上午10:03:18 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@Test
	@Ignore
	public void testSendTelMsg(){
		SmsTelMsg smsTelMsg = new SmsTelMsg(
				12, 
				Long.parseLong(TEL), 
				"同步测试-恭喜您已成功注册为鸿坤金服用户。如有任何问题请随时致电乾坤袋，客服电话：400-900-9630，感谢您对鸿坤金服的关注与支持！", 
				SmsConstants.SMS_TYPE_NOTICE
				);
		System.out.println(this.smsTelMsgService);
		ResponseEntity<?> result = this.smsTelMsgService.insertSmsTelMsg(smsTelMsg);
		logger.info("result {}", result.toString());
	}
	
	/**
	 *  @Description    : 同步发送站内信
	 *  @Method_Name    : testSendWebMsg
	 *  @return         : void
	 *  @Creation Date  : 2017年6月13日 上午10:54:06 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@Test
	@Ignore
	public void testSendWebMsg(){
		SmsWebMsg smsWebMsg = new SmsWebMsg(
				12,
				TITLE,
				REGIST_TEMPLATE, 
				SmsConstants.SMS_TYPE_NOTICE
				);
		this.smsWebMsgService.insertSmsWebMsg(smsWebMsg);
	}
	
	/**
	 *  @Description    : 同步发送邮件消息
	 *  @Method_Name    : testSendEmail
	 *  @return         : void
	 *  @Creation Date  : 2017年6月13日 上午10:56:39 
	 *  @Author         : zhichaoding@hongkun.com zc.ding
	 */
	@Test
	@Ignore
	public void testSendEmail(){
		SmsEmailMsg smsEmailMsg = new SmsEmailMsg(
				12, 
				EMAIL,
				REGIST_TEMPLATE, 
				SmsConstants.SMS_TYPE_NOTICE
				);
		this.smsEmailMsgService.insertSmsEmailMsg(smsEmailMsg);
	}
	/** 同步发送消息    测试    end **/
	
	@Test
	@Ignore
	public void testInsertBatchWebMsg(){
		List<SmsWebMsg> list = new ArrayList<>();
		list.add(new SmsWebMsg(12, "test_batch", "test_batch", SmsConstants.SMS_TYPE_SYS_NOTICE));
		this.smsWebMsgService.insertSmsWebMsgBatch(list);
	}
	
	
	@Autowired
	private SmsTelMsgDao smsTelMsgDao;
	@Test
	public void testBatchInsert() {
		List<SmsTelMsg> list = Arrays.asList(new SmsTelMsg(5, 14510000110L, "test batch insert", 1),
				new SmsTelMsg(5, 14510000110L, "test batch insert", 1),
				new SmsTelMsg(5, 14510000110L, "test batch insert", 1),
				new SmsTelMsg(5, 14510000110L, "test batch insert", 1),
				new SmsTelMsg(5, 14510000110L, "test batch insert", 1));
		this.smsTelMsgDao.insertBatch(SmsTelMsg.class, list);
		
		logger.info("测试批量插入 okok....");
	}
}
