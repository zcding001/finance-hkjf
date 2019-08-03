
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
 * @Description : 钱袋子第三方垫息
 * @Project : finance-qdz-service
 * @Program Name :
 *          com.hongkun.finance.qdz.Timer.QdzCalculateInterestTaskTimer.java
 * @Author : caoxinbang@hongkun.com.cn 曹新帮
 */

public class QdzThirdAccountPadBearingTimer implements SimpleJob {

	private static final Logger logger = LoggerFactory.getLogger(QdzThirdAccountPadBearingTimer.class);
	@Autowired
	private QdzTaskJobFacade qdzTaskJobFacade;

	@Override
	public void execute(ShardingContext shardingContext) {
		// 垫息第二天凌晨处理
		Date currentDate = DateUtils.addDays(DateUtils.getCurrentDate(), -1);
		//自定义参数 JobParameter，用于修复没有产生利息的数据 格式 yyyymmdd 如修改2018-12-01利息
		String executeDate = shardingContext.getJobParameter();
		logger.info("QdzThirdAccountPadBearingTimer:executeDate:{}",executeDate);
		if(!StringUtilsExtend.isEmpty(executeDate)) {
			currentDate = DateUtils.addDays(DateUtils.parse(executeDate), -1);
		}
		qdzTaskJobFacade.thirdAccountPadBearing(DateUtils.getFirstTimeOfDay(currentDate));
		logger.info("QdzThirdAccountPadBearingTimer:跑批时间{}，分片项处理结束{}", DateUtils.format(currentDate), shardingContext.getShardingItem());


	}

}
