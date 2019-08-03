package com.hongkun.finance.vas.timer;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hongkun.finance.vas.service.VasCouponDetailService;
import com.yirun.framework.core.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @Description : 设置已过期的卡券、投资红包为已过期状态
 * @Project : framework
 * @Program Name  : com.hongkun.finance.vas.timer.CouponDetailOverdueTaskTime
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class CouponDetailOverdueTaskTime implements SimpleJob {

    private final static Logger logger = LoggerFactory.getLogger(CouponDetailOverdueTaskTime.class);

    @Autowired
    private VasCouponDetailService couponDetailService;

    @Override
    public void execute(ShardingContext shardingContext) {
        Date currentDate = DateUtils.getCurrentDate();
        couponDetailService.couponDetailOverDue(currentDate, shardingContext.getShardingItem());
        logger.info("CouponDetailOverdueTaskTime, 设置已过期的卡券、投资红包为已过期状态, 跑批时间: {}, 分片项处理结束: {}", currentDate,
                shardingContext.getShardingItem());
    }
}
