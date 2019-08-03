package com.hongkun.finance.payment.service;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yirun.framework.core.utils.DateUtils;

/**
 * @Description : 宝付对账服务测试类
 * @Project : finance
 * @Program Name  : com.hongkun.finance.payment.timer
 * @Author : binliang@hongkun.com.cn 梁彬
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-payment.xml"})
public class FinPayCheckAccountServiceImplTest {

    @Reference
    private FinPayCheckAccountService finPayCheckAccountService;
    
    @Test
    public void initFinPayCheckAccount(){
    	Date date = DateUtils.addDays(new Date(), -20);
    	finPayCheckAccountService.excuteBaoFuReconciliation(date);
    }
}
