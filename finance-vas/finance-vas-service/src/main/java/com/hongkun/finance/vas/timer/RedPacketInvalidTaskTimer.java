
/**
 * 
 */
package com.hongkun.finance.vas.timer;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hongkun.finance.vas.facade.VasRedpacketFacade;
import com.yirun.framework.core.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @Description : 红包失效定时扫描
 * @Project : finance
 * @Program Name : com.hongkun.finance.vas.timer.RedPacketInvalidTaskTimer
 * @Author : zhongpingtang@hongkun.com.cn
 */

public class RedPacketInvalidTaskTimer implements SimpleJob {

	private static final Logger logger = LoggerFactory.getLogger(RedPacketInvalidTaskTimer.class);
	@Autowired
	private VasRedpacketFacade vasRedpacketFacade;

	@Override
	public void execute(ShardingContext shardingContext) {
		Date currentDate = DateUtils.getCurrentDate();
		vasRedpacketFacade.invalidateRedPacketOverTime(currentDate,shardingContext.getShardingItem());
		if (logger.isInfoEnabled()) {
			logger.info("跑批时间{}，分片项处理结束{}", currentDate, shardingContext.getShardingItem());
		}
	}

}
