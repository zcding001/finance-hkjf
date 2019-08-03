package com.hongkun.finance.contract.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hongkun.finance.contract.model.InvestPreServeTemplate;
import com.hongkun.finance.contract.service.InvestPreserveService;
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
public class InvestPreserveServiceImpl implements InvestPreserveService {

	private static final Logger logger = LoggerFactory.getLogger(InvestPreserveServiceImpl.class);
	
	@Override
	public ResponseEntity<?> sendToAncunInvestData(InvestPreServeTemplate template) {
		ResponseEntity<?> result = null;
		
		return result;
	}
	
	
}
