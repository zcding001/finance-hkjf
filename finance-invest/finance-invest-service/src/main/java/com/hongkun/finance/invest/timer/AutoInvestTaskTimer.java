package com.hongkun.finance.invest.timer;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hongkun.finance.invest.constants.InvestConstants;
import com.hongkun.finance.invest.facade.BidInvestFacade;
import com.yirun.framework.jms.JmsService;
import com.yirun.framework.jms.enums.DestinationType;
import com.yirun.framework.jms.enums.JmsMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 自动投资
 *
 * @author zc.ding
 * @create 2018/8/28
 */
public class AutoInvestTaskTimer implements SimpleJob {

    private static final Logger logger = LoggerFactory.getLogger(AutoInvestTaskTimer.class);
    @Autowired
    private JmsService jmsService;
    @Override
    public void execute(ShardingContext shardingContext) {
        logger.info("自动投资定时跑批开始.");
        jmsService.sendMsg(InvestConstants.MQ_QUEUE_AUTO_INVEST, DestinationType.QUEUE, InvestConstants.MQ_QUEUE_AUTO_INVEST, JmsMessageType.TEXT);
        logger.info("自动投资定时跑批结束.");
    }
}
