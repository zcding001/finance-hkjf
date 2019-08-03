
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
import com.yirun.framework.core.model.ResponseEntity;
import com.yirun.framework.core.utils.DateUtils;

/**
 * @Description :钱袋子跑批定时任务
 * @Project : finance-qdz-service
 * @Program Name : com.hongkun.finance.qdz.Timer.QdzMatchTaskTimer.java
 * @Author : caoxinbang@hongkun.com.cn 曹新帮
 */

public class QdzMatchTaskTimer implements SimpleJob {

	private static final Logger logger = LoggerFactory.getLogger(QdzMatchTaskTimer.class);
	@Autowired
	private QdzTaskJobFacade qdzTaskJobFacade;

	@Override
	public void execute(ShardingContext shardingContext) {
		ResponseEntity<?> responseEntity = qdzTaskJobFacade.creditorMatch(DateUtils.getCurrentDate());
		if(responseEntity!=null) {
			logger.info("creditorMatch钱袋子跑批债券匹配结果:{}",responseEntity.getResMsg());
		}
	}

}
