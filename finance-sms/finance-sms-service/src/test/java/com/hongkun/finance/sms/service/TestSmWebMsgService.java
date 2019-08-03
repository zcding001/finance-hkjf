package com.hongkun.finance.sms.service;

import com.hongkun.finance.sms.facade.SmsFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hongkun.finance.sms.model.SmsWebMsg;
import com.yirun.framework.core.utils.pager.Pager;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = {
		"classpath:spring/applicationContext-dubbo-provider.xml",
		"classpath:spring/applicationContext-sms4test.xml",
		"classpath:spring/applicationContext-jms4test.xml"
		}
)
public class TestSmWebMsgService {

	Logger logger = LoggerFactory.getLogger(TestSendSmsMsg.class);
	
	@Autowired
	private SmsWebMsgService  smsWebMsgService;
	
	@Autowired
	private SmsFacade smsFacade;
	
	@Test
	public void testFindSmsWebMsgWithDetailList() {
		SmsWebMsg smsWebMsg = new SmsWebMsg();
		smsWebMsg.setRegUserId(5);
		Pager pager = this.smsWebMsgService.findSmsWebMsgWithDetailList(smsWebMsg, new Pager());
		logger.info("{}", pager);
	} 
	
	@Test
	public void testFindWebMsgList(){
        this.smsFacade.findWebMsgList(new Pager(), new SmsWebMsg());
    }
}
