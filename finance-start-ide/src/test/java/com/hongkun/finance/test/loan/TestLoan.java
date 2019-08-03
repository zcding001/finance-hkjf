package com.hongkun.finance.test.loan;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.loan.facade.LoanFacade;
import com.hongkun.finance.test.BaseTest;
import com.yirun.framework.core.utils.pager.Pager;
import org.junit.Test;

/**
 * TODO
 *
 * @author zc.ding
 * @create 2018/6/3
 */
public class TestLoan extends BaseTest {

    @Reference
    private LoanFacade loanFacade;
    @Test
    public void testFind(){
        this.loanFacade.findRepayPlanListCount(68, new Pager());
        
        
    }
}
