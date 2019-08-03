package com.hongkun.finance.bi.facade;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.loan.model.BidRepayPlan;
import com.hongkun.finance.payment.model.FinFundtransfer;

import java.util.List;

/**
 * @Description   : 收入统计服务
 * @Project       : finance-bi-service
 * @Program Name  : com.hongkun.finance.bi.facade.StaIncomeFacade.java
 * @Author        : xuhuiliu@hongkun.com.cn 劉旭輝
 */
public interface StaIncomeFacade {

    public void initStaIncomes();

}
