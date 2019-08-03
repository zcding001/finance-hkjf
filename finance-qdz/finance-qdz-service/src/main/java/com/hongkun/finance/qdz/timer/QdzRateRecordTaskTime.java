
/**
 * 
 */
package com.hongkun.finance.qdz.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hongkun.finance.qdz.facade.QdzTaskJobFacade;

/**
 * @Description : 钱袋子利率记录
 * @Project : finance-qdz-service
 * @Program Name : com.hongkun.finance.qdz.Timer.QdzRateRecordTaskTime.java
 * @Author : caoxinbang@hongkun.com.cn 曹新帮
 */

public class QdzRateRecordTaskTime implements SimpleJob {

	private static final Logger logger = LoggerFactory.getLogger(QdzMatchTaskTimer.class);
	@Autowired
	private QdzTaskJobFacade qdzTaskJobFacade;

	@Override
	public void execute(ShardingContext shardingContext) {
		qdzTaskJobFacade.qdzRateRecord();
	}

}
