package com.hongkun.finance.payment.timer;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hongkun.finance.payment.facade.FinPaymentConsoleFacade;
import com.yirun.framework.core.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description : 宝付提现定时
 * @Project : finance
 * @Program Name  : com.hongkun.finance.payment.timer
 * @Author : binliang@hongkun.com.cn 梁彬
 */
public class FinPayWithDrawTaskTimer implements SimpleJob {

    private static final Logger logger = LoggerFactory.getLogger(FinPayWithDrawTaskTimer.class);
    @Autowired
    private FinPaymentConsoleFacade finPaymentConsoleFacade;
    
    @Override
    public void execute(ShardingContext shardingContext) {
    	logger.info("FinPayWithDrawTaskTimer execute method start.");
    	String endTime=DateUtils.getCurrentDate(DateUtils.DATE);
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		int hour=calendar.get(Calendar.HOUR_OF_DAY)-1;//小时
        int minute=calendar.get(Calendar.MINUTE);//分           
        int second=calendar.get(Calendar.SECOND);//秒
        endTime+=" "+hour;
        endTime+=":"+minute;
        endTime+=":"+second;
        String startTime=DateUtils.format(DateUtils.addDays(new Date(), -1), DateUtils.DATE);
        logger.info("FinPayWithDrawTaskTimer execute endTime：{}",endTime);
        this.finPaymentConsoleFacade.executeWithDrawFacade(startTime, endTime);
        logger.info("跑批时间{}，分片项处理结束{}", DateUtils.getCurrentDate(), shardingContext.getShardingItem());
    }

}
