
/**
 * 
 */
package com.hongkun.finance.qdz.timer;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hongkun.finance.qdz.facade.QdzTaskJobFacade;
import com.yirun.framework.core.utils.DateUtils;
import com.yirun.framework.core.utils.StringUtilsExtend;

/**
 * @Description : 计算利息
 * @Project : finance-qdz-service
 * @Program Name :
 *          com.hongkun.finance.qdz.Timer.QdzCalculateInterestTaskTimer.java
 * @Author : caoxinbang@hongkun.com.cn 曹新帮
 */

public class QdzCalculateInterestTaskTimer implements SimpleJob {

	private static final Logger logger = LoggerFactory.getLogger(QdzCalculateInterestTaskTimer.class);
	@Autowired
	private QdzTaskJobFacade qdzTaskJobFacade;

	@Override
	public void execute(ShardingContext shardingContext) {
		Date currentDate = DateUtils.getCurrentDate();
		//自定义参数 JobParameter，用于修复没有产生利息的数据 格式 yyyy-mm-dd 如修改20181201利息
		String executeDate = shardingContext.getJobParameter();
		if(!StringUtilsExtend.isEmpty(executeDate)) {
			currentDate = DateUtils.parse(executeDate);
		}
		qdzTaskJobFacade.calculateInterest(currentDate, shardingContext.getShardingItem());
		logger.info("QdzCalculateInterestTaskTimer:跑批时间{}，分片项处理结束{}", DateUtils.format(currentDate), shardingContext.getShardingItem());

	}

}
