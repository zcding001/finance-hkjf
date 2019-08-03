package com.hongkun.finance.vas.timer;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hongkun.finance.vas.service.VasSimRecordService;
import com.yirun.framework.core.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @Description : 设置体验金过期
 * @Project : framework
 * @Program Name  : com.hongkun.finance.vas.timer.SimgoldOverdueTaskTime
 * @Author :  
 */
public class SimgoldOverdueTaskTime implements SimpleJob {

    private final static Logger logger = LoggerFactory.getLogger(SimgoldOverdueTaskTime.class);

    @Autowired
    private VasSimRecordService vasSimRecordService;
    
    @Override
    public void execute(ShardingContext shardingContext) {
        Date currentDate = DateUtils.getCurrentDate();
        vasSimRecordService.simGoldOverDue(currentDate, shardingContext.getShardingItem());
        logger.info("SimgoldOverdueTaskTime, 设置已过期体验金记录为已过期状态, 跑批时间: {}, 分片项处理结束: {}", currentDate,
                shardingContext.getShardingItem());
    }
}
