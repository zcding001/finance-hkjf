package com.hongkun.finance.invest.timer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hongkun.finance.invest.facade.TransferManualFacade;
import com.yirun.framework.core.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @Description : 债权转让定时，每天跑批，把到期得转让设置为已失效
 * @Project : finance
 * @Program Name  : com.hongkun.finance.invest.timer
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class TransferManualTaskTimer implements SimpleJob {

    private static final Logger logger = LoggerFactory.getLogger(TransferManualTaskTimer.class);
    @Autowired
    private TransferManualFacade transferManualFacade;

    @Override
    public void execute(ShardingContext shardingContext) {
        Date currentDate = DateUtils.getCurrentDate();
        transferManualFacade.dealTransferTimeOut();
        logger.info("跑批时间{}，分片项处理结束{}", currentDate, shardingContext.getShardingItem());
    }

}
