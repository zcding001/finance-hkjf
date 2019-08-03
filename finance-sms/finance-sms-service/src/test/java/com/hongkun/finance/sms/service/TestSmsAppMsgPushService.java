package com.hongkun.finance.sms.service;

import com.yirun.framework.core.utils.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Description : app消息推送服务测试类
 * @Project : framework
 * @Program Name  : com.hongkun.finance.sms.service.TestSmsAppMsgPushService
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-sms.xml"})
public class TestSmsAppMsgPushService {

    Logger logger = LoggerFactory.getLogger(TestSmsAppMsgPushService.class);

    @Autowired
    private SmsAppMsgPushService smsAppMsgPushService;

    @Test
    public void testAppMsgPush(){
        smsAppMsgPushService.appMsgPush(DateUtils.getCurrentDate(),2);
    }
}
