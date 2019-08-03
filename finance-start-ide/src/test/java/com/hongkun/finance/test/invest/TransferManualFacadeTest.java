package com.hongkun.finance.test.invest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hongkun.finance.invest.facade.TransferManualFacade;
import com.hongkun.finance.test.BaseTest;
import org.junit.Test;

/**
 * @Description : TODO
 * @Project : finance
 * @Program Name  : com.hongkun.finance.test.invest
 * @Author : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public class TransferManualFacadeTest extends BaseTest {
    @Reference
    private TransferManualFacade transferManualFacade;

    @Test
    public void dealTimeOut(){
        transferManualFacade.dealTransferTimeOut();
    }
}
