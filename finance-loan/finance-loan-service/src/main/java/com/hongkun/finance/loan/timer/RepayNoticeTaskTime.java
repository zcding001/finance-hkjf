package com.hongkun.finance.loan.timer;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hongkun.finance.loan.facade.LoanFacade;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zc.ding
 * @Description 散标还款通知
 * @create 2018/4/25
 */
public class RepayNoticeTaskTime implements SimpleJob {
    
    @Autowired
    private LoanFacade loanFacade;
    
    @Override
    public void execute(ShardingContext shardingContext) {
        this.loanFacade.findRepayPlanAndNotice();
    }
}
