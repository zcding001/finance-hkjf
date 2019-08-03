package com.hongkun.finance.bi.timer;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hongkun.finance.bi.facade.StaIncomeFacade;
import com.yirun.framework.core.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @Description : 收入统计定时
 * @Project : finance
 * @Program Name  : com.hongkun.finance.bi.timer
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class StaIncomeTaskTimer implements SimpleJob {

    private static final Logger logger = LoggerFactory.getLogger(StaIncomeTaskTimer.class);

    @Autowired
    private StaIncomeFacade staIncomeFacade;

    @Override
    public void execute(ShardingContext shardingContext) {
        Date currentDate = DateUtils.getCurrentDate();
        staIncomeFacade.initStaIncomes();
        logger.info("跑批时间{}，分片项处理结束{}", currentDate, shardingContext.getShardingItem());
    }
}
