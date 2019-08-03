package com.hongkun.finance.test.bi;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.bi.facade.StaIncomeFacade;
import com.hongkun.finance.test.BaseTest;
import org.junit.Test;

/**
 * @Description : 收入统计测试
 * @Project : finance
 * @Program Name  : com.hongkun.finance.test.bi
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class StaIncomeFacadeTest extends BaseTest {

    @Reference
    private StaIncomeFacade staIncomeFacade;

    @Test
    public void initStaIncomes(){
        staIncomeFacade.initStaIncomes();
    }
}
