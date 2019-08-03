package com.hongkun.finance.user.timer;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hongkun.finance.user.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * VIP用户每月降级计数
 *
 * @author zc.ding
 * @create 2018/6/6
 */
public class RegUserVipRecordTaskTime implements SimpleJob {

    @Autowired
    private UserFacade userFacade;

    @Override
    public void execute(ShardingContext shardingContext) {
        this.userFacade.updateRegUserVip();
    }
}
