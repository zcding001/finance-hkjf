package com.hongkun.finance.sms.timer;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hongkun.finance.sms.service.SmsAppMsgPushService;
import com.yirun.framework.core.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @Description : 定时执行app消息推送
 * @Project : framework
 * @Program Name  : com.hongkun.finance.sms.timer.AppMsgPushTaskTimer
 * @Author : pengwu@hongkun.com.cn 吴鹏
 */
public class AppMsgPushTaskTimer implements SimpleJob {

    private Logger logger = LoggerFactory.getLogger(AppMsgPushTaskTimer.class);

    @Autowired
    private SmsAppMsgPushService smsAppMsgPushService;

    @Override
    public void execute(ShardingContext shardingContext) {
        Date currentDate = DateUtils.getCurrentDate();
        smsAppMsgPushService.appMsgPush(currentDate, shardingContext.getShardingItem());
        logger.info("AppMsgPushTaskTimer, 定时执行app消息推送, 跑批时间: {}, 分片项处理结束: {}", currentDate,
                shardingContext.getShardingItem());
    }
}
