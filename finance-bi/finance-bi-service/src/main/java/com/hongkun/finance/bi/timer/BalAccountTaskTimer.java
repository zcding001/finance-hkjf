package com.hongkun.finance.bi.timer;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hongkun.finance.bi.facade.BalAccountFacade;
import com.yirun.framework.core.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @Description : 平台对账定时
 * @Project : finance
 * @Program Name  : com.hongkun.finance.bi.timer
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class BalAccountTaskTimer implements SimpleJob {

    private static final Logger logger = LoggerFactory.getLogger(StaIncomeTaskTimer.class);
    @Autowired
    private BalAccountFacade balAccountFacade;

    @Override
    public void execute(ShardingContext shardingContext) {
        Date currentDate = DateUtils.getCurrentDate();
        balAccountFacade.initBalAccountForChange();
        logger.info("跑批时间{}，分片项处理结束{}", currentDate, shardingContext.getShardingItem());
    }

}
