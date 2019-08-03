package com.hongkun.finance.contract.facade.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.contract.facade.InvestPreserveFacade;
import com.hongkun.finance.contract.factory.AncunFactory;
import com.hongkun.finance.contract.model.InvestPreServeTemplate;
import com.hongkun.finance.contract.service.InvestPreserveService;
import com.hongkun.finance.invest.service.BidInvestService;
import com.yirun.framework.core.model.ResponseEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Project       : finance-hkjf
 * @Program Name  : com.hongkun.finance.contract.service.impl.CarInfoServiceImpl.java
 * @Class Name    : CarInfoServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class InvestPreserveFacadeImpl implements InvestPreserveFacade {
	
	private static final Logger logger = LoggerFactory.getLogger(InvestPreserveFacadeImpl.class);
	@Reference
	private InvestPreserveService investPreserveService;
	@Reference
	private BidInvestService bidInvestService;
	
	@Override
	public void sendToAncunInvestData(InvestPreServeTemplate template) {
		logger.info("方法：InvestPreserveFacadeImpl sendToAncunInvestData，投资数据进行保全，param，template：{}" , template);
		ResponseEntity<?> result = this.investPreserveService.sendToAncunInvestData(template);
		
	}
	
	
	
	
}
