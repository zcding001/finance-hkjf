package com.hongkun.finance.payment.service;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.payment.facade.FinPaymentConsoleFacade;
import com.yirun.framework.core.utils.DateUtils;

/**
 * @Description : 宝付提现服务测试类
 * @Project : finance
 * @Program Name  : com.hongkun.finance.payment.timer
 * @Author : binliang@hongkun.com.cn 梁彬
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-payment.xml"})
public class FinPaymentConsoleFacadeImplTest {
	
    @Reference
    private FinPaymentConsoleFacade finPaymentConsoleFacade;
    
    @Test
    public void initWithDraw(){
    	String endTime = DateUtils.getCurrentDate(DateUtils.DATE);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int hour = calendar.get(Calendar.HOUR_OF_DAY)-1;//小时
        int minute = calendar.get(Calendar.MINUTE);//分           
        int second = calendar.get(Calendar.SECOND);//秒
        endTime += " "+hour;
        endTime += ":"+minute;
        endTime += ":"+second;
        String startTime = DateUtils.format(DateUtils.addDays(new Date(), -1), DateUtils.DATE);
        this.finPaymentConsoleFacade.executeWithDrawFacade(startTime, endTime);
    }
}
