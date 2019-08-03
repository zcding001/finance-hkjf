package com.hongkun.finance.sms.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yirun.framework.core.utils.mail.MailUtil;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = {
		"classpath:spring/applicationContext-dubbo-provider.xml",
		"classpath:spring/applicationContext-jms.xml",
		"classpath:spring/applicationContext-sms.xml"
		}
)
public class TestReceSmsMsg {
	
	@Test
	public void test(){
		while(true){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testRec(){
		MailUtil mailUtil = new MailUtil("zhichaoding@hongkun.com.cn", "", "smtp.263.net");
		mailUtil.sendMail(
				"zhichaoding@hongkun.com.cn", 
				new String[]{"zhichaoding@hongkun.com.cn"}, 
				"test", 
				"hello world");
	}

}
