package com.hongkun.finance.test.bi;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.bi.facade.BalAccountFacade;
import com.hongkun.finance.test.BaseTest;
import org.junit.Test;

/**
 * @Description : TODO
 * @Project : finance
 * @Program Name  : com.hongkun.finance.test.bi
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class BalAccountFacadeImplTest  extends BaseTest {
    @Reference
    private BalAccountFacade balAccountFacade;

    @Test
    public void initBalAccountForChange(){
        balAccountFacade.initBalAccountForChange();
    }
}
