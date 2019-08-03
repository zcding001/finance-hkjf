package com.hongkun.finance.payment.timer;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hongkun.finance.payment.service.FinPayCheckAccountService;
import com.yirun.framework.core.utils.DateUtils;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description : 宝付对账
 * @Project : finance
 * @Program Name  : com.hongkun.finance.payment.timer
 * @Author : binliang@hongkun.com.cn 梁彬
 */
public class FinPayCheckAccountTaskTimer implements SimpleJob {

    private static final Logger logger = LoggerFactory.getLogger(FinPayCheckAccountTaskTimer.class);
    @Autowired
    private FinPayCheckAccountService finPayCheckAccountService;
    
    @Override
    public void execute(ShardingContext shardingContext) {
    	logger.info("FinPayCheckAccountTaskTimer execute method start.");
    	Date settleDateTime = DateUtils.addDays(new Date(), -1);
    	Date settleDate = DateUtils.parse(DateUtils.format(settleDateTime), DateUtils.DATE);
    	finPayCheckAccountService.excuteBaoFuReconciliation(settleDate);
        logger.info("跑批时间{}，分片项处理结束{}", DateUtils.getCurrentDate(), shardingContext.getShardingItem());
    }

}
