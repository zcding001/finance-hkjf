package com.hongkun.finance.bi.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yirun.framework.core.utils.pager.Pager;

import com.hongkun.finance.bi.model.StaFunRecommendSend;

import com.alibaba.dubbo.config.annotation.Service;

import com.hongkun.finance.bi.dao.StaFunRecommendSendDao;
import com.hongkun.finance.bi.service.StaFunRecommendSendService;

/**
 * @Project       : finance
 * @Program Name  : com.hongkun.finance.bi.service.impl.StaFunRecommendSendServiceImpl.java
 * @Class Name    : StaFunRecommendSendServiceImpl.java
 * @Description   : GENERATOR SERVICE实现类
 * @Author        : generator
 */
@Service
public class StaFunRecommendSendServiceImpl implements StaFunRecommendSendService {

	private static final Logger logger = LoggerFactory.getLogger(StaFunRecommendSendServiceImpl.class);
	
	/**
	 * StaFunRecommendSendDAO
	 */
	@Autowired
	private StaFunRecommendSendDao staFunRecommendSendDao;
	
	@Override
	public List<StaFunRecommendSend> findStaFunRecommendSendList(StaFunRecommendSend staFunRecommendSend) {
		return this.staFunRecommendSendDao.findByCondition(staFunRecommendSend);
	}
	
	@Override
	public Pager findStaFunRecommendSendList(StaFunRecommendSend staFunRecommendSend, Pager pager) {
		return this.staFunRecommendSendDao.findByCondition(staFunRecommendSend, pager);
	}
}
